package org.example.basics;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class Demo3 {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStartBasic");
// Create a source from an Iterable
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Source.from(list);

// Create a source form a Future
        Source.future(Futures.successful("Hello Streams!"));

// Create a source from a single element
        Source.single("only one element");

// an empty source
        Source.empty();

// Sink that folds over the stream and returns a Future
// of the final result in the MaterializedMap
        Sink.fold(0, Integer::sum);

// Sink that returns a Future in the MaterializedMap,
// containing the first element of the stream
        Sink.head();

// A Sink that consumes a stream without doing anything with the elements
        Sink.ignore();

// A Sink that executes a side-effecting call for every element of the stream
        Sink.foreach(System.out::println);
    }
}
