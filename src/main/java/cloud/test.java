package cloud;

import java.util.ArrayList;
import java.util.List;

import engine.Game;
import shapes.RectangularPrism;
import shapes.Shape;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;

public class test {

	public static void main(String[] args) {
		Game g = new Game(100f);
		CloudFactory cFactory = CloudFactory.getInstance();
	
		//testing geneting on terrain size
		Terrain t = Terrain.forMountains(new Point(0000,0000), 2048, 6);
		
		List<Shape> shapes = new ArrayList<Shape>();
		//shapes.addAll(t.getShapes());
		//Method to create a list of cloud on a given terrain 
		List<Cloud> clouds = cFactory.getClouds( new Point(0,0), 16000);
		//shapes.add(new RectangularPrism(3000, 10, 1000, 0, 1000, 0));
		
		for (Cloud cloud : clouds)
		{
			cloud.distFromCamera(1000);
			shapes.addAll(cloud.getShapes());
		}
		//shapes.addAll(t.getShapes());
	
		Cloud cloud = cFactory.getCloud(0, 0, 0, 0, cFactory.getRandomSize(0));
		Cloud cloud2 = cFactory.getCloud(100, 0, 100, 1, cFactory.getRandomSize(1));
		Cloud cloud3 = cFactory.getCloud(200, 0, 200, 2, cFactory.getRandomSize(2));
		
		List<Shape> shape2 = new ArrayList<Shape>();
		shape2.addAll(cloud.getShapes());
		shape2.addAll(cloud2.getShapes());
		shape2.addAll(cloud3.getShapes());
		
		Cloud cloud4 = cFactory.getCloud(1000, 0, 0, 3, cFactory.getRandomSize(3));
		Cloud cloud5 = cFactory.getCloud(0, 3000, 0, 4, cFactory.getRandomSize(4));
		
		List<Shape> shape3 = new ArrayList<Shape>();
		//shape3.addAll(cloud4.getShapes());
		
		shape3.addAll(cloud5.getShapes());
		g.start();
		
		g.addShapes(shape2);		
		//g.addShapes(shape3);
		//g.addShapes(shapes);

	}

}
