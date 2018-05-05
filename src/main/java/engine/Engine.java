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
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.blender.math.Vector3d;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.water.WaterFilter;

import virtualworld.terrain.Point;
import worldmanager.WorldManager;

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
		private static Engine INSTANCE = null;
		public static Engine getInstance() {
			if(INSTANCE != null) {
				return INSTANCE;
			}
			else {
				try {
					EngineLoader.INSTANCE = new Engine();
					INSTANCE.setShowSettings(false);
					AppSettings settings = new AppSettings(true);
					settings.setResolution(1000, 800);
					INSTANCE.setSettings(settings);
					return INSTANCE;
				} catch (Exception e) {
					throw new ExceptionInInitializerError(e);
				}
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
			//this.initialWaterHeight = initialWaterHeight;
		}
	}
	
	/**
	 * Call this function to retrieve the application's Engine instance
	 * @return singleton instance of Engine
	 */
	public static Engine getInstance() {
		return EngineLoader.getInstance();
	}
	
	/**
	 * This buffer holds a collection of meshes (boxes, spheres, etc.) that
	 * are to be rendered in the NEXT frame.
	 * The positions of these meshes in the world are provided by the 
	 * "meshPositions" collection; a mesh at index i will have its
	 * position data at index i inside of "meshPositions"
	 */
	private final ArrayList<EngineShape> spatialBuffer = new ArrayList<EngineShape>();
	/**
	 * This buffer holds new shapes that should be added on next update.
	 * The buffer is cleared when the shapes are added to the render state.
	 */
	private final ArrayList<EngineShape> spatialsToAdd = new ArrayList<EngineShape>();
	
	/**
	 * The position of the camera/player in world coordinates
	 */
	private final Vector3d worldPosition = new Vector3d(0,0,0);
	
	private DirectionalLight sun;
	private Geometry skyDome;
	private Node objectNode;
	private Node mainNode;
	private final ColorRGBA sunColor = new ColorRGBA(0.50f, 0.40f, 0.50f, 1.0f);
	private final ColorRGBA skyColor = new ColorRGBA(0.7f, 0.8f, 1f, 1f);
	private final Vector3f initialCameraLoc = new Vector3f(0, 500, 0);
	private FilterPostProcessor fpp;
	private WaterFilter waterFilter;
	private final Vector3f lightDir = (new Vector3f(1f, -0.5f, -0.1f)); 
	private final float initialWaterHeight = 250f;
	private float time = 0.0f;
	private float waterHeight = 0.0f;
	private DebugTools debugTools;

	private boolean shouldUpdateShapes = false;
	private boolean shouldResetObjectMaterials = false;
	
	//private static final Terrain terrain = new Terrain(new Point(0.0,0.0), 100, 10, ))
	private static final Logger logger = Logger.getLogger(Engine.class.getName());
	private static final Random random = new Random(System.currentTimeMillis());
	
	public static final float drawDistance = 50000;

    private Material matWire;
    private boolean wireframe = false;
    private boolean showAxes = false;
    protected BitmapText hintText;
    protected BitmapText hitLocText;
    private Geometry collisionMarker;
    private BulletAppState bulletAppState;

    private boolean flyCamera;
    private float flySpeed = 10000f;
    
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
    	this.mainNode = new Node("MainNode");
    	this.flyCamera = true;
    	cam.setFrustumFar(drawDistance);
    	debugTools = new DebugTools(assetManager);
    	rootNode.attachChild(debugTools.debugNode);


		this.flyCam.setMoveSpeed(flySpeed);
    	ScreenshotAppState state = new ScreenshotAppState();
    	this.stateManager.attach(state);

        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        
        matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
    	
        this.getCamera().setLocation(initialCameraLoc);

        // set up sky dome
        Dome dome = new Dome(this.getWorldPosition().toVector3f(), 50, 50, drawDistance/2f, true);
        Material skyMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        skyMat.setColor("Color", skyColor);
        skyDome = new Geometry("SkyDome", dome);
        skyDome.setMaterial(skyMat);
        Vector3f skyDomeLoc = this.getWorldPosition().toVector3f();
        skyDomeLoc.y = initialCameraLoc.y - 300;
        skyDome.setLocalTranslation(skyDomeLoc);
        skyDome.setQueueBucket(Bucket.Sky);
        skyDome.setCullHint(Spatial.CullHint.Never);
        mainNode.attachChild(skyDome);
        this.viewPort.setBackgroundColor(skyColor);
        
        /**
         * add the sun (white directional light) to the root node
         */
        sun = new DirectionalLight();
        sun.setDirection(lightDir.normalizeLocal());
        sun.setColor(sunColor);
        mainNode.addLight(sun);
        
        // set up object geometries
        objectNode = new Node("ObjectNode");
		for (int i = 0; i < spatialBuffer.size(); i++) {
        	//Engine.logInfo("adding mesh from index " + i + " in meshBuffer, its position is " + geomPositions.get(i).toString());
            EngineShape engSpatial = spatialBuffer.get(i);
			Vector3f localPos = (engSpatial.getJME3Position().subtract(this.getWorldPosition())).toVector3f();
            //Engine.logInfo("mesh origin is " + localPos.toString());
            Node node = engSpatial.getJME3Node(this.assetManager, this.showAxes);
            node.getControl(RigidBodyControl.class).setPhysicsLocation(localPos);
            objectNode.attachChild(node);
        }
		mainNode.attachChild(objectNode);
		bulletAppState.getPhysicsSpace().addAll(objectNode);

		fpp = new FilterPostProcessor(assetManager);
		waterFilter = new WaterFilter(rootNode, lightDir);
		waterFilter.setWaterHeight(initialWaterHeight);
		waterFilter.setLightColor(sunColor);
		waterFilter.setLightDirection(lightDir);
		waterFilter.setRadius(drawDistance/2f);
		waterFilter.setCenter(worldPosition.toVector3f());
		waterFilter.setColorExtinction(new Vector3f(7,11,7));
		fpp.addFilter(waterFilter);
		viewPort.addProcessor(fpp);
		
		// set up player physics object
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.5f, 1.8f, 1);
        player = new CharacterControl(capsuleShape, 0.5f);
        player.setJumpSpeed(50);
        player.setFallSpeed(2000);
        player.setGravity(new Vector3f(0,-80,0));
        player.setPhysicsLocation(cam.getLocation().clone());
        
        this.rootNode.attachChild(mainNode);
        
        this.initKeys();
    }
    
    /**
     * Adds key mappings to keyboard buttons.
     * TODO: Controller support?
     */
    private void initKeys() {
        // You can map one or several inputs to one named action
    	inputManager.addListener(actionListener, "shoot");
	    inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    	inputManager.addListener(actionListener, "wireframe");
    	inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_T));
    	inputManager.addListener(actionListener, "showAxes");
    	inputManager.addMapping("showAxes", new KeyTrigger(KeyInput.KEY_X));
    	inputManager.addListener(actionListener, "cameraDown");
        inputManager.addMapping("cameraDown", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        
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
                if (wireframe) {
                	objectNode.setMaterial(matWire);
                } else {
                	shouldResetObjectMaterials = true;
                }
        	} else if(name.equals("showAxes") && !keyPressed) {
        		showAxes = !showAxes;
        		if(showAxes && !shouldUpdateShapes) {
        			addAxes(objectNode);
        		} else if (!shouldUpdateShapes) {
        			removeAxes(objectNode);
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
                    updateHitLocText(hit.getContactPoint());
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
            		Engine.this.flyCam.setMoveSpeed(flySpeed);
            		Engine.this.flyCamera = true;
            	}
            }
        }
    };

    public void loadHintText() {
        hintText = new BitmapText(guiFont, false);
        hintText.setSize(guiFont.getCharSet().getRenderedSize());
        hintText.setLocalTranslation(0, getCamera().getHeight(), 0);
        hintText.setText("Hit T to switch to wireframe\nHit Left Ctrl to toggle gravity\nHit X to toggle showing local axes of objects");
        guiNode.attachChild(hintText);
    }
    
    public void updateHitLocText(Vector3f hitLoc) {
    	if(hitLocText != null) {
    		guiNode.detachChild(hitLocText);
    	}
    	else {
    		hitLocText = new BitmapText(guiFont, false);
    		hitLocText.setSize(guiFont.getCharSet().getRenderedSize());
        	hitLocText.setLocalTranslation(300, getCamera().getHeight(), 0);
    	}
    	hitLocText.setText(hitLoc.toString());
    	guiNode.attachChild(hitLocText);
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
    	// update Engine time
        time += tpf;
        
    	if(shouldUpdateShapes) {
    		updateSpatialsInNode(objectNode);
    	} else if(shouldResetObjectMaterials) {
    		resetSpatialMaterialsInNode(objectNode);
    	}
    	if(!spatialsToAdd.isEmpty()) {
    		addSpatialsToNode(objectNode);
    	}
    	updatePlayerLocation();
    	updateSkyDomeLocation(this.cam.getLocation());

        updateWater();
    }
    
    private void resetSpatialMaterialsInNode(Node node) {
    	for (int i = 0; i < spatialBuffer.size(); i++) {
			EngineShape es = spatialBuffer.get(i);
			Node n = (Node)node.getChild(es.getShapeNodeName());
			n.setMaterial(es.getMaterial(assetManager));
		}
		shouldResetObjectMaterials = false;
    }
    
    private void updateSpatialsInNode(Node node) {
    	bulletAppState.getPhysicsSpace().removeAll(node);
		node.detachAllChildren();
		for (int i = 0; i < spatialBuffer.size(); i++) {
        	//Engine.logInfo("adding mesh from index " + i + " in meshBuffer, its position is " + geomPositions.get(i).toString());
            EngineShape engSpatial = spatialBuffer.get(i);
			Vector3f localPos = (engSpatial.getJME3Position().subtract(this.getWorldPosition())).toVector3f();
            Node tmpNode = engSpatial.getJME3Node(this.assetManager,this.showAxes);
            tmpNode.getControl(RigidBodyControl.class).setPhysicsLocation(localPos);
            if(wireframe) {
            	tmpNode.getChild(engSpatial.getShapeNodeName()).setMaterial(matWire);
            }
            node.attachChild(tmpNode);
        }
    	bulletAppState.getPhysicsSpace().addAll(node);
    	shouldUpdateShapes = false;
    }
    
    private void addSpatialsToNode(Node node) {
    	for (int i = 0; i < spatialsToAdd.size(); i++) {
        	//Engine.logInfo("adding mesh from index " + i + " in meshBuffer, its position is " + geomPositions.get(i).toString());
            EngineShape engSpatial = spatialsToAdd.get(i);
			spatialBuffer.add(engSpatial);
			Vector3f localPos = (engSpatial.getJME3Position().subtract(this.getWorldPosition())).toVector3f();
            Node tmpNode = engSpatial.getJME3Node(this.assetManager,this.showAxes);
            tmpNode.getControl(RigidBodyControl.class).setPhysicsLocation(localPos);
            if(wireframe) {
            	tmpNode.getChild(engSpatial.getShapeNodeName()).setMaterial(matWire);
            }
            node.attachChild(tmpNode);
            bulletAppState.getPhysicsSpace().add(tmpNode);
        }
		spatialsToAdd.clear();
    }
    
    private void updateWorldLocation() {
    	this.worldPosition.x = this.cam.getLocation().x;
        this.worldPosition.z = this.cam.getLocation().z;
    }
    
    private void updatePlayerLocation() {
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
            if(this.moves) {
            	this.cam.setLocation(this.player.getPhysicsLocation());
            }
        }
        if(this.moves) {
        	updateWorldLocation();
        }
        WorldManager world = WorldManager.getInstance();
        Point p = new Point(this.worldPosition.x,this.worldPosition.z);
        world.updateCamera(p);
    }
    
    private void updateSkyDomeLocation(final Vector3f camLoc) {
    	 Vector3f skyDomeLoc = camLoc.clone();
         skyDomeLoc.y = this.skyDome.getLocalTranslation().y;
         this.skyDome.setLocalTranslation(skyDomeLoc);
    }
    
    private void updateWater() {
    	this.waterHeight = (float) Math.cos(((this.time * 0.6f) % FastMath.TWO_PI)) * 1.5f;
        this.waterFilter.setWaterHeight(this.initialWaterHeight + this.waterHeight);
    }

    private void addAxes(Node node) {
    	this.enqueue(new Runnable() {
	    	public void run() {
	    		for(int i = 0; i < spatialBuffer.size(); i++) {
		    		EngineShape es = spatialBuffer.get(i);
		    		Spatial s = node.getChild(es.getNodeName());
		    		if(s != null && s instanceof Node) {
		    			Node n = (Node)s;
		    			if(n.getChild(es.getAxesNodeName()) == null) {
		        			n.attachChild(es.getXYZAxes(Engine.this.assetManager));
		        		}
		    		}
	    		}
	    	}
    	});
    }
    
    private void removeAxes(Node node) {
    	this.enqueue(new Runnable() {
	    	public void run() {
		    	for(int i = 0; i < spatialBuffer.size(); i++) {
		    		EngineShape es = spatialBuffer.get(i);
		    		Node top = (Node)node.getChild(es.getNodeName());
		    		Spatial axes = top.getChild(es.getAxesNodeName());
		    		if(axes != null && axes instanceof Node) {
		    			top.detachChild(axes);
		    		}
	    		}
	    	}
    	});
    }
    
    public String getCoordinates() {
        return this.getWorldPosition().toString();
    }
    
    public Vector3d getWorldPosition() {
    	return new Vector3d(this.worldPosition.x,0,this.worldPosition.z);
    }

    public void changeShapes(List<shapes.Shape> shapes) {
    	this.enqueue(new Runnable() {
    		public void run() {
    			spatialBuffer.clear();
    	    	for (shapes.Shape shape : shapes) {
    	            EngineShape es = new EngineShape(shape);
    	            spatialBuffer.add(es);
    	    	}
    	    	shouldUpdateShapes = true;
    		}
    	});
    }
    
    public void addShapes(List<shapes.Shape> shapes) {
    	this.enqueue(new Runnable() {
    		public void run() {
	    		for(shapes.Shape shape : shapes) {
	        		EngineShape es = new EngineShape(shape);
	        		spatialsToAdd.add(es);
	        	}
    		}
    	});
    }
    
    public void addShape(shapes.Shape shape) {
    	this.enqueue(new Runnable() {
    		public void run() {
    			EngineShape es = new EngineShape(shape);
    			spatialsToAdd.add(es);
    		}
    	});
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
