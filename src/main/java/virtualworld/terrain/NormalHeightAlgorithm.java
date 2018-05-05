package virtualworld.terrain;

public class NormalHeightAlgorithm extends TerrainHeightAlgorithm {

	NormalHeightAlgorithm(double freq, int octaves, double scale, double height, double size) {
		frequency = freq;
		numOctaves = octaves;
		octaveScale = scale;
		baseHeight = height;
		heightSeed = size;
		treeSparseness = 1;
		noise = Perlin.getInstance();
	}
	
	
	public static TerrainHeightAlgorithm forFields(double size) {
		double freq = size/1200;
		return new NormalHeightAlgorithm(freq, 3, 4, 250, 1);
	}
	
	public static TerrainHeightAlgorithm forHills(double size) {
		double freq = size / 800;
		return new NormalHeightAlgorithm(freq, 3, 4, 350, 1);
	}
	
	public static TerrainHeightAlgorithm forMountains(double size) {
		double freq = size / 400;
		return new NormalHeightAlgorithm(freq, 3, 3, 500, 1);
	}
	

}
