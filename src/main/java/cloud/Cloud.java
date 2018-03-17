package cloud;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import virtualworld.terrain.Pair;
import virtualworld.terrain.Perlin;
public class Cloud implements Entity {
	
	public Cloud (double x, double y, double z, int newWidth, int newLength, int newHeight)
	{
		this.z = z;
		width = newWidth;
		length = newLength;
		height = newHeight;
		func = Perlin.getInstance();	
		dimArr = new double[length][height];
		center = new Pair<Double,Double> (x,y);
		makeCloudArray2d();
		cloud = makeShape();
	}
	//determine how big the cloud is 
	
	
	private final int width;
	private final int length;
	private final int height;
	private final double z;
	private final Pair<Double,Double> center;
	Perlin func;
	private double[][] dimArr; //actual representation of the cloud, might have to change later , for now, only 2d represenetation
	private List<Sphere> cloud;
			
    public double OctavePerlin(double x, double y, int octaves, double persistence) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
        for(int i=0;i<octaves;i++) {
            total += func.noise2D(x * frequency, y * frequency) * amplitude;
            
            maxValue += amplitude;
            
            amplitude *= persistence;
            frequency *= 2;
        }
        
        return total/maxValue;
    }
    
	private void makeCloudArray2d()
	{
		//only for 2d cloud
		//2d array to represent 2d cloud
		//increase the increment increase the number of scattered clouds within a frame
		double xInc = 3.0/length;
		double yInc = 3.0/height;
		//System.out.println(xInc +" " + yInc +"\n\n");
		double yOff = 0;
		for (int y = 0; y < height; y++){
			double xOff = 0;
			for (int x = 0; x < length; x++){
				//get value from perlin noise function
				double value = (((float)(OctavePerlin(xOff,yOff, 3, 4) % 1.0)));
				//offset so it is between 0 and 1
				double check = (value + 1) /2;
				//if (check > 0.5)
					//check = 0;
				dimArr[x][y] = check; 
				xOff += xInc;
			}
			yOff += yInc;
		}
	}
	
	private void makeCloudArray3d() 
	{
		
	}
	
	public List<Sphere> makeShape()
	{
		List<Sphere> cSphere = new ArrayList<Sphere>();
		//calculate midpoint to get the right center
		int midX = length/2;
		int midY = length/2;
		int midZ = length/2;
		
		double originX = center.getLeft();
		double originY = center.getRight();
		
		double posX = 0;
		double posY = 0;
		double posZ =0;
		for (int x = 0; x < length; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (dimArr[x][y] > 0)
				{
					posX = originX - midX + x; //xcoord
					posY = originY + midY - y; //yCoord    
					cSphere.add(new Sphere(posX, posY, posZ, 2));
					//radius 2 is arbitrary for testing purposes
				}
			}
		}
		return cSphere;
	}
	
	public double[][] getCloudArray ()
	{
		return dimArr;
	}
	
	public Pair<Double,Double> getCenter() 
	{
		return center;
	}
	
	public double getSize() 
	{
		return (height * width * length);
	}
	
	public List<Sphere> getCloud()
	{
		return cloud;
	}
}




