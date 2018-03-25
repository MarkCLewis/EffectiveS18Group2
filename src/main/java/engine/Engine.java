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
			currentLevel = super.getStateManager().getState(DefaultState.class);
	        currentLevel.takeOverParent();
	        currentLevel.getRootNode().setLocalScale(Vector3f.UNIT_XYZ);
	        currentLevel.getRootNode().setLocalTranslation(Vector3f.ZERO);
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
				meshBuffer.add(new Sphere(10,10,10));
				meshPositions.add(new Vector3d(random.nextDouble()*100, random.nextDouble()*100, random.nextDouble()*100));
			}
		}
	}
	
	/**
	 * This buffer holds a collection of
	 * meshes (boxes, spheres, etc.) that
	 * are to be rendered in the NEXT frame
	 */
	private static ArrayList<Mesh> meshBuffer;
	/**
	 * This buffer holds the positions of the above meshes
	 * in world coordinates
	 */
	private static ArrayList<Vector3d> meshPositions;
	private static final Logger logger = Logger.getLogger(Engine.class.getName());
	private static Vector3d worldPosition = new Vector3d();
	private static final float scaleDist = 10;
	private static final float fogDistance = 100;
	private static final Random random = new Random(System.currentTimeMillis());
    private static CollisionShape defaultCollisionShape;
    private static Material defaultMaterial;
    private static Mesh defaultMesh;
	private final Vector3f walkDirection = new Vector3f();
	private static DebugTools debugTools;
	
    private DefaultState currentLevel;
    
	
    @Override
    public void simpleInitApp() {
    	cam.setFrustumFar(10000);
    	cam.setLocation(Vector3f.ZERO);
    	debugTools = new DebugTools(assetManager);
    	rootNode.attachChild(debugTools.debugNode);
    	defaultMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        defaultMaterial.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
        setupKeys();
        setupDisplay();
        setupFog();
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
    	currentLevel = currentLevel.getCurrentLevel();
        currentLevel.move(walkDirection);
        fpsText.setText("Location: " + currentLevel.getCoordinates());
        walkDirection.set(Vector3f.ZERO);
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
     * This state machine kinda sorta mocks a world manager
     * I'm using this implementation until the official world manager is finished
     * @author kayla
     *
     */
    public static class DefaultState extends AbstractAppState {

        private final DefaultState parent;
        private final Vector3d inParentPosition;
        private SimpleApplication application;
        private BulletAppState physicsState;
        private Node rootNode;
        private Vector3d playerPos;
        private DefaultState currentActiveChild;
        private DefaultState currentReturnLevel;
        private float curScaleAmount = 0;

        public DefaultState(DefaultState parent, Vector3d inParentPosition) {
            this.parent = parent;
            this.inParentPosition = inParentPosition;
        }

        @Override
        public void update(float tpf) {
            super.update(tpf);
            if (currentReturnLevel != this) {
                return;
            }
            debugTools.setYellowArrow(new Vector3f(0, 0, -2), playerPos.divide(fogDistance).toVector3f());
            double curLocalDist = getPlayerPosition().length();
            // If we are outside the range of one point of interest, move out to
            // the next upper level
            if (curLocalDist > fogDistance + FastMath.ZERO_TOLERANCE) { //DAFUQ normalize?
                if (parent == null) {
                    //TODO: could add new nodes coming in instead for literally endless space
                    logger.log(Level.INFO, "Hit event horizon");
                    currentReturnLevel = this;
                    return;
                }
                //give to parent
                logger.log(Level.INFO, "give to parent");;
                parent.takeOverChild(inParentPosition.add(playerPos.normalize()));
                application.getStateManager().attach(parent);
                currentReturnLevel = parent;
                return;
            }

            AppStateManager stateManager = application.getStateManager();
            for (int i = 0; i < meshBuffer.size(); i++) {
                Mesh mesh = meshBuffer.get(i);
                Vector3d meshPos = meshPositions.get(i);
                Vector3f distVect = Utils.worldToLocalCoords(meshPositions.get(i));
                float distance = distVect.length();
                if (distance <= 1) {
                    checkActiveChild(meshPos);
                    float percent = 0;
                    curScaleAmount = 0;
                    this.scaleAsParent(percent, playerPos, distVect);
                    currentActiveChild.scaleAsChild(percent, distVect);
                    logger.log(Level.INFO, "Give over to child {0}", currentActiveChild);
                    currentActiveChild.takeOverParent();
                    stateManager.detach(this);
                    currentReturnLevel = currentActiveChild;
                    return;
                } else if (distance <= 1 + scaleDist) {
                    debugTools.setRedArrow(Vector3f.ZERO, distVect);
                    checkActiveChild(meshPos);
                    //TODO: scale percent nicer for less of an "explosion" effect
                    float percent = 1 - mapValue(distance - 1, 0, scaleDist, 0, 1);
                    curScaleAmount = percent;
                    rootNode.getChild(i).setCullHint(Spatial.CullHint.Always);
                    this.scaleAsParent(percent, playerPos, distVect);
                    currentActiveChild.scaleAsChild(percent, distVect);
                    currentReturnLevel = this;
                    return;
                } else if (currentActiveChild != null && currentActiveChild.getPositionInParent().equals(meshPos)) {
                    rootNode.getChild(i).setCullHint(Spatial.CullHint.Inherit);
                }
            }
            checkActiveChild(null);
            curScaleAmount = 0;
            rootNode.setLocalScale(1);
            rootNode.setLocalTranslation(playerPos.negate().toVector3f());
            debugTools.setRedArrow(Vector3f.ZERO, Vector3f.ZERO);
            debugTools.setBlueArrow(Vector3f.ZERO, Vector3f.ZERO);
            debugTools.setGreenArrow(Vector3f.ZERO, Vector3f.ZERO);
        }

        private void checkActiveChild(Vector3d vector3d) {
            AppStateManager stateManager = application.getStateManager();
            if(vector3d == null){
                if(currentActiveChild != null){
                    logger.log(Level.INFO, "Detach child {0}", currentActiveChild);
                    stateManager.detach(currentActiveChild);
                    currentActiveChild = null;
                }
                return;
            }
            if (currentActiveChild == null) {
                currentActiveChild = new DefaultState(this, vector3d);
                stateManager.attach(currentActiveChild);
                logger.log(Level.INFO, "Attach child {0}", currentActiveChild);
            } else if (!currentActiveChild.getPositionInParent().equals(vector3d)) {
                logger.log(Level.INFO, "Switching from child {0}", currentActiveChild);
                stateManager.detach(currentActiveChild);
                currentActiveChild = new DefaultState(this, vector3d);
                stateManager.attach(currentActiveChild);
                logger.log(Level.INFO, "Attach child {0}", currentActiveChild);
            }
        }

        private void scaleAsChild(float percent, Vector3f dist) {
            float childScale = mapValue(percent, 1.0f / fogDistance, 1);
            Vector3f distToHorizon = dist.normalize();
            Vector3f scaledDistToHorizon = distToHorizon.mult(childScale * fogDistance);
            Vector3f rootOff = dist.add(scaledDistToHorizon);
            debugTools.setBlueArrow(Vector3f.ZERO, rootOff);
            getRootNode().setLocalScale(childScale);
            getRootNode().setLocalTranslation(rootOff);
            //prepare player position already
            Vector3f playerPosition = dist.normalize().mult(-fogDistance);
            setPlayerPosition(playerPosition);
        }

        private void scaleAsParent(float percent, Vector3d playerPos, Vector3f dist) {
            float scale = mapValue(percent, 1.0f, fogDistance);
            Vector3f distToHorizon = dist.subtract(dist.normalize());
            Vector3d offLocation = playerPos.add(distToHorizon.x, distToHorizon.y, distToHorizon.z);
            Vector3d rootOff = offLocation.mult(scale).negate();
            rootOff.addLocal(dist.x,dist.y,dist.z);
            debugTools.setGreenArrow(Vector3f.ZERO, offLocation.toVector3f());
            getRootNode().setLocalScale(scale);
            getRootNode().setLocalTranslation(rootOff.toVector3f());
        }

        public void takeOverParent() {
            //got playerPos from scaleAsChild before
            getPlayerPosition().normalizeLocal().multLocal(fogDistance);
            currentReturnLevel = this;
        }

        public void takeOverChild(Vector3d playerPos) {
            this.playerPos.set(playerPos);
            currentReturnLevel = this;
        }

        private void initData() {
            getRootNode();
            physicsState = new BulletAppState();
            physicsState.startPhysics();
            physicsState.getPhysicsSpace().setGravity(Vector3f.ZERO);
            //horizon
            physicsState.getPhysicsSpace().add(new RigidBodyControl(defaultCollisionShape, 0));
            for (int i = 0; i < meshBuffer.size(); i++) {
                Vector3f vector3f = Utils.worldToLocalCoords(meshPositions.get(i));
                Geometry defGeom = new Geometry("sphere" + i, meshBuffer.get(i));
                defGeom.setLocalTranslation(vector3f);
                defGeom.setMaterial(defaultMaterial);
                RigidBodyControl control = new RigidBodyControl(defaultCollisionShape, 0);
                //!!! Important
                control.setApplyPhysicsLocal(true);
                defGeom.addControl(control);
                physicsState.getPhysicsSpace().add(defGeom);
                rootNode.attachChild(defGeom);

            }

        }

        private void cleanupData() {
            physicsState.stopPhysics();
            //TODO: remove all objects?
            physicsState = null;
            rootNode = null;
        }

        @Override
        public void initialize(AppStateManager stateManager, Application app) {
            super.initialize(stateManager, app);
            //only generate data and attach node when we are actually attached (or picking)
            initData();
            application = (SimpleApplication) app;
            application.getRootNode().attachChild(getRootNode());
            application.getStateManager().attach(physicsState);
        }

        @Override
        public void cleanup() {
            super.cleanup();
            //detach everything when we are detached
            application.getRootNode().detachChild(rootNode);
            application.getStateManager().detach(physicsState);
            cleanupData();
        }

        public Node getRootNode() {
            if (rootNode == null) {
                rootNode = new Node("ZoomLevel");
                if (parent != null) {
                    rootNode.setLocalScale(1.0f / fogDistance);
                }
            }
            return rootNode;
        }

        public Vector3d getPositionInParent() {
            return inParentPosition;
        }

        public Vector3d getPlayerPosition() {
            if (playerPos == null) {
                playerPos = new Vector3d();
            }
            return playerPos;
        }

        public void setPlayerPosition(Vector3d vec) {
            if (playerPos == null) {
                playerPos = new Vector3d();
            }
            playerPos.set(vec);
        }
        
        public void setPlayerPosition(Vector3f vecf) {
            if (playerPos == null) {
                playerPos = new Vector3d();
            }
            Vector3d vec = new Vector3d(vecf.x,vecf.y,vecf.z);
            playerPos.set(vec);
        }

        public void move(Vector3f dir) {
            if (playerPos == null) {
                playerPos = new Vector3d();
            }
            playerPos.addLocal(dir.x,dir.y,dir.z);
            worldPosition.addLocal(dir.x,dir.y,dir.z);
        }

        public float getCurrentScaleAmount() {
            return curScaleAmount;
        }

        public DefaultState getParent() {
            return parent;
        }

        public DefaultState getCurrentLevel() {
            return currentReturnLevel;
        }

        public String getCoordinates() {
            DefaultState cur = this;
            StringBuilder strb = new StringBuilder();
            strb.insert(0, this.getPlayerPosition());
            strb.insert(0, this.getPositionInParent() + " / ");
            cur = cur.getParent();
            while (cur != null) {
                strb.insert(0, cur.getPositionInParent() + " / ");
                cur = cur.getParent();
            }
            return strb.toString();
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
