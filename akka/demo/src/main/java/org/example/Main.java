package org.example;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Source<Integer, NotUsed> source = Source.range(1, 10);
        final CompletionStage<Done> done = source.runForeach(System.out::println, system);
        done.thenRun(system::terminate);

        final Source<BigInteger, NotUsed> factorials = source.scan(BigInteger.ONE,
                (accrue, next) -> accrue.multiply(BigInteger.valueOf(next)));
        final CompletionStage<Done> done2 = factorials.runForeach(System.out::println, system);
        done2.thenRun(system::terminate);

        Sink<ByteString, CompletionStage<IOResult>> sink = FileIO.toPath(Paths.get("factorials.txt"));
        final CompletionStage<IOResult> result =
                factorials
                        .map(number -> ByteString.fromString(number.toString() + "\n"))
                        .runWith(sink, system);

        ReuseDemo reuseDemo = new ReuseDemo();
        factorials.map(BigInteger::toString).runWith(reuseDemo.lineSink("factorials2.txt"), system);


    }
}