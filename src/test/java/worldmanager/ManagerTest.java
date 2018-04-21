package worldmanager;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import shapes.Shape;
import virtualworld.terrain.Point;
import java.util.Random;

public class ManagerTest {
	
	Point p1 = new Point(0,0);	
	WorldManager world = new WorldManager(p1, 100);
	
	@Test
	public void test() {
		
		System.out.println("Starting test:");
		
		Node node = world.rootNode;
		
		Random rand = new Random();
		for(int i = 0; i < 25; i++) {
			double x = rand.nextDouble() + rand.nextInt(49);
			double y = rand.nextDouble() + rand.nextInt(49);
			Point p = new Point(x,y);
			TestEntity tester = new TestEntity(p,rand.nextInt(30));
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
		System.out.println(node.children.length);
		
		assertTrue(allShapes.size() == 25);
	}
}
