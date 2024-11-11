package com.example.businessclientservice.service;

import com.example.businessclientservice.protobuf.BusinessPayload;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class BusinessClientService {
  private static final Logger logger = LoggerFactory.getLogger(BusinessClientService.class);

  private final WebClient webClient;
  private final String dataSourceServiceUrl;
  // Add this field to the class
  private final StringBuilder bufferForPrefix = new StringBuilder();

  public BusinessClientService(
      WebClient webClient, @Value("${datasource.service.url}") String dataSourceServiceUrl) {
    this.webClient = webClient;
    this.dataSourceServiceUrl = dataSourceServiceUrl;
  }

  public Flux<BusinessPayload> getBusinessPayloadStreamLarge() {
    ChunkBuffer buffer = new ChunkBuffer();
    final AtomicInteger counter = new AtomicInteger(0);
    final long startTime = System.currentTimeMillis();

    return webClient
        .get()
        .uri("/api/mvc/business-info/test-protobuf-flux")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(byte[].class)
        .doOnNext(bytes -> logger.debug("Received byte array of size: {}", bytes.length))
        .doOnNext(buffer::addChunk)
        .filter(chunk -> buffer.isMessageComplete())
        .map(
            chunk -> {
              List<byte[]> chunks = buffer.getPayloadChunks();
              buffer.reset(); // Reset for next message
              return processChunks(chunks);
            });
  }

  private static class ChunkBuffer {
    private final List<byte[]> chunks = new ArrayList<>();
    private final StringBuilder prefixBuffer = new StringBuilder();
    private final List<byte[]> suffixBuffer = new ArrayList<>();
    private boolean foundPrefix = false;
    private boolean foundSuffix = false;
    private boolean isComplete = false;

    public ChunkBuffer addChunk(byte[] chunk) {
      // Detect "data:" prefix first
      if (!foundPrefix) {
        String content = new String(chunk);
        prefixBuffer.append(content);

        if (prefixBuffer.toString().contains("data:")) {
          foundPrefix = true;
          // Don't add this chunk - it contains SSE formatting
          return this;
        }
        return this;
      }

      // If we've found the prefix, check for message completion
      if (!foundSuffix) {
        if (chunk.length == 2 && chunk[0] == '\n' && chunk[1] == '\n') {
          isComplete = true;
          foundSuffix = true;
          return this;
        }
        if (chunk.length == 1 && chunk[0] == '\n') {
          suffixBuffer.add(chunk);
          if (suffixBuffer.size() == 2
              && suffixBuffer.get(0).length == 1
              && suffixBuffer.get(1).length == 1
              && suffixBuffer.get(0)[0] == '\n'
              && suffixBuffer.get(1)[0] == '\n') {
            isComplete = true;
            foundSuffix = true;
            return this;
          }
          return this;
        }
        return this;
      }

      // Only add non-SSE chunks
      chunks.add(chunk);
      return this;
    }

    public boolean isMessageComplete() {
      return isComplete;
    }

    public List<byte[]> getPayloadChunks() {
      // For complete messages, return all accumulated chunks
      if (isComplete) {
        return new ArrayList<>(chunks);
      }
      // For incomplete messages, return empty list
      return Collections.emptyList();
    }

    // Add reset method for reuse
    public void reset() {
      chunks.clear();
      prefixBuffer.setLength(0);
      foundPrefix = false;
      isComplete = false;
    }
  }

  private BusinessPayload processChunks(List<byte[]> chunks) {
    int totalLength = chunks.stream().mapToInt(chunk -> chunk.length).sum();

    byte[] completeMessage = new byte[totalLength];
    int offset = 0;
    for (byte[] chunk : chunks) {
      System.arraycopy(chunk, 0, completeMessage, offset, chunk.length);
      offset += chunk.length;
    }

    try {
      return BusinessPayload.parseFrom(completeMessage);
    } catch (InvalidProtocolBufferException e) {
      logger.error(
          "Failed to parse protobuf message from {} chunks, total size: {}",
          chunks.size(),
          completeMessage.length,
          e);
      throw new RuntimeException("Error parsing protobuf message", e);
    }
  }

  public Flux<BusinessPayload> getBusinessPayloadStreamWorksBelow10000() {
    return webClient
        .get()
        .uri("/api/mvc/business-info/test-protobuf-flux")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(byte[].class)
        .doOnNext(bytes -> logger.debug("Received byte array of size: {}", bytes.length))
        .bufferUntil(
            bytes -> {
              // Skip SSE formatting bytes
              if (bytes.length <= 5 && new String(bytes).startsWith("data:")) {
                logger.debug("Found SSE prefix");
                return false;
              }
              if (bytes.length == 2 && bytes[0] == '\n' && bytes[1] == '\n') {
                logger.debug("Found SSE suffix");
                return true; // End of message
              }
              return false; // Keep buffering
            })
        .filter(chunks -> !chunks.isEmpty())
        .map(
            chunks -> {
              // Filter out SSE formatting and combine only payload chunks
              List<byte[]> payloadChunks =
                  chunks.stream()
                      .filter(
                          bytes -> {
                            // Skip SSE formatting
                            if (bytes.length <= 5 && new String(bytes).startsWith("data:")) {
                              return false;
                            }
                            if (bytes.length == 2 && bytes[0] == '\n' && bytes[1] == '\n') {
                              return false;
                            }
                            return true;
                          })
                      .toList();

              int totalLength = payloadChunks.stream().mapToInt(chunk -> chunk.length).sum();

              byte[] completeMessage = new byte[totalLength];
              int offset = 0;
              for (byte[] chunk : payloadChunks) {
                System.arraycopy(chunk, 0, completeMessage, offset, chunk.length);
                offset += chunk.length;
              }

              logger.debug(
                  "Combined {} payload chunks into message of size: {}",
                  payloadChunks.size(),
                  completeMessage.length);

              try {
                BusinessPayload payload = BusinessPayload.parseFrom(completeMessage);
                logger.debug("Successfully parsed payload with name: {}", payload.getName());
                return payload;
              } catch (InvalidProtocolBufferException e) {
                logger.error(
                    "Failed to parse protobuf message from {} chunks, total size: {}",
                    payloadChunks.size(),
                    completeMessage.length,
                    e);
                throw new RuntimeException("Error parsing protobuf message", e);
              }
            });
  }

  public Flux<BusinessPayload> getBusinessPayloadStreamNew() {
    return webClient
        .get()
        .uri("/api/mvc/business-info/test-protobuf-flux")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(byte[].class)
        .filter(bytes -> bytes.length > 10) // Filter out SSE formatting bytes
        .buffer() // Aggregates the byte[] chunks into a list for each payload
        .map(
            chunks -> {
              int totalSize = chunks.stream().mapToInt(chunk -> chunk.length).sum();
              byte[] fullPayload = new byte[totalSize];
              int position = 0;
              for (byte[] chunk : chunks) {
                System.arraycopy(chunk, 0, fullPayload, position, chunk.length);
                position += chunk.length;
              }
              return fullPayload; // Full payload as byte[]
            })
        .doOnNext(fullPayload -> logger.debug("Aggregated payload size: {}", fullPayload.length))
        .map(
            fullPayload -> {
              try {
                BusinessPayload payload = BusinessPayload.parseFrom(fullPayload);
                logger.debug(
                    "Successfully parsed aggregated payload: name={}, size={}",
                    payload.getName(),
                    payload.getSerializedSize());
                return payload;
              } catch (InvalidProtocolBufferException e) {
                logger.error(
                    "Failed to parse aggregated protobuf message, bytes length: {}",
                    fullPayload.length,
                    e);
                return BusinessPayload
                    .getDefaultInstance(); // Return empty payload if parsing fails
              }
            })
        .filter(
            payload ->
                !payload.equals(BusinessPayload.getDefaultInstance())); // Filter out empty payloads
  }

  // New method for buffered API
  // this is not working... as each buffer came as a list of byte[] into one object...
  // which can not be parsed into ONE BusinessPayload
  public Flux<BusinessPayload> getBusinessPayloadStreamBuffered(
      Integer totalMessages, Integer bufferSize, Boolean parallel) {

    return webClient
        .get()
        .uri(
            builder ->
                builder
                    .path("/api/mvc/business-info/test-protobuf-flux/buffered")
                    .queryParamIfPresent("totalMessages", Optional.ofNullable(totalMessages))
                    .queryParamIfPresent("bufferSize", Optional.ofNullable(bufferSize))
                    .queryParamIfPresent("parallel", Optional.ofNullable(parallel))
                    .build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(byte[].class)
        .doOnNext(bytes -> logger.debug("Received message of size: {}", bytes.length))
        .handle(
            (bytes, sink) -> {
              try {
                BusinessPayload payload = BusinessPayload.parseFrom(bytes);
                logger.debug("Parsed payload with name: {}", payload.getName());
                sink.next(payload);
              } catch (InvalidProtocolBufferException e) {
                logger.error("Failed to parse protobuf message", e);
                sink.error(new RuntimeException("Error parsing protobuf message", e));
              }
            });
  }
}
