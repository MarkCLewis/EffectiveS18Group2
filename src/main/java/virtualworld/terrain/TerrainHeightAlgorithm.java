package virtualworld.terrain;

import org.joml.Math;

import javafx.geometry.Point3D;

public abstract class TerrainHeightAlgorithm {
	
	protected double frequency;
	protected int numOctaves;
	protected double octaveScale;
	protected double baseHeight;
	protected double heightSeed;
	protected double treeSparseness;
	protected Perlin noise;
	
	protected double generateSubValue(double height, double freq, double x, double z) {
		double sub = (height * noise.noise2D( freq * x, freq*z));
		return sub; 
	}
	
	public double generateHeight(double x, double z) {
		double acc = generateSubValue(heightSeed, frequency, x, z);
		for (int i = 1; i < numOctaves; i++) {
			acc += generateSubValue(heightSeed / (octaveScale*i), frequency * ((octaveScale*i)/2), x, z);
		}
		return acc * baseHeight;
	}
	
	public Point3D placeTree(double x, double z) {
		Point3D max = new Point3D(0,0,0);
		for(int zn = (int) (z - treeSparseness); zn <= z + treeSparseness; zn++) {
			for(int xn = (int) (x - treeSparseness); xn <= x + treeSparseness; xn++) {
				double e = noise.noise2D(xn, zn);
				if (e > max.getY()) {
					max = new Point3D(xn, e, zn);
				}
			}
		}
		if (Math.floor(z) == max.getZ() && Math.floor(x) == max.getX()) {
			return max;
		} else {
			return null;
		}
	}
	
}
