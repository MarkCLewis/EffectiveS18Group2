package plants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.Entity;
import shapes.Cylinder;
import shapes.RenderMaterial;
import shapes.Shape;
import shapes.Sphere;
import virtualworld.terrain.Point;
import worldmanager.WorldManager;

public class Truffula implements Entity{
	float height;
    float trunkWidth;
    float headWidth;
    double xPos;
    double yPos;
    double zPos;
    static Random rand = new Random();

    List<Shape> theTree = new ArrayList<>();
    double distance;
	
	public Truffula(float h, float w, float hw, double x, double y, double z){
		height = h;
		trunkWidth = w;
		headWidth = hw;
		xPos = x;
		yPos = y;
		zPos = z;
		
		RenderMaterial treeTrunk = new RenderMaterial();
		treeTrunk.setUseTexture(true);
		treeTrunk.setTextureDiffusePath("Textures/Terrain/splat/bark.jpg");
		
		RenderMaterial treeMat = new RenderMaterial();
		treeMat.setUseTexture(true);
		treeMat.setTextureDiffusePath("Textures/Terrain/splat/tree.jpg");
		
		Cylinder trunk = new Cylinder(height, trunkWidth, xPos, yPos, zPos);
		trunk.setMaterial(treeTrunk);
		theTree.add(trunk);
		
		Sphere head = new Sphere(headWidth, xPos, yPos +height/2+headWidth/2, zPos);
		head.setMaterial(treeMat);
		theTree.add(head);
	}
	
	public Point getCenter(){
        Point center = new Point(xPos, zPos);
        return center;
    }

    public double getSize(){
        return headWidth;
    }

    // gives you value for distance from camera
    public void distFromCamera(double dist){
        distance = dist;
    }

    // return a list of shapes that can be passed into graphics
    public List<Shape> getShapes(){
        return theTree;
    }
    
    public boolean isActive(){
    	return true;
    }
    public static Truffula randTree(Point cent, double sz) {
		rand.setSeed((long)(cent.getX()*cent.getZ())+1);
		double minX = cent.getX() - sz/2;
		double maxX = cent.getX() + sz/2;
		double newX = minX  + (rand.nextDouble() * (maxX-minX));
		double minZ = cent.getZ() - sz/2;
		double maxZ = cent.getZ() + sz/2;
		double newZ = minZ  + (rand.nextDouble() * (maxZ-minZ));
		double newY = WorldManager.getInstance().getHeight(new Point(newX,newZ));
		return new Truffula(50,3,20,newX,newY+20,newZ);
	}
}