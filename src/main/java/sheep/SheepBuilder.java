package sheep;

import java.util.ArrayList;
import java.util.List;

import virtualworld.terrain.Perlin;
import virtualworld.terrain.Point;

public class SheepBuilder {
	
	//SheepBuilder takes the center of the world and the size of the world
	public SheepBuilder(Point cen, double wSz){
		worldSize = wSz;
		center = cen;
		func = Perlin.getInstance();
	}
	
	Perlin func;
	private final Point center;
	private double worldSize;
	
	
	//distribute uses Perlin noise to randomly distribute herd locations
	public List<Herd> distribute(){
		List<Herd> herd = new ArrayList<Herd>();
		return herd;
	}
	
	/*private static SheepBuilder sb = null;
	
	public static SheepBuilder getInstance(){
		if (sb == null)
			return new SheepBuilder();
		else
			return sb;
	}
	
	public List<Sheep> getSheep(Point center)
	{
		List<Sheep> shoop = new ArrayList<Sheep>();
		Point cen = center;	
		return shoop;
	}*/
}
