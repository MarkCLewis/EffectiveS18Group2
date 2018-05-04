package cloud;

import java.util.List;

import engine.Game;
import shapes.Shape;

public class test {

	public static void main(String[] args) {
		Game g = new Game(400f);
		CloudFactory cFactory = new CloudFactory();

		
		Cloud b = cFactory.getCloud(5, 0000,0);
		b.distFromCamera(150);
		List<Shape> spheres = b.getShapes();
		
		g.start();
		g.addShapes(spheres);
		System.out.println(spheres.size());

	}

}
