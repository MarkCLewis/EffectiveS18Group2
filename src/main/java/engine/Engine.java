package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.HeightfieldCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.debug.DebugTools;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.blender.math.Vector3d;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainGridListener;
import com.jme3.terrain.geomipmap.TerrainGridLodControl;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.grid.FractalTileLoader;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.noise.ShaderUtils;
import com.jme3.terrain.noise.basis.FilteredBasis;
import com.jme3.terrain.noise.filter.IterativeFilter;
import com.jme3.terrain.noise.filter.OptimizedErode;
import com.jme3.terrain.noise.filter.PerturbFilter;
import com.jme3.terrain.noise.filter.SmoothFilter;
import com.jme3.terrain.noise.fractal.FractalSum;
import com.jme3.terrain.noise.modulator.NoiseModulator;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

import shapes.Shape;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.opencl.Image.ImageFormat;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;

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
	private final ArrayList<Geometry> geomBuffer = new ArrayList<Geometry>();
	/**
	 * This buffer holds the positions of meshes in world coordinates that 
	 * are to be rendered in the NEXT frame.
	 * Elements in this collection correspond to the elements in "meshBuffer"
	 * with the same index.
	 */
	private final ArrayList<Vector3d> geomPositions = new ArrayList<Vector3d>();
	
	/**
	 * The position of the camera/player in world coordinates
	 */
	private final Vector3d worldPosition = new Vector3d(0,0,0);
	
	private DirectionalLight sun;
	private Node objectNode;
	private DebugTools debugTools;

	private boolean shouldUpdateShapes = false;
	
	//private static final Terrain terrain = new Terrain(new Point(0.0,0.0), 100, 10, ))
	private static final Logger logger = Logger.getLogger(Engine.class.getName());
	private static final Random random = new Random(System.currentTimeMillis());
	
	public static final float fogDistance = 400;
	
	private TerrainGrid terrainGrid;
    private Material mat_terrain;
    private float grassScale = 64;
    private float dirtScale = 16;
    private float rockScale = 128;
    private Material matWire;
    private boolean wireframe = false;
    private final static boolean renderKaylaTerrain = false; // set this to false to turn off kayla's terrain rendering
    protected BitmapText hintText;
    private Geometry collisionMarker;
    private BulletAppState bulletAppState;

    private final static boolean usePhysics = true;
    
    private CharacterControl player;
    private FractalSum base;
    private PerturbFilter perturb;
    private OptimizedErode therm;
    private SmoothFilter smooth;
    private IterativeFilter iterate;
    
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
    	//logger.info("simpleInitApp");
    	cam.setFrustumFar(10000);
    	debugTools = new DebugTools(assetManager);
    	rootNode.attachChild(debugTools.debugNode);

		this.flyCam.setMoveSpeed(1000f);
    	ScreenshotAppState state = new ScreenshotAppState();
    	this.stateManager.attach(state);

        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
    	
        if(renderKaylaTerrain) {
	    	this.mat_terrain = new Material(this.assetManager, "Common/MatDefs/Terrain/HeightBasedTerrain.j3md");
	    	
	    	// Parameters to material:
	        // regionXColorMap: X = 1..4 the texture that should be appliad to state X
	        // regionX: a Vector3f containing the following information:
	        //      regionX.x: the start height of the region
	        //      regionX.y: the end height of the region
	        //      regionX.z: the texture scale for the region
	        //  it might not be the most elegant way for storing these 3 values, but it packs the data nicely :)
	        // slopeColorMap: the texture to be used for cliffs, and steep mountain sites
	        // slopeTileFactor: the texture scale for slopes
	        // terrainSize: the total size of the terrain (used for scaling the texture)
	        // GRASS texture
	        Texture grass = this.assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
	        grass.setWrap(WrapMode.Repeat);
	        this.mat_terrain.setTexture("region1ColorMap", grass);
	        this.mat_terrain.setVector3("region1", new Vector3f(15, 200, this.grassScale));
	
	        // DIRT texture
	        Texture dirt = this.assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
	        dirt.setWrap(WrapMode.Repeat);
	        this.mat_terrain.setTexture("region2ColorMap", dirt);
	        this.mat_terrain.setVector3("region2", new Vector3f(0, 20, this.dirtScale));
	
	        // ROCK texture
	        Texture rock = this.assetManager.loadTexture("Textures/Terrain/Rock2/rock.jpg");
	        rock.setWrap(WrapMode.Repeat);
	        this.mat_terrain.setTexture("region3ColorMap", rock);
	        this.mat_terrain.setVector3("region3", new Vector3f(198, 260, this.rockScale));
	
	        this.mat_terrain.setTexture("region4ColorMap", rock);
	        this.mat_terrain.setVector3("region4", new Vector3f(198, 260, this.rockScale));
	
	        this.mat_terrain.setTexture("slopeColorMap", rock);
	        this.mat_terrain.setFloat("slopeTileFactor", 32);
	
	        this.mat_terrain.setFloat("terrainSize", 513);
	        
	        this.base = new FractalSum();
	        this.base.setRoughness(0.7f);
	        this.base.setFrequency(1.0f);
	        this.base.setAmplitude(1.0f);
	        this.base.setLacunarity(2.12f);
	        this.base.setOctaves(8);
	        this.base.setScale(0.02125f);
	        this.base.addModulator(new NoiseModulator() {
	
	            @Override
	            public float value(float... in) {
	                return ShaderUtils.clamp(in[0] * 0.5f + 0.5f, 0, 1);
	            }
	        });
	
	        FilteredBasis ground = new FilteredBasis(this.base);
	
	        this.perturb = new PerturbFilter();
	        this.perturb.setMagnitude(0.119f);
	
	        this.therm = new OptimizedErode();
	        this.therm.setRadius(5);
	        this.therm.setTalus(0.011f);
	
	        this.smooth = new SmoothFilter();
	        this.smooth.setRadius(1);
	        this.smooth.setEffect(0.7f);
	
	        this.iterate = new IterativeFilter();
	        this.iterate.addPreFilter(this.perturb);
	        this.iterate.addPostFilter(this.smooth);
	        this.iterate.setFilter(this.therm);
	        this.iterate.setIterations(1);
	
	        ground.addPreFilter(this.iterate);
	
	        int patchSize = 66;
	        int maxTerrainVisible = 258;
	        this.terrainGrid = new TerrainGrid("terrain", patchSize, maxTerrainVisible, new FractalTileLoader(ground, 256f));
	
	        this.terrainGrid.setMaterial(this.mat_terrain);
	        this.terrainGrid.setLocalTranslation(0, 0, 0);
	        this.terrainGrid.setLocalScale(2f, 1f, 2f);
	        this.terrainGrid.setLocked(false); // unlock it so we can edit the height
	        this.rootNode.attachChild(this.terrainGrid);
	        
	        TerrainLodControl control = new TerrainGridLodControl(this.terrainGrid, this.getCamera());
	        control.setLodCalculator(new DistanceLodCalculator(patchSize, 2.7f)); // patch size, and a multiplier
	        this.terrainGrid.addControl(control);
	        
	        /**
	         * Create PhysicsRigidBodyControl for collision
	         */
	        List<Spatial> terrainGridChildren = terrainGrid.getChildren();
	        for(Spatial sp : terrainGridChildren) {
	        	if(sp.getClass() == TerrainQuad.class) {
	        		((TerrainQuad)sp).addControl(new RigidBodyControl(new HeightfieldCollisionShape(((TerrainQuad)sp).getHeightMap(), terrainGrid.getLocalScale()), 0));
	        	}
	        }
	        bulletAppState.getPhysicsSpace().addAll(terrainGrid);
	        
	        terrainGrid.addListener(new TerrainGridListener() {

	            public void gridMoved(Vector3f newCenter) {
	            }

	            public void tileAttached(Vector3f cell, TerrainQuad quad) {
	                while(quad.getControl(RigidBodyControl.class)!=null){
	                    quad.removeControl(RigidBodyControl.class);
	                }
	                quad.addControl(new RigidBodyControl(new HeightfieldCollisionShape(quad.getHeightMap(), terrainGrid.getLocalScale()), 0));
	                bulletAppState.getPhysicsSpace().add(quad);
	            }

	            public void tileDetached(Vector3f cell, TerrainQuad quad) {
	                if (quad.getControl(RigidBodyControl.class) != null) {
	                    bulletAppState.getPhysicsSpace().remove(quad);
	                    quad.removeControl(RigidBodyControl.class);
	                }
	            }

	        });
        }
        
        this.getCamera().setLocation(new Vector3f(0, 2000, 0));

        this.viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

        /**
         * add the sun (white directional light) to the root node
         */
        sun = new DirectionalLight();
        sun.setDirection((new Vector3f(1f, -0.5f, -0.1f)).normalizeLocal());
        sun.setColor(new ColorRGBA(0.50f, 0.40f, 0.50f, 1.0f));
        rootNode.addLight(sun);
        
        objectNode = new Node("ObjectNode");
		for (int i = 0; i < geomPositions.size(); i++) {
        	//Engine.logInfo("adding mesh from index " + i + " in meshBuffer, its position is " + geomPositions.get(i).toString());
            Vector3f localPos = (geomPositions.get(i).subtract(getWorldPosition())).toVector3f();
            //Engine.logInfo("mesh origin is " + localPos.toString());
            Geometry geom = geomBuffer.get(i);
            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setColor("Diffuse", ColorRGBA.White);
            mat.setColor("Specular", ColorRGBA.White);
            mat.setFloat("Shininess", 64);
            geom.setLocalTranslation(localPos);
            geom.setMaterial(mat);
            objectNode.attachChild(geom);
        }
		rootNode.attachChild(objectNode);
        // Add 5 physics spheres to the world, with random sizes and positions
        // let them drop from the sky
        for (int i = 0; i < 5; i++) {
            float r = (float) (8 * getRandomDouble(1,3));
            Geometry sphere = new Geometry("cannonball", new Sphere(10, 10, r));
            sphere.setMaterial(matWire);
            float x = (float) (20 * getRandomDouble(0,1)) - 40; // random position
            float y = (float) (20 * getRandomDouble(0,1)) - 40; // random position
            float z = (float) (20 * getRandomDouble(0,1)) - 40; // random position
            sphere.setLocalTranslation(new Vector3f(x, 300 + y, z));
            sphere.addControl(new RigidBodyControl(new SphereCollisionShape(r), 2));
            rootNode.attachChild(sphere);
            bulletAppState.getPhysicsSpace().add(sphere);
        }

        if (usePhysics && renderKaylaTerrain) {
            CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.5f, 1.8f, 1);
            player = new CharacterControl(capsuleShape, 0.5f);
            player.setJumpSpeed(20);
            player.setFallSpeed(10);
            player.setGravity(new Vector3f(0,-20,0));

            player.setPhysicsLocation(cam.getLocation().clone());

            bulletAppState.getPhysicsSpace().add(player);
        }
        
        this.initKeys();
    }
    
    /**
     * Adds key mappings to keyboard buttons.
     * TODO: Controller support?
     */
    private void initKeys() {
        // You can map one or several inputs to one named action
    	if(renderKaylaTerrain) {
	        inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_T));
	        inputManager.addListener(actionListener, "wireframe");
	        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	        inputManager.addListener(actionListener, "shoot");
    	}
        
        inputManager.addMapping("cameraDown", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "cameraDown");
        
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jumps", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "Lefts");
        inputManager.addListener(actionListener, "Rights");
        inputManager.addListener(actionListener, "Ups");
        inputManager.addListener(actionListener, "Downs");
        inputManager.addListener(actionListener, "Jumps");
    }
    
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean moves = true;
    private final ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(final String name, final boolean keyPressed, final float tpf) {
        	// from TerrainGridTileLoaderTest
        	if (name.equals("wireframe") && !keyPressed && renderKaylaTerrain) {
                wireframe = !wireframe;
                if (!wireframe) {
                    terrainGrid.setMaterial(matWire);
                } else {
                    terrainGrid.setMaterial(mat_terrain);
                }
            } else if (name.equals("shoot") && !keyPressed && renderKaylaTerrain) {

                Vector3f origin = cam.getWorldCoordinates(new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2), 0.0f);
                Vector3f direction = cam.getWorldCoordinates(new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2), 0.3f);
                direction.subtractLocal(origin).normalizeLocal();

                Ray ray = new Ray(origin, direction);
                CollisionResults results = new CollisionResults();
                int numCollisions = terrainGrid.collideWith(ray, results);
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
            } else if (name.equals("Jumps") && usePhysics && renderKaylaTerrain) {
            	Engine.this.player.jump(new Vector3f(0,10,0));;
            }
        }
    };

    public void loadHintText() {
        hintText = new BitmapText(guiFont, false);
        hintText.setSize(guiFont.getCharSet().getRenderedSize());
        hintText.setLocalTranslation(0, getCamera().getHeight(), 0);
        if(renderKaylaTerrain) {
        	hintText.setText("Hit T to switch to wireframe");
        }
        hintText.setText("");
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
    public void simpleUpdate(final float tpf) {
    	if(shouldUpdateShapes) {
    		objectNode.detachAllChildren();
        	for (int i = 0; i < geomPositions.size(); i++) {
            	//Engine.logInfo("adding geometry from index " + i + " in meshBuffer");
                Vector3f localPos = (geomPositions.get(i).subtract(getWorldPosition())).toVector3f();
                //Engine.logInfo("geometry origin is " + localPos.toString());
                Geometry geom = geomBuffer.get(i);
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
        if (usePhysics && renderKaylaTerrain) {
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
    	return new Vector3d(this.worldPosition.x,this.worldPosition.y,this.worldPosition.z);
    }

    public void changeShapes(List<shapes.Shape> shapes) {
    	geomBuffer.clear();
    	geomPositions.clear();
    	for (shapes.Shape shape : shapes) {
    		//Engine.logInfo("addShape");
        	double[] pos = shape.getCenter();
        	//Engine.logInfo("addShape with center: " + java.util.Arrays.toString(pos));
        	Vector3d vector3d = new Vector3d(pos[0],pos[1],pos[2]);
            Geometry m = Utils.getGeomFromShape(shape);
            geomBuffer.add(m);
            geomPositions.add(vector3d);
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
