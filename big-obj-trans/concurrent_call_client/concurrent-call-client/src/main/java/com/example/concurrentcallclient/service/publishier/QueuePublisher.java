package com.example.concurrentcallclient.service.publishier;

import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import org.springframework.stereotype.Component;

@Component
public class QueuePublisher {
  public void publish(TargetObjectFromLargeObject largeJsonObject) {
    System.out.println(
        "Published to queue: " + largeJsonObject.name() + " - " + largeJsonObject.data());
//    throw new QueuePublishingException("Failed to publish to queue");
  }
}
