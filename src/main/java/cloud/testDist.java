package cloud;

import java.util.ArrayList;
import java.util.List;

import engine.Game;
import shapes.Shape;

public class testDist {
	static CloudFactory cFactory = CloudFactory.getInstance();
	public static void main(String[] args) {
		Game g = new Game(100f);
		
		List<Shape> shapes1 = typeOne();
		List<Shape> shapes2 = typeTwo();
		List<Shape> shapes3 = typeThree();
		
		g.start();
		g.addShapes(shapes1);
		g.addShapes(shapes2);
		g.addShapes(shapes3);
		
	}
	
	private static List<Shape> typeOne()
	{
		int type = 0;
		double size = cFactory.getRandomSize(type);
		List<Shape> shapes = new ArrayList<Shape>();	
		
		Cloud a = cFactory.getCloud(0, 0, 0, type, size);
		a.distFromCamera(1000);
		
		Cloud b = cFactory.getCloud(200, 0, 0, type, size);
		b.distFromCamera(2000);
		
		Cloud c = cFactory.getCloud(400, 0, 0, type, size);
		c.distFromCamera(3000);
		
		
		shapes.addAll(a.getShapes());
		shapes.addAll(b.getShapes());
		shapes.addAll(c.getShapes());
		return shapes;
	}
	
	private static List<Shape> typeTwo()
	{
		int type = 1;
		double size = cFactory.getRandomSize(type);
		List<Shape> shapes = new ArrayList<Shape>();	
		
		Cloud a = cFactory.getCloud(1000, 2000, 500, type, size);
		a.distFromCamera(1000);
		
		Cloud b = cFactory.getCloud(1400, 2000, 500, type, size);
		b.distFromCamera(2000);
		
		Cloud c = cFactory.getCloud(1800, 2000, 500, type, size);
		c.distFromCamera(3000);
		
		
		shapes.addAll(a.getShapes());
		shapes.addAll(b.getShapes());
		shapes.addAll(c.getShapes());
		return shapes;
	}
	
	private static List<Shape> typeThree()
	{
		int type = 2;
		double size = cFactory.getRandomSize(type);
		List<Shape> shapes = new ArrayList<Shape>();	
		
		Cloud a = cFactory.getCloud(2000, 3000, 1000, type, size);
		a.distFromCamera(1000);
		
		Cloud b = cFactory.getCloud(2400, 3000, 1000, type, size);
		b.distFromCamera(2000);
		
		Cloud c = cFactory.getCloud(2800, 3000, 1000, type, size);
		c.distFromCamera(3000);
		
		
		shapes.addAll(a.getShapes());
		shapes.addAll(b.getShapes());
		shapes.addAll(c.getShapes());
		return shapes;
	}
}
