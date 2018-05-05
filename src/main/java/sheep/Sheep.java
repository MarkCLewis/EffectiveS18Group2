package sheep;

import java.util.List;
import java.util.Random;

import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;
import worldmanager.WorldManager;

public class Sheep implements Entity {
	// functions to implement

	Herd herd = null;
	Point cent;
	double size = 20;
	static Random rand = new Random();
	
	public Sheep(Point start) {
		herd = new Herd();
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
	
	public static Sheep randSheep(Point cent, double sz) {
		rand.setSeed((long)(cent.getX()*cent.getZ()));
		double minX = cent.getX() - sz/2;
		double maxX = cent.getX() + sz/2;
		double newX = minX  + (rand.nextDouble() * (maxX-minX));
		double minZ = cent.getZ() - sz/2;
		double maxZ = cent.getZ() + sz/2;
		double newZ = minZ  + (rand.nextDouble() * (maxZ-minZ));
		return new Sheep(new Point(newX,newZ));
	}
	
	
	
	
	
}
