package engine;

import com.jme3.terrain.geomipmap.TerrainQuad;

import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;

public class WorldTerrain extends TerrainQuad {
	private Terrain terrainLoader;
	
	public WorldTerrain(String name, int patchSize, int totalSize, double[][] heightMap) {
		super(name,patchSize,totalSize,flattenArrayToFloat(heightMap));
		terrainLoader = new Terrain(new Point(0.0,0.0), (double)totalSize, Engine.getRandomDouble(0, 500), patchSize, heightMap);
		// TODO ****
	}
	
	public static float[] flattenArray(float[][] tdArr) {
		float[] ret = new float[tdArr.length * tdArr[0].length];
		for(int i = 0; i < tdArr.length; i++) {
			for(int j = 0; j < tdArr[0].length; j++) {
				ret[(i*tdArr[0].length) + j] = tdArr[i][j];
			}
		}
		return ret;
	}
	
	public static double[] flattenArray(double[][] tdArr) {
		double[] ret = new double[tdArr.length * tdArr[0].length];
		for(int i = 0; i < tdArr.length; i++) {
			for(int j = 0; j < tdArr[0].length; j++) {
				ret[(i*tdArr[0].length) + j] = tdArr[i][j];
			}
		}
		return ret;
	}
	
	public static float[] flattenArrayToFloat(double[][] tdArr) {
		float[] ret = new float[tdArr.length * tdArr[0].length];
		for(int i = 0; i < tdArr.length; i++) {
			for(int j = 0; j < tdArr[0].length; j++) {
				ret[(i*tdArr[0].length) + j] = (float)(tdArr[i][j]);
			}
		}
		return ret;
	}
}
