package org.example.reactivetweets;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Demo {
    public static final Hashtag AKKA = new Hashtag("#akka");

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("reactive-tweets");
        Source<Tweet, NotUsed> tweets;
        final Source<Author, NotUsed> authors =
                tweets.filter(t -> t.hashtags().contains(AKKA)).map(t -> t.author);
        authors.runWith(Sink.foreach(System.out::println, system));
    }

    public static class Author {
        public final String handle;

        public Author(String handle) {
            this.handle = handle;
        }

        public String getHandle() {
            return handle;
        }
    }

    public static class Hashtag {
        public final String name;

        public Hashtag(String name) {
            this.name = name;
        }

        // ...

        public String getName() {
            return name;
        }
    }

    public static class Tweet {
        public final Author author;
        public final long timestamp;
        public final String body;

        public Tweet(Author author, long timestamp, String body) {
            this.author = author;
            this.timestamp = timestamp;
            this.body = body;
        }

        public Set<Hashtag> hashtags() {
            return Arrays.asList(body.split(" ")).stream()
                    .filter(a -> a.startsWith("#"))
                    .map(a -> new Hashtag(a))
                    .collect(Collectors.toSet());
        }

        // ...

        public Author getAuthor() {
            return author;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getBody() {
            return body;
        }
    }


}
