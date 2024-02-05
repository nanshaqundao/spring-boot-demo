package com.nansha.largeobjecttransfer.service;

import com.nansha.largeobjecttransfer.model.MessageState;
import com.nansha.largeobjecttransfer.util.MessageStateMock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    public List<MessageState> getMessages(int pageNum, int pageSize) {
        List<MessageState> messageStates = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            messageStates.add(MessageStateMock.createRandomMessageState());
        }

        return messageStates;
    }
}
