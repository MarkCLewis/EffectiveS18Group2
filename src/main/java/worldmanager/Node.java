package worldmanager;

import virtualworld.terrain.Pair;
import virtualworld.terrain.Terrain;

public class Node {
	//terrain
	Terrain terrain;

	//Nodes for traversal
	Node parent = null;
	Node[] children = null;
	
	//Constructor
	public Node(Terrain t) {
		terrain = t;
	}
	
	//updating when connecting to parent or child
	public void updateParent(Node p) {
		parent = p;
	}
	
	public void updateChild(Node[] c) {
		children = c;
	}
	
	//Functions to implement
	private Pair<Double,Double> center() {
		return terrain.getCenter();
	}
	
	public Node findCamera() {
		Node current = this;
		while(current.parent != null) {
			current = current.parent;
		}
		return current;
	}
	
	// Function to split. Recommended by Ian.
	// Will talk later to see what the functionality for it he had in mind was as Terrain already has a similar function.
	private void split() {
		//TODO
	}
}
