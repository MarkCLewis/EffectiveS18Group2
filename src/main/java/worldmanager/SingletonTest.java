package worldmanager;

import java.util.List;

import shapes.Shape;
import virtualworld.terrain.Point;

public class SingletonTest {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WorldManager world = WorldManager.getInstance();
		
		TestEntity tester = new TestEntity(new Point(0,-100),2.4,5);
		
		world.addEntity(tester);
		
		WorldManager.getInstance().updateMaxView(2000);
		WorldManager.getInstance().updateCameraStep(100);
		
		Point p = new Point(0,0);
		
		List<Shape> allShapes = WorldManager.getInstance().getGeometry(p);
		System.out.println(allShapes.size());
		
		TestEntity.fillTest();
		allShapes = WorldManager.getInstance().getGeometry(p);
		System.out.println(allShapes.size());

	}

}
