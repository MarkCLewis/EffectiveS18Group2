package cloud;

import java.util.List;

import engine.Game;
import shapes.Shape;

public class test {

	public static void main(String[] args) {
		Game g = new Game(400f);
		CloudFactory cFactory = new CloudFactory();

		
		Cloud b = cFactory.getCloud(5, 0000,0);
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
