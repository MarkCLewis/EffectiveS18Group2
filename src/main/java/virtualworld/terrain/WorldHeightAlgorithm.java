package virtualworld.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import engine.Engine;

public class WorldHeightAlgorithm extends TerrainHeightAlgorithm {
	
	double size;
	Point center;
	public Point l1t;
	public Point l1b;
	public Point l2t;
	public Point l2b;
	List<Region> regions;

	WorldHeightAlgorithm(Point c, double length) {
		size = length;
		center = c;
		noise = Perlin.getInstance();
		splitWorld();
	}
	
	private void splitWorld() {
		TerrainHeightAlgorithm fieldHeight = NormalHeightAlgorithm.forFields(size);
		TerrainHeightAlgorithm mountainHeight = NormalHeightAlgorithm.forMountains(size);
		TerrainHeightAlgorithm hillHeight = NormalHeightAlgorithm.forHills(size);
		
		Point topLeft = new Point (center.getX() - size/2, center.getZ() + size/2);
		System.out.println("Top Left: " + topLeft.getX() + " " + topLeft.getZ());
		Point bottomRight = new Point (center.getX() + size/2, center.getZ() - size/2);
		int randomPoint = ThreadLocalRandom.current().nextInt((int)(center.getX() - (size/4)), (int)(center.getX() + (size/4)));
		System.out.println(randomPoint);
		Point line1Top = new Point(randomPoint, center.getZ() + (size/2));
		Point line1Bottom = new Point(randomPoint, center.getZ() - (size/2));
		
		int randomPoint2 = ThreadLocalRandom.current().nextInt((int)(center.getZ() - (size/4)), (int)(center.getZ() + (size/4)));
		System.out.println(randomPoint2);
		Point line2Top = new Point(randomPoint, randomPoint2);
		Point line2Bottom = new Point(center.getX() + (size/2), randomPoint2);
		
		regions = new ArrayList<Region>();
		
		regions.add(new Region(topLeft, new Point(line1Bottom.getX() + (size * .15), line1Bottom.getZ()), mountainHeight));
		regions.add(new Region(line1Top, line2Bottom, hillHeight));
		regions.add(new Region(new Point(line2Top.getX() - (size * .15), line2Top.getZ()), bottomRight , fieldHeight));
		
		l1t = line1Top;
		l1b = line1Bottom;
		l2t = line2Top;
		l2b = line2Bottom;
	}
	
	@Override
	public double generateHeight(double x, double z) {
		double height = 0;
		int layers = 0; 
		for(Region r : regions) {
			if (r.containsPoint(x, z)) {
				height += r.noise.generateHeight(x,z);
				layers += 1;
			}
		}
		
		if (layers == 0) {
			height = 200;
			Engine.logInfo("X AND Z COORDINATES NOT FOUND");
			System.out.println("no layers");
		} else {
			System.out.println("layers: " + layers);
		}
		
		return (height/layers);
	}
	
	public List<Region> getRegions() {
		return regions;
	}
	
	
}
