package cloud;

import java.util.List;

import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;

public interface Cloud extends Entity {
	
	public void makeShape3d();
	
	public Point getCenter();

	public double getSize();

	public void distFromCamera(double dist);

	public List<Shape> getShapes();
	
	public boolean isActive();
	
}