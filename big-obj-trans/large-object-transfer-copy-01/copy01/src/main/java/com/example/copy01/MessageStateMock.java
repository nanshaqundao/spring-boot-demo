package com.example.copy01;

import java.util.Random;


public class MessageStateMock {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            builder.append(CHARACTERS.charAt(index));
        }

        return builder.toString();
    }

    public static MessageState createRandomMessageState() {
        MessageState messageState = new MessageState();
        messageState.setFieldA(MessageStateMock.generateRandomString(5));
        messageState.setFieldB(MessageStateMock.generateRandomString(5));
        messageState.setFieldC(MessageStateMock.generateRandomString(5));
        messageState.setFieldD(MessageStateMock.generateRandomString(5));

        return messageState;
    }
}
