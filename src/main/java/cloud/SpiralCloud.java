package cloud;

import java.util.ArrayList;
import java.util.List;

import shapes.RenderMaterial;
import shapes.Shape;
import shapes.Sphere;
import virtualworld.terrain.Point;

public class SpiralCloud implements Cloud{
	public SpiralCloud(double x, double y, double z, double gap, RenderMaterial mat)
	{
		cloudMat = mat;
		this.y = y;
		center = new Point(x,z);
		this.gap = gap;
		
		makeShapeBest();
		makeShapeSecondBest();
		currentLevel = best;
	}
	
	RenderMaterial cloudMat;
	private double gap;
	private double radius = 200;
	private double y;
	private final Point center;
	
	private List<Shape> best = new ArrayList<Shape>();
	private List<Shape> secondBest = new ArrayList<Shape>();
	private List<Shape> tooFar = new ArrayList<Shape>();
	
	private List<Shape> currentLevel = new ArrayList<Shape>();
	
	private void makeShapeBest()
	{ 
		//float size = (float)gap;
		float size = (float)(gap * 40);
		double theta = 0.0;
		double thetaInc = 2*Math.PI/100.0;
		for (int i = 0; i < 350; i++) {
		    double x = Math.cos(theta)*radius;
		    double z = Math.sin(theta)*radius;
		    y += 0.01;
		    Sphere temp = new Sphere(size + (float)(i/300.0) * size, x + center.getX() , y + 0.1 * i, z + center.getZ());
		    temp.setMaterial(cloudMat);
		    best.add(temp);
		    //best.add(new Sphere(size + (float)(i/300.0) * size, x + center.getX() , y + 0.1 * i, z + center.getZ()));
		    radius+= gap + (i/100) * gap;
		    theta += thetaInc;
		}
	}
	
	private void makeShapeSecondBest()
	{ 
		//float size = (float)gap;
		float size = (float)(gap * 40);
		double theta = 0.0;
		double thetaInc = 2*Math.PI/100.0;
		for (int i = 0; i < 350; i++) {
			if (i % 2 == 0)
			{
			    double x = Math.cos(theta)*radius;
			    double z = Math.sin(theta)*radius;
			    y += 0.01;
			    Sphere temp = new Sphere((float)(1.5 * (size + (i/300.0) * size)), x + center.getX(), y + 0.15 * i, z + center.getZ());
			    temp.setMaterial(cloudMat);
			    secondBest.add(temp);
			    //secondBest.add(new Sphere((float)(1.5 * (size + (i/300.0) * size)), x + center.getX(), y + 0.15 * i, z + center.getZ() ));
			    radius+= gap + (i/100) * gap;
			    theta += thetaInc;
			}
		}
	}
	
	@Override public Point getCenter()
	{
		return center;
	}
	
	@Override public double getSize()
	{
		return ( (gap + 1) * 20) * ((gap + 1) * 20) ;
	}

	@Override public void distFromCamera(double dist)
	{
		double firstLevel = 2000;
		double secondLevel = 4000;
		
		if (dist <= firstLevel)
			currentLevel = best;
		if (dist > firstLevel && dist <= secondLevel)
			currentLevel = secondBest;
		if (dist > secondLevel)
			currentLevel = tooFar;
	}

	@Override public List<Shape> getShapes()
	{
		return currentLevel;
	}
	
	@Override public boolean isActive()
	{
		return true;
	}
	
}
