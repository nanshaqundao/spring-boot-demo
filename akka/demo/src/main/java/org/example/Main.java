package org.example;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        final ActorSystem system1 = ActorSystem.create("QuickStart1");
        final ActorSystem system2 = ActorSystem.create("QuickStart2");
        final ActorSystem system3 = ActorSystem.create("QuickStart3");
        final ActorSystem system4 = ActorSystem.create("QuickStart4");
        final ActorSystem system5 = ActorSystem.create("QuickStart5");
        final ActorSystem system6 = ActorSystem.create("QuickStart6");
        final Source<Integer, NotUsed> source = Source.range(1, 10);
        final CompletionStage<Done> done = source.runForeach(System.out::println, system1);
        done.thenRun(system1::terminate);

        final Source<BigInteger, NotUsed> factorials = source.scan(BigInteger.ONE,
                (accrue, next) -> accrue.multiply(BigInteger.valueOf(next)));
        final CompletionStage<Done> done2 = factorials.runForeach(System.out::println, system2);
        done2.thenRun(system2::terminate);

        Sink<ByteString, CompletionStage<IOResult>> sink = FileIO.toPath(Paths.get("factorials.txt"));
        final CompletionStage<IOResult> result =
                factorials
                        .map(number -> ByteString.fromString(number.toString() + "\n"))
                        .runWith(sink, system3);

        ReuseDemo reuseDemo = new ReuseDemo();
        factorials.map(BigInteger::toString).runWith(reuseDemo.lineSink("factorials2.txt"), system4);

        factorials
                .zipWith(Source.range(0, 9), (num, idx) -> String.format("%d! = %s", idx, num))
                .throttle(1, Duration.ofSeconds(1))
                .runForeach(System.out::println, system5)
                .thenRun(system5::terminate);

        //fold

        CompletionStage<Integer> sum =
                source.runWith(Sink.fold(0, Integer::sum), system6);
        sum.thenAccept(System.out::println);
    }
}