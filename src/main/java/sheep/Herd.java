package sheep;

import java.util.ArrayList;
import java.util.List;

import shapes.Cylinder;
import shapes.Shape;
import shapes.Sphere;
import shapes.RenderColor;
import virtualworld.terrain.Point;

public class Herd {
	public Herd() {
		//location = loc;
		//List<Sheep> herd = new ArrayList<Sheep>();
	}

	//Point location;
	//public List<Sheep> herd;
	//private double xPos = location.getX();
	//private double zPos = location.getZ();
	//private double yPos = 0;

	// sphere arguments: radius, xPos, yPos, zPos
	List<Shape> Sheep(double xPos, double yPos, double zPos) {
		List<Shape> sheep = new ArrayList<Shape>();
		Sphere body = new Sphere(20, xPos, yPos+20, zPos);
		Sphere head = new Sphere(10, xPos-20, yPos+20, zPos-20);
		Cylinder fl = new Cylinder(20, 2, xPos-10, yPos+10, zPos+10);
		Cylinder fr = new Cylinder(20, 2, xPos-10, yPos+10, zPos-10);
		Cylinder bl = new Cylinder(20, 2, xPos+10, yPos+10, zPos+10);
		Cylinder br = new Cylinder(20, 2, xPos+10, yPos+10, zPos+10);
		
		head.setMaterialColor(RenderColor.Black);
		fl.setMaterialColor(RenderColor.Black);
		fr.setMaterialColor(RenderColor.Black);
		bl.setMaterialColor(RenderColor.Black);
		br.setMaterialColor(RenderColor.Black);
		body.setMaterialColor(RenderColor.White);
		
		sheep.add(body);
		sheep.add(head);
		sheep.add(fl);
		sheep.add(fr);
		sheep.add(bl);
		sheep.add(br);
		return sheep;
	}
	
	double getXBound(Sheep sheep){
		return 0;
	}
	
	double getZBound(Sheep sheep){
		return 0;
	}

	List<Shape> drawSheep(int xPos, int yPos, int zPos) {
		List<Shape> sheep = new ArrayList<Shape>();
		Sphere body = new Sphere(20, xPos, yPos, zPos);
		Sphere head = new Sphere(10, xPos-20, yPos, zPos-20);
		Cylinder fl = new Cylinder(20, 2, xPos-10, yPos-10, zPos+10);
		Cylinder fr = new Cylinder(20, 2, xPos-10, yPos-10, zPos-10);
		Cylinder bl = new Cylinder(20, 2, xPos+10, yPos-10, zPos+10);
		Cylinder br = new Cylinder(20, 2, xPos+10, yPos-10, zPos+10);
		
		sheep.add(body);
		sheep.add(head);
		sheep.add(fl);
		sheep.add(fr);
		sheep.add(bl);
		sheep.add(br);
		return sheep;
	}
	
	List<Sheep> makeHerd(Point location){
		List<Sheep> herd = new ArrayList<Sheep>();
		
		return herd;
	}
	
	//boolean collision(Point agent, Point newAgent){}
}
