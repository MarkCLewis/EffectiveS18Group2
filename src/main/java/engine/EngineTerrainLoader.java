package engine;

import java.io.IOException;
import java.nio.FloatBuffer;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainGridTileLoader;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.grid.FractalTileLoader;
import com.jme3.terrain.geomipmap.grid.FractalTileLoader.FloatBufferHeightMap;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.terrain.noise.Basis;
import com.jme3.terrain.noise.basis.ImprovedNoise;
import com.jme3.terrain.noise.basis.Noise;
import com.jme3.terrain.noise.modulator.Modulator;
import com.jme3.terrain.noise.modulator.NoiseModulator;

import virtualworld.terrain.Perlin;

/**
 * Analogous implementation of Ian's Terrain.
 * Plays nicely with jMonkeyEngine.
 * I might have missed some minor details of Ian's implementation.
 * @author kayla
 *
 */
public final class EngineTerrainLoader implements TerrainGridTileLoader {

	public class FloatBufferHeightMap extends AbstractHeightMap {

        private final FloatBuffer buffer;

        public FloatBufferHeightMap(FloatBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public boolean load() {
            this.heightData = this.buffer.array();
            return true;
        }

    }
	
	private EngineTerrainNoise noise;
	/**
	 * Length of one side of square quad in world units.
	 * Set by the TerrainGrid that uses an instance of this class.
	 */
	private int quadSize; // "length" in Ian's Terrain
	/**
	 * Length of one geometry tile on terrain.
	 * Set by the TerrainGrid that uses an instance of this class.
	 */
	private int patchSize; // "pointsPerSide" in Ian's Terrain
	private float heightScale;
	private float heightSeed;

	public EngineTerrainLoader(float heightScale, float heightSeed) {
		this.heightSeed = heightSeed;
		this.heightScale = heightScale;
		noise = new EngineTerrainNoise(heightSeed);
	}
	
	private HeightMap getHeightMapAt(Vector3f location) {
        AbstractHeightMap heightmap = null;
        
        FloatBuffer buffer = this.noise.getBuffer(location.x * (this.quadSize - 1), location.z * (this.quadSize - 1), 7, this.quadSize);

        float[] arr = buffer.array();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] * this.heightScale;
        }
        heightmap = new FloatBufferHeightMap(buffer);
        heightmap.load();
        return heightmap;
    }
	
	@Override
	public TerrainQuad getTerrainQuadAt(Vector3f location) {
		HeightMap heightMapAt = getHeightMapAt(location);
        TerrainQuad q = new TerrainQuad("Quad" + location, patchSize, quadSize, ((heightMapAt == null) ? null : heightMapAt.getHeightMap()));
        return q;
	}
	
	@Override
	public void setPatchSize(int patchSize) {
        this.patchSize = patchSize;
    }

	@Override
    public void setQuadSize(int quadSize) {
        this.quadSize = quadSize;
    }

	@Override
	public void write(JmeExporter ex) throws IOException {
		// left empty on purpose
		
	}

	@Override
	public void read(JmeImporter im) throws IOException {
		// left empty on purpose
		
	}
	
	
}
