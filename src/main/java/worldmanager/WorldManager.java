package worldmanager;

import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import roads.Road;
import shapes.Shape;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;

public class WorldManager {
	//	TODO
	//	finished - update camera location
	//		finished - 1. resends camera location to all Nodes
	//		finished - 2. only updates after a few feet has walked so it isn't computing every frame/step
	//
	//	finished - tree traversal to collect geometry
	//
	//	Create a function that will return a height at a given Point
	//		1. should find the active terrain on that point, should only be one
	//		2. create a function in Entity that will allow me to turn their active state on/off
	
	//data
	private static WorldManager world = null;
	Node rootNode;
	Point cameraLoc = null;
	
	//This value is how far you can walk before the WorldManager loads more objects
	double cameraStep = 10;
	
	//this value is the absolute farthest you can see.
	double maxView = 10;
	
	//constructor
	private WorldManager(Point cent, double sz) {
		rootNode = new Node();
		rootNode.updateCenter(cent);
		rootNode.updateSize(sz);
		rootNode.updateDepth(0);
	}
	
	public static synchronized WorldManager getInstance() {
		if (world == null) {
			world = new WorldManager(new Point(0,0),536870912);
		}
		return world;
	}
	
	public double getSize() {
		return rootNode.getSize();
	}
	
	//Don't know how the much the point size scales to actual length in game.
	//Initial values are purposefully super low so there's less of a chance of overworking the engine
	//updateMaxView() lets you change the view distance quickly so the sweet spot can be found.
	public void updateMaxView(double viewDistance) {
		maxView = viewDistance;
	}
	
	//similar to updateMaxView(), updateCameraStep() changes length it takes to update loaded objects
	public void updateCameraStep(double dist) {
		cameraStep = dist;
	}
	
	//just in case
	public void updateNode(Node n) {
		rootNode = n;
	}
	
	// add objects
	public void addEntity(Entity ent) {
		rootNode.updateEntites(ent);
	}
	
	//update camera location
	public boolean updateCamera(Point point) {
		if (cameraLoc == null || Node.findDist(point, cameraLoc) > cameraStep) {
			rootNode.cameraDist(point);
			cameraLoc = point;
			updateWorld(point);
			return true;
		}
		return false;
	}
	
	//gets height at a given point
	public double getHeight(Point point) {
		return rootNode.findHeight(point);
	}
	
	//tree traversal to get geometry that is within the max specified distance
	public List<Shape> getGeometry(Point point) {
		if(updateCamera(point)) {
			return traverseGeometry(rootNode, maxView);
		}
		return new ArrayList<>();
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
		if (Node.findDist(node.center, cameraLoc) < max) {
			for(Entity e: ents) {
				if(e.isActive()) {
					if(Node.findDist(e.getCenter(), cameraLoc) < max) {
						if (nodeShapes == null || nodeShapes.isEmpty()) {
							nodeShapes = e.getShapes();
						}
						else {
							nodeShapes.addAll(e.getShapes());
						}
					}
				}
			}
		}
		return nodeShapes;
	}
	
	public static void initializeWorld() {
		WorldManager world = WorldManager.getInstance();
		Point cent = world.rootNode.center;
		double worldSize = world.getSize();
		Terrain t = Terrain.forFields(cent, worldSize, 6);
		Road r = new Road(cent, worldSize);
		world.addEntity(t);
		world.addEntity(r);
		defineWorld(t, cent);
		defineRoads(r, cent);
	}
	
	public static void defineWorld(Terrain t, Point cent) {
		if(Node.findDist(t.getCenter(),cent) < t.getSize()*2 && t.getSize() > 2000) {
			Terrain[] ters = t.split();
			for(Terrain ter: ters) {
				WorldManager.getInstance().addEntity(ter);
				defineWorld(ter,cent);
			}
		}
	}
	
	public static void defineRoads(Road r, Point cent) {
		if((Node.findDist(r.getCenter(),cent) < r.getSize()*16) && !r.isActive()) {
			Road[] roads = r.split();
			for(Road road: roads) {
				WorldManager.getInstance().addEntity(road);
				defineRoads(road,cent);
			}
		}
	}
	
	public static void updateWorld(Point cent) {
		WorldManager world = WorldManager.getInstance();
		List<Terrain> actives = world.activeTerrains();
		for(Terrain t: actives) {
			defineWorld(t,cent);
		}
	}
	
	private List<Terrain> activeTerrains() {
		return rootNode.findActiveTerrains();
	}
}
