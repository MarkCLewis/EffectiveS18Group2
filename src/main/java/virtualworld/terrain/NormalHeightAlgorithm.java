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
	
	public static TerrainHeightAlgorithm forFeilds() {
		return new NormalHeightAlgorithm(.25, 3, 4, 200, 200);
	}
	
	public static TerrainHeightAlgorithm forHills() {
		return new NormalHeightAlgorithm(.75, 3, 6, 200, 300);
	}
	
	public static TerrainHeightAlgorithm forMountains() {
		return new NormalHeightAlgorithm(1.5, 3, 10, 150, 400);
	}
	

}
