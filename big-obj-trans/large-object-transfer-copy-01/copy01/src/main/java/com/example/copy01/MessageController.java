package com.example.copy01;



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
        List<MessageState> messageStates;
        if (pageNum < 5) {
             messageStates = messageService.getMessages(pageNum, pageSize);
        } else {
             messageStates = messageService.getMessages(pageNum, 2);
        }
        return ResponseEntity.ok(messageStates);
    }
}
