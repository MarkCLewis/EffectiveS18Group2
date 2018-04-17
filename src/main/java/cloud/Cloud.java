package cloud;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import shapes.*;
import virtualworld.terrain.Pair;
import virtualworld.terrain.Perlin;
import virtualworld.terrain.Point;
public class Cloud implements Entity {
	
//height might be an optional to create different kind of cloud since it is often not more than 5 or 10	

	public Cloud (double x, double y, double z, int newLength, int newHeight, int newWidth)
	{
		this.y = y;
		width = newWidth;
		length = newLength;
		height = newHeight;
		func = Perlin.getInstance();	
		dimArr = new double[length][width];
		center = new Point(x,z);
		makeCloudArray2d();
		cloudArr = new double[length][width][height];
	}
	//determine how big the cloud is 
	
	
	private final int width;
	private final int length;
	private final int height;
	private final double y;
	private final Point center;
	Perlin func;
	private double[][] dimArr; //actual representation of the cloud, might have to change later , for now, only 2d represenetation
	private double[][][] cloudArr;
	private ArrayList<Shape> cloud;
	private int neighborCount = 0;		

	private double xInc = 0;
	private double yInc = 0;
	private double zInc = 0;
	private int oct = 0;
	private double res = 0;
	private boolean inverse = false;
	
	public void getInverse()
	{
		inverse = true;
	}
	
	public void setOffSets(double x, double y, double z, int octave, double resistance)
	{
		xInc = x/length;
		yInc = y/height;
		zInc = z/width;
		oct = octave;
		res = resistance;
	}
	private void makeCloudArray2d()
	{
		//working code
		//only for 2d cloud
		//2d array to represent 2d cloud
		//increase the increment increase the number of scattered clouds within a frame
		double xInc = 3.0/length;
		double yInc = 3.0/width;
		//System.out.println(xInc +" " + yInc +"\n\n");
		double yOff = 0;
		for (int y = 0; y < width; y++){
			double xOff = 0;
			for (int x = 0; x < length; x++){
				//get value from perlin noise function
				double value = (((float)(func.OctavePerlin(xOff,yOff, 3, 4))));
				//offset so it is between 0 and 1
				double check = (value + 1) /2;
				//if (check > 0.5)
				//	check = 0;
				dimArr[x][y] = check; 
				xOff += xInc;
			}
			yOff += yInc;
		}
	}
	
	private void makeCloudArray3d() 
	{
		//semi working ish
		//double xInc = 0.75/length;
		//double yInc = 1.0/height;
		//double zInc = 3.0/width; // have to change later
		//System.out.println(xInc +" " + yInc +"\n\n");
		double yOff = 0;
		
		for (int y = 0; y < height; y++) {
			double zOff = 0;
			for (int z = 0; z < width; z++){				
				//double xOff = func.noise2D(height - y, width - z);
				//double xOff = 0;
				double xOff = func.noise2D(y,z);
				for (int x = 0; x < length; x++){
					//get value from perlin noise function
					double value = (func.OctavePerlin3d(xOff,zOff, yOff, oct, res));
					//offset so it is between 0 and 1
					double check = (value + 1) /2;
					if (inverse) 
					{
						if(check < 0.45)
							check = 0;
					}
					else
					{
						if (check > 0.45 )//|| check < 0.20)
							check = 0;
					}
					cloudArr[x][z][y] = check; 
					xOff += xInc;
				}
				zOff += zInc;
			}
			yOff += yInc;
		}
		//testing
		//clearEdges();
	}
	
	
	//Given a particular array, create a sphere for each location where cloudArr[x][z][y] is non - zero
	//The radius of the sphere is dependent on the value hold by cloudArr[x][z][y]
	
	public ArrayList<Shape> makeShape3d()
	{
		makeCloudArray3d();
		reduceCluster();
		//offTheTop();
		//cirrocumulus();
		ArrayList<Shape> cSphere = new ArrayList<Shape>();
		//calculate midpoint to get the right center
		int midX = length/2;
		int midY = height/2;
		int midZ = width/2;
		
		double sF = 1;
		
		double originX = center.getX();
		double originZ = center.getY();
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
					if (cloudArr[x][z][y] > 0)
					{
						posX = originX - midX/sF + x/sF; //xcoord
						posY = originY + midY/sF - y/sF; //yCoord
						posZ = originZ - midZ/sF + z/sF;
						cSphere.add(new Sphere((float) (cloudArr[x][z][y] - 0.1) * 3 ,posX, posY, posZ));
						//cSphere.add(new Sphere((float) (0.35),posX, posY, posZ));
						//radius 2 is arbitrary for testing purposes
					}
				}
			}
		}
		return cSphere;
	}
	
	
	public double[][] getCloudArray ()
	{
		return dimArr;
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
		return cloud;
	}
	
	public List<Shape> getCloud()
	{
		return cloud;
	}
	
	
	//go through the array and do neighborTest on each coordinate
	private void reduceCluster ()
	{
		for (int x = 0; x < length; x++)
			for (int z = 0; z < width; z++)
				for(int y = 0; y < height; y++)
				{
					neighborTest(x,z,y);
				}
	}
	
	//Perform a neighbors check, if at least 4 neighbors are present, condense them into one sphere by
	//zeroing out all the neighbors and has the current coordinate to be their total radius /3
	private void neighborTest (int x, int z, int y)
	{
		int pass = 3;
		Pair<Integer,Double> check = neighBors(x,z,y);
		
		if (check.getLeft() >= pass)
		{
			double newRadius = check.getRight() / 2.0 + cloudArr[x][z][y]/2;
			cloudArr[x][z][y] = newRadius;
			clearNeighbors(x,z,y);
		}
		
	}
	
	//checking 6 nearby coor to see if there is a non zero number nearby
	//return the pair of <number of neightbors, and their combined radius> 		
	
	private Pair<Integer, Double> neighBors (int x, int z, int y)
	{

		neighborCount = 0;
		double totalCoverage = 0;
		if (validLoc (x - 1, z, y))
			totalCoverage += checkNeighbor(x-1,z,y);
		
		if (validLoc (x, z-1, y))
			totalCoverage += checkNeighbor(x, z-1, y);
		
		if (validLoc (x, z, y-1))
			totalCoverage += checkNeighbor(x,z,y-1);
		
		if (validLoc (x+1, z, y))
		    totalCoverage += checkNeighbor(x+1,z,y);
		
		if (validLoc (x, z+1, y))
		    totalCoverage += checkNeighbor(x, z+1, y);
		
		if (validLoc (x, z ,y+1))
			totalCoverage += checkNeighbor(x,z,y+1);
		
		return new Pair<Integer,Double> (neighborCount, totalCoverage);
	}
	
	//check to prevent index out of bound
	private boolean validLoc (int x, int z, int y)
	{
		if (x > 0 && x < length)
			if (z > 0 && z < width)
				if (y > 0 && y < height)
					return true;
		return false;
	}
	
	//if the coordinate is non zero, that means there it is a neighbor of the coordinate passed in
	private double checkNeighbor (int x, int z, int y)
	{
		if (cloudArr[x][z][y] > 0) {
			neighborCount++;
			return cloudArr[x][z][y];
		}
		return 0;
	}
	
	//Use this function to clean up the neighbors after condense them into a single sphere
	private void clearNeighbors(int x, int z, int y)
	{
		if (validLoc (x - 1, z, y))
			cloudArr[x-1][z][y] = 0;
		
		if (validLoc (x, z-1, y))
			cloudArr[x][ z-1][ y] = 0;
		
		if (validLoc (x, z, y-1))
			cloudArr[x][z][y-1] = 0;
		
		if (validLoc (x+1, z, y))
		    cloudArr[x+1][z][y] = 0;
		
		if (validLoc (x, z+1, y))
		   cloudArr[x][ z+1][y] = 0;
		
		if (validLoc (x, z ,y+1))
			cloudArr[x][z][y+1] = 0;
	}
	
	private void clearEdges()
	{
		for (int x = 0; x < length; x++)
			for (int z = 0; z < width; z++)
				for (int y = 0; y < height; y++)
				{
					if (x < 2 || x > (length - 3))
					{
						cloudArr[x][z][y] = 0;
					}
					else if (z < 2 || z > (width - 3))
					{
						cloudArr[x][z][y] = 0;
					}
				}
	}
	
    //trying to make another type of cloud, not based on perlin noise
    private void cirrocumulus()
    {
		double xInc = 1.0/length;
		double yInc = 2.0/height;
		double zInc = 1.3/width;
		
		double yOff = 0;
    	for (int y = 0; y < height; y++)
    	{
    		double zOff = 0;
    		for (int z = 1; z < width ; z++)
    		{
    			double xOff = 0;
    			for (int x = 1; x < length; x++)
    			{
    				if (y == 0)
    				{
    					if ( (z % 6 == 0))
    					{
    						if (x % 5 == 0)
    						{
    							double val = (func.OctavePerlin3d(xOff, zOff, yOff, 1, 2) + 1 ) /2;
    							//changing the neighbor to surround this particular point, creating a pseudo small clous
    							cloudArr[x][z][y] = val;
    							cloudArr[x-1][z][y] = val;
    							cloudArr[x+1][z][y] = val;
    							cloudArr[x][z-1][y] = val;
    							cloudArr[x][z+1][y] = val;
    							cloudArr[x][z][y+1] = val;
    						}
    					}
    					else
    						cloudArr[x][z][y] = 0;
    				}
    				
    				if (y == 3)
    				{
    					if (z % 6 == 3)
    					{
    						if (x % 5 == 3)
    						{
    							double val = (func.OctavePerlin3d(xOff, zOff, yOff, 1, 2) + 1 ) /2;
    							//changing the neighbor to surround this particular point, creating a pseudo small clous
    							cloudArr[x][z][y] = val;
    							cloudArr[x-1][z][y] = val;
    							cloudArr[x+1][z][y] = val;
    							cloudArr[x][z-1][y] = val;
    							cloudArr[x][z+1][y] = val;
    							cloudArr[x][z][y+1] = val;
    							cloudArr[x][z][y-1] = val;
    						}
    					}
    				}
    				
    				else 
    				{
    					/*if (cloudArr[x][z][y-1] > 0)
    					{
    						double val = (func.OctavePerlin3d(xOff, zOff, yOff, 3, 4) + 1)/2.0;
    						cloudArr[x][z][y] = val;
    						if (z + 1 < width)
    							cloudArr[x][z+1][y] = val;
    					}
    					else*/
    						cloudArr[x][z][y] = 0;
    				}
    				xOff += xInc;
    			}   				
    			zOff += zInc;
    		}
    		yOff += yInc;
    	}
    }
    
    private void offTheTop()
    {
    	for (int x = 0; x < length; x++)
    		for (int z = 0; z < width; z++)
    			cloudArr[x][z][height-1] = 0;
    }
    
    //todo smooth the edges so it looks less square
    
    private void smoothEdges()
    {
    	
    }
    
}




