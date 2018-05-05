package virtualworld.terrain;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
	
	public double partialHeight(double x, double z) {
		double acc = generateSubValue(heightSeed, frequency, x, z);
		for (int i = 1; i < numOctaves; i++) {
			acc += generateSubValue(heightSeed / (octaveScale*i), frequency * ((octaveScale*i)/2), x, z);
		}
		return acc;
	}
	
	
	public List<Region> getRegions () {
		return null;
	}
	
}
