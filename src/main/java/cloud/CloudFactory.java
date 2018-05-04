package cloud;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Math;

import engine.Engine;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;
public class CloudFactory {
	private CloudFactory()
	{
		
	}
	
	
	private static CloudFactory CF = null;
	
	private Map<String, CloudArr> arrMap = new HashMap<String, CloudArr>();
	
	public static CloudFactory getInstance()
	{
		if (CF == null)
			return new CloudFactory();
		else
			return CF;
	}
	
	
	public Cloud getCloud(double x, double y, double z, int rand)
	{
		//int rand;
		int length;
		int height;
		int width;
		
		//rand = Engine.getRandomInt(0, 4);
		//rand = 4;
		
		double newY;
		double skyLevel = 2500;
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
				length = 60;//30 original;
				height = 20;//10 original;
				width = 40;//20 original;
				arr = new CloudArr(length,height,width);
				arr.setOffSets(6.5, 3, 3, 2, 8);
				arr.setFilters(0.55, 0.75);
				arr.getInverse();
				arr.processing();
				arrMap.put("single", arr);
			}
			double sf = getRandomSize("single");
			return (new PerlinCloud(x, newY, z, arr, 0.75, sf));
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
			double sf = getRandomSize("scatter");
			return (new PerlinCloud(x, newY, z, arr, 0.65, sf));
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
			double sf = getRandomSize("sky");
			return (new PerlinCloud(x, newY, z, arr, 0.75, sf));	
		}
		else if (rand == 3)
		{
			int sizeOfSpiral = (int) getRandomSize("squareSpiral");
			return (new SquareSpiralCloud(x, newY, z, sizeOfSpiral * 10));
		}
		
		else if (rand == 4)
		{
			//testing
			double gap = getRandomSize("regularSpiral");
			return (new SpiralCloud(x, newY, z, gap));
			
		}
		return null;
	}
	
	private double getRandomSize(String type)
	{
		if (type.equals("single"))
			return Engine.getRandomDouble(1, 7.5);	
		if (type.equals("scatter"))
			return Engine.getRandomDouble(3, 9);
		if (type.equals("sky"))
			return Engine.getRandomDouble(5, 8); //kinda laggy at 10
		if (type.equals("squareSpiral"))
			return Engine.getRandomInt(5, 30);
		if (type.equals("regularSpiral"))
			return Engine.getRandomDouble(0.3, 0.5);
		return 0;
			
	}
	
	public List<Cloud> getClouds(Terrain t)
	{
		List<Cloud> clouds = new ArrayList<Cloud>();
		
		double dim = t.getSize();
		Point center = t.getCenter();
		
		double UL = center.getX() - dim/2;
		double UR = center.getX() + dim/2;
		double LL = center.getZ() - dim/2;
		double LR = center.getZ() + dim/2;
		
		int noOfClouds = Math.min(5,Engine.getRandomInt(6, (int)dim/100 + 1));
		
		//int spiral = 1;
		int spiral = Engine.getRandomInt(0, 7);
		
		if (spiral != 0)
		{
			for (int i = 0; i < noOfClouds; i++)
			{
				double x = Engine.getRandomDouble(UL, UR);
				double z = Engine.getRandomDouble(LL, LR);
				int type = Engine.getRandomInt(0, 3);
				clouds.add(getCloud(x, 0, z, type));
			}
		}
		
		else
		{
			int type = Engine.getRandomInt(3,5) + 3;
			System.out.println("Type = " + type);
			clouds.add(getCloud(center.getX(), 0, center.getZ(),type));
		}
		System.out.println(spiral);
		
		return clouds;
	}
	
	public int getSizeMap()
	{
		return arrMap.size();
	}
}
