package worldmanager;

import virtualworld.terrain.Pair;
import virtualworld.terrain.Terrain;

public class Node {
	
	//TODO
	// Create entity interface for all classes that use WorldManager- terrain, Sheep, Trees, Towns, Roads, and clouds (Water too?)
	// Make a list of entities that exist within specific node
	// Also Node should hold center value outside of Terrain, and size
	// items of a given size should be held in node that is big enough to hold object and the object's center exists within
	// limit node depth / make minimum size of node
	
	//terrain
	Terrain terrain;

	//Nodes for traversal
	Node parent = null;
	Node[] children = null;
	private double size;
	
	
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
	
	//find out what position of camera uses
	public Node findCamera(Pair<Double,Double> target) {
		Node currNode = this;
		while(currNode.center() != target) {
			Pair<Double,Double> currcenter = currNode.center();
			if (currcenter.getRight() > target.getRight()) {
				if (currcenter.getLeft() > target.getLeft()) {
					currNode = children[0];
				}
				else if (currcenter.getLeft() < target.getLeft()) {
					currNode = children[1];
				}
			}
			else if (currcenter.getLeft() < target.getLeft()) {
				if (currcenter.getLeft() > target.getLeft()) {
					currNode = children[2];
				}
				else if (currcenter.getLeft() < target.getLeft()) {
					currNode = children[3];
				}
			}
		}
		return currNode;
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
