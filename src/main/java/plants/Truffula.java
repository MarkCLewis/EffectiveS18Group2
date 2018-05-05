package plants;

import java.util.ArrayList;
import java.util.List;
import entity.Entity;
import shapes.Shape;
import shapes.Cylinder;
import shapes.RenderColor;
import shapes.Sphere;
import virtualworld.terrain.Point;
import shapes.RenderMaterial;

public class Truffula implements Entity{
	float height;
    float trunkWidth;
    float headWidth;
    double xPos;
    double yPos;
    double zPos;

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
}