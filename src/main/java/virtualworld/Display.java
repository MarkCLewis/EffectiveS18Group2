package virtualworld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

public class Display {
	/* 
	 * This class is a singleton
	 * No public constructors are provided
	 * 
	 * Access class members by referencing Display.instance
	 */
	
	public static final Display instance = new Display();
	
	private Display() {
    	this.init();
    }
	
	private long window = NULL;
	private long monitor = NULL;
	private boolean windowed = true;
    private int width = 800;
    private int height = 600;
    
    private GLFWVidMode vidMode;
    
    private GLFWWindowSizeCallback wsCallback;
    
    private void init() throws AssertionError{
    	RenderEngine rendEng = RenderEngine.instance;
    	monitor = glfwGetPrimaryMonitor();
        vidMode = glfwGetVideoMode(monitor);
        if (!windowed) {
            width = vidMode.width();
            height = vidMode.height();
            rendEng.setFBWidth(width);
            rendEng.setFBHeight(height);
        }
    	window = glfwCreateWindow(width, height, Game.title, !windowed ? monitor : 0L, NULL);
        if (window == NULL) {
            throw new AssertionError("Failed to create the GLFW window");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SAMPLES, 4);
        
        wsCallback = new GLFWWindowSizeCallback() {
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
    }
    
    public void close() {
    	wsCallback.free();
    	glfwDestroyWindow(window);
    }
    
    public void update(float dt) {
    	RenderEngine.instance.update(dt);
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
    
    public long getWindow() throws AssertionError {
    	return window;
    }
    
    public long getMonitor() throws AssertionError {
    	return monitor;
    }
}
