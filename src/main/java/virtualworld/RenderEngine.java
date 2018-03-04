package virtualworld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryUtil;

public class RenderEngine implements Runnable {
	/* Controls frame buffers and rendering calculations for display */
	private static class RenderEngineLoader {
		/* Lazily instantiate the RenderEngine */
		private static final RenderEngine INSTANCE;
		static {
			try {
				INSTANCE = new RenderEngine();
			} catch (Exception e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}
	
	private RenderEngine() throws IOException, AssertionError, IllegalStateException {
		if(RenderEngineLoader.INSTANCE != null) {
			throw new IllegalStateException("Already instantiated");
		}
		else {
			gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		}
	}
	
	public static RenderEngine getInstance() {
		/* the first time this is called, the RenderEngine will
		 * be instantiated.
		 * */
		return RenderEngineLoader.INSTANCE;
	}
	
	private static class Camera {
		private float maxLinearVel = 200.0f;
		private Vector3f linearAcc = new Vector3f();
	    private Vector3f linearVel = new Vector3f();
	    private float linearDamping = 0.08f;
	    /** ALWAYS rotation about the local XYZ axes of the camera! */
	    private Vector3f angularAcc = new Vector3f();
	    private Vector3f angularVel = new Vector3f();
	    private float angularDamping = 0.5f;

	    private Vector3d position = new Vector3d(0, 0, 10);
	    private Quaternionf rotation = new Quaternionf();

	    private Camera update(float dt) {
	        // update linear velocity based on linear acceleration
	        linearVel.fma(dt, linearAcc);
	        // update angular velocity based on angular acceleration
	        angularVel.fma(dt, angularAcc);
	        // update the rotation based on the angular velocity
	        rotation.integrate(dt, angularVel.x, angularVel.y, angularVel.z);
	        angularVel.mul(1.0f - angularDamping * dt);
	        // update position based on linear velocity
	        position.fma(dt, linearVel);
	        linearVel.mul(1.0f - linearDamping * dt);
	        return this;
	    }
	    public Vector3f right(Vector3f dest) {
	        return rotation.positiveX(dest);
	    }
	    public Vector3f up(Vector3f dest) {
	        return rotation.positiveY(dest);
	    }
	    public Vector3f forward(Vector3f dest) {
	        return rotation.positiveZ(dest).negate();
	    }
	    public float getMaxLinearVelocity() {
	    	return maxLinearVel;
	    }
	    public void stabilize() {
	    	linearAcc.zero();
	    }
	}
	
	private class ShaderProgram {
		private final int programId;
		private int vertexShaderId;
		private int fragmentShaderId;
		
		public ShaderProgram() throws Exception {
			programId = glCreateProgram();
			if (programId == 0) {
				throw new Exception("Could not create Shader");
			}
		}
		
		public void createVertexShader(String shaderCode) throws Exception {
			vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
		}
		
		public void createFragmentShader(String shaderCode) throws Exception {
			fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
		}
		
		private int createShader(String shaderCode, int shaderType) throws Exception {
			int shaderId = glCreateShader(shaderType);
			if (shaderId == 0) {
				throw new Exception("Error creating shader. Type: " + shaderType);
			}
			glShaderSource(shaderId, shaderCode);
			glCompileShader(shaderId);
			if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
				throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
			}
			glAttachShader(programId, shaderId);
			return shaderId;
		}
		
		public void link() throws Exception {
			glLinkProgram(programId);
			if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
				throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
			}
			if (vertexShaderId != 0) {
				glDetachShader(programId, vertexShaderId);
			}
			if (fragmentShaderId != 0) {
				glDetachShader(programId, fragmentShaderId);
			}
			glValidateProgram(programId);
			if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
				System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
			}
		}
		public void bind() {
			glUseProgram(programId);
		}
			
		public void unbind() {
			glUseProgram(0);
		}
		
		public void close() {
			unbind();
			if (programId != 0) {
				glDeleteProgram(programId);
			}
		}
	}

	private class Display {
		private class Input {
			private boolean[] keyDown = new boolean[GLFW.GLFW_KEY_LAST];
		    private boolean leftMouseDown = false;
		    private boolean rightMouseDown = false;
		    private float mouseX = 0.0f;
		    private float mouseY = 0.0f;

			private GLFWKeyCallback keyCallback;
		    
		    private GLFWCursorPosCallback cpCallback;
		    
		    private GLFWMouseButtonCallback mbCallback;
		    
		    public Input() {
		    	init();
		    }
		    
			private void init() {
				System.out.println("Press W/S to move forward/backward");
		        System.out.println("Press L.Ctrl/Spacebar to move down/up");
		        System.out.println("Press A/D to strafe left/right");
		        System.out.println("Press Q/E to roll left/right");
		        System.out.println("Hold the left mouse button to shoot");
		        System.out.println("Hold the right mouse button to rotate towards the mouse cursor");
		        keyCallback = new GLFWKeyCallback() {
		            @Override
					public void invoke(long window, int key, int scancode, int action, int mods) {
		                if (key == GLFW_KEY_UNKNOWN) 
		                    return;
		                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
		                    glfwSetWindowShouldClose(window, true);
		                }
		                if (action == GLFW_PRESS || action == GLFW_REPEAT) {
		                    keyDown[key] = true;
		                } else {
		                    keyDown[key] = false;
		                }
		            }
		        };
		        cpCallback = new GLFWCursorPosCallback() {
		            @Override
					public void invoke(long window, double xpos, double ypos) {
		                float normX = (float) ((xpos - getWidth()/2.0) / getWidth() * 2.0);
		                float normY = (float) ((ypos - getHeight()/2.0) / getHeight() * 2.0);
		                mouseX = Math.max(-getWidth()/2.0f, Math.min(getWidth()/2.0f, normX));
		                mouseY = Math.max(-getHeight()/2.0f, Math.min(getHeight()/2.0f, normY));
		            }
		        };
		        mbCallback = new GLFWMouseButtonCallback() {
		            @Override
					public void invoke(long window, int button, int action, int mods) {
		                if (button == GLFW_MOUSE_BUTTON_LEFT) {
		                    if (action == GLFW_PRESS)
		                        leftMouseDown = true;
		                    else if (action == GLFW_RELEASE)
		                        leftMouseDown = false;
		                } else if (button == GLFW_MOUSE_BUTTON_RIGHT) {
		                    if (action == GLFW_PRESS)
		                        rightMouseDown = true;
		                    else if (action == GLFW_RELEASE)
		                        rightMouseDown = false;
		                }
		            }
		        };
		        glfwSetKeyCallback(window, keyCallback);
		        glfwSetCursorPosCallback(window, cpCallback);
		        glfwSetMouseButtonCallback(window, mbCallback);
			}
			
			private void close() {
				keyCallback.free();
				cpCallback.free();
				mbCallback.free();
			}
			
			public void update(float dt) {
				camera.stabilize();;
				float rotZ = 0.0f;
		        if (keyDown[GLFW_KEY_W]) {
		            // TODO: move forward
		        }
		        if (keyDown[GLFW_KEY_S]) {
		            // TODO: move backward
		        }
		        if (keyDown[GLFW_KEY_D]) {
		        	// TODO: move right
		            // example: cam.linearAcc.fma(straveThrusterAccFactor, cam.right(tmp2));
		        }
		        if (keyDown[GLFW_KEY_A]) {
		        	// TODO: move left
		            // example: cam.linearAcc.fma(-straveThrusterAccFactor, cam.right(tmp2));
		        }
		        if (keyDown[GLFW_KEY_Q]) {
		            rotZ = -1.0f;
		        }
		        if (keyDown[GLFW_KEY_E]) {
		            rotZ = +1.0f;
		        }
		        if (keyDown[GLFW_KEY_SPACE]) {
		            // TODO: jump?
		        }
		        if (keyDown[GLFW_KEY_LEFT_CONTROL]) {
		            // TODO
		        }
		        if (rightMouseDown) {
		            camera.angularAcc.set(2.0f*mouseY*mouseY*mouseY, 2.0f*mouseX*mouseX*mouseX, rotZ);
		        }
		        else if (!rightMouseDown) {
		            camera.angularAcc.set(0, 0, rotZ);
		        }
		        double linearVelAbs = camera.linearVel.length();
		        if (linearVelAbs > camera.getMaxLinearVelocity()) {
		            camera.linearVel.normalize().mul(camera.getMaxLinearVelocity());
		        }
			}
		}
		
		private long window = NULL;
		private long monitor = NULL;
		private boolean windowed = true;
	    private int width = 800;
	    private int height = 600;
	    
	    private GLFWVidMode vidMode;
	    
	    private GLFWWindowSizeCallback wsCallback;
	    private GLFWFramebufferSizeCallback fbCallback;
	    
	    private Input input;
	    
	    public Display() {
	    	this.init();
	    	input = new Input();
	    }
	    
	    private void init() throws AssertionError{
	    	monitor = glfwGetPrimaryMonitor();
	        vidMode = glfwGetVideoMode(monitor);
	        if (!windowed) {
	            width = vidMode.width();
	            height = vidMode.height();
	            fbWidth = width;
	            fbHeight = height;
	        }
	        glfwDefaultWindowHints();
	        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
	        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
	        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
	        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
	        glfwWindowHint(GLFW_SAMPLES, 4);
	    	window = glfwCreateWindow(width, height, Game.title, !windowed ? monitor : 0L, NULL);
	        if (window == NULL) {
	            throw new AssertionError("Failed to create the GLFW window");
	        }
	        wsCallback = new GLFWWindowSizeCallback() {
	            @Override
				public void invoke(long window, int _width, int _height) {
	                if (_width > 0 && _height > 0 && (width != _width || height != _height)) {
	                    width = _width;
	                    height = _height;
	                }
	            }
	        };
	        
	        glfwSetWindowSizeCallback(window, wsCallback);
	        glfwMakeContextCurrent(window);
	        glfwSwapInterval(0);
	        glfwShowWindow(window);
	        
	        fbCallback = new GLFWFramebufferSizeCallback() {
		        @Override
				public void invoke(long window, int _width, int _height) {
		            if (_width > 0 && _height > 0 && (fbWidth != _width || fbHeight != _height)) {
		                fbWidth = width;
		                fbHeight = height;
		            }
		        }
		    };
	        glfwSetFramebufferSizeCallback(window, fbCallback);
	    }
	    
	    public void close() {
	    	wsCallback.free();
	    	fbCallback.free();
	    	input.close();
	    	glfwDestroyWindow(window);
	    }
	    
	    private void updateInput(float dt) {
	    	input.update(dt);
	    }
	    
	    public boolean isWindowed() {
	    	return windowed;
	    }
	    
	    public int getWidth() {
	    	return width;
	    }
	    
	    public int getHeight() {
	    	return height;
	    }
	}
	
	private Thread gameLoopThread;
	
	private long nanoSecsPerFrame = 20000000; // 50 frames per second
	private long lastTime = System.nanoTime();
	
	private Camera camera;
	private Display display;
	
	private int fbWidth = 800;
    private int fbHeight = 600;
    
    private IntBuffer frameBufferSize;
    private GLCapabilities caps;
    private Callback debugProc;
    
    private Matrix4f projMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f viewProjMatrix;
    private Matrix4f invViewMatrix;
    private Matrix4f invViewProjMatrix;
    private FloatBuffer verticesBuffer;
    private FrustumIntersection frustumIntersection;
    
    private ByteBuffer quadVertices;
    
    private int vaoId;
    private int vboId;
    
    private ShaderProgram shaderProgram;

    public int getFBWidth() {
    	return fbWidth;
    }
    
    public void setFBWidth(int _fbWidth) {
    	fbWidth = _fbWidth;
    }
    
    public int getFBHeight() {
    	return fbHeight;
    }
    
    public void setFBHeight(int _fbHeight) {
    	fbHeight = _fbHeight;
    }
    
    public GLCapabilities getCaps() {
    	return caps;
    }
    
    private void init() throws AssertionError, IOException, IllegalStateException, Exception {
    	if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    	camera = new Camera();
    	display = new Display();
    	
    	frameBufferSize = BufferUtils.createIntBuffer(2);
        nglfwGetFramebufferSize(display.window, memAddress(frameBufferSize), memAddress(frameBufferSize) + 4);
        fbWidth = frameBufferSize.get(0);
        fbHeight = frameBufferSize.get(1);
        
        caps = GL.createCapabilities();
        if (!caps.OpenGL20) {
            throw new AssertionError("This demo requires OpenGL 2.0.");
        }

	    projMatrix = new Matrix4f();
	    viewMatrix = new Matrix4f();
	    viewProjMatrix = new Matrix4f();
	    invViewMatrix = new Matrix4f();
	    invViewProjMatrix = new Matrix4f();
	    frustumIntersection = new FrustumIntersection();
	    
        debugProc = GLUtil.setupDebugMessageCallback();
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        
        try {
        	shaderProgram = new ShaderProgram();
        	shaderProgram.createVertexShader(Utils.readFile("/vertex.vs", Charset.defaultCharset()));
            shaderProgram.createFragmentShader(Utils.readFile("/fragment.fs", Charset.defaultCharset()));
            shaderProgram.link();
        } catch (Exception e) {
        	System.out.println("Problem initializing the shader program");
        	throw e;
        }
        
        initTriangle();
    }
    
    public void close() {
    	if(debugProc != null) {
    		debugProc.free();
    	}
    	if(shaderProgram != null) {
    		shaderProgram.close();
    	}
    	// Cleanup GL resources
    	glDisableVertexAttribArray(0);
    	// Delete the VBO
    	glBindBuffer(GL_ARRAY_BUFFER, 0);
    	glDeleteBuffers(vboId);
    	// Delete the VAO
    	glBindVertexArray(0);
    	glDeleteVertexArrays(vaoId);
    	
    	display.close();
    }
    
    public void start() {
    	gameLoopThread.start();
    }
    
    @Override
    public void run() {
    	try {
    		init();
            loop();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            glfwTerminate();
        }
    }
    
    public void update(float dt) {
    	camera.update(dt);
    	projMatrix.setPerspective((float) Math.toRadians(40.0f), display.getWidth() / display.getHeight(), 0.1f, 5000.0f);
        (viewMatrix.set(camera.rotation)).invert(invViewMatrix);
        viewProjMatrix.set(projMatrix).mul(viewMatrix).invert(invViewProjMatrix);
        frustumIntersection.set(viewProjMatrix);
        display.updateInput(dt);
    }
    
    public void loop() {
    	while (!glfwWindowShouldClose(display.window)) {
    		long thisTime = System.nanoTime();
        	float dt = (thisTime - lastTime) / 1E9f;
        	lastTime = thisTime;
            glfwPollEvents();
            glViewport(0, 0, fbWidth, fbHeight);
            update(dt);
            render();
            glfwSwapBuffers(display.window);
            sync(thisTime);
        }
    }
    
    private void sync(long loopStartTime) {
    	long endTime = loopStartTime + nanoSecsPerFrame;
    	while(System.nanoTime() < endTime) {
    		try {
    			Thread.sleep(1);
    		} catch (InterruptedException e) {
    			//TODO handle this
    		}
    	}
    	
    }
    
    private void createFullScreenQuad() {
        quadVertices = BufferUtils.createByteBuffer(4 * 2 * 6);
        FloatBuffer fv = quadVertices.asFloatBuffer();
        fv.put(-1.0f).put(-1.0f);
        fv.put( 1.0f).put(-1.0f);
        fv.put( 1.0f).put( 1.0f);
        fv.put( 1.0f).put( 1.0f);
        fv.put(-1.0f).put( 1.0f);
        fv.put(-1.0f).put(-1.0f);
    }
    
    private void initTriangle() {
    	final float[] vertices = new float[]{
    			0.0f, 0.5f, 0.0f,
    			-0.5f, -0.5f, 0.0f,
    			0.5f, -0.5f, 0.0f
    		};
    	verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
    	verticesBuffer.put(vertices).flip();
    	vaoId = glGenVertexArrays();
    	glBindVertexArray(vaoId);
    	vboId = glGenBuffers();
    	glBindBuffer(GL_ARRAY_BUFFER, vboId);
    	glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
    	MemoryUtil.memFree(verticesBuffer);
    	glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    	// Unbind the VBO
    	glBindBuffer(GL_ARRAY_BUFFER, 0);
    	// Unbind the VAO
    	glBindVertexArray(0);
    	if (verticesBuffer != null) {
    		MemoryUtil.memFree(verticesBuffer);
    	}
    }
    
    private void drawTriangle() {
    	shaderProgram.bind();
	    // Bind to the VAO
	    glBindVertexArray(vaoId);
	    glEnableVertexAttribArray(0);
	    // Draw the vertices
	    glDrawArrays(GL_TRIANGLES, 0, 3);
	    // Restore state
	    glDisableVertexAttribArray(0);
	    glBindVertexArray(0);
	    shaderProgram.unbind();
    }
    
    private void render() {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        drawTriangle();
    }
	
}
