package com.linpossible;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author jasonlin
 *
 */
public class InetAddressCache implements AddressCache {

	// AddressNode class
	private class InetAddressNode {	
		InetAddress inet;
		public InetAddressNode prev, next;
	
		InetAddressNode(InetAddress address, InetAddressNode prev, InetAddressNode next) {
			this.inet = address;
			this.prev = prev;
			this.next = next;
		} 
	}

	HashMap<InetAddress, InetAddressNode> inetCache;
	private InetAddressNode head;
	private InetAddressNode tail;
	private Object lock = new Object();
	Timer timer;

    /**
     * Constructor
     */
    public InetAddressCache(int timeToLive) {
    	inetCache = new HashMap<InetAddress, InetAddressNode>();
        head = null;
        tail = null;
        
        // cleanup task schedule
        timer = new Timer();
		timer.schedule(new CleanupTask(), 01, timeToLive);
    }
    
    /**
     * A cleanup task runs every 5 seconds to evict expired addresses
     */
    class CleanupTask extends TimerTask {
    	public void run() {
    		close();
    		// DEBUG
    		// System.out.println("Cache emptied! Current size: " + inetCache.size());
    	}
    }
    
	/**
     * Adds the given {@link InetAddress} and returns {@code true} on success.
     * Average case complexity = O(1). In the worst case, put in HashMap could be O(n) time.
     */
    public boolean offer(InetAddress address) {
    	
    	// coordinated multi-threading to complement take()
    	synchronized (lock) {
    		if(inetCache.size() == 0) {
    			lock.notify();
    		}
    	
    	
        // first finds if address exists already 
        if (inetCache.containsKey(address)) {
        	// Shift priority of current address to the first
        	InetAddressNode current = inetCache.get(address);
        	if (current.next == null && current.prev != null) {
        		InetAddressNode prev = current.prev;
        		prev.next = null;
        		tail = prev;
            	
        		// Moves current to the front of the linked list
        		current.prev = null;
        		current.next = head;
        		head.prev = current;
        		head = current;
        	}
        	else if (current.next != null && current.prev != null) {
        		// current is in the middle of the linked list.
        		InetAddressNode prev = current.prev;
        		InetAddressNode next = current.next;
        		next.prev = prev;
        		prev.next = next;
        		
        		// Moves current to the front of the linked list
        		current.prev = null;
        		current.next = head;
        		head.prev = current;
        		head = current;	
        	}
            return false;
        }
    	
    	InetAddressNode prev = null;
    	InetAddressNode next = null;
    	InetAddressNode addressNode = new InetAddressNode(address, prev, next);

    	// inserts the element into hashmap 
    	inetCache.put(address, addressNode);

    	// inserts element into doubly-linked-list
		if (head == null) {
			head = addressNode;
		}
		else {
			// adds new linked element to the beginning
			addressNode.next = head;
			head.prev = addressNode;
			head = addressNode;
		}
		
		if (tail == null) {
			// Updates tail
			tail = addressNode;
		}
		
        return true;
        }

    } 
    
    /**
     * Returns {@code true} if the given {@link InetAddress} 
     * is in the {@link AddressCache}.
     * Because a HashMap is layered on top for lookup, average case complexity = O(1).
     */
    public boolean contains(InetAddress address) {
		if (inetCache.containsKey(address)) {
			return true;
		}
		else {
			return false;
		}
    }
    
    /**
     * Removes the given {@link InetAddress} and returns {@code true}
     * on success.
     * Deletion from HashMap = O(1), deletion in doubly-linked list = O(1). 
     * Overall complexity is O(1) because we can quickly find a pointer to
     * the node of removal in the doubly-linked list with the use of HashMap.
     */
    public boolean remove(InetAddress address) {

    	if (inetCache.containsKey(address)) {
    		
			// Remove item from linked list - there exists different cases
			if (head == tail) {
				// Special case 1: only one item in cache, both head and tail become null
				head = null;
				tail = null;
				return true;
			}
			
			InetAddressNode current = inetCache.get(address);
			if (current == head) {
				// if current address is at the head
				InetAddressNode tmp = head.next; // backup of old head
				tmp.prev = null;
				head = tmp;
			}
			else if (current == tail){
				// if current address is at the tail
				InetAddressNode tmp = tail.prev;
				tmp.next = null;
				tail = tmp;
			}
			else {
				// all other cases
				InetAddressNode addrAfter = current.next;
				InetAddressNode addrBefore = current.prev;
				
				addrBefore.next = addrAfter;
				addrAfter.prev = addrBefore;
			}
			// Removes item from HashMap
			inetCache.remove(address);
			return true;
		}
		else {
			// Cache does not contain such address, return false
			System.out.println("Does not contain such address!");
			return false;
		}
    }
    
    /**
     * Returns the most recently added {@link InetAddress} and returns 
     * {@code null} if the {@link AddressCache} is empty.
     * Retrieval of first element in a doubly-linked list = O(1).
     */
    public InetAddress peek() {
    	if (isEmpty() || head == null) {
    		return null;
    	} 
    	else {
    		return head.inet;
    	}
    }
    
    /**
     * Removes and returns the most recently added {@link InetAddress} and  
     * returns {@code null} if the {@link AddressCache} is empty.
     * Overall complexity of O(1) on average case.
     */
    public InetAddress remove() {
    	if (isEmpty() || head == null) {
    		return null;
    	}
    	else {
    		InetAddress firstAddress = head.inet; 
    		inetCache.remove(firstAddress);
    		
    		// Check if there exists a second item in the cache
    		if (head.next != null) {
    			// Caution: Garbage collection - removing reference
    			head = head.next;
    			head.prev = null;

                return firstAddress;
    		}
            else {
                head = null;
                tail = null;
                return firstAddress;
            }
    	}
    }
    
    /**
     * Retrieves and removes the most recently added {@link InetAddress},
     * waiting if necessary until an element becomes available.
     * Complexity = O(1)
     */
    public InetAddress take() throws InterruptedException {
//    	
    	// so that loop only runs once
    	int counter = 1;
    	
    	synchronized(lock) {
	    	while(inetCache.size() == 0 && counter > 0){
	    		System.out.println("remove thread on pause..");
	    		lock.wait(5000);
	    		System.out.println("remove thread resumed.");
	    		counter--;
	    	}
		    return remove();
    	}
	    
    	   
    }
    
    
    /**
     * Closes the {@link AddressCache} and releases all resources.
     * map.clear() method loops through the whole map and sets 
     * every element to null - time complexity would be O(n).
     */
    public void close() {
    	inetCache.clear();
    	head = null;
    	tail = null;
    };
    
    /**
     * Returns the number of elements in the {@link AddressCache}.
     * Constant time complexity, O(1).
     */
    public int size() {
    	return inetCache.size();
    }
    
    /**
     * Returns {@code true} if the {@link AddressCache} is empty.
     * Constant time complexity, O(1).
     */
    public boolean isEmpty() {
    	return inetCache.isEmpty();
    }

    /*
     * Own tests to test for correctness
     */
    public static void main(String[] args) {
    	
    	Scanner reader = new Scanner(System.in);
    	int ttl;
    	try {
	    	System.out.println("Enter custom caching time: ");
	    	ttl = reader.nextInt();
    	} finally {
    		reader.close();
    	}
    	
    	AddressCache test = new InetAddressCache(ttl);
    	// ADD YOUR CUSTOM TEST CASES HERE
    }
}