package org.example.basics;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.Arrays;

public class Demo4 {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStartBasic");
        // Explicitly creating and wiring up a Source, Sink and Flow
        Source.from(Arrays.asList(1, 2, 3, 4))
                .via(Flow.of(Integer.class).map(elem -> elem * 2))
                .to(Sink.foreach(System.out::println))
                .run(system);


        // Starting from a Source
        final Source<Integer, NotUsed> source =
                Source.from(Arrays.asList(1, 2, 3, 4)).map(elem -> elem * 2);
        source.to(Sink.foreach(System.out::println))
                .run(system);

        // Starting from a Sink
        final Sink<Integer, NotUsed> sink =
                Flow.of(Integer.class).map(elem -> elem * 2).to(Sink.foreach(System.out::println));
        Source.from(Arrays.asList(1, 2, 3, 4)).to(sink)
                .run(system);
        system.terminate();
    }
}
