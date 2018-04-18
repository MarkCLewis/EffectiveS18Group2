
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

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SimplexCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.BufferUtils;

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
    
    public static Geometry getGeomFromShape(shapes.Shape shape) {
    	if(shape instanceof shapes.Cylinder) {
    		return getGeomFromCylinder((shapes.Cylinder)shape);
    	} else if(shape instanceof shapes.Sphere) {
    		return getGeomFromSphere((shapes.Sphere)shape);
    	} else if(shape instanceof shapes.RectangularPrism) {
    		return getGeomFromRectPrism((shapes.RectangularPrism)shape);
    	} else if(shape instanceof shapes.Quad) {
    		return getGeomFromQuad((shapes.Quad)shape);
    	} else {
    		throw new IllegalArgumentException();
    	}
    }
    
    public static Geometry getGeomFromQuad(shapes.Quad shape) {
    	Mesh mesh = new Mesh();
    	float[] cornerHeights = shape.getCornerHeights();
    	float halfSize = shape.getSize()/2f;
    	Vector3f[] vertices = new Vector3f[4];
    	/**
    	 * Vertex Layout:
    	 *   2 - - - 3
    	 *   |       |
    	 *   |       |
    	 *   |       |
    	 *   0 - - - 1
    	 */
    	vertices[0] = new Vector3f(-halfSize,cornerHeights[3],halfSize); // bottom left
    	vertices[1] = new Vector3f(halfSize,cornerHeights[2],halfSize); // bottom right
    	vertices[2] = new Vector3f(-halfSize,cornerHeights[0],-halfSize); // top left
    	vertices[3] = new Vector3f(halfSize,cornerHeights[1],-halfSize); // top right
    	Vector2f[] texCoord = new Vector2f[4];
    	texCoord[0] = new Vector2f(0,0);
    	texCoord[1] = new Vector2f(1,0);
    	texCoord[2] = new Vector2f(0,1);
    	texCoord[3] = new Vector2f(1,1);
    	int[] indexes = { 2,0,1, 1,3,2 };
    	float[] normals = new float[12];
    	Vector3f[] normalVecs = new Vector3f[4];
    	normalVecs[0] = (vertices[1].subtract(vertices[0])).cross((vertices[3].subtract(vertices[0])));
    	normalVecs[1] = (vertices[2].subtract(vertices[1])).cross((vertices[0].subtract(vertices[1])));
    	normalVecs[2] = (vertices[1].subtract(vertices[2])).cross((vertices[3].subtract(vertices[2])));
    	normalVecs[3] = (vertices[2].subtract(vertices[3])).cross((vertices[0].subtract(vertices[3])));
    	normals = new float[]{
    			normalVecs[0].x,normalVecs[0].y,normalVecs[0].z, 
    			normalVecs[1].x,normalVecs[1].y,normalVecs[1].z,
    			normalVecs[2].x,normalVecs[2].y,normalVecs[2].z,
    			normalVecs[3].x,normalVecs[3].y,normalVecs[3].z};
    	mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
    	mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
    	mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
    	mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
    	mesh.updateBound();
    	MeshCollisionShape mcs = new MeshCollisionShape(mesh, false);
    	RigidBodyControl rbc = new RigidBodyControl();
    	rbc.setMass(0);
    	rbc.setKinematicSpatial(false);
    	rbc.setCollisionShape(mcs);
    	Geometry geom = new Geometry("Quad"+shape.hashCode(),mesh);
    	geom.addControl(rbc);
    	return geom;
    }
    
    public static Geometry getGeomFromSphere(shapes.Sphere shape) {
    	Mesh mesh = new Sphere(10,10,shape.getRadius());
    	Geometry geom = new Geometry("Sphere"+shape.hashCode(),mesh);
    	geom.rotate(shape.getXRot(), shape.getYRot(), shape.getZRot());
    	SphereCollisionShape scs = new SphereCollisionShape(shape.getRadius());
    	RigidBodyControl rbc = new RigidBodyControl(scs);
    	geom.addControl(rbc);
    	return geom;
    }
    
    public static Geometry getGeomFromCylinder(shapes.Cylinder shape) {
    	int axisSamples = 20;
    	int radialSamples = 20;
    	Mesh mesh = new Cylinder(axisSamples,radialSamples,shape.getRadius(),shape.getHeight(),true);
    	Geometry geom = new Geometry("Cylinder"+shape.hashCode(),mesh);
    	CylinderCollisionShape scs = new CylinderCollisionShape(new Vector3f(shape.getRadius(),shape.getHeight()/2,shape.getRadius()), 2);
    	RigidBodyControl rbc = new RigidBodyControl(scs);
    	Quaternion rot = (new Quaternion()).fromAngles(shape.getXRot(), shape.getYRot(), shape.getZRot());
    	geom.addControl(rbc);
    	rbc.setPhysicsRotation(rot);
    	return geom;
    }
    
    public static Geometry getGeomFromRectPrism(shapes.RectangularPrism shape) {
    	float[] dim = shape.getDimensions();
    	Mesh mesh = new Box(dim[0],dim[1],dim[2]);
    	Geometry geom = new Geometry("RectPrism"+shape.hashCode(),mesh);
    	BoxCollisionShape bcs = new BoxCollisionShape(new Vector3f(dim[0]/2,dim[1]/2,dim[2]/2));
    	RigidBodyControl rbc = new RigidBodyControl(bcs);
    	Quaternion rot = (new Quaternion()).fromAngles(shape.getXRot(), shape.getYRot(), shape.getZRot());
    	geom.addControl(rbc);
    	rbc.setPhysicsRotation(rot);
    	return geom;
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
