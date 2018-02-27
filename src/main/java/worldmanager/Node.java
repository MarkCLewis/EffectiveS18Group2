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
	
	//may never be needed because of split function, but keeping for possible unforeseen use cases
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
	
	// Function to split into sectors and save as children
	private void split() {
		Terrain[] terrs = terrain.split();
		Node [] temp = new Node[terrs.length];
		for (int i = 0; i < terrs.length; i++) {
			Node newNode = new Node(terrs[i]);
			newNode.updateParent(this);
			temp[i] = newNode;
		}
		children = temp;
	}
}
