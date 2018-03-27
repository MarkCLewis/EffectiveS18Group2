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
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.blender.math.Vector3d;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Torus;
import com.sun.javafx.geom.Vec3f;

import shapes.Shape;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;

public class Engine extends SimpleApplication implements AnalogListener {
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
	
	private Engine() {
		if(EngineLoader.INSTANCE != null) {
			throw new IllegalStateException("Already instantiated");
		}
		else {
			// do nothing here for now
		}
	}
	
	public static Engine getInstance() {
		return EngineLoader.INSTANCE;
	}
	
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
		 */
		private static void fillBuffersWithRandomShapes(int count) {
			for(int i = 0; i < count; i++) {
				meshBuffer.add(new Sphere(10,10,1));
				meshPositions.add(new Vector3d(random.nextDouble()*20, random.nextDouble()*20, 0));
			}
		}
	}
	
	/**
	 * This buffer holds a collection of
	 * meshes (boxes, spheres, etc.) that
	 * are to be rendered in the NEXT frame
	 */
	private static ArrayList<Mesh> meshBuffer = new ArrayList<Mesh>();
	/**
	 * This buffer holds the positions of the above meshes
	 * in world coordinates
	 */
	private static ArrayList<Vector3d> meshPositions = new ArrayList<Vector3d>();
	private static final Logger logger = Logger.getLogger(Engine.class.getName());
	private static Vector3d worldPosition = new Vector3d();
	private static final float fogDistance = 100;
	private static final Random random = new Random(System.currentTimeMillis());
	private final Vector3f walkDirection = new Vector3f();
	private static DebugTools debugTools;
	private static BulletAppState physicsState;
	private static Node playerNode;
	
    @Override
    public void simpleInitApp() {
    	cam.setFrustumFar(10000);
    	cam.setLocation(Vector3f.ZERO);
    	debugTools = new DebugTools(assetManager);
    	rootNode.attachChild(debugTools.debugNode);
    	
        Utils.fillBuffersWithRandomShapes(5);
        
        playerNode = new Node("player");
        rootNode.attachChild(playerNode);
        setupKeys();
        setupDisplay();
        setupFog();
        
        physicsState = new BulletAppState();
        physicsState.startPhysics();
        physicsState.getPhysicsSpace().setGravity(Vector3f.ZERO);
        // int hashCode = worldPosition.hashCode(); // Use this later for randomization
        for (int i = 0; i < meshPositions.size(); i++) {
            Vector3f vector3f = Utils.worldToLocalCoords(meshPositions.get(i));
            Material defaultMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            defaultMaterial.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
            CollisionShape defaultCollisionShape = new SphereCollisionShape(1f);
            Geometry defGeom = new Geometry("def", meshBuffer.get(i));
            defGeom.setLocalTranslation(vector3f);
            defGeom.setMaterial(defaultMaterial);
            RigidBodyControl control = new RigidBodyControl(defaultCollisionShape, 0);
            //!!! Important
            control.setApplyPhysicsLocal(true);
            defGeom.addControl(control);
            physicsState.getPhysicsSpace().add(defGeom);
            playerNode.attachChild(defGeom);

        }
    }
    
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
    
    @Override
    public void simpleUpdate(float tpf) {
        move(walkDirection);
        fpsText.setText("Location: " + getCoordinates());
        walkDirection.set(Vector3f.ZERO);
    }
    
    public void move(Vector3f dir) {
        if (worldPosition == null) {
            worldPosition = new Vector3d();
        }
        worldPosition.addLocal(dir.x,dir.y,dir.z);
    }
    
    public String getCoordinates() {
        return worldPosition.toString();
    }
    
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
