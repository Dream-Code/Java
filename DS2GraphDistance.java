package homework;  
import algs13.Queue;
import algs41.Graph;
import algs41.GraphGenerator;
import algs42.Digraph;
import stdlib.*;

/**
 *   Version 1.0 
 * 
 *  This is basically exercise 4.1.16 from the text
 *    see the exercise and/or video overview for definitions and hints
 * 
 *   The provided structure follows the design pattern illustrated
 *   by the examples in 4.1
 * 
 *  you're free to add instance variables and other methods
 *  you should add in code to support bfs 
 * 	     feel free to grab and adapt this from the text and/or algs41
 *   you MAY NOT use classes from algs41 as part of your solution
 *     That is: you may NOT create an instance of BreadthFirstPaths,
 *     but you MAY copy code from BreadthFirstPaths to this file
 *   you might find queue or stack to be useful, if so use
 *   the versions from algs13
 * 
 *   you shouldn't need (or use) anything else, ask me if not sure
 *
 *  YOU MUST DOCUMENT your code to explain your approach
 *  If I can't follow what you're doing, you will get reduced (or no) credit
 * 
 */


public class DS2GraphDistance {

	int[] eccentricity;         	// the eccentricity of each vertex
	int diameter;               	// the diameter of the graph
	int radius;	                	// the radius of the graph
	private boolean[] marked;  		// marked[v] = is there an s-v path
	private final int[] edgeTo;     // edgeTo[v] = previous edge on shortest s-v path
	private final int[] distTo;     // distTo[v] = number of edges shortest s-v path


	// this method just illustrates what throwing an exception might look like
	// you can delete this method
	private void anExampleMethod( Graph G) {
		boolean somethingUnpected;
		// pretend we did some investigation of G here
		somethingUnpected = true;

		if ( somethingUnpected) throw new IllegalArgumentException("something bad happened");
		
		
	}


	//  The constructor will initiate all the calculations 
	//  and store the results in the instance variables above
	//
	public DS2GraphDistance(Graph G) {

		this.eccentricity = new int[G.V()];
		marked = new boolean[G.V()];
		edgeTo = new int[G.V()];
		distTo = new int[G.V()];
		int diameterCalc = Integer.MIN_VALUE;   
		int radiusCalc = Integer.MAX_VALUE;
		//int max = eccentricity[0];
		//int min = eccentricity[0];
	
		//anExampleMethod(G);   // just to illustrate calling a method that throws an exception

		// TODO
		// add code here to compute the eccentricity of each vertex, the diameter, the radius 
		// you will probably want to create some methods(functions) and just call them from here
		for(int v = 0; v < G.V(); v++) {
			bfs(G, v); 
			for(int w = 0; w < G.V(); w++) { 
				if (w == v) { continue; }		//self loops are skipped over
					
				//eccentricity is the larger of eccentricty of v AND the distance to w
				if(eccentricity[v] > distTo(w)) {
					eccentricity[v] = eccentricity[v];
				}
				else {
					eccentricity[v] = distTo(w);
				}
			}
				
			//standard MAX calculation
			if(eccentricity[v] > diameterCalc) {
				diameterCalc = eccentricity[v];
				//diameterCalc = max;
			}
			//standard MIN calculation
			else if(eccentricity[v] < radiusCalc) {
				radiusCalc = eccentricity[v];
				//radiusCalc = min;
			}
			
			//throw exception if graph is disconnected
			if(diameterCalc == Integer.MAX_VALUE && G.V() != 0) {
				throw new IllegalArgumentException("Warning: Graph not connected");
			}
		}

		this.diameter = diameterCalc;   
		this.radius   = radiusCalc;

	}
	
	//length of longest shortest path
	private void diameter(int v, int diam) {
		if (eccentricity[v] > diam) {
			diam = eccentricity[v];
		}
		
	}

	//smallest eccentricity of all points in the graph
	private void radius(int v, int rad) {
		if (eccentricity[v] < rad) { 
			radius = eccentricity[v];    
		}
		//return rad;	
	}

	// BFS from from DepthfirstPaths in algs41
	private void bfs(Graph G, int s) { 
        Queue<Integer> q = new Queue<Integer>();
        marked = new boolean[G.V()]; 
        for (int v = 0; v < G.V(); v++) {  distTo[v] = Integer.MAX_VALUE; }  //why does distTo[v] need to be reset?-
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);
        
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }
	
	// Do not change the following constant time methods
	public int eccentricity(int v) { return eccentricity[v]; }
	public int diameter()          { return diameter; }
	public int radius()            { return radius; }
	public boolean isCenter(int v) { return eccentricity[v] == radius; }
	public int distTo(int v) 	   { return distTo[v]; }
	

	public static void main(String[] args) {
		// ToDo   test your class with different graphs by commenting in/out graphs below    

		//Graph G = GraphGenerator.fromIn(new In("data/tinyG.txt")); // this is non-connected -- should throw an exception
		//Graph G = GraphGenerator.connected (10, 20, 2); // Random non-connected graph -- should throw an exception
		Graph G = GraphGenerator.fromIn(new In("data/tinyCG.txt")); // diameter=2, radius=2, every node is a center
		//Graph G = GraphGenerator.binaryTree (10); // A complete binary tree  diameter:5, radius 3
		//Graph G = GraphGenerator.path (6); // A path -- diameter=V-1
	    //Graph G = GraphGenerator.connected (20, 40); // Random connected graph, typical diameter 4 or 5, radius: 3 or 4

		//StdOut.println(G);       // comment in if you want to see the adj list
		//G.toGraphviz ("g.png");  // comment in if you want a png of the graph and you have graphViz installed

		//  nothing to change below here
		try {
			DS2GraphDistance theGraph = new DS2GraphDistance(G);
			for (int v = 0; v < G.V(); v++)
				StdOut.format ("eccentricity of %d: %d\n", v, theGraph.eccentricity (v));
			StdOut.format ("\ndiameter = %d\n\nradius = %d\n\n", theGraph.diameter(), theGraph.radius() );
			StdOut.format ("checking for centers... \n" );
			for (int i = 0; i < G.V(); i++) {
				if ( theGraph.isCenter(i))
					StdOut.format ("center=%d\n", i);
			}
			StdOut.format ("done. \n" );
		} 
		catch (IllegalArgumentException e) {
			StdOut.println( " Exception was caught: " + e.getMessage());
		}
	}
}
