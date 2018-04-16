package worldmanager;

import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;

public class WorldManager {
	
	//data
	Node rootNode;
	Point cameraLoc;
	double cameraStep = 10;
	double maxView = 100;
	
	public WorldManager() {}
	
	public void updateNode(Node n) {
		rootNode = n;
	}
	
	// add objects
	public void addEntity(Entity ent) {
		rootNode.updateEntites(ent);
	}
	
	//update camera location
	public void updateCamera(Point point) {
		if (rootNode.findDist(point, cameraLoc) > cameraStep) {
			rootNode.cameraDist(point);
			cameraLoc = point;
		}
	}
	
	//tree traversal to get geometry that is within the max specified distance
	public List<Shape> getGeometry() {
		return traverseGeometry(rootNode, maxView);
	}
	
	//traverses through node tree to collect shapes given back by nodeGeometry()
	private List<Shape> traverseGeometry(Node node, double max) {
		List<Shape> travShapes = nodeGeometry(node, max);
		Node[] travChildren = node.children;
		for(Node n: travChildren) {
			travShapes.addAll(traverseGeometry(n, max));
		}
		return travShapes;
	}
		
	//returns all entities within a node as long as the node in question's center is close enough to the camera
	//returns an empty list if the node is too far away
	private List<Shape> nodeGeometry(Node node, double max) {
		List<Entity> ents = node.getEntities();
		List<Shape> nodeShapes = new ArrayList<>();
		if (node.findDist(node.center, cameraLoc) < max) {
			for(Entity e: ents) {
				if (nodeShapes == null || nodeShapes.isEmpty()) {
					nodeShapes = e.getShapes();
				}
				else {
					nodeShapes.addAll(e.getShapes());
				}
			}
		}
		return nodeShapes;
	}
	
	//	TODO
	//	update camera location
	//		finished - 1. resends camera location to all Nodes
	//		2. only updates after a few feet has walked so it isn't computing every frame/step
	//
	//	tree traversal to collect geometry
	
}
