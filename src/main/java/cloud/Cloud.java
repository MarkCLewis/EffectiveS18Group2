package cloud;


import java.util.List;

import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;

public interface Cloud extends Entity {
	
	
	@Override public Point getCenter();

	@Override public double getSize();

	// gives you value for distance from camera
	@Override public void distFromCamera(double dist);

	// return a list of shapes that can be passed into graphics
	@Override public List<Shape> getShapes();
	
	//determines if an object is active or not. Only really used for Terrain
	@Override public boolean isActive();

	
}