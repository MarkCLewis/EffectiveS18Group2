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
		List<Cloud> clouds = cFactory.getClouds( new Point(1000,1000), 16000);
		//shapes.add(new RectangularPrism(3000, 10, 1000, 0, 1000, 0));
		
		for (Cloud cloud : clouds)
		{
			cloud.distFromCamera(1000);
			shapes.addAll(cloud.getShapes());
		}
		shapes.addAll(t.getShapes());
	
		Cloud cloud = cFactory.getCloud(0, 0, 0, 0, cFactory.getRandomSize(0));
		Cloud cloud2 = cFactory.getCloud(0, 0, 0, 1, cFactory.getRandomSize(1));
		Cloud cloud3 = cFactory.getCloud(0, 0, 0, 2, cFactory.getRandomSize(2));
		Cloud cloud4 = cFactory.getCloud(0, 0, 0, 3, cFactory.getRandomSize(3));
		Cloud cloud5 = cFactory.getCloud(0, 0, 0, 4, cFactory.getRandomSize(4));
		
		g.start();
		
		//g.addShapes(cloud.getShapes());
		//g.addShapes(cloud2.getShapes());
		//g.addShapes(cloud3.getShapes());
		//g.addShapes(cloud4.getShapes());
		//g.addShapes(cloud5.getShapes());
		
		
		g.addShapes(shapes);


	
		
		
		
		//System.out.println(spheres.size());
	}

}
