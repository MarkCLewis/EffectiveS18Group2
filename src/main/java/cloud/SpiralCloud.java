package cloud;

import java.util.ArrayList;
import java.util.List;

import shapes.Shape;
import shapes.Sphere;
import virtualworld.terrain.Point;

public class SpiralCloud implements Cloud{
	public SpiralCloud(double x, double y, double z, double gap)
	{
		this.y = y;
		center = new Point(x,z);
		this.gap = gap;
		
		makeShapeBest();
		makeShapeSecondBest();
	}
	
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
		float size = (float)(gap * 20);
		double theta = 0.0;
		double thetaInc = 2*Math.PI/100.0;
		for (int i = 0; i < 350; i++) {
		    double x = Math.cos(theta)*radius;
		    double z = Math.sin(theta)*radius;
		    y += 0.01;
		    best.add(new Sphere(size + (float)(i/300.0) * size, x , y + 0.01 * i, z ));
		    radius+= gap + (i/100) * gap;
		    theta += thetaInc;
		}
	}
	
	private void makeShapeSecondBest()
	{ 
		//float size = (float)gap;
		float size = (float)(gap * 20);
		double theta = 0.0;
		double thetaInc = 2*Math.PI/100.0;
		for (int i = 0; i < 350; i++) {
			if (i % 2 == 0)
			{
			    double x = Math.cos(theta)*radius;
			    double z = Math.sin(theta)*radius;
			    y += 0.01;
			    secondBest.add(new Sphere((float)(1.5 * (size + (i/300.0) * size)), x , y + 0.15 * i, z ));
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
		return 0;
	}

	@Override public void distFromCamera(double dist)
	{
		double firstLevel = 500;
		double secondLevel = 1000;
		
		if (dist <= firstLevel)
			currentLevel = best;
		if (dist > firstLevel && dist <= secondLevel)
			currentLevel = secondBest;
		if (dist > secondLevel)
			currentLevel = tooFar;
	}

	@Override public List<Shape> getShapes()
	{
		//todo
		return currentLevel;
	}
	
	@Override public boolean isActive()
	{
		return true;
	}
	
}
