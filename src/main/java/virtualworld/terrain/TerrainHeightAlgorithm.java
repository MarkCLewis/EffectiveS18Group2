package virtualworld.terrain;

public abstract class TerrainHeightAlgorithm {
	
	protected double frequency;
	protected int numOctaves;
	protected double octaveScale;
	protected double baseHeight;
	protected double heightSeed;
	protected Perlin noise;
	
	private double generateSubValue(double height, double freq, double x, double z) {
		double sub = baseHeight + (height * noise.noise2D( freq * x, freq*z));
		return sub; 
	}
	
	public double generateHeight(double x, double z) {
		double acc = generateSubValue(heightSeed, frequency, x, z);
		for (int i = 1; i < numOctaves; i++) {
			acc += generateSubValue(heightSeed / (octaveScale*i), frequency * (octaveScale*i), x, z);
		}
		return acc;
	}
}
