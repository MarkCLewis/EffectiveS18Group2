package worldmanager;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;

public class ManagerTest {
	
	Point p1 = new Point(0,0);	
	WorldManager world = new WorldManager(p1, 5000);
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
	
		//Terrain t = new Terrain(new Point(0.0,0.0), length, seed, points, heightMap);
		Terrain t = Terrain.forHills(new Point(0.0,0.0), length, 100);
		world.addEntity(t);
		
		System.out.println("Starting test:");
		
		Node node = world.rootNode;
		world.updateMaxView(2000);
		
		Random rand = new Random();
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
		}
		
		List<Shape> allShapes = world.getGeometry(new Point(0.0,0.0));
		/*for(Shape s : allShapes) {
			System.out.println(s);
			System.out.println(s.getCenter()[0]);
			System.out.println(s.getCenter()[1]);
			System.out.println(s.getCenter()[2]);
		}*/
				
		boolean b = entlist.size() == 25;
		System.out.println(getMax(node));
		System.out.println(allShapes.size());
		assertTrue(b);
	}
}
