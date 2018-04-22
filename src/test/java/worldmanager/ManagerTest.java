package worldmanager;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;

public class ManagerTest {
	
	Point p1 = new Point(0,0);	
	WorldManager world = new WorldManager(p1, 10000);
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
		
		System.out.println("Starting test:");
		
		Node node = world.rootNode;
		world.updateMaxView(2000);
		
		Random rand = new Random();
		for(int i = 0; i < 100; i++) {
			double x = rand.nextDouble() + rand.nextInt(9999) - 5000;
			double y = rand.nextDouble() + rand.nextInt(9999) - 5000;
			Point p = new Point(x,y);
			TestEntity tester = new TestEntity(p,2.4);
			System.out.println(tester.getSize());
			System.out.println(node.findDist(p, p1));
			entlist.add(tester);
			world.addEntity(tester);
		}
		
		List<Shape> allShapes = world.getGeometry();
		System.out.println(allShapes.size());
		for(Shape s : allShapes) {
			System.out.println(s);
			System.out.println(s.getCenter()[0]);
			System.out.println(s.getCenter()[1]);
			System.out.println(s.getCenter()[2]);
		}
				
		boolean b = entlist.size() == 100;
		System.out.println(getMax(node));
		System.out.println(allShapes.size());
		assertTrue(b);
	}
}
