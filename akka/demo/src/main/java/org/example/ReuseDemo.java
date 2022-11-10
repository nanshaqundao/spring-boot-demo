package org.example;

import akka.stream.IOResult;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;

import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

public class ReuseDemo {
    public Sink<String, CompletionStage<IOResult>> lineSink(String fileName) {
        return Flow.of(String.class)
                .map(s -> ByteString.fromString(s + "\n"))
                .toMat(FileIO.toPath(Paths.get(fileName)), Keep.right());
    }
}
