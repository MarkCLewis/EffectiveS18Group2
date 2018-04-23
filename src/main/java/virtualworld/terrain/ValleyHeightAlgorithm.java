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
		noise = Perlin.getInstance();
		redistribution = redis;
	}
	
	@Override
	public double generateHeight(double x, double z) {
		double height = super.generateHeight(x, z);
		if (height <= 200) {
			return Math.pow(height, 1/redistribution);	
		} else {
			return Math.pow(height, redistribution);
		}
	}
	
}
