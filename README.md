#Project: Java self-evicting Address Cache
Given an interface for a InetAddress cache (used in web browsers), implement a cache in Java that supports the following policies:

- Last-in-First-Out retrieval
- peek(), remove(), and take() in O(1) time, handling exceptions if necessary
- Internal cleanup that periodically (user-defined) removes oldest elements (FIFO eviction)


#Solution Rationale

Considering the purpose of a cache being to store small amounts of information that can be retrieved and looked up very quickly, I've tried to implement **add/remove/get/lookup** in **linear** time.

There're three common types of caches - **FIFO, LFU** and **LRU**. In our case, no perfect caching algorithm exists because it would require knowledge of how a program accesses its InetAddresses. We do however, know from the interface signatures that the cache needs to support LIFO access and removal. 

Though we are told that the cache should follow a FIFO eviction policy, there are no signs of it being enforced in the interface; I've implemented a LIFO cache so it always has the "most recently added" element at front. The way the cache is implemented also supports switching to FIFO eviction if necessary. However, because the rules of an *expired address* is not defined and neither is the *cache size* specified, for simplicity, removal will always be LIFO and an internal cleanup task will run every USER_DEFINE seconds to empty the cache so it doesn't overflow. **USER_DEFINE** = user-defined Time to Live/Caching Time value at instatiation.

#Process Design

Commonly a LRU is implemented with a Hash table hooked up to a doubly linked list. Java comes with a *LinkedHashMap* class that does exactly that and fulfills the implementation of a LRU. 
The use of a doubly linkedlist ensures the time efficiency of add and remove at constant time while keeping the items in insertion-order for future iteration. 

I've tried extending the LinkedHashMap class to implement a "Most Recently Added" cache that essentially stores items in reverse-insertion order but the encapsulation of the official class doesn't yield any reference to its inner linked list implementation.  

So I turned to the other two ways of implementing a cache.

1. Given that we know the maximum cache size, we can implement the cache using an ArrayList. Add/remove elements at one end would only take constant O(1) time. However, looking up elements may require traversal of the entire list in the worst case, yielding O(n) worst case complexity. But because our cache runs a cleanup on a regular interval, its size shouldn't be too big so this method is doable. Is this the most optimal though?
2. A second and the way I went forward is to implement a custom LinkedHashMap extending a HashMap. By maintaining a doubly-linked list with both its head and tail, I could redefine the eviction and retrieval policies easily while not compromising on performance. I was also able to implement the cleanup task internally so that immediately after instatiation the cache will begin to empty itself every **n** seconds.

#Multi-threading

Having self-taught Java through Android development, I did not initially think multi-threading was necessary because of the uniform nature of operations. Yet the waiting action required by *take( )* method mimics the same method in a Java **BlockingQueue**, which encapsulates multi-threading from the user. While pondering whether I should migrate the implementation onto a BlockingQueue, I realized it's not feasible because the List data type underlying it is inefficient for many operations (slower than a RBTree when it comes to lookup). Eventually I incorporated a **coordination synchronization** between the two threads that represented offer/remove.

#File Structure

> addresscache
>> src
>>> com.linpossible
>>>> AddressCacheJL.java
>>>> AddressCacheJLTest.java

There are 4 test cases in total in *AddressCacheJLTest.java*. JUnit tests are used.

Jason Lin © [jlin.xyz](http://jlin.xyz) 