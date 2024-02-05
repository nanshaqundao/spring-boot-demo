package com.nansha.largeobjecttransfer;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @GetMapping("/get")
    public ResponseEntity<List<MessageState>> getMessages() {
        return ResponseEntity.ok(List.of(MessageStateMock.createRandomMessageState(), MessageStateMock.createRandomMessageState()));
    }
}
