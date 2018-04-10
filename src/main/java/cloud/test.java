package cloud;

import java.util.List;

import engine.Game;
import shapes.Sphere;

public class test {

	public static void main(String[] args) {
		Game g = new Game();
		//RectangularPrism rP = new RectangularPrism(3, 3, 1, 5, 5, 0);
		//RectangularPrism rP2 = new RectangularPrism(3, 3, 1, 5, 5, 5);
		Sphere s = new Sphere((float)0.5, 0 ,3, 3);
		Cloud t = new Cloud (5,10,0,20,3,10);
		List<Sphere> spheres = t.makeShape3d();
		
		
		for (Sphere sp : spheres)
		{
			g.addShape(sp);
		}
		//g.addShape(rP);
		//g.addShape(rP2);
		//g.addShape(s);
		g.start();
		System.out.println(spheres.size());

	}

}
