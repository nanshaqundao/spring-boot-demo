package com.nansha.largeobjecttransfer.rest;

import com.nansha.largeobjecttransfer.model.LargeJsonObject;
import com.nansha.largeobjecttransfer.model.MessageState;
import com.nansha.largeobjecttransfer.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.nansha.largeobjecttransfer.util.MessageStateMock.generateRandomString;

@RestController
@RequestMapping("/message")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @GetMapping("/get")
  public ResponseEntity<List<MessageState>> getMessages(
      @RequestParam int pageNum, @RequestParam int pageSize) {
    List<MessageState> messageStates;
    if (pageNum < 5) {
      messageStates = messageService.getMessages(pageNum, pageSize);
    } else {
      messageStates = messageService.getMessages(pageNum, 2);
    }
    return ResponseEntity.ok(messageStates);
  }

  @GetMapping("/getLargeJsonObject")
  public ResponseEntity<LargeJsonObject> getLargeJsonObject(
      @RequestParam int pageNum, @RequestParam int pageSize) {
    // random generate a integer between 1 and 10
    int randomInt = (int) (Math.random() * 10 + 1);
    if (randomInt >= 3) {

      String data = generateRandomString(40000);

      String uuid = UUID.randomUUID().toString();
      LargeJsonObject largeJsonObject = new LargeJsonObject(uuid, data);
      return ResponseEntity.ok(largeJsonObject);
    }
    else {
      return ResponseEntity.internalServerError().build();
    }
  }
}
