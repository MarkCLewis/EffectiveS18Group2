package sheep;

import java.util.List;

import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;
import worldmanager.WorldManager;

public class Sheep implements Entity {
	// functions to implement

	Herd herd = null;
	Point cent;
	double size = 20;
	
	
	public Sheep(Point start) {
		herd = new Herd(start);
		cent = start;
	}
	
	public Point getCenter() {
		return cent;
	}

	public double getSize() {
		return size;
	}

	// gives you value for distance from camera
	public void distFromCamera(double dist) {}

	// return a list of shapes that can be passed into graphics
	@Override 
	public List<Shape> getShapes() {
		double yPos = WorldManager.getInstance().getHeight(cent);
		return herd.Sheep(cent.getX(), yPos, cent.getZ());
	}
	
	//determines if an object is active or not. Only really used for Terrain
	@Override 
	public boolean isActive(){
		return true;
	}
}
