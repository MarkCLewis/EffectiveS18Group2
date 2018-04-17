package cloud;

import java.util.ArrayList;

import engine.Game;
import shapes.Shape;
import shapes.Sphere;

public class test {

	public static void main(String[] args) {
		Game g = new Game();
		//RectangularPrism rP = new RectangularPrism(3, 3, 1, 5, 5, 0);
		//RectangularPrism rP2 = new RectangularPrism(3, 3, 1, 5, 5, 5);
		//Sphere s = new Sphere((float)0.5, 0 ,3, 3);
		
		//distinct pattern
		//Cloud t = new Cloud (5,375,0,50,5,50);
		//t.setOffSets(.75, 1, 3, 2, 8);		
		//t.setOffSets(3,5,2,2,4);
		
		//cluster
		Cloud t = new Cloud (5,400,0,50,15,35);
		//Cloud t = new Cloud (5,400,0,30,10,20);
		t.setOffSets(6.5, 3, 3, 2, 8);
		//t.setOffSets(2, 1, 1, 2, 4); //blocky cloud
		t.getInverse();
		//Cloud t = new Cloud (5,300,0,100,30,100);
		ArrayList<Shape> spheres = t.makeShape3d();
		
		/*Cloud z = new Cloud(5,400, 50, 40,10,60);
		z.setOffSets(.75, 1, 3, 2, 8);
		ArrayList<Shape> s2 = z.makeShape3d();
		spheres.addAll(s2);
		*/

			g.addShapes(spheres);
			//g.addShapes(s2);
		
/*		for (Shape sp : s2)
		{
			g.addShape(sp);
		}
*/
		g.start();
		System.out.println(spheres.size());

	}

}
