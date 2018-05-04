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
		
		makeShapeBest();
		secondBestShapes();
		
	}
	
	private final double y;
	private final Point center;
	private final double dist;
	private final int noOfSpirals;
	
	private List<Shape> best = new ArrayList<Shape>();
	private List<Shape> secondBest = new ArrayList<Shape>();
	private List<Shape> tooFar = new ArrayList<Shape>();
	
	private List<Shape> currentLevel = new ArrayList<Shape>();
	
	
	
	private void makeShapeBest()
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
            best.add(new RectangularPrism(sizeOfLine, sizeOfLine , (float)Math.abs( (heightCenter - (dist * i) - (heightCenter + dist + (dist * i)) ))  , widthCenter + (dist * i + dist/2 * i), y, heightCenter + sizeOfLine));
 
            //draw the right to left line
            best.add(new RectangularPrism((float)(Math.abs((widthCenter + (dist * i)) - (widthCenter - dist - (dist * i)) ) - dist/4) , sizeOfLine , sizeOfLine, widthCenter - (dist * i) + (dist/2 * (i - 1)) - balanceOffset , y , heightCenter + 2 * (dist + (dist * i) - dist/2)));

            //draw the top to bottom line
            best.add(new RectangularPrism(sizeOfLine, sizeOfLine, (float)Math.abs( (heightCenter + dist + (dist * i)) - (heightCenter - dist - (dist * i))), widthCenter - (dist * (i + 1)) - (dist * i + dist/2 * i) - (balanceOffset * 2), y , heightCenter - dist + sizeOfLine));
            
            //draw the left to right line
            best.add(new RectangularPrism((float)(Math.abs((widthCenter - dist - (dist * i)) - (widthCenter + dist + (dist * i))) - dist/2) , sizeOfLine , sizeOfLine , widthCenter - dist - (dist/2 * (i - 1)) + dist/2 + sizeOfLine, y ,  heightCenter - 2 * (dist + (dist * i) - dist/2) - dist * 2));
        }
	}
	
	private void secondBestShapes()
	{
		secondBest.add(new RectangularPrism((float)dist * noOfSpirals, 5, (float)dist * noOfSpirals, center.getX(), y, center.getZ()));
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
