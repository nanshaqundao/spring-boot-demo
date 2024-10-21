package com.example.datasourceservice.service;

import com.example.datasourceservice.entity.BusinessInfo;
import com.example.datasourceservice.repository.BusinessInfoR2dbcRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessInfoR2dbcService {

  private final BusinessInfoR2dbcRepository repository;

  public BusinessInfoR2dbcService(BusinessInfoR2dbcRepository repository) {
    this.repository = repository;
  }

  public Flux<BusinessInfo> findAllByBusinessName(String name) {
    return repository.findByName(name);
  }

  public Flux<BusinessInfo> saveBusinessInfoFromCsv(FilePart file) {
    return DataBufferUtils.join(file.content())
        .map(
            dataBuffer -> {
              byte[] bytes = new byte[dataBuffer.readableByteCount()];
              dataBuffer.read(bytes);
              DataBufferUtils.release(dataBuffer);
              return new String(bytes, StandardCharsets.UTF_8);
            })
        .flatMap(
            content ->
                Mono.fromCallable(
                    () -> {
                      List<BusinessInfo> businessInfos = new ArrayList<>();
                      try (CSVParser csvParser =
                          new CSVParser(new StringReader(content), CSVFormat.DEFAULT)) {
                        for (CSVRecord record : csvParser) {
                          if (record.size() >= 2) {
                            String name = record.get(0);
                            String description = record.get(1);
                            businessInfos.add(new BusinessInfo(name, description));
                          }
                        }
                      }
                      return businessInfos;
                    }))
        .flatMapMany(
            list -> Flux.fromIterable(list).flatMap(businessInfo -> repository.save(businessInfo)))
        .onErrorResume(
            e -> {
              System.err.println("Error processing CSV file: " + e.getMessage());
              return Flux.empty();
            })
        .subscribeOn(Schedulers.boundedElastic());
  }
}
