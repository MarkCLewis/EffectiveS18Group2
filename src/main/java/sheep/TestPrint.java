package sheep;

import java.util.ArrayList;
import java.util.List;

import engine.Game;
import shapes.Shape;
import shapes.Sphere;

public class TestPrint {
	
	public static void main(String[] args) {
		Game g = new Game(400f);
		g.start();	
		
		// Practice implementation
		//sphere arguments: radius, xPos, yPos, zPos
		//cylinder arguments: height, radius, xPos, yPos, zPos
		Sphere body = new Sphere(20, 20, 400, 20);
		Sphere head = new Sphere(10, 0, 400, 0);
		//Cylinder legFL = new Cylinder(20, 2, 30, 380, 20);
		//Cylinder legFR = new Cylinder(20, 2, 30, 380, 30);
		//Cylinder legBL = new Cylinder(20, 2, 30, 380, 40); //ok
		//Cylinder legBR = new Cylinder(20, 2, 40, 380, 50); //xpos ox
		g.addShape(body);
		g.addShape(head);
		//g.addShape(legFL);
		//g.addShape(legFR);
		//g.addShape(legBL);
		//g.addShape(legBR);
		
		//SheepBuilder build = SheepBuilder.getInstance();
		//List<Shape> shapes = new ArrayList<Shape>();

	}
}
