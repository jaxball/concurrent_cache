package com.linpossible;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

/**
 * @author jasonlin
 *
 */
public class AddressCacheTest {

	/** In the following test cases (1-4), we assume a caching time 
	 *  of 5 seconds unless otherwise specified.
	 */
	
	@Test
	public void test1() {
		
		InetAddressCache test = new InetAddressCache(5000);
		
		try {
			
			// Testing insertion of up to 3 elements: size, peek()
			System.out.println("\n\n==========Test1: Offer Addresses ==========");
			InetAddress addr = InetAddress.getLocalHost();
			System.out.println("0 InetAddresses offered.. test.isEmpty(): " + test.isEmpty());
			test.offer(addr);
			System.out.println("1 InetAddresses offered.. test.isEmpty(): " + test.isEmpty());
			int sizeAfterFirstOffer = test.size();
			assertEquals(1, sizeAfterFirstOffer);
			assertEquals(addr, test.peek());
			
	    	InetAddress addr2 = InetAddress.getByName("www.linpossible.com");
	    	InetAddress addr3 = InetAddress.getByName("www.google.com");
	    	InetAddress addr4 = InetAddress.getByName("www.apple.com");
	    	InetAddress addr5 = InetAddress.getByName("www.microsoft.com");
	    	InetAddress addr6 = InetAddress.getByName("www.usc.edu");
	    	InetAddress addr7 = InetAddress.getByName("www.facebook.com");
	    	InetAddress addr8 = InetAddress.getByName("www.jlin.xyz");
	    	InetAddress addr9 = InetAddress.getByName("www.msight.co");
	    	InetAddress addr10 = InetAddress.getByName("www.geeksforgeeks.org");
	    
	    	// Check list after 2 offers.
	    	test.offer(addr2);
	    	assertEquals(2, test.size());
	    	assertEquals(addr2, test.peek());
	    	
	    	// Re-offering address 1, now address 1 should be at front of list.
	    	test.offer(addr);
	    	assertEquals(2, test.size());
	    	assertEquals(addr, test.peek());
	    	
	    	// Offering InetAddresses up to 10
	    	test.offer(addr2);
	    	test.offer(addr3);
	    	test.offer(addr4);
	    	test.offer(addr5);
	    	test.offer(addr6);
	    	test.offer(addr7);
	    	test.offer(addr8);
	    	test.offer(addr9);
	    	test.offer(addr10);
	    	int sizeAfterTenthOffer = test.size();
			assertEquals(10, sizeAfterTenthOffer);
			assertEquals(addr10, test.peek());

			System.out.println("10 InetAddresses offered.. test.size(): " + test.size());
	    	System.out.println("Most recently added InetAddress is : " + test.peek());
			
			// Offering InetAddresses up to > 16, which the default init capacity for HashMap
			InetAddress addr11 = InetAddress.getByName("www.yahoo.com");
			InetAddress addr12 = InetAddress.getByName("www.twitter.com");
	    	InetAddress addr13 = InetAddress.getByName("www.leetcode.com");
	    	InetAddress addr14 = InetAddress.getByName("www.github.com");
	    	InetAddress addr15 = InetAddress.getByName("www.linkedin.com");
	    	InetAddress addr16 = InetAddress.getByName("www.gmail.com");
	    	InetAddress addr17 = InetAddress.getByName("www.tmobile.com");
	    	test.offer(addr11);
	    	test.offer(addr12);
	    	test.offer(addr13);
	    	test.offer(addr14);
	    	test.offer(addr15);
	    	test.offer(addr16);
	    	test.offer(addr17);
	    	test.offer(addr15);
	    	int sizeAfterSeventeenthOffer = test.size();
			assertEquals(17, sizeAfterSeventeenthOffer);
			assertEquals(addr15, test.peek());
	    	
		} catch (UnknownHostException ex){
            System.out.println("Failed to create InetAddress!");
        }
		
	}

	@Test
	public void test2() {
		
		InetAddressCache test = new InetAddressCache(5000);
		
		try {
			
			// Testing insertion of up to 3 elements: size, peek()
			System.out.println("\n\n==========Test2: Remove Addresses ==========");
			InetAddress addr = InetAddress.getLocalHost();
	    	InetAddress addr2 = InetAddress.getByName("www.linpossible.com");
	    	InetAddress addr3 = InetAddress.getByName("www.google.com");
	    	InetAddress addr4 = InetAddress.getByName("www.apple.com");
	    	InetAddress addr5 = InetAddress.getByName("www.microsoft.com");
	    	InetAddress addr6 = InetAddress.getByName("www.usc.edu");
	    	InetAddress addr7 = InetAddress.getByName("www.facebook.com");
	    	InetAddress addr8 = InetAddress.getByName("www.jlin.xyz");
	    	InetAddress addr9 = InetAddress.getByName("www.msight.co");
	    	InetAddress addr10 = InetAddress.getByName("www.geeksforgeeks.org");
	    	InetAddress addr11 = InetAddress.getByName("www.yahoo.com");
			InetAddress addr12 = InetAddress.getByName("www.twitter.com");
	    	InetAddress addr13 = InetAddress.getByName("www.leetcode.com");
	    	InetAddress addr14 = InetAddress.getByName("www.github.com");
	    	InetAddress addr15 = InetAddress.getByName("www.linkedin.com");
	    	InetAddress addr16 = InetAddress.getByName("www.gmail.com");
	    	InetAddress addr17 = InetAddress.getByName("www.tmobile.com");
	    	
	    	// Offer address 1, then address 2, then address 1, address 1 at front of list;
	    	// Remove most recently added (Assuming: most recently added = front of list)
	    	test.offer(addr);
	    	test.offer(addr2);
	    	test.offer(addr);
	    	System.out.println("2 InetAddresses offered.. test.size(): " + test.size());
	    	test.remove();
	    	assertEquals(addr2, test.peek());
	    	System.out.println("1 InetAddress removed at front.. test.size(): " + test.size());
	    	
	    	// Resume to before removal. Now remove address 2, address 1 should be left in the cache.
	    	test.offer(addr);
	    	System.out.println("Address 1 re-offered, remove address 2.. test.peek(): " + test.peek());
	    	test.remove(addr2);
	    	assertEquals(addr, test.peek());
	    	
	    	test.offer(addr2);
	    	test.offer(addr3);
	    	test.offer(addr4);
	    	test.offer(addr5);
	    	test.offer(addr6);
	    	test.offer(addr7);
	    	test.offer(addr8);
	    	test.offer(addr9);
	    	test.offer(addr10);
	    	test.offer(addr11);
	    	test.offer(addr12);
	    	test.offer(addr13);
	    	test.offer(addr14);
	    	test.offer(addr15);
	    	test.offer(addr16);
	    	test.offer(addr17);
	    	test.offer(addr15);
	    	int sizeAfterSeventeenthOffer = test.size();
			assertEquals(17, sizeAfterSeventeenthOffer);
			System.out.println("16 more InetAddress offered.. test.size(): " + test.size());
			
			test.remove();
			assertEquals(addr17, test.peek());
			assertEquals(16, test.size());
			System.out.println("1 InetAddress removed.. test.size(): " + test.size());
			
			// Remove 2 more, add one, then peek() and check size(); 
			test.remove();
			test.remove();
			test.offer(addr16);
			assertEquals(addr16, test.peek());
			assertEquals(15, test.size());
			System.out.println("2 InetAddress removed, 1 offered.. test.size(): " + test.size());
	    	
		} catch (UnknownHostException ex){
            System.out.println("Failed to create InetAddress!");
        }
	}
	
	@Test
	public void test3() throws InterruptedException {
		
		InetAddressCache test = new InetAddressCache(5000);
		
		try {
			
			// Testing insertion of up to 17 elements: size, peek()
			System.out.println("\n\n==========Test3: Internal Cleanup ==========");
			InetAddress addr = InetAddress.getLocalHost();
	    	InetAddress addr2 = InetAddress.getByName("www.linpossible.com");
	    	InetAddress addr3 = InetAddress.getByName("www.google.com");
	    	InetAddress addr4 = InetAddress.getByName("www.apple.com");
	    	InetAddress addr5 = InetAddress.getByName("www.microsoft.com");
	    	InetAddress addr6 = InetAddress.getByName("www.usc.edu");
	    	InetAddress addr7 = InetAddress.getByName("www.facebook.com");
	    	InetAddress addr8 = InetAddress.getByName("www.jlin.xyz");
	    	InetAddress addr9 = InetAddress.getByName("www.msight.co");
	    	InetAddress addr10 = InetAddress.getByName("www.geeksforgeeks.org");
	    	InetAddress addr11 = InetAddress.getByName("www.yahoo.com");
			InetAddress addr12 = InetAddress.getByName("www.twitter.com");
	    	InetAddress addr13 = InetAddress.getByName("www.leetcode.com");
	    	InetAddress addr14 = InetAddress.getByName("www.github.com");
	    	InetAddress addr15 = InetAddress.getByName("www.linkedin.com");
	    	InetAddress addr16 = InetAddress.getByName("www.gmail.com");
	    	InetAddress addr17 = InetAddress.getByName("www.tmobile.com");

	    	test.offer(addr);
	    	test.offer(addr2);
	    	test.offer(addr3);
	    	test.offer(addr4);
	    	test.offer(addr5);
	    	test.offer(addr6);
	    	test.offer(addr7);
	    	test.offer(addr8);
	    	test.offer(addr9);
	    	test.offer(addr10);
	    	test.offer(addr11);
	    	test.offer(addr12);
	    	test.offer(addr13);
	    	test.offer(addr14);
	    	test.offer(addr15);
	    	test.offer(addr16);
	    	test.offer(addr17);
	    	int sizeAfterSeventeenthOffer = test.size();
			assertEquals(17, sizeAfterSeventeenthOffer);
			System.out.println("17 InetAddresses offered.. test.size(): " + test.size());
	    	
			Thread.sleep(5000);
			
			// size should be 0 because of cleanup
			System.out.println("Thread elapsed for 5 seconds, now test.size(): " + test.size());
			assertEquals(0, test.size());
			
			test.offer(addr4);
	    	test.offer(addr5);
	    	test.offer(addr6);
	    	assertEquals(3, test.size());
	    	assertEquals(addr6, test.peek());
	    	
		} catch (UnknownHostException ex){
            System.out.println("Failed to create InetAddress!");
        }
	}
	
	@Test
	public void test4() throws UnknownHostException, InterruptedException {
		
		InetAddressCache test = new InetAddressCache(5000);
		System.out.println("\n\n==========Test4: Removal When Available ==========");
		Thread remove = new Thread (new Runnable() {
			public void run() {
				try {
					System.out.println("test.size() before test.take() in remove thread: " + test.size());
					test.take();
					System.out.println("test.size() after test.take() in remove thread: " + test.size());
					assertEquals(0, test.size());
				} catch(InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		});
		
		Thread offer = new Thread (new Runnable() {
			public void run() {
				try {
					System.out.println("test.size() before test.offer() in offer thread: " + test.size());
					InetAddress addr = InetAddress.getByName("www.linpossible.com");
					test.offer(addr);
					System.out.println("test.size() after test.offer() in offer thread: " + test.size());
					assertEquals(1, test.size());
				} catch(UnknownHostException e) {
					System.out.println(e.getMessage());
				}
			}
		});
		
		remove.start();
		Thread.sleep(1000);
		offer.start(); 
		remove.join();
		offer.join();
		System.out.println("Remove thread waits up to 5 seconds and was able to remove 1 InetAddress.");
		
	}
}
