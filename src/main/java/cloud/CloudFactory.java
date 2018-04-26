package cloud;
import java.util.concurrent.ThreadLocalRandom;
public class CloudFactory {
	public CloudFactory()
	{
		
	}
	
	public Cloud getCloud(int x, int y, int z)
	{
		int rand;
		int length;
		int height;
		int width;
		
		ThreadLocalRandom ran = ThreadLocalRandom.current();
		rand = ran.nextInt(0,2);
		
		int newY;
		if (y < 6000) 
		{
			newY = 6000;
		}
		else
		{
			newY = y;
		}
			//Create a single uniform cloud
		if (rand == 0)
		{
			length = 30;
			height = 10;
			width = 20;
			Cloud temp = new Cloud(x, newY, z, length, height, width);
			temp.setOffSets(6.5, 3, 3, 2, 8);
			temp.setFilters(0.55, 0.75);
			temp.getInverse();
			return temp;
		}
		
		//A little bit scatter cloud
		else if (rand == 1)
		{
			length = 50;
			height = 15;
			width = 35;
			Cloud temp = new Cloud(x, newY, z, length, height, width);
			temp.setOffSets(2, 1, 1, 2, 4);
			temp.setFilters(0.45, 0.65);
			temp.getInverse();
			return temp;
		}
		
		else if (rand == 2)
		{
			length = 75;
			height = 20;
			width = 100;
			Cloud temp = new Cloud(x, newY, z, length, height, width);
			temp.setOffSets(3,5,2,2,4);
			temp.setFilters(0.65, 0.75);
			temp.getInverse();
			return temp;			
		}
		return null;
	}
}
