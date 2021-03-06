package virtualworld.terrain;

public class ValleyHeightAlgorithm extends TerrainHeightAlgorithm {

	//keep the redistribution value small
	double redistribution;
	
	ValleyHeightAlgorithm(double freq, int octaves, double scale, double height, double size, double redis ) {
		frequency = freq;
		numOctaves = octaves;
		octaveScale = scale;
		baseHeight = height;
		heightSeed = size;
		treeSparseness = 10;
		noise = Perlin.getInstance();
		redistribution = redis;
	}
	
	public static TerrainHeightAlgorithm forNormalValley(double size) {
		double freq = size/50000;
		return new ValleyHeightAlgorithm (freq, 4, 3, 700, 2, 2.5);
	}
	
	@Override
	public double generateHeight(double x, double z) {
		double acc = super.generateSubValue(heightSeed, frequency, x, z);
		for (int i = 1; i < numOctaves; i++) {
			acc += super.generateSubValue(heightSeed / (octaveScale*i), frequency * ((octaveScale*i)/2), x, z);
		}
		return Math.pow(acc, redistribution) * baseHeight;
	}
	
	@Override
	public double partialHeight(double x, double z) {
		double acc = super.generateSubValue(heightSeed, frequency, x, z);
		for (int i = 1; i < numOctaves; i++) {
			acc += super.generateSubValue(heightSeed / (octaveScale*i), frequency * ((octaveScale*i)/2), x, z);
		}
		return Math.pow(acc, redistribution);
	}

	
}
