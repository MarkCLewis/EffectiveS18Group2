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
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.plugins.blender.math.Vector3d;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.util.BufferUtils;

import shapes.Cylinder;
import shapes.HeightMapSurface;
import shapes.Quad;
import shapes.RectangularPrism;
import shapes.RenderColor;
import shapes.RenderMaterial;
import shapes.Shape;
import shapes.Sphere;
import shapes.VectorCylinder;

public class EngineSpatial {
	private Shape shape;
	private Vector3d pos;
	private Node node;
	
	public EngineSpatial(Shape shape) {
		this.shape = shape;
		this.initData();
	}
	
	public void initData() {
		double[] tmpPos = this.shape.getCenter();
		this.pos = new Vector3d(tmpPos[0],tmpPos[1],tmpPos[2]);
		this.node = new Node("EngineSpatial"+this.hashCode());;
		if(this.shape instanceof shapes.Cylinder) {
    		setupCylinder((shapes.Cylinder)this.shape);
    	} else if(this.shape instanceof shapes.Sphere) {
    		setupSphere((shapes.Sphere)this.shape);
    	} else if(this.shape instanceof shapes.RectangularPrism) {
    		setupRectPrism((shapes.RectangularPrism)this.shape);
    	} else if(this.shape instanceof shapes.Quad) {
    		setupQuad((shapes.Quad)this.shape);
    	} else if(this.shape instanceof shapes.HeightMapSurface) {
    		setupHeightMap((shapes.HeightMapSurface)this.shape);
    	} else if(this.shape instanceof shapes.VectorCylinder) {
    		setupVectorCylinder((shapes.VectorCylinder)this.shape);
    	} else {
    		throw new IllegalArgumentException();
    	}
	}
	
	public void setupCylinder(final Cylinder shape) {
		int axisSamples = (int)shape.getRadius() * 8;
    	int radialSamples = (int)shape.getRadius() * 8;
    	Mesh mesh = new com.jme3.scene.shape.Cylinder(axisSamples,radialSamples,shape.getRadius(),shape.getHeight(),true);
    	Geometry geom = new Geometry("Cylinder"+shape.hashCode(),mesh);
    	geom.rotateUpTo(new Vector3f(0,0,1));
    	CylinderCollisionShape scs = new CylinderCollisionShape(new Vector3f(shape.getRadius(),shape.getHeight()/2,shape.getRadius()), 2);
    	RigidBodyControl rbc = new RigidBodyControl(scs);
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
    		rbc.setKinematic(false);
    	}
    	this.node.addControl(rbc);
    	this.node.attachChild(geom);
	}
	
	public void setupVectorCylinder(final VectorCylinder shape) {
		int axisSamples = (int)shape.getRadius() * 8;
    	int radialSamples = (int)shape.getRadius() * 8;
    	Mesh mesh = new com.jme3.scene.shape.Cylinder(axisSamples,radialSamples,shape.getRadius(),shape.getHeight(),true);
    	Geometry geom = new Geometry("Cylinder"+shape.hashCode(),mesh);
    	geom.rotateUpTo(new Vector3f(0,0,1));
    	CylinderCollisionShape scs = new CylinderCollisionShape(new Vector3f(shape.getRadius(),shape.getHeight()/2,shape.getRadius()), 2);
    	RigidBodyControl rbc = new RigidBodyControl(scs);
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
    		rbc.setKinematic(false);
    	}
    	this.node.addControl(rbc);
    	this.node.attachChild(geom);
	}
	
	public void setupSphere(final Sphere shape) {
		float rad = shape.getRadius();
		com.jme3.scene.shape.Sphere mesh = new com.jme3.scene.shape.Sphere((int)rad*8,(int)rad*8,rad);
		mesh.setTextureMode(com.jme3.scene.shape.Sphere.TextureMode.Projected);
		Geometry geom = new Geometry("Sphere"+shape.hashCode(),mesh);
    	SphereCollisionShape scs = new SphereCollisionShape(shape.getRadius());
    	RigidBodyControl rbc = new RigidBodyControl(scs);
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
    		rbc.setKinematic(false);
    	}
    	this.node.addControl(rbc);
    	this.node.attachChild(geom);
	}
	
	public void setupRectPrism(final RectangularPrism shape) {
		float[] dim = shape.getDimensions();
    	Mesh mesh = new Box(dim[0],dim[1],dim[2]);
    	Geometry geom = new Geometry("RectPrism"+shape.hashCode(),mesh);
    	BoxCollisionShape bcs = new BoxCollisionShape(new Vector3f(dim[0]/2,dim[1]/2,dim[2]/2));
    	RigidBodyControl rbc = new RigidBodyControl(bcs);
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
    		rbc.setKinematic(false);
    	}
    	this.node.addControl(rbc);
    	this.node.attachChild(geom);
	}
	
	public void setupHeightMap(final HeightMapSurface shape) {
		float[] heightMapTransformed = new float[shape.getSideLength() * shape.getSideLength()];
		for(int r=0; r < shape.getSideLength(); r++) {
			for(int c=0; c < shape.getSideLength(); c++) {
				int idx = (c + (r * shape.getSideLength()));
				int rT = (shape.getSideLength() - 1) - r;
				int idxT = (c + (rT * shape.getSideLength()));
				heightMapTransformed[idxT] = shape.getHeightAt(idx);
			}
		}
		TerrainQuad q = new TerrainQuad(("TerrainQuad" + shape.hashCode()), shape.getPatchSize(), shape.getSideLength(), heightMapTransformed);
		q.setLocalScale(shape.getScaleX(), shape.getScaleY(), shape.getScaleZ());
		RigidBodyControl rbc = new RigidBodyControl(0);
    	this.node.addControl(rbc);
    	this.node.attachChild(q);
	}
	
	public void setupQuad(final Quad shape) {
		Mesh mesh = new Mesh();
		Geometry geom = new Geometry("Quad"+shape.hashCode(),mesh);
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
    	if(shape.isImmobile()) {
    		rbc.setMass(0);
    		rbc.setKinematic(false);
    	}
    	rbc.setCollisionShape(mcs);
    	this.node.addControl(rbc);
    	this.node.attachChild(geom);
	}
	
	/**
	 * Called during "getJME3Spatial" method if the spatial should
	 * have its local axes rendered (for debugging purposes).
	 * @param armLength
	 * @param assetManager
	 * @return
	 */
	public Spatial getXYZAxes(AssetManager assetManager) {
		String hash = String.valueOf(this.shape.hashCode());
		float scale = 100f;
		Engine.logInfo("XYZAxes scale: " + scale);
		Node nodeAxes = new Node(this.getAxesNodeName());
		Material negAxisMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		negAxisMat.setColor("Color", ColorRGBA.Black);
		Material xAxisMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		xAxisMat.setColor("Color", ColorRGBA.Red);
		Material yAxisMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		yAxisMat.setColor("Color", ColorRGBA.Yellow);
		Material zAxisMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		zAxisMat.setColor("Color", ColorRGBA.Blue);
		Geometry xAxisGeom = new Geometry("XAxis"+hash,new Arrow(new Vector3f(scale,0,0)));
		xAxisGeom.setMaterial(xAxisMat);
		Geometry yAxisGeom = new Geometry("YAxis"+hash,new Arrow(new Vector3f(0,scale,0)));
		yAxisGeom.setMaterial(yAxisMat);
		Geometry zAxisGeom = new Geometry("ZAxis"+hash,new Arrow(new Vector3f(0,0,scale)));
		zAxisGeom.setMaterial(zAxisMat);
		Geometry negXAxisGeom = new Geometry("negXAxis"+hash,new Arrow(new Vector3f(-scale,0,0)));
		negXAxisGeom.setMaterial(negAxisMat);
		Geometry negYAxisGeom = new Geometry("negYAxis"+hash,new Arrow(new Vector3f(0,-scale,0)));
		negYAxisGeom.setMaterial(negAxisMat);
		Geometry negZAxisGeom = new Geometry("negZAxis"+hash,new Arrow(new Vector3f(0,0,-scale)));
		negZAxisGeom.setMaterial(negAxisMat);
		nodeAxes.attachChild(xAxisGeom);
		nodeAxes.attachChild(yAxisGeom);
		nodeAxes.attachChild(zAxisGeom);
		nodeAxes.attachChild(negXAxisGeom);
		nodeAxes.attachChild(negYAxisGeom);
		nodeAxes.attachChild(negZAxisGeom);
		nodeAxes.rotate(this.shape.getXRot(), this.shape.getYRot(), this.shape.getZRot());
		return nodeAxes;
	}
	
	public String getNodeName() {
		return this.node.getName();
	}
	
	public String getAxesNodeName() {
		return (getAxesNodeNamePrefix() + String.valueOf(this.shape.hashCode()));
	}
	
	public static String getAxesNodeNamePrefix() {
		return "XYZAxes";
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
	 * Returns this EngineSpatial's material object
	 * @return the material object
	 */
	public RenderMaterial getRenderMaterial() {
		return this.shape.getMaterial();
	}
	
	private Material geomMat = null;
	public Node getJME3Node(AssetManager assetManager, boolean attachAxes) {
		if(this.geomMat == null) {
			this.geomMat = this.getMaterial(assetManager);
			this.node.getChildren().forEach(c -> {
				c.setMaterial(this.geomMat);
				if(this.shape.getMaterial().isUsingTransparency()) {
					c.setQueueBucket(Bucket.Transparent);
				}});
		}
		if(attachAxes) {
			Spatial axes = getXYZAxes(assetManager);
			node.attachChild(axes);
		}
		this.node.rotateUpTo((new Vector3f(this.shape.getXRot(), this.shape.getYRot(), this.shape.getZRot()).normalizeLocal()));
		return this.node;
	}
    
    public Material getMaterial(AssetManager assetManager) {
    	Material ret = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
    	RenderMaterial mat = this.shape.getMaterial();
    	ret.setBoolean("UseMaterialColors",true);
    	ret.setColor("Diffuse", Utils.getColor(mat.getDiffuseColor()));
    	ret.setColor("Ambient", Utils.getColor(mat.getAmbientColor()));
    	ret.setColor("Specular", Utils.getColor(mat.getSpecularColor()));
    	ret.setFloat("Shininess", mat.getShininess());
    	ret.setBoolean("VertexLighting",mat.isUsingVertexLighting());
    	if(mat.isUsingTexture()) {
    		ret.setTexture("DiffuseMap", assetManager.loadTexture(mat.getTextureDiffusePath()));
    		ret.setTexture("NormalMap", assetManager.loadTexture(mat.getTextureNormalPath()));
    	}
    	if(mat.isUsingTransparency()) {
    		ret.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    	}
    	return ret;
    }
}
