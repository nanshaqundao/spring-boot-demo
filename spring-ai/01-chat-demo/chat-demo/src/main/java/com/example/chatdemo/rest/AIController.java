package com.example.chatdemo.rest;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AIController {
  private final ChatClient chatClient;

  public AIController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/ai")
  Map<String, String> completion(
      @RequestParam(value = "message", defaultValue = "Tell me a jooke") String message) {
    return Map.of("completion", chatClient.prompt().user(message).call().content());
  }
}
