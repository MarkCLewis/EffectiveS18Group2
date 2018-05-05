package sheep;

import engine.Game;
import shapes.Cylinder;
import shapes.Sphere;

public class TestPrint {

	public static void main(String[] args) {
		Game g = new Game(400f);
		g.start();

		/*
		 * public static void main(String[] args) { Game g = new Game(250f);
		 * Point center = new Point(0,0);
		 * 
		 * WorldManager world = WorldManager.getInstance();
		 * 
		 * //Sphere s = new Sphere(0 ,3, 3);
		 * 
		 * Terrain t = Terrain.forFeilds(center, 20000, 16000); Terrain[] ters =
		 * t.split(); List<shapes.Shape> shapes = new ArrayList<shapes.Shape>();
		 * for(int i = 0; i < ters.length; i++) { Terrain[] tmpters =
		 * ters[i].split(); shapes.add(tmpters[0].getHeightMapSurface());
		 * shapes.add(tmpters[1].getHeightMapSurface());
		 * shapes.add(tmpters[2].getHeightMapSurface());
		 * shapes.add(tmpters[3].getHeightMapSurface()); } g.start();
		 * g.addShapes(shapes); //g.addShape(s); }
		 */

		// Practice implementation
		// sphere arguments: radius, xPos, yPos, zPos
		// cylinder arguments: height, radius, xPos, yPos, zPos
		Sphere body = new Sphere(20, 20, 400, 0);
		Sphere head = new Sphere(10, 0, 400, 0);
		Cylinder legFL = new Cylinder(20, 2, 10, 390, 10);
		Cylinder legFR = new Cylinder(20, 2, 10, 390, -10);
		Cylinder legBL = new Cylinder(20, 2, 30, 390, 10); //ok
		Cylinder legBR = new Cylinder(20, 2, 30, 390, -10); //xpos ox
		g.addShape(body);
		g.addShape(head);
		g.addShape(legFL);
		g.addShape(legFR);
		g.addShape(legBL);
		g.addShape(legBR);

		// SheepBuilder build = SheepBuilder.getInstance();
		// List<Shape> shapes = new ArrayList<Shape>();
		

	}
}
