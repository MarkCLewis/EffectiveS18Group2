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
	}
	
	private double gap;
	private double radius = 200;
	private double y;
	private final Point center;
	private List<Shape> bestSpheres = new ArrayList<Shape>();
	
	@Override public void makeShape3d()
	{ 
		//float size = (float)gap;
		float size = (float)(gap * 20);
		double theta = 0.0;
		double thetaInc = 2*Math.PI/100.0;
		for (int i = 0; i < 350; i++) {
		    double x = Math.cos(theta)*radius;
		    double z = Math.sin(theta)*radius;
		    y += 0.01;
		    bestSpheres.add(new Sphere(size + (float)(i/300.0) * size, x , y + 0.01 * i, z ));
		    radius+= gap + (i/100) * gap;
		    theta += thetaInc;
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
		
	}

	@Override public List<Shape> getShapes()
	{
		//todo
		return bestSpheres;
	}
	
	@Override public boolean isActive()
	{
		return true;
	}
	
}
