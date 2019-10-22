package FedExTracker;

public class Node {
	private String location;
	private int distance;
	private Node prevNode;
	
	public Node() {
		this.location = "";
		this.distance = 50;
		this.prevNode = null;
	}
	
	public Node(String location) {
		this.location = location;
		this.distance = 50;
		this.prevNode = null;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public Node getPrevNode() {
		return prevNode;
	}
	
	public void setPrevNode(Node prevNode) {
		this.prevNode = prevNode;
	}


}
