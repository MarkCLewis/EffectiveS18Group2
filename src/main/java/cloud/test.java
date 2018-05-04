package cloud;

import java.util.ArrayList;
import java.util.List;

import engine.Engine;
import engine.Game;
import shapes.RectangularPrism;
import shapes.Shape;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;

public class test {

	public static void main(String[] args) {
		Game g = new Game(100f);
		CloudFactory cFactory = CloudFactory.getInstance();

		/*List<Cloud> clouds = new ArrayList<Cloud>();
		for (int i = 0; i < 10; i++)
		{
			double x = i * 150;
			double y = 1500 + i * 150;
			double z = i * 200;
			clouds.add(cFactory.getCloud(x,y,z));
		}*/
		
		
		//testing geneting on terrain size
		
		List<Shape> spheres = new ArrayList<Shape>();
		
		Terrain t = Terrain.forMountains(new Point(2000,2000), 2048, 6);
		Point center = t.getCenter();
		
		/*System.out.printf("The length of terrain is %f\n", t.getSize());
		System.out.printf("The width of the terrain is %f\n", t.getSize());
		//System.out.println(clouds.size());
		
		spheres.addAll(b.getShapes());
		System.out.printf("The size of the cloud is %f\n", b.getSize());
		*/
		
		//Cloud b = cFactory.getClouds(0, 0000,0);
		//best
		//b.distFromCamera(100);

		//secondBest
		//b.distFromCamera(450);

		//worst
		//b.distFromCamera(750);

		//no soon
		//b.distFromCamera(1500);
		
		//spheres.addAll(b.getShapes());
		//spheres.addAll(t.getShapes());
		/*Cloud c = cFactory.getCloud(100, 0000,0);
		c.distFromCamera(150);
		
		
		

		
		System.out.printf("b cloud size : %d\n", b.getShapes().size());
		System.out.printf("c cloud size : %d\n", c.getShapes().size());
		
		
		spheres.addAll(c.getShapes());
		System.out.printf("a and b cloud size : %d\n", spheres.size());
		*/
		/*for (Cloud d : clouds)
		{
			d.distFromCamera(10);
			spheres.addAll(d.getShapes());
			System.out.println(spheres.size());
		}*/
		
		List<Shape> shapes = new ArrayList<Shape>();
		shapes.addAll(t.getShapes());
		
		List<Cloud> clouds = cFactory.getClouds(t);
		//shapes.add(new RectangularPrism(3000, 10, 1000, 0, 1000, 0));
		
		for (Cloud cloud : clouds)
		{
			cloud.distFromCamera(1000);
			shapes.addAll(cloud.getShapes());
		}
		
		
		/*for (int i = 0; i < 20; i++)
		{
			cFactory.getClouds(t);	
		}*/
		g.start();
		g.addShapes(shapes);
		//g.addShapes(spheres);
		
		//System.out.printf("x = %f, y = %f, z = %f\n", pos.x, pos.y, pos.z);
	
		
		
		
		//System.out.println(spheres.size());
	}

}
