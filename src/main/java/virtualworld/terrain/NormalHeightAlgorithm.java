package virtualworld.terrain;

public class NormalHeightAlgorithm extends TerrainHeightAlgorithm {

	NormalHeightAlgorithm(double freq, int octaves, double scale, double height, double size ) {
		frequency = freq;
		numOctaves = octaves;
		octaveScale = scale;
		baseHeight = height;
		heightSeed = size;
		noise = Perlin.getInstance();
	}

}
