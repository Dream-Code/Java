/*
 * Name: Steven Rivera
 * 
 * Java program that creates and tests a generic sorted array symbol table
 */
package homework;  

import stdlib.StdOut;

public class SortedArrayST<Key extends Comparable<Key>, Value> {
	private static final int MIN_SIZE = 2;
	private Key[] keys;      // the keys array
	private Value[] vals;    // the values array
	private int N = 0;       // size of the symbol table, 
	// may be less than the size of the arrays

	/**
	 * Constructor
	 * 
	 * Initializes an empty symbol table.
	 */
	public SortedArrayST() {
		this(MIN_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * Initializes an empty symbol table of given size.
	 */
	@SuppressWarnings("unchecked")   // this tells the compiler that the Generic code below is actually okay
	public SortedArrayST(int size) {
		keys = (Key[])(new Comparable[size]);
		vals = (Value[])(new Object[size]);
	}

	/**
	 * Constructor
	 * 
	 * Initializes a symbol table with given sorted key-value pairs.
	 * If given keys list is not sorted in (strictly) increasing order,
	 * then the input is discarded and an empty symbol table is initialized.
	 */
	public SortedArrayST(Key[] keys, Value[] vals) {
		this(keys.length < MIN_SIZE ? MIN_SIZE : keys.length);
		N = (keys.length == vals.length ? keys.length : 0);
		int i;
		for (i = 1; i < N && keys[i].compareTo(keys[i - 1]) > 0; i++);
		if (i < N) { // input is not sorted
			System.err.println("SortedArrayST(Key[], Value[]) constructor error:");
			System.err.println("Given keys array of size " + N + " was not sorted!");
			System.err.println("Initializing an empty symbol table!");
			N = 0;
		} else {
			for (i = 0; i < N; i++) {
				this.keys[i] = keys[i];
				this.vals[i] = vals[i];
			}
		}
	}

	/**
	 * keysArray
	 * 
	 * Returns the keys array of this symbol table.
	 */
	public Comparable<Key>[] keysArray() {
		return keys;
	}

	/**
	 * valsArray
	 * 
	 * Returns the values array of this symbol table.
	 */
	public Object[] valsArray() {
		return vals;
	}

	/**
	 * size
	 * 
	 * Returns the number of keys in this symbol table.
	 */
	public int size() {
		return N;
	}

	/**
	 * checkFor
	 * 
	 * Returns whether the given key is contained in this symbol table at index r.
	 */
	private boolean checkFor(Key key, int r) {
		return (r >= 0 && r < N && key.equals(keys[r]));
	}

	/**
	 * get
	 * 
	 * Returns the value associated with the given key in this symbol table.
	 */
	public Value get(Key key) {
		int r = rank(key);
		if (checkFor(key, r)) return vals[r];
		else return null; 
	}

	/**
	 * put
	 * 
	 * Inserts the specified key-value pair into the symbol table, overwriting the old 
	 * value with the new value if the symbol table already contains the specified key.
	 * Deletes the specified key (and its associated value) from this symbol table
	 * if the specified value is null.
	 */
	public void put(Key key, Value val) {
		int r = rank(key);
		if (!checkFor(key, r)) {
			shiftRight(r);       //  make space for new key/value pair
			keys[r] = key;       //  put the new key in the table

		}
		vals[r] = val;           //  ?
	}

	/**
	 * delete
	 * 
	 * Removes the specified key and its associated value from this symbol table     
	 * (if the key is in this symbol table). 
	 */
	public void delete(Key key) {
		int r = rank(key);
		if (checkFor(key, r)) {
			shiftLeft(r);        // remove the specified key/value 

		}
	}

	/**
	 * contains
	 * 
	 * return true if key is in the table
	 */
	public boolean contains(Key key) {
		return ( this.get(key)!= null);
	}

	/**
	 * resize
	 * 
	 * resize the underlying arrays to the specified size
	 * copy old contents to newly allocated storage
	 */
	@SuppressWarnings("unchecked")  // tell the compiler the following Generic code is okay
	private void resize(int capacity) {
		if (capacity <= N) throw new IllegalArgumentException ();
		Key[] tempk = (Key[]) new Comparable[capacity];
		Value[] tempv = (Value[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			tempk[i] = keys[i];
			tempv[i] = vals[i];
		}
		vals = tempv;
		keys = tempk;
	}

	
	/**
	 * shiftLeft
	 * 
	 * preconditions:
	 *     r >=0 
	 *     N > 0 
	 * postcondition:
	 *     the keys (and values) at indices x > r shifted to the left by one
	 *     in effect, removing the key and value at index r 
	 *     'clear' the original 'last' elements by setting them to null 
	 *  this function does NOT need to decrease the size of the underlying arrays 
	 */
	private void shiftLeft(int r) {
		// ToDo 1.0:  implement this function, see also: ToDo 1.1, 1.2
		
		for (int j = r; j < N-1; j++)  {
	        keys[j] = keys[j+1];
	        vals[j] = vals[j+1];
		}
		
		N--;					//decreases the size of N by 1
	    keys[N] = null;   		//sets last key to null
	    vals[N] = null;			//sets last value to null
	}


	
	
	
	/**
	 * shiftRight
	 * 
	 * preconditons ?
	 * 
	 * Shifts the keys (and values) at indices r and larger to the right by one
	 * The key and value at position r do not change.
	 * This function must call the resize method (if needed) to  increase the size of the
	 * underlying keys,vals arrays
	 * 
	 * 
	 * 
	 */
	private void shiftRight(int r) {
		resize(2 * keys.length);    //resize from the outset
		
		for (int j = N; j > r; j--)  {
	        keys[j] = keys[j - 1];
	        vals[j] = vals[j - 1];    
		}  
	    N++;
	}

	/**
	 * floor
	 * 
	 * floor returns the largest key in the symbol table that is less than or equal to key.
	 * it returns null if there is no such key.
	 * must be logarithmic time for full credit.   Hint :  rank
	 */
	public Key floor(Key key) {
		int floorKey = rank(key);
		
		if (floorKey == 0) return null;
	    if (floorKey < N && key.compareTo(keys[floorKey]) == 0) { 
	        return keys[floorKey];
	    }
	    else{
	    	return keys[floorKey - 1];
	    }


	}
	/**
	 * countRange
	 * 
	 * countRange returns the number of keys in the table within the range [key1, key2] (inclusive)
	 * note that keys may not be in order (key1 may be larger than key2): your code should still consider
	 * this a valid range and report the result.
	 * must run in logarithmic time for full credit.  hint: rank
	 */
	public int countRange(Key key1, Key key2) {
		
		Key temp = key1;
		
		if(key1.compareTo(key2) > 0) {       	// when key2 < key1 we just swap it
			key1 = key2;
			key2 = temp;
		
			return rank(key2) - rank(key1) + 1;
		}
		else if(contains(key2)) {					// when key2 > key1
			return rank(key2) - rank(key1) + 1;
		}
		else{
			return rank(key2) - rank(key1);
		}
		
	}

	/**
	 * rank returns the number of keys in this symbol table that is less than the given key. 
	 */
	public int rank(Key key) {
		
		//Standard binary search algorithm returning the value of lo where lo == to the rank of the key
		
		int lo = 0;					//first element of array + checks for empty array
		int hi = keys.length - 1;	//last element of array
		
		while (lo <= hi){
			int mid = lo + (hi - lo) / 2;
			if (key.compareTo(keys[mid]) < 0) { 
				hi = mid - 1;
			}
			else if (key.compareTo(keys[mid]) > 0) { 
				lo = mid + 1;
			}
			else return mid;
		}
		
		return lo;
		//return linearTimeRank(key);

	}

	/**
	 * Linear time implementation of rank
	 */
	private int linearTimeRank(Key key) {
		int r;
		for (r = 0; r < N && key.compareTo(keys[r]) > 0; r++);
		return r;
	}



	/**
	 * Driver program
	 * 
	 * run all the tests
	 */
	public static void main(String[] args) {

		allShiftLeftTests();
		StdOut.println("Shift-left tests done");
		
		allShiftRightTests();
		StdOut.println("Shift-right tests done");

		allFloorTests();
		StdOut.println("Floor tests done");
		
		allCountRangeTests();
		StdOut.println("CountRange tests done");
		
		allRankTests();
		StdOut.println("Rank tests done");


	}

	public static void allShiftLeftTests() {
		// Testing the delete shiftLeft implementation by calling delete
		// Inputs: String of keys, String of values, key to delete, expected keys & values
		testDelete("ABDFK","12345", "A","BDFK","2345");    // delete first
		testDelete("ABDFK","12345", "B","ADFK","1345");    // delete from 'middle'
		testDelete("ABDFK","12345", "K","ABDF","1234");    // delete last
		
		/*       My Tests for SHIFT LEFT below        */
		testDelete("ABDFK","12345", "0","ABDFK","12345"); 	//testing value not in array
		testDelete("ABDFK","12345", "BD","ABDFK","12345");  //testing delete of multiple values
		
	

	}
	public static void allShiftRightTests() {

		// Testing the shiftRight implementation  by calling  put
		//  Inputs: String of keys, String of values, key,value to insert, expected keys & values
		testPut("AEIOU","13456", "B","2", "ABEIOU","123456");
		
		/*       My Tests for SHIFT RIGHT below        */
		testPut("AEIOU","13456", "Y","7", "AEIOUY","134567");   //adding to end of array
		testPut("AEIOU","13456", "K","4", "AEIKOU","134456");	//adding new key with a duplicate value

		
	}
	public static void allFloorTests() {
		// Testing the floor function  
		//  Inputs:String of keys, String of values, key to test, expected answer
		testFloor("AEIOU","13456", "B","A");  
		testFloor("AEIOU","13456", "E","E");
		testFloor("AEIOU","13456", "Z","U");
		testFloor("VWXYZ","13456", "A",null);
		
		/*       My Tests for FLOOR below        */
		testFloor("12345","ABCDE", "2","2");
		testFloor("PQRST","13456", "Q", "Q");
		testFloor("12ABC","13456", "D","C");
		testFloor("AEIOU","13456", "B","A");
		
		
	}

	public static void allCountRangeTests() {
		// Testing the countRange function  
		//  Inputs: String of keys, String of values, range: [key1,key2]  expected answer
		testCountRange("BEIOU","13456", "B","U", 5);   // whole Range
		testCountRange("BEIOU","13456", "U","B", 5);   // whole Range
		testCountRange("BEIOU","13456", "A","Z",5);    // whole Range
		testCountRange("BEIOU","13456", "C","P",3);    // partial Range
		
		/*       My Tests for COUNT RANGES below        */
		testCountRange("BEIOU","13456", "I","U", 3);   //partial range with both keys present  --> CORRECT
		testCountRange("13456","BEIOU", "1","6", 5);
		testCountRange("12ABC","24689", "C","2", 4);   // Another attempt at key2 < key1
		testCountRange("BEIOU","13456", "E","I", 2);

	}
	public static void allRankTests() {

		// Testing the rank function
		// Inputs: String of keys, String of values, Key to search on, expected answer
		testRank("BDFK","1234", "A",0);
		testRank("BDFK","1234","B",0);
		testRank("BDFK","1234","C",1);
		testRank("BDFK","1234","D",1);
		testRank("BDFK","1234","K",3);
		testRank("BDFK","1234","Z",4);
		

	}


	/*  testing suite  
	 * 
	 *   nothing for you to change below
	 * 
	 * */

	/*
	 * 	from
	 * 
	 *    a Utility function used by the testing framework to
	 *    build and return a symbol table from a pair of strings.
	 *    The individual characters of each string are extracted as substrings of length 1
	 *    and then stored in parallel arrays.
	 *    The parallel arrays are used as input to the SortArrayST constructor
	 *    The characters in the keyData need to be in sorted order.
	 *    
	 */
	public static SortedArrayST<String,String> from (String keyData, String valData) {
		int n = keyData.length();
		if ( n != valData.length()) throw new NullPointerException(" from: mismatch sizes");
		String[] keys = new String[n];
		String[] vals = new String[n];
		for (int i=0; i < n; i++ ) {
			keys[i] = keyData.substring(i, i+1);  // ith key is ith char-string of keyData string
			vals[i] = valData.substring(i, i+1);  // ith key is ith char-string of valData string
		}
		return new SortedArrayST(keys,vals);
	}


	/*
	 * testRank
	 * 
	 * Test the rank function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the rank function, compare to the expected answer
	 */
	public static void testRank( String keyData, String valData, String theKey, int expected) {
		SortedArrayST<String, String> x = from(keyData,valData);
		int actual = x.rank(theKey);
		if ( actual == expected)  // test passes
			StdOut.format("rankTest: Correct  Keys: %s, searchKey %s    actual: %d expected: %d\n", keyData, theKey, actual,expected);
		else
			StdOut.format("rankTest: *Error*  Keys: %s, searchKey %s    actual: %d expected: %d\n", keyData, theKey, actual,expected);

	}
	/*
	 * testPut
	 * 
	 * Test the Put function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the put function, compare to the expected answer
	 */
	public static void testPut(String keyInData, String valInData, 
			String putKey, String putVal, 
			String keyOutData, String valOutData) {
		SortedArrayST<String, String> actual = from(keyInData,valInData);
		SortedArrayST<String, String> expected = from(keyOutData, valOutData);
		actual.put(putKey, putVal);
		String actualKeys = actual.keysString();
		String actualVals = actual.valsString();
		String expectedKeys = expected.keysString();
		String expectedVals = expected.valsString();


		if ( actualKeys.equals(expectedKeys) && actualVals.equals(expectedVals) )  // test passes
			StdOut.format("testPut: Correct  actual keys %s  expected keys %s\n", actualKeys,expectedKeys);
		else {
			StdOut.format("testput: *Error*  actual keys %s  expected keys %s\n", actualKeys,expectedKeys);
			StdOut.format("                  actual vals %s  expected keys %s\n", actualVals,expectedVals);
		}

	}
	/*
	 * testDelete
	 * 
	 * Test the delete function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the delete function, compare to the expected answer
	 */
	public static void testDelete(String keyInData, String valInData, String delKey, 
			String keyOutData, String valOutData) {
		SortedArrayST<String, String> actual = from(keyInData,valInData);
		SortedArrayST<String, String> expected = from(keyOutData, valOutData);
		actual.delete(delKey);
		String actualKeys = actual.keysString();
		String actualVals = actual.valsString();
		String expectedKeys = expected.keysString();
		String expectedVals = expected.valsString();


		if ( actualKeys.equals(expectedKeys) && actualVals.equals(expectedVals) )  // test passes
			StdOut.format("testDelete: Correct  actual keys %s  expected keys %s\n", actualKeys,expectedKeys);
		else {
			StdOut.format("testDelete: *Error*  actual keys %s  expected keys %s\n", actualKeys,expectedKeys);
			StdOut.format("                     actual vals %s  expected vals %s\n", actualVals,expectedVals);
		}
	}
	/*
	 * testFloor
	 * 
	 * Test the floor function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the floor function, compare to the expected answer
	 * 
	 */
	public static void testFloor( String keyData, String valData, String theKey, String expected) {
		SortedArrayST<String, String> x = from(keyData,valData);
		String actual = x.floor(theKey);
		//report result
		if ( expected == null) {
			if (actual == null)
				StdOut.format("floorTest: Correct  String %s Answer: null\n", keyData);
			else
				StdOut.format("floorTest: *Error*  String %s Expected: null Actual: %s\n", keyData,actual);
			return;
		}
		if (actual == null && expected != null ) { // error

			StdOut.format("floorTest: *Error*  String %s Expected: %s Actual: null\n", keyData,expected);
			return;
		}

		if ( actual.equals(expected))  // test passes
			StdOut.format("floorTest: Correct  String %s Actual: %s\n", keyData,actual);
		else
			StdOut.format("floorTest: *Error*  String %s Expected %s Actual: %s\n", keyData,expected,actual);
	}
	/*
	 * testCountRange
	 * 
	 * Test the countRange function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the countRange function, compare to the expected answer
	 * 	testCountRange("BEIOU","13456", "B","U", 5);   // whole Range
	 */
	public static void testCountRange( String keyData, String valData, String key1,String key2, int expected) {
		SortedArrayST<String, String> x = from(keyData,valData);
		int actual = x.countRange(key1,key2);
		if ( actual == expected)  // test passes
			StdOut.format("countRangeTest: Correct  Keys: %s, key1: %s  key2: %s     actual: %d expected: %d\n", keyData, key1,key2, actual,expected);
		else
			StdOut.format("countRangeTest: *Error*  Keys: %s, key1: %s  key2: %s     actual: %d expected: %d\n", keyData, key1,key2, actual,expected);

	}
	/* keysString
	 * 
	 * returns the keys of the table as a single String
	 * used by the testing suite
	 */
	public  String keysString(){
		StringBuilder S = new StringBuilder();
		S.append('[');
		for (int i=0; i < N; i++) 
			S.append(keys[i]);
		S.append(']');
		return S.toString();
	}
	/* valsString
	 * 
	 * returns the values of the table as a single String
	 * used by the testing suite
	 */
	public  String valsString(){
		StringBuilder S = new StringBuilder();
		S.append('[');
		for (int i=0; i < N; i++) 
			S.append(vals[i]);
		S.append(']');
		return S.toString();
	}



}
