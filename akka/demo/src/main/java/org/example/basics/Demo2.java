package org.example.basics;

import akka.actor.ActorSystem;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;

public class Demo2 {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStartBasic");
        // connect the Source to the Sink, obtaining a RunnableGraph
        final Sink<Integer, CompletionStage<Integer>> sink = Sink.fold(0, Integer::sum);
        final RunnableGraph<CompletionStage<Integer>> runnable =
                Source.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).toMat(sink, Keep.right());

// get the materialized value of the FoldSink
        final CompletionStage<Integer> sum1 = runnable.run(system);
        final CompletionStage<Integer> sum2 = runnable.run(system);

// sum1 and sum2 are different Futures
        sum1.thenAccept(System.out::println);
        sum2.thenAccept(System.out::println);
    }
}
