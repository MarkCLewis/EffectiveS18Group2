package cloud;

import java.util.ArrayList;
import java.util.List;

import engine.Engine;
import shapes.RectangularPrism;
import shapes.Shape;
import shapes.Sphere;
import virtualworld.terrain.Point;

public class PerlinCloud implements Cloud{
	public PerlinCloud (double x, double y, double z, CloudArr arr, double newF2)
	{
		this.y = y;
		center = new Point(x,z);
		cloudArr = arr.cloudArr;
		length = cloudArr.length;
		height = cloudArr[0][0].length;
		width = cloudArr[0].length;
		f2 = newF2;
		
		//method calls to make 3 levels of details
		makeShape3dBest();
		secondBestShapes();
		furthest();
	}
	//determine how big the cloud is 
	
	
	private final int width;
	private final int length;
	private final int height;
	private final double y;
	private final Point center;
	private double[][][] cloudArr;
	private double f2 = 0;
	
	List<Shape> best = new ArrayList<Shape>();
	List<Shape> secondBest = new ArrayList<Shape>();
	List<Shape> worst = new ArrayList<Shape>();
	List<Shape> tooFar = new ArrayList<Shape>();
	
	List<Shape> currentLevel = new ArrayList<Shape>();
	
	private void makeShape3dBest()
	{

		//calculate midpoint to get the right center

		
		int midX = length/2;
		int midY = height/2;
		int midZ = width/2;
		
		//use this to scale up
		double sF = 1;
		
		double originX = center.getX();
		double originZ = center.getZ();
		double originY = y;
		
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < length; x++)
			{
				for (int z = 0; z < width; z++)
				{
					if (cloudArr[x][z][y] > f2)
					{
						posX = originX - midX + x; //xcoord
						posY = originY + midY - y; //yCoord
						posZ = originZ - midZ + z;
						double variation = Engine.getRandomDouble(-.5, 2);
						//random offset so that not every cloud of the same kind will look exactly the same, more random
						best.add(new Sphere((float) ((cloudArr[x][z][y] - 0.1) * 3 + variation) ,posX, posY, posZ));
					}
				}
			}
		}
	}
	
	private void secondBestShapes()
	{
		int midX = length/2;
		int midY = height/2;
		int midZ = width/2;
		
		//use this to scale up
		double sF = 1;
		
		double originX = center.getX();
		double originZ = center.getZ();
		double originY = y;
		
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		
		int count = 0;
		float scale = (float)2;
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < length; x++)
			{
				for (int z = 0; z < width; z++)
				{
					if (count % 4 == 0)
					{
						if (cloudArr[x][z][y] > f2)
						{
							posX = originX - midX + x; //xcoord
							posY = originY + midY - y; //yCoord
							posZ = originZ - midZ + z;
							double variation = Engine.getRandomDouble(-.5, 2);
							//random offset so that not every cloud of the same kind will look exactly the same, more random
							secondBest.add(new Sphere((float) ((cloudArr[x][z][y] - 0.1) * 3 + variation) * scale ,posX, posY, posZ));
						}
					}
					count++;
				}
			}
		}
	}
	
	private void furthest()
	{
		worst.add(new RectangularPrism(length, height, width, center.getX(), y, center.getZ()));
	}
	
	public double[][][] getCloudArray3d()
	{
		return cloudArr;
	}
	
	@Override public Point getCenter() 
	{
		return center;
	}
	
	@Override public double getSize() 
	{
		return (height * width * length);
	}
	
	@Override public void distFromCamera(double dist)
	{
		double firstLevel = 150;
		double secondLevel = 500;
		double thirdLevel = 1000;
		
		if (dist > firstLevel && dist <= secondLevel)
			currentLevel = secondBest;
		if (dist <= firstLevel)
			currentLevel = best;
		if (dist > secondLevel && dist <= thirdLevel)
			currentLevel = worst;
		if (dist > thirdLevel)
			currentLevel = tooFar;
		
	}
	
	@Override public List<Shape> getShapes()
	{
		//return bestSpheres;
		return currentLevel;
	}
	
    @Override public boolean isActive() 
    {
    	return true;
    }
	

    
}





