
package engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.plugins.blender.math.Vector3d;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.BufferUtils;

import shapes.RenderColor;
import shapes.RenderMaterial;

public class Utils {
	
	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}
	
	/**
	 * Reads the specified resource and returns the raw data as a ByteBuffer.
	 *
	 * @param resource   the resource to read
	 * @param bufferSize the initial buffer size
	 *
	 * @return the resource data
	 *
	 * @throws IOException if an IO error occurs
	 */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        File file = new File(url.getFile());
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fc.close();
            fis.close();
        } else {
            buffer = BufferUtils.createByteBuffer(bufferSize);
            InputStream source = url.openStream();
            if (source == null) {
                throw new FileNotFoundException(resource);
            }
            try {
                ReadableByteChannel rbc = Channels.newChannel(source);
                try {
                    while (true) {
                        int bytes = rbc.read(buffer);
                        if (bytes == -1)
                            break;
                        if (buffer.remaining() == 0)
                            buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                    buffer.flip();
                } finally {
                    rbc.close();
                }
            } finally {
                source.close();
            }
        }
        return buffer;
    }
    
    public static String readFile(String path, Charset encoding) throws IOException {
    	String root = System.getProperty("user.dir");
    	byte[] encoded = Files.readAllBytes(Paths.get(root, path));
    	return new String(encoded, encoding);
    }
    
    public static ColorRGBA getColor(RenderColor color) {
    	return new ColorRGBA(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
    }
    
    /**
     * Maps a value from 0-1 to a range from min to max.
     *
     * @param x
     * @param min
     * @param max
     * @return
     */
    public static float mapValue(float x, float min, float max) {
        return mapValue(x, 0, 1, min, max);
    }

    /**
     * Maps a value from inputMin to inputMax to a range from min to max.
     *
     * @param x
     * @param inputMin
     * @param inputMax
     * @param min
     * @param max
     * @return
     */
    public static float mapValue(float x, float inputMin, float inputMax, float min, float max) {
        return (x - inputMin) * (max - min) / (inputMax - inputMin) + min;
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
	
	public static Vector3f getVectorFromArray(float[] arr) {
		if(arr.length > 3) {
			throw new IllegalArgumentException("Array must contain exactly 3 floats.");
		}
		return (new Vector3f(arr[0],arr[1],arr[2]));
	}
	
	public static Vector3d getVectorFromArray(double[] arr) {
		if(arr.length > 3) {
			throw new IllegalArgumentException("Array must contain exactly 3 doubles.");
		}
		return (new Vector3d(arr[0],arr[1],arr[2]));
	}
}
