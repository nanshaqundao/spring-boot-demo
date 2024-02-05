package com.nansha.largeobjecttransfer.rest;


import com.nansha.largeobjecttransfer.model.MessageState;
import com.nansha.largeobjecttransfer.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<MessageState>> getMessages(
            @RequestParam int pageNum,
            @RequestParam int pageSize
    ) {

        List<MessageState> messageStates = messageService.getMessages(pageNum, pageSize);
        return ResponseEntity.ok(messageStates);
    }
}
