/* Steven Rivera  
 * 
 * Java program that implements a generic linear probing hash table
 */
package homework; 
import stdlib.*;
import java.time.LocalDate;
import algs13.Queue;
import algs31.SequentialSearchST;

public class DS2LinProbHashST<K, V> {
	private static final int INIT_CAPACITY = 4;

	private int N;          // number of key-value pairs in the symbol table
	private int M;          // size of linear probing table
	private K[] keys;       // the keys
	private V[] vals;       // the values
	
	private  int putCount;  // for experimental data collection
	private  int getCount;  // 

	// create an empty hash table - INIT_CAPACITY as default size
	public DS2LinProbHashST() {
		this(INIT_CAPACITY);
	}

	// create linear probing hash table of given capacity
	@SuppressWarnings("unchecked")
	public DS2LinProbHashST(int capacity) {
		M = capacity;
		keys = (K[]) new Object[M];
		vals = (V[]) new Object[M];
	}

	public void resetPutCount(int val) {   		// for experimental data collection
		putCount = 0;
	}
	public void resetGetCount(int val) {   		// for experimental data collection
		getCount = 0;
	}
	public int getGetCount() { return getCount; } // for experimental data collection
	public int getPutCount() { return putCount; } // for experimental data collection

	// return the number of key-value pairs in the symbol table
	public int size() { return N; }

	// is the symbol table empty?
	public boolean isEmpty() { return size() == 0; }

	// does a key-value pair with the given key exist in the symbol table?
	public boolean contains(K key) { return get(key) != null; }

	// hash function for keys - returns value between 0 and M-1
	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % M;
	}

	// resize the hash table to the given capacity by re-hashing all of the keys
	private void resize(int capacity) {

		DS2LinProbHashST<K, V> temp = new DS2LinProbHashST<>(capacity);
		for (int i = 0; i < M; i++) {
			if (keys[i] != null) {
				temp.put(keys[i], vals[i]);
			}
		}
		keys = temp.keys;
		vals = temp.vals;
		M    = temp.M;
	}

	// insert the key-value pair into the symbol table
	// the book's put function was reorganized to (hopefully) make the experiments more clear
	public void put(K key, V val) {
		if (val == null) delete(key);

		// double table size if 50% full    !!! commented out for experiments
		// if (N >= M/2) resize(2*M);

		

		int i = hash(key);
		putCount++;     		// count the first probe
		if ( keys[i] == null) { // space is available, store the key/value pair
			keys[i] = key;
			vals[i] = val;
			N++;
			return;
		}
		
		//toDo   experiment 3
		//       Modify the code to choose an alternate starting point for linear probing
		//       as described in the instructions
		
		// collision occurred.  Select a place to start probing
		//int start = (i+1) % M;   // normal linear probing starting place
		int start2 = (i + (M * (1/3)));
		putCount++;
		for (i = start2; keys[i] != null; i = (i + 1) % M) {   // linear probing
			putCount++;
			if (keys[i].equals(key)) { 
				vals[i] = val; 
				return; 
			}
		}
		// empty location found at location i, insert key/value pair
		keys[i] = key;
		vals[i] = val;
		N++;
	}

	// return the value associated with the given key, null if no such value
	public V get(K key) {
		int i;
		// this search *should* match the search done by put, otherwise?
		// Since the experiments don't use get, you do not need to do this for this homework 6
		for (i=hash(key); keys[i] != null; i = (i + 1) % M) {
			getCount++;
			if (keys[i].equals(key))
				return vals[i];
		}
		return null;
	}

	// delete the key (and associated value) from the symbol table
	public void delete(K key) {
		if (!contains(key)) return;

		// delete would need to be modified to work with the adjusted 'put'
		// Since the experiments don't use delete, you do not need to do this for this homework 
		
		
		// find position i of key
		int i = hash(key);
		while (!key.equals(keys[i])) {
			i = (i + 1) % M;
		}

		// delete key and associated value
		keys[i] = null;
		vals[i] = null;

		// rehash all keys in same cluster
		i = (i + 1) % M;
		while (keys[i] != null) {
			// delete keys[i] an vals[i] and reinsert
			K keyToRehash = keys[i];
			V valToRehash = vals[i];
			keys[i] = null;
			vals[i] = null;
			N--;
			put(keyToRehash, valToRehash);
			i = (i + 1) % M;
		}

		N--;

		// halves size of array if it's 12.5% full or less
		if (N > 0 && N <= M/8) resize(M/2);

		assert check();
	}

	// return all of the keys as an Iterable
	public Iterable<K> keys() {
		Queue<K> queue = new Queue<>();
		for (int i = 0; i < M; i++)
			if (keys[i] != null) queue.enqueue(keys[i]);
		return queue;
	}

	// integrity check - don't check after each put() because
	// integrity not maintained during a delete()
	private boolean check() {

		// check that hash table is at most 50% full   commented out for experiment
		//if (M < 2*N) {
		//	System.err.println("Hash table size M = " + M + "; array size N = " + N);
		//	return false;
		//}

		// check that each key in table can be found by get()
		for (int i = 0; i < M; i++) {
			if (keys[i] == null) continue;
			else if (get(keys[i]) != vals[i]) {
				System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
				return false;
			}
		}
		return true;
	}
	/*  putExperiment
	 * 
	 * determine the average number of compares required to 'put' a specified number of keys
	 * into a hash table 
	 * 
	 * method:  
	 * 1) read in some data from a file, store them in an auxiliary symbol table
	 *       to be used to populate our testing hash table
	 * 2) put the desired number of keys from the auxiliary table into the testing hash table
	 *   (the put method counts the number of compares required for each put)
	 * 3)  print the average number of compares
	 * 4)  repeat steps 2-5 for the other table sizes
	 */
	public static void putExperiment( double loadFactor, String file, boolean checkIntegrity) { 
		//  'read-in'  data ; place into an auxiliary ST 
		//  (this eliminates any duplicates and guarantees that every
		//  'put' adds a new value to the table ) 
		//   other iterable collections could be made to work  

		SequentialSearchST<GpsTime, String> sourceData = new SequentialSearchST<>();

		double longitude, latitude;

		//In in = new In (file);  // reading from the file passed to the function
		StdIn.fromFile(file);
		while (! StdIn.isEmpty()) {
			longitude = StdIn.readDouble();
			latitude = StdIn.readDouble();
			LocalDate x = LocalDate.parse(StdIn.readString());

			//ToDo note:  make sure your GpsTime class provides this constructor form
			GpsTime key = new GpsTime( longitude, latitude, x);
	
			sourceData.put(key, x.toString());  
			// for simplicity, all  'values' are: the key date - in string form 
		}
		// file has been read 
        if ( sourceData.size() <10000)   // we want at least 10000 unique keys
        	System.err.println("\nError in sourceData table constuction");
        
		// do 10 experiments  with expSize ranging from 1000, 2000, ..., 10000
		// for each experiment size, we create a table with extra space specified by the loadFactor
		// loadFactor: 0.50  means the table will be only half full, there is twice as much space as we need
		// loadFactor: 0.80  means the table will be 80% full,  20% of the space will not be used

		StdOut.format("\n Put Experiment results. LoadFactor %5.3f\n", loadFactor);
		StdOut.format(" ----------------------------------------\n");

		for ( int expSize = 1000; expSize <= 10000; expSize+=1000) {
			int expTableSize = (int) ( expSize /loadFactor);  
			DS2LinProbHashST<GpsTime, String> aHashST = new DS2LinProbHashST<>(expTableSize);

			aHashST.resetPutCount(0);
			// fill the testing hash table from the auxiliary table
			int count = 0;
			for ( GpsTime s: sourceData.keys() ) {    //iterate through aux table
				aHashST.put(s, sourceData.get(s));
				count++;
				if ( count == expSize) break; // all puts done.
			}

			if (checkIntegrity)
				if ( aHashST.check()) 
					StdOut.println(" Integrity check passes");
				else
					StdOut.println(" Integrity check fails");

			double avgNumComp = aHashST.getPutCount()/(double)expSize;
			StdOut.format(" expSize  %5d  average number of compares  %6.2f \n", expSize, avgNumComp);

		}
	}
 public static void equalsTest() {
	 GpsTime one = new GpsTime(1.1,1.2, LocalDate.parse("2016-08-23"));
	 GpsTime two = new GpsTime(1.1,1.2, LocalDate.parse("2016-08-23"));
	 GpsTime three = new GpsTime(1.1,9.9, LocalDate.parse("2016-08-23"));
	 if ( one.equals(two)) 
		 StdOut.println("GPSTime equals test1 passes");
	 else
		 StdOut.println("GPSTime equals test1 fails");
	 if ( !one.equals(three)) 
		 StdOut.println("GPSTime equals test2 passes");
	 else
		 StdOut.println("GPSTime equals test2 fails");
 }
	/* *********************************************************************
	 *  Unit test client.
	 ***********************************************************************/
	public static void main(String[] args) {
		putExperiment( 0.8, "data/hw6.txt", true);   // true -  check integrity 
		StdOut.println("-----------------");
		equalsTest();  // test the  GPS equals function
	}
}
