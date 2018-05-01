package cloud;

import java.util.ArrayList;
import java.util.List;

import shapes.Shape;
import shapes.Sphere;
import virtualworld.terrain.Perlin;
import virtualworld.terrain.Point;

public class PerlinCloud implements Cloud{
	public PerlinCloud (double x, double y, double z, CloudArr arr, double newF2)
	{
		this.y = y;
		func = Perlin.getInstance();	
		center = new Point(x,z);
		cloudArr = arr.cloudArr;
		length = cloudArr.length;
		height = cloudArr[0][0].length;
		width = cloudArr[0].length;
		f2 = newF2;
	}
	//determine how big the cloud is 
	
	
	private final int width;
	private final int length;
	private final int height;
	private final double y;
	private final Point center;
	Perlin func;
	private double[][][] cloudArr;
	private double f2 = 0;
	
	List<Shape> bestSpheres = new ArrayList<Shape>();

	
	@Override public void makeShape3d()
	{

		//calculate midpoint to get the right center

		
		int midX = length/2;
		int midY = height/2;
		int midZ = width/2;
		
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
						posX = originX - midX/sF + x/sF; //xcoord
						posY = originY + midY/sF - y/sF; //yCoord
						posZ = originZ - midZ/sF + z/sF;
						bestSpheres.add(new Sphere((float) (cloudArr[x][z][y] - 0.1) * 3 ,posX, posY, posZ));
						//cSphere.add(new Sphere((float) (0.35),posX, posY, posZ));
						//radius 2 is arbitrary for testing purposes
					}
				}
			}
		}
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
		
	}
	
	@Override public List<Shape> getShapes()
	{
		return bestSpheres;
	}
	
    @Override public boolean isActive() 
    {
    	return true;
    }
	

    
}





