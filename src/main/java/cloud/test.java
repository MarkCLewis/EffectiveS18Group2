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
		Sphere s = new Sphere((float)0.5, 0 ,3, 3);
		Cloud t = new Cloud (5,375,0,10,30,10);
		ArrayList<Shape> spheres = t.makeShape3d();
		
		/*Cloud z = new Cloud(3,10, -10, 20,3,10);
		List<Shape> s2 = z.makeShape3d();
		*/

			g.addShapes(spheres);
		
/*		for (Shape sp : s2)
		{
			g.addShape(sp);
		}
*/
		g.start();
		System.out.println(spheres.size());

	}

}
