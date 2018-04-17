
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

import org.lwjgl.BufferUtils;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

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
    
    public static Mesh getMeshFromShape(shapes.Shape shape) {
    	if(shape instanceof shapes.Cylinder) {
    		return getMeshFromCylinder((shapes.Cylinder)shape);
    	} else if(shape instanceof shapes.Sphere) {
    		return getMeshFromSphere((shapes.Sphere)shape);
    	} else if(shape instanceof shapes.RectangularPrism) {
    		return getMeshFromRectPrism((shapes.RectangularPrism)shape);
    	} else if(shape instanceof shapes.Quad) {
    		return getMeshFromQuad((shapes.Quad)shape);
    	} else {
    		throw new IllegalArgumentException();
    	}
    }
    
    public static Mesh getMeshFromQuad(shapes.Quad shape) {
    	Mesh result = new Mesh();
    	float[] cornerHeights = shape.getCornerHeights();
    	float halfSize = shape.getSize()/2f;
    	Vector3f [] vertices = new Vector3f[4];
    	vertices[0] = new Vector3f(halfSize,cornerHeights[0],-halfSize); // top left
    	vertices[1] = new Vector3f(halfSize,cornerHeights[1],halfSize); // top right
    	vertices[2] = new Vector3f(-halfSize,cornerHeights[2],halfSize); // bottom right
    	vertices[3] = new Vector3f(-halfSize,cornerHeights[3],-halfSize); // bottom left
    	Vector2f [] texCoord = new Vector2f[4];
    	texCoord[0] = new Vector2f(0,0);
    	texCoord[1] = new Vector2f(1,0);
    	texCoord[2] = new Vector2f(0,1);
    	texCoord[3] = new Vector2f(1,1);
    	int [] indexes = { 0,3,2, 2,1,0 };
    	result.setBuffer(Type.Position, 3, com.jme3.util.BufferUtils.createFloatBuffer(vertices));
    	result.setBuffer(Type.TexCoord, 2, com.jme3.util.BufferUtils.createFloatBuffer(texCoord));
    	result.setBuffer(Type.Index,    3, com.jme3.util.BufferUtils.createIntBuffer(indexes));
    	result.updateBound();
    	return result;
    }
    
    public static Mesh getMeshFromSphere(shapes.Sphere shape) {
    	return new Sphere(10,10,shape.getRadius());
    }
    
    public static Mesh getMeshFromCylinder(shapes.Cylinder shape) {
    	return new Cylinder(10,10,shape.getRadius(),shape.getHeight());
    }
    
    public static Mesh getMeshFromRectPrism(shapes.RectangularPrism shape) {
    	float[] dim = shape.getDimensions();
    	return new Box(dim[0],dim[1],dim[2]);
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
}
