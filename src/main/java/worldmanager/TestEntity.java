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
	
	public TestEntity(Point p, double s) {
		cent = p;
		sz = s;
		Shape shape = new Shape(cent.getX(), cent.getZ(), 4.0);
		shapes.add(shape);
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

}
