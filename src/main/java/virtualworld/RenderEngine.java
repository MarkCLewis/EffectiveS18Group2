package virtualworld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Callback;

public class RenderEngine {
	/* Controls frame buffers and rendering calculations for display */
	public static final RenderEngine instance = new RenderEngine();
	
	private RenderEngine() {
		this.init();
	}
	
	private Camera activeCam;
	
	private int fbWidth = 800;
    private int fbHeight = 600;
    
    private IntBuffer frameBufferSize;
    private GLCapabilities caps;
    private Callback debugProc;
    
    private Vector3d tmp;
    private Vector3d newPosition;
    private Vector3f tmp2;
    private Vector3f tmp3;
    private Vector3f tmp4;
    private Matrix4f projMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f modelMatrix;
    private Matrix4f viewProjMatrix;
    private Matrix4f invViewMatrix;
    private Matrix4f invViewProjMatrix;
    private FloatBuffer matrixBuffer;
    private FrustumIntersection frustumIntersection;
    
    GLFWFramebufferSizeCallback fbCallback;
    
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
    
    private void init() throws AssertionError {
    	Display d = Display.instance;
    	
    	activeCam = new Camera();
    	
    	frameBufferSize = BufferUtils.createIntBuffer(2);
        nglfwGetFramebufferSize(d.getWindow(), memAddress(frameBufferSize), memAddress(frameBufferSize) + 4);
        fbWidth = frameBufferSize.get(0);
        fbHeight = frameBufferSize.get(1);
        
        caps = GL.createCapabilities();
        if (!caps.OpenGL20) {
            throw new AssertionError("This demo requires OpenGL 2.0.");
        }
        
        tmp = new Vector3d();
	    newPosition = new Vector3d();
	    tmp2 = new Vector3f();
	    tmp3 = new Vector3f();
	    tmp4 = new Vector3f();
	    projMatrix = new Matrix4f();
	    viewMatrix = new Matrix4f();
	    modelMatrix = new Matrix4f();
	    viewProjMatrix = new Matrix4f();
	    invViewMatrix = new Matrix4f();
	    invViewProjMatrix = new Matrix4f();
	    matrixBuffer = BufferUtils.createFloatBuffer(16);
	    frustumIntersection = new FrustumIntersection();
	    
        debugProc = GLUtil.setupDebugMessageCallback();
        
        fbCallback = new GLFWFramebufferSizeCallback() {
	        @Override
			public void invoke(long window, int width, int height) {
	            if (width > 0 && height > 0 && (fbWidth != width || fbHeight != height)) {
	                fbWidth = width;
	                fbHeight = height;
	            }
	        }
	    };
        glfwSetFramebufferSizeCallback(d.getWindow(), fbCallback);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
    }
    
    public void close() {
    	if(debugProc != null) {
    		debugProc.free();
    	}
    	fbCallback.free();
    }
    
    public void update(float dt) {
    	activeCam.update(dt);
    	projMatrix.setPerspective((float) Math.toRadians(40.0f), (float) (Display.instance.getWidth() / Display.instance.getHeight()), 0.1f, 5000.0f);
        (viewMatrix.set(activeCam.rotation)).invert(invViewMatrix);
        viewProjMatrix.set(projMatrix).mul(viewMatrix).invert(invViewProjMatrix);
        frustumIntersection.set(viewProjMatrix);
        /* Update the background shader */
        glUseProgram(Game.instance.getCubemapProgram());
        glUniformMatrix4fv(Game.instance.getCubemapInvViewProjUniform(), false, invViewProjMatrix.get(matrixBuffer));

        /* Update the particle shader */
        glUseProgram(Game.instance.getParticleProgram());
        glUniformMatrix4fv(Game.instance.getParticleProjUniform(), false, matrixBuffer);
    }
    
    public Camera getCamera() {
    	return activeCam;
    }
    
    public void stabilizeCamera() {
    	activeCam.linearAcc.zero();
    }
	
}
