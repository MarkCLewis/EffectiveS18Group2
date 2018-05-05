package sheep;

import java.util.List;

import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;

public interface Sheep extends Entity {
	// functions to implement

	public Point getCenter();

	public double getSize();

	// gives you value for distance from camera
	public void distFromCamera(double dist);

	// return a list of shapes that can be passed into graphics
	@Override 
	public List<Shape> getShapes();
	
	//determines if an object is active or not. Only really used for Terrain
	@Override 
	public default boolean isActive(){
		return true;
	}
}
