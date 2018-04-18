package engine;

import java.nio.FloatBuffer;

import com.jme3.terrain.noise.Basis;
import com.jme3.terrain.noise.basis.Noise;

import virtualworld.terrain.Perlin;

public final class EngineTerrainNoise extends Noise implements Basis {
	
	private final Perlin pNoise = Perlin.getInstance();
	private float heightSeed;
	private float noiseScale = 1.0f;
	
	public EngineTerrainNoise(float heightSeed) {
		this.heightSeed = heightSeed;
		Engine.logInfo("EngineTerrainNoise: heightSeed = " + heightSeed);
	}
	
	@Override
	public void init() {

	}

	@Override
	public float value(float x, float y, float z) {
		float ret = (float) (pNoise.noise3D(this.noiseScale * x, this.noiseScale * y, this.noiseScale * z) * heightSeed);
		//Engine.logInfo("EngineTerrainNoise: value at (" + x + "," + y + "," + x + ") = " + ret);
		return ret;
	}

	@Override
	public FloatBuffer getBuffer(float sx, float sy, float base, int size) {
		FloatBuffer retval = FloatBuffer.allocate(size * size);
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				retval.put(this.modulate((sx + x) / size, (sy + y) / size, base));
			}
		}
		return retval;
	}
	
	@Override
	public Basis setScale(float scale) {
		this.noiseScale = scale;
		return this;
	}

	@Override
	public float getScale() {
		return this.noiseScale;
	}

}
