package org.example;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.javadsl.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Source<Integer, NotUsed> source = Source.range(1, 100);
    }
}