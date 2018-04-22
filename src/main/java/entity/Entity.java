package entity;

import java.util.List;

import shapes.Shape;
import virtualworld.terrain.Point;

public interface Entity {

	// functions to implement

	public Point getCenter();

	public double getSize();

	// gives you value for distance from camera
	public void distFromCamera(double dist);

	// return a list of shapes that can be passed into graphics
	public List<Shape> getShapes();
	
	//determines if an object is active or not. Only really used for Terrain
	public boolean isActive();

	//	pseudocode for distFromCamera:
	//
	//		public void distFromCamer(double dist);
	//			cameraDist = dist;					
	//		}
	//
	//	cameraDist is some double value you will hold that
	//	will determine what you will return to graphics
	//	i.e. 10 meters away you return a higher resolution object,
	//	but less than that you lower the amount of polygons
}
