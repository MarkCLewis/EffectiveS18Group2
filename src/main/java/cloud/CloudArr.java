package cloud;

import virtualworld.terrain.Pair;
import virtualworld.terrain.Perlin;

public class CloudArr {
	public CloudArr(int newLength, int newHeight, int newWidth)
	{
		func = Perlin.getInstance();
		length = newLength;
		height = newHeight;
		width = newWidth;
		cloudArr = new double[length][width][height];
	}
	
	Perlin func;
	private final int width;
	private final int length;
	private final int height;
	private double xInc = 0;
	private double yInc = 0;
	private double zInc = 0;
	private int oct = 0;
	private double res = 0;
	private boolean inverse = false;
	private double f1 = 0;
	public double[][][] cloudArr;
	private int neighborCount = 0;		
	public void getInverse()	
	{
		inverse = true;
	}
	
	public void setOffSets(double x, double y, double z, int octave, double resistance)
	{
		double tempXInc = x/length;
		double tempYInc = y/height;
		double tempZInc = z/width;
		
		setXInc(tempXInc);
		setYInc(tempYInc);
		setZInc(tempZInc);
		setOctave(octave);
		setResistance(resistance);
	}
	
	public void setFilters(double fil1, double fil2)
	{
		setFilter1(fil1);
	}
	
	private void setXInc(double newXInc)
	{
		xInc = newXInc;
	}
	
	private void setYInc(double newYInc)
	{
		yInc = newYInc;
	}
	
	private void setZInc(double newZInc)
	{
		zInc = newZInc;
	}
	
	private void setOctave(int newOct)
	{
		oct = newOct;
	}
	
	private void setResistance(double resist)
	{
		res = resist;
	}
	
	private void setFilter1(double fil1)
	{
		f1 = fil1;
	}
	
	
	private void makeCloudArray3d() 
	{
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
						if(check < f1)
							check = 0;
					}
					else
					{
						if (check > f1 )//|| check < 0.20)
							check = 0;
					}
					cloudArr[x][z][y] = check; 
					xOff += xInc;
				}
				zOff += zInc;
			}
			yOff += yInc;
		}
		clearEdges();
	}
	
	public void processing()
	{
		makeCloudArray3d();
		filterIsolated();
		reduceCluster();
	}
	
	//go through the array and do neighborTest on each coordinate
	private void reduceCluster ()
	{
		for (int x = 0; x < length; x++)
		{
			for (int z = 0; z < width; z++)
			{
				for(int y = 0; y < height; y++)
				{
					neighborTest(x,z,y);
				}
			}
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
		{
			if (z > 0 && z < width)
			{
				if (y > 0 && y < height)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	//if the coordinate is non zero, that means there it is a neighbor of the coordinate passed in
	private double checkNeighbor (int x, int z, int y)
	{
		if (cloudArr[x][z][y] > 0) 
		{
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
		{
			for (int z = 0; z < width; z++)
			{
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
		}
	}
	

    //miscellaneous function for debugging
    public void printYLevel(int i )
    {
    	for (int x = 0; x < length; x++)
    	{
    		for (int z = 0; z < width; z++)
    		{
    			System.out.printf("%-10.2f", cloudArr[x][z][i]);
    		}
    		System.out.println();
    	}
    }
    
    public boolean isEmpty(int x, int z, int y)
    {
    	if (validLoc (x,z,y))
    	{
    		if (cloudArr[x][z][y] > 0)
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    private void filterIsolated()
    {
    	for (int y = 0; y < height; y++)
    	{
	    	for (int x = 0; x < length; x++)
	    	{
	    		for (int z = 0; z < width; z++)
	    		{
	    			clearIsolatedSphere(x,z,y);
	    		}
	    	}
    	}
    }
    
    private void clearIsolatedSphere(int x, int z, int y)
    {
    	int count = 0;
    	//combinations of y
    	if (!isEmpty(x,z,y-1))
    		count++;
    	
    	if (!isEmpty(x,z,y+1))
    		count++;
    	
    	//combinations of z
    	if (!isEmpty(x,z-1, y))
    		count++;
    	
    	if (!isEmpty(x,z-1,y+1))
    		count++;
    	
    	if (!isEmpty(x,z-1,y-1))
    		count++;
    	
    	if (!isEmpty(x,z+1,y))
    		count++;
    	
    	if (!isEmpty(x,z+1,y+1))
    		count++;
    	
    	if (!isEmpty(x,z+1,y-1))
    		count++;
    	
    	//combinations of x+1
    	if (!isEmpty(x+1,z,y))
    		count++;
    	
    	if (!isEmpty(x+1,z,y+1))
    		count++;
    	
    	if (!isEmpty(x+1,z,y-1))
    		count++;
    	
    	if (!isEmpty(x+1,z+1,y))
    		count++;
    	
    	if (!isEmpty(x+1,z+1,y+1))
    		count++;
    	
    	if (!isEmpty(x+1,z+1,y-1))
    		count++;
    	
    	if (!isEmpty(x+1,z-1,y))
    		count++;
    	
    	if (!isEmpty(x+1,z-1,y+1))
    		count++;
    	
    	if (!isEmpty(x+1,z-1,y-1))
    		count++;
    	
    	//combinations of x-1
    	
    	if (!isEmpty(x-1,z,y))
    		count++;
    	
    	if (!isEmpty(x-1,z,y+1))
    		count++;
    	
    	if (!isEmpty(x-1,z,y-1))
    		count++;
    	
    	if (!isEmpty(x-1,z+1,y))
    		count++;
    	
    	if (!isEmpty(x-1,z+1,y+1))
    		count++;
    	
    	if (!isEmpty(x-1,z+1,y-1))
    		count++;
    	
    	if (!isEmpty(x-1,z-1,y))
    		count++;
    	
    	if (!isEmpty(x-1,z-1,y+1))
    		count++;
    	
    	if (!isEmpty(x-1,z-1,y-1))
    		count++;
    	
    	if (count == 0)
    		cloudArr[x][z][y] = 0;
    }
}
