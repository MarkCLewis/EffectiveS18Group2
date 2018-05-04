package cloud;

import java.util.ArrayList;
import java.util.List;

import engine.Engine;
import shapes.RectangularPrism;
import shapes.RenderMaterial;
import shapes.Shape;
import shapes.Sphere;
import virtualworld.terrain.Point;

public class PerlinCloud implements Cloud{
	public PerlinCloud (double x, double y, double z, CloudArr arr, double newF2, double newScalingFactor, RenderMaterial mat)
	{
		
		cloudMat = mat;
		this.y = y;
		center = new Point(x,z);
		cloudArr = arr.cloudArr;
		length = cloudArr.length;
		height = cloudArr[0][0].length;
		width = cloudArr[0].length;
		f2 = newF2;
		scalingFactor = newScalingFactor * 4;
		
		//method calls to make 3 levels of details
		makeShape3dBest();
		secondBestShapes();
		furthest();
		

	}
	//determine how big the cloud is 
	RenderMaterial cloudMat;
	private final double scalingFactor;
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
		
		//use this to scale ups
		
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
						posX = originX - (midX * scalingFactor) + (x * scalingFactor); //xcoord
						posY = originY + (midY * scalingFactor) - (y * scalingFactor); //yCoord
						posZ = originZ - (midZ * scalingFactor) + (z * scalingFactor);
						double variation = Engine.getRandomDouble(-.5, 2);
						//random offset so that not every cloud of the same kind will look exactly the same, more random
						Sphere temp = new Sphere((float) (Math.max(1, scalingFactor) * ((cloudArr[x][z][y] - 0.1) * 3 + variation)) ,posX, posY, posZ);
						//best.add(new Sphere((float) (Math.max(1, scalingFactor / 2) * ((cloudArr[x][z][y] - 0.1) * 3 + variation)) ,posX, posY, posZ));
						if (cloudMat != null)
							temp.setMaterial(cloudMat);
						best.add(temp);
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
		
		double originX = center.getX();
		double originZ = center.getZ();
		double originY = y;
		
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		
		int count = 0;
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
							posX = originX - (midX * scalingFactor) + (x * scalingFactor); //xcoord
							posY = originY + (midY * scalingFactor) - (y * scalingFactor); //yCoord
							posZ = originZ - (midZ * scalingFactor) + (z * scalingFactor);
							double variation = Engine.getRandomDouble(-.5, 2);
							//random offset so that not every cloud of the same kind will look exactly the same, more random
							Sphere temp = new Sphere((float) (Math.max(1, scalingFactor * 2) * ((cloudArr[x][z][y] - 0.1) * 3 + variation)) ,posX, posY, posZ);
							if (cloudMat != null)
								temp.setMaterial(cloudMat);
							//secondBest.add(new Sphere((float) (Math.max(1, scalingFactor) * ((cloudArr[x][z][y] - 0.1) * 3 + variation)) ,posX, posY, posZ));							
							secondBest.add(temp);
						}
					}
					count++;
				}
			}
		}
		currentLevel = secondBest;
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
		return (scalingFactor * width * length/ 100);
	}
	
	@Override public void distFromCamera(double dist)
	{
		double firstLevel = 1500;
		double secondLevel = 2500;
		double thirdLevel = 3500;
		
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





