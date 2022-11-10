package org.example.reactivetweets;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Demo {
    public static final Hashtag AKKA = new Hashtag("#akka");
    public static class Author{
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
