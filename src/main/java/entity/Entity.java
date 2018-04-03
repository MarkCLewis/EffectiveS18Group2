package entity;

import virtualworld.terrain.Point;

public interface Entity {
	
	//functions to implement
	
	public Point getCenter();
	
	public double getSize();
	
	//gives you value for distance from camera
	public void distFromCamera(double size);
	
}
