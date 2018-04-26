package cloud;

import java.util.List;

import engine.Game;
import shapes.Shape;

public class test {

	public static void main(String[] args) {
		Game g = new Game();
		CloudFactory cFactory = new CloudFactory();
		//RectangularPrism rP = new RectangularPrism(3, 3, 1, 5, 5, 0);
		//RectangularPrism rP2 = new RectangularPrism(3, 3, 1, 5, 5, 5);
		//Sphere s = new Sphere((float)0.5, 0 ,3, 3);
		
		//distinct pattern
		Cloud t = new Cloud (5,6000,0,75,20,100);
		
		//t.setOffSets(.75, 1, 3, 2, 8);
		t.setFilters(0.65, 0.75);
		t.setOffSets(3,5,2,2,4); //working cloud too
		//may need to make a new class since it might need to reduce the isolated sphere 
		
		//cluster
		//Cloud t = new Cloud (5,6000,0,50,15,35);
		//Cloud t = new Cloud (5,6000,0,30,10,20);
		
		//t.setOffSets(6.5, 3, 3, 2, 8); //the ratio of (30,10,20) is best so far
		//t.setFilters(0.55, 0.75);
		
		//Cloud b = new CloudBuilder(5, 6000, 0).normal().build();
		
		
		//f1 between 0.45 - 0.55 still produce a cloud, slightly different shapes, can use this to alter the shapes so not all clouds look exactly the same.
		//maybe using random function to calculate a value between 0.0- 0.1 from x,y,z coordinates
		//scalable . Few numbers of shapes so pretty good
		
		/*t.setOffSets(2, 1, 1, 2, 4); //blocky cloud, good for big cloud can use a bit polish. Works with both (50,15,35) and (30,10,20)
		t.setFilters(0.45, 0.65);*/
		
		t.getInverse();
		//Cloud t = new Cloud (5,300,0,100,30,100);
		//ArrayList<Shape> spheres = t.makeShape3d();
		
		Cloud b = cFactory.getCloud(5,6000,0);
		List<Shape> spheres = b.getShapes();
		
		/*Cloud z = new Cloud(5,400, 50, 40,10,60);
		z.setOffSets(.75, 1, 3, 2, 8);
		ArrayList<Shape> s2 = z.makeShape3d();
		spheres.addAll(s2);
		*/

			
			//g.addShapes(s2);
		g.start();
		g.addShapes(spheres);
		System.out.println(spheres.size());

	}

}
