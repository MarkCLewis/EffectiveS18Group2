package engine;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.debug.DebugTools;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.blender.math.Vector3d;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Torus;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.sun.javafx.geom.Vec3f;

import shapes.Shape;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;

/**
 * The game's main engine logic
 * Contains rendering and window management classes
 * Uses the jMonkeyEngine3 library
 * @author Kayla Hood
 *
 */
public class Engine extends SimpleApplication implements AnalogListener {
	/**
	 * EngineLoader guarantees that the
	 * Engine is only instantiated once
	 * @author Kayla Hood
	 *
	 */
	private static class EngineLoader {
		private static final Engine INSTANCE;
		static {
			try {
				INSTANCE = new Engine();
			} catch (Exception e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}
	
	/**
	 * This constructor throws an exception if Engine has already been instantiated
	 */
	private Engine() {
		if(EngineLoader.INSTANCE != null) {
			throw new IllegalStateException("Already instantiated");
		}
		else {
			// do nothing here for now
		}
	}
	
	/**
	 * Call this function to retrieve the application's Engine instance
	 * @return singleton instance of Engine
	 */
	public static Engine getInstance() {
		return EngineLoader.INSTANCE;
	}
	
	/**
	 * This buffer holds a collection of meshes (boxes, spheres, etc.) that
	 * are to be rendered in the NEXT frame.
	 * The positions of these meshes in the world are provided by the 
	 * "meshPositions" collection; a mesh at index i will have its
	 * position data at index i inside of "meshPositions"
	 */
	private static ArrayList<Mesh> meshBuffer = new ArrayList<Mesh>();
	/**
	 * This buffer holds the positions of meshes in world coordinates that 
	 * are to be rendered in the NEXT frame.
	 * Elements in this collection correspond to the elements in "meshBuffer"
	 * with the same index.
	 */
	private static ArrayList<Vector3d> meshPositions = new ArrayList<Vector3d>();
	
	/**
	 * The position of the camera/player in world coordinates
	 */
	private static Vector3d worldPosition = new Vector3d();
	
	private DirectionalLight sun;
	private Node skyNode;
	private Node terrainNode;
	private Node objectNode;
	private DebugTools debugTools;
	
	private TerrainQuad terrainQuad;
	
	private boolean shouldUpdateShapes = false;
	
	//private static final Terrain terrain = new Terrain(new Point(0.0,0.0), 100, 10, ))
	private static final Logger logger = Logger.getLogger(Engine.class.getName());
	private static final Random random = new Random(System.currentTimeMillis());
	
	public static final float fogDistance = 100;
	
	private final Vector3f walkDirection = new Vector3f();
    
	@Override
    public void initialize() {
    	this.setShowSettings(false);
    	super.initialize();
    }
    
    /**
     * Called when the Engine first starts up (after start() is called).
     * Sets up the members of the class, including the camera, debugging tools, 
     * player node, keyboard events, display, fog, physics state, and adds the 
     * initial meshes to the root node at their given positions.
     */
    @Override
    public void simpleInitApp() {
    	logger.info("simpleInitApp");
    	cam.setFrustumFar(10000);
    	cam.setLocation(Vector3f.ZERO);
    	debugTools = new DebugTools(assetManager);
    	rootNode.attachChild(debugTools.debugNode);
        setupKeys();
        setupDisplay();
        setupFog();

        skyNode = new Node("Sky");
        Material skyMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        skyMaterial.setColor("Color", ColorRGBA.Blue);
        Mesh skyMesh = new Dome(50,50,fogDistance);
        Geometry skyGeom = new Geometry("Sky", skyMesh);
        skyGeom.setMaterial(skyMaterial);
        Vector3f skyPos = new Vector3f();
        skyGeom.worldToLocal(new Vector3f(0,-1,0), skyPos);
        skyGeom.setLocalTranslation(skyPos);
        skyNode.attachChild(skyGeom);
        
        sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        skyNode.addLight(sun);
        
        skyNode.updateGeometricState();
        rootNode.attachChild(skyNode);
        
        meshBuffer = new ArrayList<Mesh>();
		meshPositions = new ArrayList<Vector3d>();
		
		objectNode = new Node("ObjectNode");
		terrainNode = new Node("TerrainNode");
		
		double length = 100;
		int seed = 10;
	    int points = 9;
		double[][] heightMap = {{0.25, 0.0 , 1.0, 0.0, 0.35},
								{0.0, 0.0 , 0.0, 0.0, 0.0},
								{0.0, 0.0 , 1.0, 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0, 0.0},
								{0.5, 0.0 , 1.0, 0.0, 0.25}};
		Terrain terrain = new Terrain(new Point(0.0,0.0), length, seed, points, heightMap);
		double[][] render = terrain.renderHeights();
		float[] flatRender = new float[render.length * render[0].length];
		for (int i = 0; i < render.length; i++) {
			for (int j = 0; j < render[0].length; j++) {
				flatRender[(i * render[0].length) + j] = (float) render[i][j];
			}
		}
		Material terrainMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        terrainMaterial.setColor("Color", ColorRGBA.Brown);
		terrainQuad = new TerrainQuad("Terrain",points,(int)length,flatRender);
		TerrainLodControl control = new TerrainLodControl(terrainQuad, getCamera());
		control.setLodCalculator( new DistanceLodCalculator(points, 2.7f) ); // patch size, and a multiplier
		terrainQuad.addControl(control);
        terrainQuad.setMaterial(terrainMaterial);
        terrainQuad.setLocalTranslation(0, -100, 0);
        terrainQuad.setLocalScale(2f, 0.5f, 2f);
        terrainNode.attachChild(terrainQuad);
        
		for (int i = 0; i < meshPositions.size(); i++) {
        	Engine.logInfo("adding mesh from index " + i + " in meshBuffer");
            Vector3f localPos = (meshPositions.get(i).subtract(Engine.getWorldPosition())).toVector3f();
            Engine.logInfo("mesh origin is " + localPos.toString());
            Geometry geom = new Geometry("Object" + i, meshBuffer.get(i));
            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setColor("Diffuse", ColorRGBA.White);
            mat.setColor("Specular", ColorRGBA.White);
            mat.setFloat("Shininess", 64);
            geom.setLocalTranslation(localPos);
            geom.setMaterial(mat);
            objectNode.attachChild(geom);
        }
		
		rootNode.attachChild(objectNode);
		rootNode.attachChild(terrainNode);
    }
    
    /**
     * Adds key mappings to keyboard buttons.
     * TODO: Controller support?
     */
    private void setupKeys() {
        inputManager.addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("StrafeUp", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("StrafeDown", new KeyTrigger(KeyInput.KEY_Z), new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Return", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Esc", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(this, "StrafeLeft", "StrafeRight", "Forward", "Back", "StrafeUp", "StrafeDown", "Space", "Reset", "Esc", "Up", "Down", "Left", "Right");
    }

    /**
     * Sets up the window (also called display) that will have 
     * the game rendered into it
     */
    private void setupDisplay() {
        if (fpsText == null) {
            fpsText = new BitmapText(guiFont, false);
        }
        fpsText.setLocalScale(0.7f, 0.7f, 0.7f);
        fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        fpsText.setText("");
        fpsText.setCullHint(Spatial.CullHint.Never);
        guiNode.attachChild(fpsText);
    }
    
    /**
     * Sets up the fog in the game which helps add a sense of depth and
     * obscures all the ugly pop-in artifacts in the distance
     */
    private void setupFog() {
        // use fog to give more sense of depth
        FilterPostProcessor fpp;
        FogFilter fog;
        fpp=new FilterPostProcessor(assetManager);
        fog=new FogFilter();
        fog.setFogColor(new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f));
        fog.setFogDistance(fogDistance);
        fog.setFogDensity(2.0f);
        fpp.addFilter(fog);
        viewPort.addProcessor(fpp);
    }
    
    /**
     * Updates the player/camera position and changes display text.
     * Note: this isn't quite working yet. We're using the default behavior
     * of "SimpleApplication" in jME3 right now.
     */
    @Override
    public void simpleUpdate(float tpf) {
    	if(shouldUpdateShapes) {
    		objectNode.detachAllChildren();
        	for (int i = 0; i < meshPositions.size(); i++) {
            	Engine.logInfo("adding mesh from index " + i + " in meshBuffer");
                Vector3f localPos = (meshPositions.get(i).subtract(Engine.getWorldPosition())).toVector3f();
                Engine.logInfo("mesh origin is " + localPos.toString());
                Geometry geom = new Geometry("Object" + i, meshBuffer.get(i));
                Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
                mat.setColor("Diffuse", ColorRGBA.White);
                mat.setColor("Specular", ColorRGBA.White);
                mat.setFloat("Shininess", 64);
                geom.setLocalTranslation(localPos);
                geom.setMaterial(mat);
                objectNode.attachChild(geom);
            }
        	shouldUpdateShapes = false;
    	}
        move(walkDirection);
        fpsText.setText("Location: " + getCoordinates());
        walkDirection.set(Vector3f.ZERO);
    }
    
    /**
     * Moves the player/camera by adding the given vector to the 
     * "worldPosition" vector. Note: this just sets the "worldPosition"
     * vector, the actual effect of "moving" is produced by the renderer
     * when it takes into account the value of "worldPosition"
     * @param dir Direction of player/camera movement (should be a normalized vector)
     */
    public void move(Vector3f dir) {
    	Vector3f loc = (rootNode.localToWorld(dir, null));
        Engine.worldPosition.addLocal(loc.x,loc.y,loc.z);
        terrainNode.setLocalTranslation(terrainNode.worldToLocal(worldPosition.toVector3f(),null));
        skyNode.setLocalTranslation(terrainNode.worldToLocal(worldPosition.toVector3f(),null));
    }
    
    public static String getCoordinates() {
        return Engine.worldPosition.toString();
    }
    
    public static Vector3d getWorldPosition() {
    	return Engine.worldPosition.clone();
    }

    public void changeShapes(ArrayList<shapes.Shape> shapes) {
    	meshBuffer.clear();
    	meshPositions.clear();
    	for (shapes.Shape shape : shapes) {
    		Engine.logInfo("addShape");
        	double[] pos = shape.getCenter();
        	Vector3d vector3d = new Vector3d(pos[0],pos[1],pos[2]);
            Mesh m = Utils.getMeshFromShape(shape);
            meshBuffer.add(m);
            meshPositions.add(vector3d);
    	}
    	shouldUpdateShapes = true;
    }
    
    /**
     * Returns a random double between min (inclusive) and max (exclusive)
     * @param min lower bound (inclusive)
     * @param max upper bound (exclusive)
     * @return a random double in the range [min,max)
     */
    public static double getRandomDouble(double min, double max) {
    	return (random.nextDouble() * (max-min) + min);
    }
    
    /**
     * Returns a random float between min (inclusive) and max (exclusive)
     * @param min lower bound (inclusive)
     * @param max upper bound (exclusive)
     * @return a random float in the range [min,max)
     */
    public static float getRandomFloat(float min, float max) {
    	return (random.nextFloat() * (max-min) + min);
    }
    
    /**
     * Returns a random int between min (inclusive) and max (exclusive).
     * @param min lower bound (inclusive)
     * @param max upper bound (exclusive)
     * @return a random int in the range [min,max)
     */
    public static int getRandomInt(int min, int max) {
    	return (random.nextInt((max-min)));
    }
    
    /**
     * Controller input handler -- needs to be tested
     */
    public void onAnalog(String name, float value, float tpf) {
        Vector3f left = rootNode.getLocalRotation().mult(Vector3f.UNIT_X.negate());
        Vector3f forward = rootNode.getLocalRotation().mult(Vector3f.UNIT_Z.negate());
        Vector3f up = rootNode.getLocalRotation().mult(Vector3f.UNIT_Y);
        if (name.equals("StrafeLeft") && value > 0) {
            walkDirection.addLocal(left.mult(tpf));
        } else if (name.equals("StrafeRight") && value > 0) {
            walkDirection.addLocal(left.negate().multLocal(tpf));
        } else if (name.equals("Forward") && value > 0) {
            walkDirection.addLocal(forward.mult(tpf));
        } else if (name.equals("Back") && value > 0) {
            walkDirection.addLocal(forward.negate().multLocal(tpf));
        } else if (name.equals("StrafeUp") && value > 0) {
            walkDirection.addLocal(up.mult(tpf));
        } else if (name.equals("StrafeDown") && value > 0) {
            walkDirection.addLocal(up.negate().multLocal(tpf));
        } else if (name.equals("Up") && value > 0) {
            // TODO : rotate rootNode
        } else if (name.equals("Down") && value > 0) {
        	// TODO : rotate rootNode
        } else if (name.equals("Left") && value > 0) {
        	// TODO : rotate rootNode
        } else if (name.equals("Right") && value > 0) {
        	// TODO : rotate rootNode
        } else if (name.equals("Esc")) {
            stop();
        }
    }
    
    public static void logInfo(String msg) {
    	logger.info(msg);
    }
    
    public static void log(Level level, String msg) {
    	logger.log(level, msg);
    }
    
    
}
