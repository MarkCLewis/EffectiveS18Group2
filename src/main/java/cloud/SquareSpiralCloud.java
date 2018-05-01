package cloud;

import java.util.ArrayList;
import java.util.List;

import org.joml.Math;


import shapes.RectangularPrism;
import shapes.Shape;
import virtualworld.terrain.Point;

public class SquareSpiralCloud implements Cloud{
	public SquareSpiralCloud(double x, double y, double z, double newDist)
	{
		center = new Point(x, z);
		this.y = y;
		dist = newDist;
		noOfSpirals = (int)dist / 10;
	}
	
	private final double y;
	private final Point center;
	private final double dist;
	private final int noOfSpirals;
	private List<Shape> bestSpheres = new ArrayList<Shape>();
	
	@Override public void makeShape3d()
	{
 //       int width = getSize().width;
 //       int height = getSize().height;
		float sizeOfLine = (float)dist/5;
        double widthCenter = center.getX();
        double heightCenter = center.getZ();        
        double balanceOffset = dist/sizeOfLine;
        
        
        for (int i = 0; i < 5 ; i++)
        {
            
        	//draw the bottom to top line
            bestSpheres.add(new RectangularPrism(sizeOfLine, sizeOfLine , (float)Math.abs( (heightCenter - (dist * i) - (heightCenter + dist + (dist * i)) ))  , widthCenter + (dist * i + dist/2 * i), y, heightCenter + sizeOfLine));
 
            //draw the right to left line
            bestSpheres.add(new RectangularPrism((float)(Math.abs((widthCenter + (dist * i)) - (widthCenter - dist - (dist * i)) ) - dist/4) , sizeOfLine , sizeOfLine, widthCenter - (dist * i) + (dist/2 * (i - 1)) - balanceOffset , y , heightCenter + 2 * (dist + (dist * i) - dist/2)));

            //draw the top to bottom line
            bestSpheres.add(new RectangularPrism(sizeOfLine, sizeOfLine, (float)Math.abs( (heightCenter + dist + (dist * i)) - (heightCenter - dist - (dist * i))), widthCenter - (dist * (i + 1)) - (dist * i + dist/2 * i) - (balanceOffset * 2), y , heightCenter - dist + sizeOfLine));
            
            //draw the left to right line
            bestSpheres.add(new RectangularPrism((float)(Math.abs((widthCenter - dist - (dist * i)) - (widthCenter + dist + (dist * i))) - dist/2) , sizeOfLine , sizeOfLine , widthCenter - dist - (dist/2 * (i - 1)) + dist/2 + sizeOfLine, y ,  heightCenter - 2 * (dist + (dist * i) - dist/2) - dist * 2));
        }
	}
	
	@Override public Point getCenter()
	{
		return center;
	}
	
	@Override public double getSize()
	{
		return (double)(dist * dist * noOfSpirals * noOfSpirals);
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
