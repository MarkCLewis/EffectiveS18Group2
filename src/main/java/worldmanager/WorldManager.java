package worldmanager;

import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;

public class WorldManager {
	
	//Starting Node
	Node rootNode;
	
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
		rootNode.cameraDist(point);
	}
	
	//tree traversal for geometry
	public List<Shape> getGeometry() {
		List<Entity> ents = rootNode.getEntities();
		List<Shape> shapes = new ArrayList<>();
		for(Entity e: ents) {
			if (shapes == null || shapes.isEmpty()) {
				shapes = e.getShapes();
			}
			shapes.addAll(e.getShapes());
		}
		return shapes;
	}
	
	//	TODO
	//	update camera location
	//		finished - 1. resends camera location to all Nodes
	//		2. only updates after a few feet has walked so it isn't computing every frame/step
	//
	//	tree traversal to collect geometry
	
}
