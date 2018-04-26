package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.HeightfieldCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.debug.DebugTools;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.blender.math.Vector3d;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainGridListener;
import com.jme3.terrain.geomipmap.TerrainGridLodControl;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 * The game's main engine logic
 * Contains rendering and window management classes
 * Uses the jMonkeyEngine3 library
 * @author Kayla Hood
 *
 */
public class Engine extends SimpleApplication {
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
				INSTANCE.setShowSettings(false);
				AppSettings settings = new AppSettings(true);
				settings.setResolution(1000, 800);
				INSTANCE.setSettings(settings);
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
	private final ArrayList<EngineSpatial> spatialBuffer = new ArrayList<EngineSpatial>();
	
	/**
	 * The position of the camera/player in world coordinates
	 */
	private final Vector3d worldPosition = new Vector3d(0,0,0);
	
	private DirectionalLight sun;
	private Node objectNode;
	private DebugTools debugTools;

	private boolean shouldUpdateShapes = false;
	private boolean shouldResetObjectMaterials = false;
	
	//private static final Terrain terrain = new Terrain(new Point(0.0,0.0), 100, 10, ))
	private static final Logger logger = Logger.getLogger(Engine.class.getName());
	private static final Random random = new Random(System.currentTimeMillis());
	
	public static final float fogDistance = 400;

    private Material matWire;
    private boolean wireframe = false;
    protected BitmapText hintText;
    private Geometry collisionMarker;
    private BulletAppState bulletAppState;

    private boolean flyCamera;
    
    private CharacterControl player;
    
    private final Vector3f walkDirection = new Vector3f();

	
	@Override
    public void initialize() {
    	super.initialize();
    	loadHintText();
        initCrossHairs();
    }
    
    /**
     * Called when the Engine first starts up (after start() is called).
     * Sets up the members of the class, including the camera, debugging tools, 
     * player node, keyboard events, display, fog, physics state, and adds the 
     * initial meshes to the root node at their given positions.
     */
    @Override
    public void simpleInitApp() {
    	this.flyCamera = true;
    	cam.setFrustumFar(10000);
    	debugTools = new DebugTools(assetManager);
    	rootNode.attachChild(debugTools.debugNode);

		this.flyCam.setMoveSpeed(500f);
    	ScreenshotAppState state = new ScreenshotAppState();
    	this.stateManager.attach(state);

        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
    	
        this.getCamera().setLocation(new Vector3f(0, 500, 0));

        this.viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

        /**
         * add the sun (white directional light) to the root node
         */
        sun = new DirectionalLight();
        sun.setDirection((new Vector3f(1f, -0.5f, -0.1f)).normalizeLocal());
        sun.setColor(new ColorRGBA(0.50f, 0.40f, 0.50f, 1.0f));
        rootNode.addLight(sun);
        
        objectNode = new Node("ObjectNode");
		for (int i = 0; i < spatialBuffer.size(); i++) {
        	//Engine.logInfo("adding mesh from index " + i + " in meshBuffer, its position is " + geomPositions.get(i).toString());
            EngineSpatial engSpatial = spatialBuffer.get(i);
			Vector3f localPos = (engSpatial.getJME3Position().subtract(this.getWorldPosition())).toVector3f();
            //Engine.logInfo("mesh origin is " + localPos.toString());
            Spatial spatial = engSpatial.getJME3Spatial(this.assetManager);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(localPos);
            objectNode.attachChild(spatial);
        }
		rootNode.attachChild(objectNode);
		bulletAppState.getPhysicsSpace().addAll(objectNode);
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.5f, 1.8f, 1);
        player = new CharacterControl(capsuleShape, 0.5f);
        player.setJumpSpeed(50);
        player.setFallSpeed(2000);
        player.setGravity(new Vector3f(0,-80,0));
        player.setPhysicsLocation(cam.getLocation().clone());
        
        this.initKeys();
    }
    
    /**
     * Adds key mappings to keyboard buttons.
     * TODO: Controller support?
     */
    private void initKeys() {
        // You can map one or several inputs to one named action
	    inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	    inputManager.addListener(actionListener, "shoot");
    	inputManager.addListener(actionListener, "wireframe");
    	inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("cameraDown", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "cameraDown");
        
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jumps", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Fly", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addListener(actionListener, "Lefts");
        inputManager.addListener(actionListener, "Rights");
        inputManager.addListener(actionListener, "Ups");
        inputManager.addListener(actionListener, "Downs");
        inputManager.addListener(actionListener, "Jumps");
        inputManager.addListener(actionListener, "Fly");
    }
    
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean moves = true;
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(final String name, final boolean keyPressed, final float tpf) {
        	if (name.equals("wireframe") && !keyPressed) {
                wireframe = !wireframe;
                if (!wireframe) {
                	objectNode.setMaterial(matWire);
                } else {
                	shouldResetObjectMaterials = true;
                }
            } else if (name.equals("shoot") && !keyPressed) {
                Vector3f origin = cam.getWorldCoordinates(new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2), 0.0f);
                Vector3f direction = cam.getWorldCoordinates(new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2), 0.3f);
                direction.subtractLocal(origin).normalizeLocal();

                Ray ray = new Ray(origin, direction);
                CollisionResults results = new CollisionResults();
                int numCollisions = objectNode.collideWith(ray, results);
                if (numCollisions > 0) {
                    CollisionResult hit = results.getClosestCollision();
                    if (collisionMarker == null) {
                        createCollisionMarker();
                    }
                    collisionMarker.setLocalTranslation(hit.getContactPoint().x,hit.getContactPoint().y,hit.getContactPoint().z);
                }
            } else if (name.equals("cameraDown") && !keyPressed) {
            	if(moves) {
            		moves = false;
            		getCamera().setLocation(new Vector3f(0,300,0));
            		getCamera().lookAtDirection(new Vector3f(0, -1, 0), Vector3f.UNIT_Y);
            	}
            	else {
            		moves = true;
            	}
            } else if (name.equals("Lefts")) {
            	if (keyPressed) {
            		Engine.this.left = true;
            	}
            	else {
                	Engine.this.left = false;
                }
            } else if (name.equals("Rights")) {
            	if (keyPressed) {
                	Engine.this.right = true;
                } else {
                	Engine.this.right = false;
                }
            } else if (name.equals("Ups")) {
            	if (keyPressed) {
            		Engine.this.up = true;
                } else {
                    Engine.this.up = false;
                }
            } else if (name.equals("Downs")) {
            	if (keyPressed) {
            		Engine.this.down = true;
                } else {
                    Engine.this.down = false;
                }
            } else if (name.equals("Jumps") && !Engine.this.flyCamera) {
            	Engine.this.player.jump(new Vector3f(0,30,0));;
            } else if(name.equals("Fly") && !keyPressed) {
            	if(Engine.this.flyCamera) {
            		player.setJumpSpeed(50);
                    player.setFallSpeed(2000);
                    player.setGravity(new Vector3f(0,-80,0));
                    player.setPhysicsLocation(cam.getLocation().clone());
            		bulletAppState.getPhysicsSpace().add(player);
            		Engine.this.flyCamera = false;
            	} else {
            		player.setJumpSpeed(0);
                    player.setFallSpeed(0);
                    player.setGravity(new Vector3f(0,0,0));
                    player.setPhysicsLocation(cam.getLocation().clone());
            		bulletAppState.getPhysicsSpace().remove(player);
            		Engine.this.flyCam.setMoveSpeed(500f);
            		Engine.this.flyCamera = true;
            	}
            }
        }
    };

    public void loadHintText() {
        hintText = new BitmapText(guiFont, false);
        hintText.setSize(guiFont.getCharSet().getRenderedSize());
        hintText.setLocalTranslation(0, getCamera().getHeight(), 0);
        hintText.setText("Hit T to switch to wireframe\nHit Left Ctrl to toggle gravity");
        guiNode.attachChild(hintText);
    }

    protected void initCrossHairs() {
        //guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }
    
    @Override
    public void update() {
        super.update();
    }
    
    private void createCollisionMarker() {
        Sphere s = new Sphere(6, 6, 1);
        collisionMarker = new Geometry("collisionMarker");
        collisionMarker.setMesh(s);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Orange);
        collisionMarker.setMaterial(mat);
        rootNode.attachChild(collisionMarker);
    }

    /**
     * Updates the player/camera position and changes display text.
     * Note: this isn't quite working yet. We're using the default behavior
     * of "SimpleApplication" in jME3 right now.
     */
    @Override
    public void simpleUpdate(final float tpf) {
    	if(shouldUpdateShapes) {
    		bulletAppState.getPhysicsSpace().removeAll(objectNode);
    		objectNode.detachAllChildren();
    		for (int i = 0; i < spatialBuffer.size(); i++) {
            	//Engine.logInfo("adding mesh from index " + i + " in meshBuffer, its position is " + geomPositions.get(i).toString());
                EngineSpatial engSpatial = spatialBuffer.get(i);
    			Vector3f localPos = (engSpatial.getJME3Position().subtract(this.getWorldPosition())).toVector3f();
                Spatial spatial = engSpatial.getJME3Spatial(this.assetManager);
                spatial.getControl(RigidBodyControl.class).setPhysicsLocation(localPos);
                objectNode.attachChild(spatial);
            }
        	bulletAppState.getPhysicsSpace().addAll(objectNode);
        	shouldUpdateShapes = false;
    	} else if(shouldResetObjectMaterials) {
    		for (int i = 0; i < spatialBuffer.size(); i++) {
    			EngineSpatial eg = spatialBuffer.get(i);
    			Spatial s = objectNode.getChild(eg.getGeomName());
    			s.setMaterial(eg.getMaterial(assetManager));
    		}
    		shouldResetObjectMaterials = false;
    	}
    	Vector3f camDir = this.cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft = this.cam.getLeft().clone().multLocal(0.4f);
        this.walkDirection.set(0, 0, 0);
        if (this.left && this.moves) {
            this.walkDirection.addLocal(camLeft);
        }
        if (this.right && this.moves) {
            this.walkDirection.addLocal(camLeft.negate());
        }
        if (this.up && this.moves) {
            this.walkDirection.addLocal(camDir);
        }
        if (this.down && this.moves) {
            this.walkDirection.addLocal(camDir.negate());
        }
        if (!flyCamera) {
            this.player.setWalkDirection(this.walkDirection);
            if(moves) {
            	this.cam.setLocation(this.player.getPhysicsLocation());
            }
        }
    }
    
    public String getCoordinates() {
        return this.getWorldPosition().toString();
    }
    
    public Vector3d getWorldPosition() {
    	return new Vector3d(this.worldPosition.x,0,this.worldPosition.z);
    }

    public void changeShapes(List<shapes.Shape> shapes) {
    	spatialBuffer.clear();
    	for (shapes.Shape shape : shapes) {
            EngineSpatial eg = new EngineSpatial(shape);
            spatialBuffer.add(eg);
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
    
    public static void logInfo(String msg) {
    	logger.info(msg);
    }
    
    public static void log(Level level, String msg) {
    	logger.log(level, msg);
    }
    
    
}
