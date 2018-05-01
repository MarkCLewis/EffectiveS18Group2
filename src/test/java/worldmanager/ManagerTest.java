package worldmanager;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.NormalHeightAlgorithm;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;
import virtualworld.terrain.TerrainHeightAlgorithm;

public class ManagerTest {
	
	Point p1 = new Point(0,0);	
	WorldManager world = WorldManager.getInstance();
	WorldManager world2 = WorldManager.getInstance();
	List<Entity> entlist = new ArrayList<>();
	
	public int getMax(Node nod) {
		int depthSoFar = 0;
		for(Node n: nod.children) {
			int i = getMax(n) + 1;
			if(i > depthSoFar) {
				depthSoFar = i;
			}
		}
		return depthSoFar;
	}
	
	@Test
	public void test() {
		
		double length = 5000;
		int seed = 500;
		int points = 61;
		double[][] heightMap = {{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0}};

		
		//world.addEntity(t);
		//Terrain t = new Terrain(new Point(0.0,0.0), length, seed, points, heightMap);
		Terrain terr = Terrain.forHills(new Point(0.0,0.0), length, 100);
		//world.addEntity(terr);
		
		System.out.println("Starting test:");
		
		Node node = world.rootNode;
		world.updateMaxView(2000);
		
		/*Random rand = new Random();
		for(int i = 0; i < 25; i++) {
			double x = rand.nextDouble() + rand.nextInt(4999) - 2500;
			double y = rand.nextDouble() + rand.nextInt(4999) - 2500;
			Point p = new Point(x,y);
			double h = world.getHeight(p);
			TestEntity tester = new TestEntity(p,2.4, h);
			System.out.println(h);
			System.out.println(tester.getSize());
			System.out.println(node.findDist(p, p1));
			entlist.add(tester);
			world.addEntity(tester);
		}*/
		/*for(Shape s : allShapes) {
			System.out.println(s);
			System.out.println(s.getCenter()[0]);
			System.out.println(s.getCenter()[1]);
			System.out.println(s.getCenter()[2]);
		}*/
		
		Point p1 = new Point(0,1999);
		Point p2 = new Point(100,0);
		Point p3 = new Point(0,-100);
		Point p4 = new Point(-100,0);
		Point p5 = new Point(0,4000);
		Point p6 = new Point(4000,0);
		Point p7 = new Point(0,-4000);
		Point p8 = new Point(-4000,0);
		
		entlist.add(new TestEntity(p1,2.4,5)); //world.getHeight(p1)));
		entlist.add(new TestEntity(p2,2.4,5)); //world.getHeight(p2)));
		entlist.add(new TestEntity(p3,2.4,5)); //world.getHeight(p3)));
		entlist.add(new TestEntity(p4,2.4,5)); //world.getHeight(p4)));
		entlist.add(new TestEntity(p5,2.4,5)); //world.getHeight(p5)));
		entlist.add(new TestEntity(p6,2.4,5)); //world.getHeight(p6)));
		entlist.add(new TestEntity(p7,2.4,5)); //world.getHeight(p7)));
		entlist.add(new TestEntity(p8,2.4,5)); //world.getHeight(p8)));
		
		for(Entity e: entlist) {
			world.addEntity(e);
		}
		
		List<Shape> allShapes = world.getGeometry(new Point(0.0,0.0));	
		
		boolean b = entlist.size() == 8;
		
		System.out.println(getMax(node));
		System.out.println(allShapes.size());
		System.out.println(world.cameraLoc.getX() + " " + world.cameraLoc.getZ());
		
		assertTrue(b);
		assertTrue(allShapes.size() == 4);
		
		System.out.println("Moving small step:");
		allShapes = world.getGeometry(new Point(0.0,-5.0));
		System.out.println(allShapes.size());
		System.out.println(world.cameraLoc.getX() + " " + world.cameraLoc.getZ());
		
		assertTrue(allShapes.size() == 4);
		
		System.out.println("Moving big step:");
		allShapes = world.getGeometry(new Point(0.0, 3000));
		System.out.println(allShapes.size());
		System.out.println(world.cameraLoc.getX() + " " + world.cameraLoc.getZ());
		System.out.println(world2.cameraLoc.getX() + " " + world2.cameraLoc.getZ());
		assertTrue(allShapes.size() == 1);
		
		System.out.println("Testing singleton-ness:");
		allShapes = world.getGeometry(new Point(3000, 4000));
		System.out.println(allShapes.size());
		System.out.println(world.cameraLoc.getX() + " " + world.cameraLoc.getZ());
		allShapes = world.getGeometry(new Point(0, 0));
		System.out.println(allShapes.size());
	}
}
