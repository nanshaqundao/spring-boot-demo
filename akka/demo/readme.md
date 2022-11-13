# Akka Stream in Java

## Concepts
 - It is important to remember that even after constructing the RunnableGraph by connecting all the source, sink and different operators, no data will flow through it until it is materialized. Materialization
 - Materialization is the process of allocating all resources needed to run the computation described by a Graph (in Akka Streams this will often involve starting up Actors). 
 - Flows being a description of the processing pipeline they are immutable, thread-safe, and freely shareable, which means that it is for example safe to share and send them between actors, to have one actor prepare the work, and then have it be materialized at some completely different place in the code.