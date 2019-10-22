package FedExTracker;

import java.util.ArrayList;

public class FedExDistCenters {
	private static ArrayList<Node> locations;
	private static boolean[] visited;
	
	public static final int NUM_DIST_CENTERS = 25;
	public static final int HQ_INDEX = 13;
	public static final String[] CENTER_LOC = new String[]
			{"Los Angeles, CA", "Chino, CA", "Sacramento, CA", "Seattle, WA", "Phoenix, AZ",
			 "Salt Lake City, UT", "Denver, CO", "Kansas City, KS", "Dallas, TX", "Houston, TX",
			 "Minneapolis, MN", "St. Louis, MO", "New Berlin, WI", "Memphis, TN", "Indianapolis, IN",
			 "Detroit, MI", "Atlanta, GA", "Grove City, OH", "Pittsburgh, PA", "Orlando, FL",
			 "Charlotte, NC", "Martinsburg, WV", "Edison, NJ", "Allentown, PA", "Northborough, MA"};
	public static final int[][] DIST_CENTERS = new int[][]
		{{0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Los Angeles, CA
		 {1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Chino, CA
		 {1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Sacramento, CA
		 {1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Seattle, WA
		 {1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Phoenix, AZ
		 {1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Salt Lake City, UT
		 {0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Denver, CO
		 {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Kansas City, KS
		 {0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Dallas, TX
		 {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Houston, TX
		 {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},	// Minneapolis, MN
		 {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},	// St. Louis, MO
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},	// New Berlin, WI
		 {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},	// Memphis, TN
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},	// Indianapolis, IN
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},	// Detroit, MI
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0},	// Atlanta, GA
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0},	// Grove City, OH
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0},	// Pittsburgh, PA
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},	// Orlando, FL
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0},	// Charlotte, NC
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0},	// Martinsburg, WV
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1},	// Edison, NJ
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1},	// Allentown, PA
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0}};	// Northborough, MA
		 

	public FedExDistCenters() {
		locations = new ArrayList<>();					// Initializing ArrayList of Dist. Centers
		visited = new boolean[NUM_DIST_CENTERS];		// Boolean variable to check if Dist. Center has been visited in Dijkstra's algorithm
		for(int i = 0; i < NUM_DIST_CENTERS; i++) {		// Filling ArrayList with locations of Dist. Centers
			Node node = new Node(CENTER_LOC[i]);
			locations.add(node);
			visited [i] = false;
		}
		
		locations.get(HQ_INDEX).setDistance(0);			// Distance of start vertex from start vertex = 0
		
		// Filling ArrayList with previous node and distance from root node
		this.shortestPath(locations, (locations.get(HQ_INDEX).getDistance() + 1), HQ_INDEX);
	}
	
	public Node getNode(int index) {
		return locations.get(index);
	}
	
	public void addNode(int index, Node node) {
		locations.add(index, node);
	}
	
	public Node removeNode(int index) {
		return locations.remove(index);
	}
	
	public int size() {
		return locations.size();
	}
	
		 
	public void shortestPath(ArrayList<Node> locations, int distance, int index) {
		int minDistance = 50;
		int minIndex = 0;

		// For current vertex, examine unvisited neighbors
		// For current vertex, calculate distance of each neighbor from start vertex
		for(int i = 0; i < NUM_DIST_CENTERS; i++ ) {
			// If the calculated distance of a vertex is less than the known distance, update shortest distance.
			// Update the previous vertex for each update distance
			if(DIST_CENTERS[index][i] == 1 && visited[i] == false) {
				if(distance < locations.get(i).getDistance()) {
					locations.get(i).setDistance(distance);
					locations.get(i).setPrevNode(locations.get(index));
				}
			}
		}
		// Add current vertex to list of visited vertices
		visited[index] = true;
		
		// Visit the unvisited vertex with the smallest known distance from the start vertex
		for(int j = 0; j < NUM_DIST_CENTERS; j++) {
			if((locations.get(j).getDistance() < minDistance) && visited[j] == false) {
				minDistance = locations.get(j).getDistance();
				minIndex = j;
			}	
		}
		// Repeat until all vertices are visited (minimum distance will not change)
		if(minDistance != 50)
			shortestPath(locations, (minDistance + 1), minIndex);	
	}
	
	public String toString() {
		for(int i = 0; i < NUM_DIST_CENTERS; i++) {
			System.out.print(locations.get(i).getLocation() + " : " + locations.get(i).getDistance() + " : ");
			if(locations.get(i).getPrevNode() != null)
				System.out.println(locations.get(i).getPrevNode().getLocation());
			else
				System.out.println("");
		}
		return " ";
	}

}
