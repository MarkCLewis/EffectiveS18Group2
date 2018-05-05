package cloud;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cloud.Cloud;
import cloud.CloudFactory;
import engine.Engine;

public class CloudFactoryTest {
	
	@Test
	public void test()
	{
		
		CloudFactory cf = CloudFactory.getInstance();
		
		List<Cloud> clouds = new ArrayList<Cloud>();
		for (int i = 0; i < 1000; i++)
		{
			double x = Engine.getRandomDouble(0, 1000);
			double y = Engine.getRandomDouble(0, 1000);
			double z = Engine.getRandomDouble(0, 1000);
			int type = Engine.getRandomInt(0, 4);
			clouds.add(cf.getCloud(x, y, z, type, cf.getRandomSize(type)));
		}
		
		assertTrue(clouds.size() == 1000);
		assertTrue(cf.getSizeMap() <= 3);
	}
}

