package engine;

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
import com.jme3.util.BufferUtils;

import shapes.Cylinder;
import shapes.Quad;
import shapes.RectangularPrism;
import shapes.RenderColor;
import shapes.RenderMaterial;
import shapes.Shape;
import shapes.Sphere;

public class EngineGeometry {
	private Shape shape;
	private Vector3d pos;
	private RenderMaterial mat;
	private Material geomMat;
	private Mesh mesh;
	private Geometry geom;
	
	public EngineGeometry(Shape shape) {
		this.shape = shape;
		this.initData();
	}
	
	public void initData() {
		double[] tmpPos = this.shape.getCenter();
		this.pos = new Vector3d(tmpPos[0],tmpPos[1],tmpPos[2]);
		this.mat = null;
		this.geomMat = null;
		this.mesh = null;
		this.geom = null;
		if(this.shape instanceof shapes.Cylinder) {
    		setupCylinder((shapes.Cylinder)this.shape);
    	} else if(this.shape instanceof shapes.Sphere) {
    		setupSphere((shapes.Sphere)this.shape);
    	} else if(this.shape instanceof shapes.RectangularPrism) {
    		setupRectPrism((shapes.RectangularPrism)this.shape);
    	} else if(this.shape instanceof shapes.Quad) {
    		setupQuad((shapes.Quad)this.shape);
    	} else {
    		throw new IllegalArgumentException();
    	}
	}
	
	public void setupCylinder(Cylinder shape) {
		int axisSamples = (int)shape.getRadius() * 15;
    	int radialSamples = (int)shape.getRadius() * 15;
    	this.mesh = new com.jme3.scene.shape.Cylinder(axisSamples,radialSamples,shape.getRadius(),shape.getHeight(),true);
    	this.geom = new Geometry("Cylinder"+shape.hashCode(),this.mesh);
    	CylinderCollisionShape scs = new CylinderCollisionShape(new Vector3f(shape.getRadius(),shape.getHeight()/2,shape.getRadius()), 2);
    	RigidBodyControl rbc = new RigidBodyControl(scs);
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
        	rbc.setKinematicSpatial(false);
    	}
    	Quaternion rot = (new Quaternion()).fromAngles(shape.getXRot(), shape.getYRot(), shape.getZRot());
    	this.geom.addControl(rbc);
    	rbc.setPhysicsRotation(rot);
    	this.mat = shape.getMaterial();
	}
	
	public void setupSphere(Sphere shape) {
		float rad = shape.getRadius();
		com.jme3.scene.shape.Sphere tmpMesh = new com.jme3.scene.shape.Sphere((int)rad*15,(int)rad*15,rad);
		tmpMesh.setTextureMode(com.jme3.scene.shape.Sphere.TextureMode.Projected);
		this.mesh = tmpMesh;
		this.geom = new Geometry("Sphere"+shape.hashCode(),mesh);
    	this.geom.rotate(shape.getXRot(), shape.getYRot(), shape.getZRot());
    	SphereCollisionShape scs = new SphereCollisionShape(shape.getRadius());
    	RigidBodyControl rbc = new RigidBodyControl(scs);
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
        	rbc.setKinematicSpatial(false);
    	}
    	this.geom.addControl(rbc);
    	this.mat = shape.getMaterial();
	}
	
	public void setupRectPrism(RectangularPrism shape) {
		float[] dim = shape.getDimensions();
    	this.mesh = new Box(dim[0],dim[1],dim[2]);
    	this.geom = new Geometry("RectPrism"+shape.hashCode(),this.mesh);
    	BoxCollisionShape bcs = new BoxCollisionShape(new Vector3f(dim[0]/2,dim[1]/2,dim[2]/2));
    	RigidBodyControl rbc = new RigidBodyControl(bcs);
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
        	rbc.setKinematicSpatial(false);
    	}
    	Quaternion rot = (new Quaternion()).fromAngles(shape.getXRot(), shape.getYRot(), shape.getZRot());
    	this.geom.addControl(rbc);
    	rbc.setPhysicsRotation(rot);
    	this.mat = shape.getMaterial();
	}
	
	public void setupQuad(Quad shape) {
		this.mesh = new Mesh();
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
    	this.mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
    	this.mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
    	this.mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
    	this.mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
    	this.mesh.updateBound();
    	MeshCollisionShape mcs = new MeshCollisionShape(mesh, false);
    	RigidBodyControl rbc = new RigidBodyControl();
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
        	rbc.setKinematicSpatial(false);
    	}
    	rbc.setCollisionShape(mcs);
    	this.geom = new Geometry("Quad"+shape.hashCode(),mesh);
    	this.geom.addControl(rbc);
    	this.mat = shape.getMaterial();
	}
	
	/** 
	 * Returns this geometry's position vector.
	 * @return the geometry's position vector
	 */
	public Vector3d getJME3Position() {
		return this.pos;
	}
	
	/**
	 * Returns a copy of this EngineGeometry's position vector as an array
	 * @return a copy of the position vector as an array of doubles
	 */
	public double[] getPosition() {
		return new double[] {this.pos.x,this.pos.y,this.pos.z};
	}
	
	/**
	 * Returns this EngineGeometry's material object
	 * @return the material object
	 */
	public RenderMaterial getMaterial() {
		return this.mat;
	}
	
	public Geometry getJME3Geometry(AssetManager assetManager) {
		if(this.geomMat == null) {
			this.geomMat = this.getMaterial(assetManager);
			this.geom.setMaterial(this.geomMat);
			//if(this.mat.isUsingTransparency()) {
	    		//this.geom.setQueueBucket(Bucket.Transparent);  
	    	//}
		}
		return this.geom;
	}
    
    public Material getMaterial(AssetManager assetManager) {
    	Material ret = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
    	//ret.setBoolean("UseMaterialColors",true);
    	//ret.setColor("Diffuse", Utils.getColor(this.mat.getDiffuseColor()));
    	//ret.setColor("Ambient", Utils.getColor(this.mat.getAmbientColor()));
    	//ret.setColor("Specular", Utils.getColor(this.mat.getSpecularColor()));
    	//ret.setFloat("Shininess", this.mat.getShininess());
    	//ret.setBoolean("VertexLighting",this.mat.isUsingVertexLighting());
    	//ret.setBoolean("UseVertexColor",this.mat.isUsingVertexColor());
    	//ret.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/splat/grass.jpg"));
    	//ret.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg"));
    	//if(this.mat.isUsingTransparency()) {
    		//ret.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    	//}
    	return ret;
    }
}
