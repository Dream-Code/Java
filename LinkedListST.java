/*
 * Name: Steven Rivera
 * 
 * Java program that implements a linked list symbol table 
 */
package homework;
import algs13.Queue;
import stdlib.StdOut;

/**
 *  The LinkedListST class implements methods of the Ordered Symbol table API using
 *  an *unordered* linked-list of generic key-value pairs.  
 *  The methods:  put, get, and delete are already implemented
 */
public class LinkedListST<Key extends Comparable<Key>, Value extends Comparable<Value>> {
	private Node first;      // the linked list of key-value pairs

    // a helper linked list data type
    private class Node {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next)  {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public LinkedListST() {
    	first = null;
    }

    /**
     * Returns the value associated with the given key in this symbol table.
     */
    public Value get(Key key) {
        if (key == null) throw new NullPointerException("argument to get() is null"); 
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key))
                return x.val;
        }
        return null;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is null.
     */
    public void put(Key key, Value val) {
        if (key == null) throw new NullPointerException("first argument to put() is null"); 
        if (val == null) {
            delete(key);
            return;
        }

        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        first = new Node(key, val, first);
    }
    
    public void add(Key key, Value val) {
    	if(key == null) throw new NullPointerException("first argument to put() is null");
    	if(val == null) {
    		delete(key);
    		return;
    	}
    	first = new Node(key, val, first);
    }

    /**
     * Removes the specified key and its associated value from this symbol table     
     * (if the key is in this symbol table).    
     */
    public void delete(Key key) {
        if (key == null) throw new NullPointerException("argument to delete() is null"); 
        first = delete(first, key);
    }

    // delete key in sub-list beginning at Node x
    // return: the sub-list with the key removed
    // warning: function call stack too large if table is large
    private Node delete(Node x, Key key) {
    	
    	
        if (x == null) return null;
        if (key.equals(x.key)) {
            return x.next;
        }
        x.next = delete(x.next, key);
        return x;
    }

    /**
     * size returns the number of key-value pairs in the symbol table.
     * it returns 0 if the symbol table is empty.
     */
   
    public int size () {
    	
    	int counter = 0;  					  
    	Node node = first;                    // starts at first element in linked list
    	
    	while(node != null) {                 // this is the base case
    		counter++;			              // increments counter for each element
    		node = node.next;                 // shifts node to the node in the list until null
    	}
		return counter;
    }
    /**
     * secondLargestKey returns the second largest key in the symbol table.
     * it returns null if the symbol table is empty or if it has only one key.
     * See if you can write it with only one loop
     */
    public Key secondLargestKey () {
		
    	Node node = first;
    	Key secondKey = null; 
    	Key maxKey = null;
        
        if(node == null) return null;
        
        while (node != null){			//base case
            if(maxKey == null){			//also initializing max when list is not empty
                maxKey = node.key;
            } 
            else if(secondKey==null){						//initialized max
                if(node.key.compareTo(maxKey) < 0){
                    secondKey = node.key;
                    
                } 
                else if(node.key.compareTo(maxKey) > 0){   //initialized second max
                    secondKey = maxKey;
                    maxKey = node.key;
                }
            } 
            else{
                if(node.key.compareTo(maxKey) > 0){
                    secondKey = maxKey;
                    maxKey = node.key;
                } 
                else if(node.key.compareTo(maxKey) < 0){
                    if(node.key.compareTo(secondKey) > 0){
                        secondKey = node.key;
                    }
                }
            }
            
            node = node.next;
        }
        
    	return secondKey;
    }


    /**
     * rank returns the number of keys in this symbol table that are less than the parameter key.
     */
    
    public int rank (Key key) {
    	int rank = 0;								//counter
    	return rankHelper(key, rank, first); 		//calling to helper method with our node and key passed to method
    }
    
    // Helper function
    public int rankHelper(Key key, int rank, Node node) {
    	
    	if(node == null) return rank;      // if the list is empty or at end of list return the counter
        if(node.key.compareTo(key) < 0) {  // if the key at node is less than to key passed to method, increment counter
        	rank++;
        }
        
        return rankHelper(key, rank, node.next); //recursively move to next node in the list until null
    }

    /**
     * floor returns the largest key in the symbol table that is less than or equal to the given key.
     * it returns null if there is no such key.
     */
   
	public Key floor (Key key) {
		
		Key floorKey = null;					//temp variable
        Node node = first;
        
        if(node == null) {						
        	StdOut.println("Floor **ERROR**: EMPTY LIST");
        	System.exit(0);						
        }
        
        while (node != null){			//base case
            if(node.key.compareTo(key) <= 0){
                if(floorKey == null || floorKey.compareTo(node.key) <= 0){  //test if less than or equal to key to assign key to temp
                    floorKey = node.key;
                }
            }
            node = node.next;					
        }
    	return floorKey;	
    }
    
    
    
    /**
     * inverse returns the inverse of this symbol table.
     * if the symbol table contains duplicate values, you can use any of the keys for the inverse values
     */
    public LinkedListST<Value, Key> inverse (Key key) {
    	
    	Node node = first;
    	
    	if(this.first == null) return null;   //ALWAYS DO THIS. Get rid of empty list/data structure
		
		while(node != null) {
			if(node.key.equals(key)) {			// if key at current node is equal to key passed to method
				Value val = get(node.key); 		// creates a temp node called val and assigns the value of the key at node x
				delete(key);					// deletes the key/value pair
				put((Key)val, (Value)key);		// reverses the key and value pair by casting
			}
			
			node = node.next;					// traverses the list
		}
    	return null;	
    }
    
    /**
     * 
     * find a duplicate pairs in a linked list
     * 
     **/
    public int dupeTest() {
		int counter = 0;
    	if(first == null || first.next == null) return 0;   //base case
    	
    	for(Node temp = first; temp.next != null; temp = temp.next) {
    		if(temp.key.compareTo(temp.next.key) == 0) {
    			counter++;
    		}                                                                                           
    	}
    	
		return counter;
	}
    
    
    
    public Iterable<Key>  keys() {
    	Queue<Key> theKeys = new Queue<Key>();
    	for ( Node temp = first; temp != null; temp=temp.next) {
    		theKeys.enqueue(temp.key);
    	}
    	return theKeys;
    }

	
}