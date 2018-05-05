package virtualworld.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import engine.Engine;
import worldmanager.WorldManager;

public class WorldHeightAlgorithm extends TerrainHeightAlgorithm {
	
	double size;
	Point center;
	public Point l1t;
	public Point l1b;
	public Point l2t;
	public Point l2b;
	List<Region> regions;

	WorldHeightAlgorithm(Point c, double length) {
		size = WorldManager.getInstance().getSize();
		center = c;
		noise = Perlin.getInstance();
		splitWorld();
		System.out.println("Num Regions " + regions.size());
	}
	
	private void splitWorld() {
		TerrainHeightAlgorithm fieldHeight = NormalHeightAlgorithm.forFields(size);
		TerrainHeightAlgorithm mountainHeight = ValleyHeightAlgorithm.forNormalValley(size);
		TerrainHeightAlgorithm hillHeight = NormalHeightAlgorithm.forHills(size);
		
		Point topLeft = new Point (center.getX() - size/2, center.getZ() + size/2);
		System.out.println("Top Left: " + topLeft.getX() + " " + topLeft.getZ());
		Point bottomRight = new Point (center.getX() + size/2, center.getZ() - size/2);
		
		int randomPoint = ThreadLocalRandom.current().nextInt((int)(center.getX() - (size/4)), (int)(center.getX() + (size/4)));
		int randomPoint2 = ThreadLocalRandom.current().nextInt((int)(center.getZ() - (size/4)), (int)(center.getZ() + (size/4)));
		
		Engine.logInfo(randomPoint + "");
		System.out.println(randomPoint);
		Point line1Top = new Point(randomPoint, center.getZ() + (size/2));
		Point line1Bottom = new Point(randomPoint, center.getZ() - (size/2));
		
		
		Engine.logInfo(randomPoint2 + "");
		System.out.println(randomPoint2);
		Point line2Top = new Point(randomPoint, randomPoint2);
		Point line2Bottom = new Point(center.getX() + (size/2), randomPoint2);
		
		regions = new ArrayList<Region>();
		
		regions.add(new Region(topLeft, new Point(line1Bottom.getX() + (size*0.05), line1Bottom.getZ()), mountainHeight));
		regions.add(new Region(new Point (line1Top.getX() - (size*0.05), line1Top.getZ()), new Point(line2Bottom.getX(), line2Bottom.getZ() - (size*0.05)), hillHeight));
		regions.add(new Region(new Point(line2Top.getX() - (size*0.05), line2Top.getZ() + (size*0.05)), bottomRight , fieldHeight));
		
		l1t = line1Top;
		l1b = line1Bottom;
		l2t = line2Top;
		l2b = line2Bottom;
	}
	
	@Override
	public double generateHeight(double x, double z) {
		double nx = (x + 0.5) * size; 
		double nz = (z + 0.5) * size; 
		double e = 0;
		double avgBaseHeight = 0;
		int layers = 0; 
		for(Region r : regions) {
			if (r.containsPoint(nx, nz)) {
				e += r.noise.partialHeight(x,z);
				layers += 1;
				avgBaseHeight += r.noise.baseHeight;
			}
		}
		
		if (layers > 1) {
			avgBaseHeight = avgBaseHeight/layers + avgBaseHeight/3;
		}
		
		double d = euclideanDistance((x+.5), (z+.5));
		return (((e/layers) + .3) * (1 - (.95*Math.pow(d, 1.05)))) * (avgBaseHeight/layers);
		
	}
	
	private double euclideanDistance(double x, double z) {
		return (2*(Math.sqrt(x * x + z*z)));
	}
	
	@Override
	public List<Region> getRegions() {
		return regions;
	}
	
	
}
