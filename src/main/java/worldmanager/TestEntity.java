package worldmanager;

import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;

public class TestEntity implements Entity {
	
	Point cent;
	double sz;
	double cameraDist;
	List<Shape> shapes = new ArrayList<>();
	
	public TestEntity(Point p, double s, double h) {
		cent = p;
		sz = s;
		Shape shape = new Shape(cent.getX(), cent.getZ(), h);
		shapes.add(shape);
	}
	
	public static void fillTest(Entity ent) {
		WorldManager.getInstance().addEntity(ent);
	}
	
	public Point getCenter() {
		return cent;
	}
	
	public double getSize() {
		return sz;
	}
	
	public void distFromCamera(double dist) {
		cameraDist = dist;
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}
	
	public boolean isActive() {
		return true;
	}

	public double mySize() {
		return WorldManager.getInstance().rootNode.getSize();
	}
	
}
