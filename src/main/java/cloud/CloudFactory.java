package cloud;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joml.Math;

import shapes.RenderMaterial;
import virtualworld.terrain.Point;
public class CloudFactory {
	private CloudFactory()
	{
		cloudMat.setUseTexture(true);
		cloudMat.setShininess(0.1f);
		cloudMat.setTextureDiffusePath("Textures/Terrain/splat/clouddiffuse.jpg");
		cloudMat.setTextureAlphaPath("Textures/Terrain/splat/cloudalpha.jpg");
		cloudMat.setUseTransparency(true);
		cloudMat.setColorAlphas(0.0f);
	}
	
	Random random = new Random();
	RenderMaterial cloudMat = new RenderMaterial();
	
	private static CloudFactory CF = null;
	
	private Map<String, CloudArr> arrMap = new HashMap<String, CloudArr>();
	
	public static CloudFactory getInstance()
	{
		if (CF == null)
			return new CloudFactory();
		else
			return CF;
	}
	
	
	public Cloud getCloud(double x, double y, double z, int rand, double size)
	{
		//int rand;
		int length;
		int height;
		int width;
		
		//rand = Engine.getRandomInt(0, 4);
		//rand = 4;
		
		double newY;
		double skyLevel = 1750;
		if (y < skyLevel) 
		{
			newY = skyLevel;
		}
		else
		{
			newY = y;
		}
		
		
		//Create a single uniform cloud
		if (rand == 0)
		{
			CloudArr arr = arrMap.get("single");
			if (arr == null)
			{
				length = 30; //original;
				height = 10; //original;
				width = 20; //original;
				arr = new CloudArr(length,height,width);
				arr.setOffSets(6.5, 3, 3, 2, 8);
				arr.setFilters(0.55, 0.75);
				arr.getInverse();
				arr.processing();
				arrMap.put("single", arr);
			}
			double sf = size;
			return (new PerlinCloud(x, newY, z, arr, 0.75, sf, null));
		}
		
		//A little bit scatter cloud
		else if (rand == 1)
		{
			CloudArr arr = arrMap.get("scatter");
			if (arr == null)
			{
				length = 50;
				height = 15;
				width = 35;
				arr = new CloudArr(length, height,width);
				arr.setOffSets(2, 1, 1, 2, 4);
				arr.setFilters(0.45, 0.65);
				arr.getInverse();
				arr.processing();
				arrMap.put("scatter", arr);
			}
			double sf = size;
			return (new PerlinCloud(x, newY, z, arr, 0.65, sf, null));
		}
		
		else if (rand == 2)
		{
			CloudArr arr = arrMap.get("sky");
			if (arr == null)
			{
				length = 75;
				height = 20;
				width = 100;
				arr = new CloudArr(length,height,width);
				arr.setOffSets(3,5,2,2,4);
				arr.setFilters(0.65, 0.75);
				arr.getInverse();
				arr.processing();
				arrMap.put("sky", arr);
			}
			double sf = size;
			return (new PerlinCloud(x, newY, z, arr, 0.75, sf, null));	
		}
		else if (rand == 3)
		{
			int sizeOfSpiral = (int) size;
			return (new SquareSpiralCloud(x, newY, z, sizeOfSpiral * 10, cloudMat));
		}
		
		else if (rand == 4)
		{
			//testing
			double gap = size;
			return (new SpiralCloud(x, newY, z, gap, cloudMat));
			
		}
		return null;
	}
	
	public double getRandomSize(int type)
	{
		if (type == 0)
			return getRandomDouble(3, 5);	
		if (type == 1)
			return getRandomDouble(5, 9);
		if (type == 2)
			return getRandomDouble(5, 12); //kinda laggy at 10
		if (type == 3)
			return getRandomInt(5, 30);
		if (type == 4)
			return getRandomDouble(0.3, 0.5);
		return 0;
			
	}
	
	public List<Cloud> getClouds(Point inCenter, double requiredSize)
	{
			
		
		List<Cloud> clouds = new ArrayList<Cloud>();
		
		if (requiredSize < 1024)
			return clouds;
		
		
		double dim = requiredSize;
		Point center = inCenter;
		
		int UL = (int)(center.getX() - dim/2);
		int UR = (int)(center.getX() + dim/2);
		int LL = (int)(center.getZ() - dim/2);
		int LR = (int)(center.getZ() + dim/2);

		random.setSeed((long)(center.getX() * center.getZ()));
		
		int noOfClouds = Math.min(2, getRandomInt(6, (int) dim/100 + 1));//random.nextInt((int)dim/100 + 1) - 6);
		
		//int spiral = 1;
		int spiral = getRandomInt(0, 7);
		
		if (spiral != 0)
		{
			for (int i = 0; i < noOfClouds; i++)
			{
				int x = getRandomInt(UL,UR);
				int z = getRandomInt(LL, LR);
				int type = getRandomInt(0, 2);
				double size = getRandomSize(type);
				clouds.add(getCloud(x, 0, z, type, size));
			}
		}
		
		else
		{
			int type = getRandomInt(3,4);
			double size = getRandomSize(type);
			clouds.add(getCloud(center.getX(), 0, center.getZ(),type, size));
		}
		System.out.println(spiral);
		
		return clouds;
	}
	
	private int getRandomInt(int low, int high)
	{
		return (random.nextInt(high - low + 1) + low);
	}
	
	private double getRandomDouble(double low, double high)
	{
		return (random.nextDouble() * (low -high) + low);
	}
	
	public int getSizeMap()
	{
		return arrMap.size();
	}
}
