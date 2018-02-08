package virtualworld;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
	/* 
	 * This class is a singleton
	 * No public constructors are provided
	 * 
	 * Access class members by referencing Input.instance
	 */
	
	public static final Input instance = new Input();
	
	private Input() {
		this.init();
	}
	
	private boolean[] keyDown = new boolean[GLFW.GLFW_KEY_LAST];
    private boolean leftMouseDown = false;
    private boolean rightMouseDown = false;
    private float mouseX = 0.0f;
    private float mouseY = 0.0f;
	
	private GLFWKeyCallback keyCallback;
    
    private GLFWCursorPosCallback cpCallback;
    
    private GLFWMouseButtonCallback mbCallback;
    
	private void init() {
		System.out.println("Press W/S to move forward/backward");
        System.out.println("Press L.Ctrl/Spacebar to move down/up");
        System.out.println("Press A/D to strafe left/right");
        System.out.println("Press Q/E to roll left/right");
        System.out.println("Hold the left mouse button to shoot");
        System.out.println("Hold the right mouse button to rotate towards the mouse cursor");
        Display d = Display.instance;
        keyCallback = new GLFWKeyCallback() {
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
            public void invoke(long window, double xpos, double ypos) {
            	Display d = Display.instance;
                float normX = (float) ((xpos - d.getWidth()/2.0) / d.getWidth() * 2.0);
                float normY = (float) ((ypos - d.getHeight()/2.0) / d.getHeight() * 2.0);
                mouseX = Math.max(-d.getWidth()/2.0f, Math.min(d.getWidth()/2.0f, normX));
                mouseY = Math.max(-d.getHeight()/2.0f, Math.min(d.getHeight()/2.0f, normY));
            }
        };
        mbCallback = new GLFWMouseButtonCallback() {
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
        glfwSetKeyCallback(d.getWindow(), keyCallback);
        glfwSetCursorPosCallback(d.getWindow(), cpCallback);
        glfwSetMouseButtonCallback(d.getWindow(), mbCallback);
	}
	
	private void close() {
		keyCallback.free();
		cpCallback.free();
		mbCallback.free();
	}
	
	public void update(float dt) {
		RenderEngine.instance.stabilizeCamera();
		float rotZ = 0.0f;
        if (keyDown[GLFW_KEY_W])
            // TODO: move forward
        if (keyDown[GLFW_KEY_S])
            // TODO: move backward
        if (keyDown[GLFW_KEY_D])
        	// TODO: move right
            // example: cam.linearAcc.fma(straveThrusterAccFactor, cam.right(tmp2));
        if (keyDown[GLFW_KEY_A])
        	// TODO: move left
            // example: cam.linearAcc.fma(-straveThrusterAccFactor, cam.right(tmp2));
        if (keyDown[GLFW_KEY_Q])
            rotZ = -1.0f;
        if (keyDown[GLFW_KEY_E])
            rotZ = +1.0f;
        if (keyDown[GLFW_KEY_SPACE])
            // TODO: jump?
        if (keyDown[GLFW_KEY_LEFT_CONTROL])
            // TODO
        if (rightMouseDown)
            RenderEngine.instance.getCamera().angularAcc.set(2.0f*mouseY*mouseY*mouseY, 2.0f*mouseX*mouseX*mouseX, rotZ);
        else if (!rightMouseDown)
            RenderEngine.instance.getCamera().angularAcc.set(0, 0, rotZ);
        double linearVelAbs = RenderEngine.instance.getCamera().linearVel.length();
        if (linearVelAbs > Camera.getMaxLinearVelocity())
            RenderEngine.instance.getCamera().linearVel.normalize().mul(maxLinearVel);
	}
}
