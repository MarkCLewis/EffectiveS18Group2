package cloud;
import java.util.HashMap;
import java.util.Map;

import engine.Engine;
public class CloudFactory {
	public CloudFactory()
	{
		
	}
	
	public Map<String, CloudArr> arrMap = new HashMap<String, CloudArr>();
	
	public Cloud getCloud(int x, int y, int z)
	{
		int rand;
		int length;
		int height;
		int width;
		
		rand = Engine.getRandomInt(0, 4);
		rand = 4;
		
		int newY;
		int skyLevel = 2000;
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

			PerlinCloud temp = new PerlinCloud(x, newY, z, arr, 0.75);
			temp.makeShape3d();
			return temp;
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

			PerlinCloud temp = new PerlinCloud(x, newY, z, arr, 0.65);
			temp.makeShape3d();
			return temp;
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

			PerlinCloud temp = new PerlinCloud(x, newY, z, arr, 0.75);
			temp.makeShape3d();
			return temp;			
		}
		else if (rand == 3)
		{
			int sizeOfSpiral = Engine.getRandomInt(5,50);
			SquareSpiralCloud temp = new SquareSpiralCloud(x, newY, z, sizeOfSpiral * 10);
			temp.makeShape3d();
			return temp;
		}
		
		else if (rand == 4)
		{
			//testing
			double gap = 0.5;
			SpiralCloud temp = new SpiralCloud(x, newY, z, gap);
			temp.makeShape3d();
			return temp;
			
		}
		return null;
	}
}
