package worldmanager;

import java.util.List;

import entity.Entity;
import virtualworld.terrain.Point;
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
	Node[] children = new Node[0];
	
	//data for node
	Point center;
	private double size;
	private int depth;
	private int maxDepth = 9;
	List<Entity> entities;
	
	
	//Constructor
	public Node() {}
	
	//takes Entity as an argument and puts it into the smallest node that can still completely hold the Entity
	public void updateEntites(Entity ent) {
		Point cent = ent.getCenter();
		Point currcenter = this.center();
		double sz = ent.getSize();
		double leftent = cent.getX() - sz/2;
		double rightent = cent.getX() + sz/2;
		double upent = cent.getY() - sz/2;
		double downent = cent.getY() + sz/2;
		double leftnode = center.getX() - size/2;
		double rightnode = center.getX() + size/2;
		double upnode = center.getY() - size/2;
		double downnode = center.getY() + size/2;
		boolean test = (leftent < leftnode) && (rightent > rightnode);
		
		if (sz < size && this.getDepth() != maxDepth) {
			if (children.length == 0) {
				this.createChildren();
			}
			if (currcenter.getY() > cent.getY()) {
				if (currcenter.getX() > cent.getX()) {
					children[0].updateEntites(ent);
				}
				else if (currcenter.getX() < cent.getX()) {
					children[1].updateEntites(ent);
				}
			}
			else if (currcenter.getY() < cent.getY()) {
				if (currcenter.getX() > cent.getX()) {
					children[2].updateEntites(ent);
				}
				else if (currcenter.getX() < cent.getX()) {
					children[3].updateEntites(ent);
				}
			}
		} else {
			entities.add(ent);
		}
	}
	
	//updating when connecting to parent or child
	public void updateParent(Node p) {
		parent = p;
	}
	
	//may never be needed because of split function, but keeping for possible unforeseen use cases
	public void updateChild(Node[] c) {
		children = c;
	}
	
	public void updateSize(double sz) {
		size = sz;
	}
	
	public void updateCenter(Point cent) {
		center = cent;
	}
	
	public void updateDepth(int dep) {
		depth = dep;
	}
	
	public void createChildren() {
		double offset = size/4;
		children = new Node[4];
		for (int i = 0; i < 4; i++) {
			Node newchild = new Node();
			Point newcent;
			if (i == 0) {
				newcent = new Point(this.center.getX()-offset, this.center.getY()+offset);
			}
			else if (i == 1) {
				newcent = new Point(this.center.getX()+offset, this.center.getY()+offset);
			}
			else if (i == 2) {
				newcent = new Point(this.center.getX()-offset, this.center.getY()-offset);
			}
			else {
				newcent = new Point(this.center.getX()+offset, this.center.getY()-offset);
			}
			newchild.updateSize(size/2);
			newchild.updateParent(this);
			newchild.updateDepth(this.depth+1);
			newchild.updateCenter(newcent);
			children[i] = newchild;
		}
	}
	
	//Functions to implement
	private Point center() {
		return terrain.getCenter();
	}
	
	public double getSize() {
		return size;
	}
	
	public int getDepth() {
		return depth;
	}
	
	//Gives all entities distance from camera
	public void cameraDist(Point target) {
		while(children.length > 0) {
			for(Entity e: entities) {
				Point start = e.getCenter();
				double numer = start.getY()-target.getY();
				double denom = start.getX()-target.getX();
				double dist = numer/denom;
				e.distFromCamera(dist);
			}
			for(Node newnode: children) {
				newnode.cameraDist(target);
			}
		}
	}
	
	//find out what position of camera uses
	//probably won't need, as cameraDist covers pretty much all cases surrounding camera needs
	public Node findCamera(Point target) {
		Node currNode = this;
		while(currNode.center() != target && currNode.getDepth() != maxDepth) {
			Point currcenter = currNode.center();
			if (currcenter.getY() > target.getY()) {
				if (currcenter.getX() > target.getX()) {
					currNode = children[0];
				}
				else if (currcenter.getX() < target.getX()) {
					currNode = children[1];
				}
			}
			else if (currcenter.getY() < target.getY()) {
				if (currcenter.getX() > target.getX()) {
					currNode = children[2];
				}
				else if (currcenter.getX() < target.getX()) {
					currNode = children[3];
				}
			}
		}
		return currNode;
	}
}
