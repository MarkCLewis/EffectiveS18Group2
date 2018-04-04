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
import com.sun.javafx.geom.Vec3f;

import shapes.Shape;

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
				INSTANCE.setupApp();
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
	 * Provides utility functions that help the engine
	 * process data for rendering
	 * @author Kayla Hood
	 *
	 */
	private static class Utils {
		private static float[] worldToLocalCoords(double[] worldCoords) {
			return new float[] {(float) (worldCoords[0]-worldPosition.x),
			                    (float) (worldCoords[1]-worldPosition.y),
			                    (float) (worldCoords[2]-worldPosition.z)};
		}
		
		private static Vector3f worldToLocalCoords(Vector3d worldCoords) {
			return new Vector3f(worldCoords.subtract(worldPosition).toVector3f());
		}
		
		/**
		 * fills the meshBuffer and meshPositions lists with random spheres and positions
		 * Development function only
		 */
		private static void fillBuffersWithRandomShapes(int count) {
			for(int i = 0; i < count; i++) {
				meshBuffer.add(new Sphere(10,10,1));
				meshPositions.add(new Vector3d(random.nextDouble()*20, random.nextDouble()*20, 0));
			}
		}
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
	
	private static DirectionalLight sun;
	private static Node playerNode;
	private static DebugTools debugTools;
	
	private static final Logger logger = Logger.getLogger(Engine.class.getName());
	private static final Random random = new Random(System.currentTimeMillis());
	
	private static final float fogDistance = 100;
	
	private final Vector3f walkDirection = new Vector3f();
    
	/**
	 * Starts the game using the rendering logic defined in this class (Engine)
	 * @param args
	 * The command line arguments given to the application
	 */
    public static void main(String[] args) {
		Engine.getInstance().start();
	}
    
    public void setupApp() {
    	this.setShowSettings(false);
    }
    
    /**
     * Called when the Engine first starts up (after start() is called).
     * Sets up the members of the class, including the camera, debugging tools, 
     * player node, keyboard events, display, fog, physics state, and adds the 
     * initial meshes to the root node at their given positions.
     */
    @Override
    public void simpleInitApp() {
    	viewPort.setBackgroundColor(ColorRGBA.Blue);
    	
    	logger.info("simpleInitApp");
    	cam.setFrustumFar(10000);
    	cam.setLocation(Vector3f.ZERO);
    	debugTools = new DebugTools(assetManager);
    	rootNode.attachChild(debugTools.debugNode);

        playerNode = new Node("player");
        setupKeys();
        setupDisplay();
        setupFog();

        Material skyMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        skyMaterial.setColor("Color", ColorRGBA.Blue);
        Mesh skyMesh = new Dome(50,50,fogDistance/6);
        Geometry skyGeom = new Geometry("sky", skyMesh);
        skyGeom.setMaterial(skyMaterial);
        Vector3f skyPos = new Vector3f();
        skyGeom.worldToLocal(new Vector3f(0,-1,0), skyPos);
        skyGeom.setLocalTranslation(skyPos);
        playerNode.attachChild(skyGeom);
        
        Material groundMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        groundMaterial.setColor("Color", ColorRGBA.Brown);
        Mesh groundMesh = new Quad(fogDistance/3,fogDistance/3);
        Geometry groundGeom = new Geometry("ground", groundMesh);
        groundGeom.setMaterial(groundMaterial);
        groundGeom.rotateUpTo(new Vector3f(0,0,-1));
        Vector3f groundPos = new Vector3f();
        groundGeom.worldToLocal(new Vector3f(-(fogDistance/6),(fogDistance/6),1), groundPos);
        groundGeom.setLocalTranslation(groundPos);
        playerNode.attachChild(groundGeom);
        
        // int hashCode = worldPosition.hashCode(); // Use this later for randomization
        for (int i = 0; i < meshPositions.size(); i++) {
        	logger.info("adding mesh from index " + i + " in meshBuffer");
            Vector3f vector3f = Utils.worldToLocalCoords(meshPositions.get(i));
            logger.info("mesh origin is " + vector3f.toString());
            Geometry defGeom = new Geometry("def", meshBuffer.get(i));
            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setColor("Diffuse", ColorRGBA.White);
            mat.setColor("Specular", ColorRGBA.White);
            mat.setFloat("Shininess", 64);
            defGeom.setLocalTranslation(vector3f);
            defGeom.setMaterial(mat);
            playerNode.attachChild(defGeom);
        }
        
        sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        
        playerNode.addLight(sun);
        playerNode.updateGeometricState();
        rootNode.attachChild(playerNode);
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
        move(walkDirection);
        fpsText.setText("Location: " + getCoordinates());
        walkDirection.set(Vector3f.ZERO);
    }
    
    /**
     * Moves the player/camera by adding the given vector to the 
     * "worldPosition" vector. Note: this just sets the "worldPosition"
     * vector, the actual effect of "moving" is produced by the renderer
     * when it takes into account the value of "worldPosition"
     * @param dir
     * Direction of player/camera movement (should be a normalized vector)
     */
    public void move(Vector3f dir) {
        if (worldPosition == null) {
            worldPosition = new Vector3d();
        }
        worldPosition.addLocal(dir.x,dir.y,dir.z);
    }
    
    public String getCoordinates() {
        return worldPosition.toString();
    }
    
    private Mesh getMeshFromShape(shapes.Shape shape) {
    	if(shape instanceof shapes.Cylinder) {
    		logger.info("Adding Cylinder");
    		return getMeshFromCylinder((shapes.Cylinder)shape);
    	} else if(shape instanceof shapes.Sphere) {
    		logger.info("Adding Sphere");
    		return getMeshFromSphere((shapes.Sphere)shape);
    	} else if(shape instanceof shapes.RectangularPrism) {
    		logger.info("Adding RectangularPrism");
    		return getMeshFromRectPrism((shapes.RectangularPrism)shape);
    	} else throw new IllegalArgumentException();
    }
    
    private Mesh getMeshFromSphere(shapes.Sphere shape) {
    	return new Sphere(10,10,shape.getRadius());
    }
    
    private Mesh getMeshFromCylinder(shapes.Cylinder shape) {
    	return new Cylinder(10,10,shape.getRadius(),shape.getHeight());
    }
    
    private Mesh getMeshFromRectPrism(shapes.RectangularPrism shape) {
    	float[] dim = shape.getDimensions();
    	return new Box(dim[0],dim[1],dim[2]);
    }
    
    private Geometry getGeometryFromShape(shapes.Shape shape) {
    	if(shape instanceof shapes.Cylinder) {
    		logger.info("Adding Cylinder");
    		return new Geometry("cylinder",getMeshFromCylinder((shapes.Cylinder)shape));
    	} else if(shape instanceof shapes.Sphere) {
    		logger.info("Adding Sphere");
    		return new Geometry("sphere",getMeshFromSphere((shapes.Sphere)shape));
    	} else if(shape instanceof shapes.RectangularPrism) {
    		logger.info("Adding RectangularPrism");
    		return new Geometry("rectprism",getMeshFromRectPrism((shapes.RectangularPrism)shape));
    	} else throw new IllegalArgumentException();
    }

    public void addShape(shapes.Shape shape) {
    	logger.info("addShape");
    	double[] pos = shape.getCenter();
    	Vector3d vector3d = new Vector3d(pos[0],pos[1],pos[2]);
        Mesh m = getMeshFromShape(shape);
        meshBuffer.add(m);
        meshPositions.add(vector3d);
    }
    
    /**
     * Controller input handler -- needs to be tested
     */
    public void onAnalog(String name, float value, float tpf) {
        Vector3f left = rootNode.getLocalRotation().mult(Vector3f.UNIT_X.negate());
        Vector3f forward = rootNode.getLocalRotation().mult(Vector3f.UNIT_Z.negate());
        Vector3f up = rootNode.getLocalRotation().mult(Vector3f.UNIT_Y);
        //TODO: properly scale input based on current scaling level
        tpf = tpf * 10; // TODO: remove magic number
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
            //TODO: rotate rootNode, needs to be global
        } else if (name.equals("Down") && value > 0) {
        } else if (name.equals("Left") && value > 0) {
        } else if (name.equals("Right") && value > 0) {
        } else if (name.equals("Esc")) {
            stop();
        }
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
}
