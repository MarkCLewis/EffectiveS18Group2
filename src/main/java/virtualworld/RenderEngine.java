package virtualworld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBSeamlessCubeMap.GL_TEXTURE_CUBE_MAP_SEAMLESS;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAddress;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.FrustumIntersection;
import org.joml.GeometryUtils;
import org.joml.Intersectiond;
import org.joml.Intersectionf;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4d;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Callback;

import javafx.scene.shape.TriangleMesh;

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
    
    private int cubemapProgram;
    private int cubemap_invViewProjUniform;

    private int particleProgram;
    private int particle_projUniform;

    private static float maxParticleLifetime = 1.0f;
    private static float particleSize = 1.0f;
    private static final int maxParticles = 4096;
    private ByteBuffer quadVertices;
    private TriangleMesh sphere;
    private Vector3d[] particlePositions = new Vector3d[maxParticles];
    private Vector4d[] particleVelocities = new Vector4d[maxParticles];
    {
        for (int i = 0; i < particlePositions.length; i++) {
            Vector3d particlePosition = new Vector3d();
            particlePositions[i] = particlePosition;
            Vector4d particleVelocity = new Vector4d();
            particleVelocities[i] = particleVelocity;
        }
    }
    private FloatBuffer particleVertices = BufferUtils.createFloatBuffer(6 * 6 * maxParticles);
    private ByteBuffer charBuffer = BufferUtils.createByteBuffer(16 * 270);
    
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
    
    private void drawCubemap() {
        glUseProgram(cubemapProgram);
        glVertexPointer(2, GL_FLOAT, 0, quadVertices);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    private void drawParticles() {
        particleVertices.clear();
        int num = 0;
        for (int i = 0; i < particlePositions.length; i++) {
            Vector3d particlePosition = particlePositions[i];
            Vector4d particleVelocity = particleVelocities[i];
            if (particleVelocity.w > 0.0f) {
                float x = (float) (particlePosition.x - RenderEngine.instance.getCamera().position.x);
                float y = (float) (particlePosition.y - RenderEngine.instance.getCamera().position.y);
                float z = (float) (particlePosition.z - RenderEngine.instance.getCamera().position.z);
                if (RenderEngine.instance.getFrustumIntersection().testPoint(x, y, z)) {
                    float w = (float) particleVelocity.w;
                    viewMatrix.transformPosition(tmp2.set(x, y, z));
                    particleVertices.put(tmp2.x - particleSize).put(tmp2.y - particleSize).put(tmp2.z).put(w).put(-1).put(-1);
                    particleVertices.put(tmp2.x + particleSize).put(tmp2.y - particleSize).put(tmp2.z).put(w).put( 1).put(-1);
                    particleVertices.put(tmp2.x + particleSize).put(tmp2.y + particleSize).put(tmp2.z).put(w).put( 1).put( 1);
                    particleVertices.put(tmp2.x + particleSize).put(tmp2.y + particleSize).put(tmp2.z).put(w).put( 1).put( 1);
                    particleVertices.put(tmp2.x - particleSize).put(tmp2.y + particleSize).put(tmp2.z).put(w).put(-1).put( 1);
                    particleVertices.put(tmp2.x - particleSize).put(tmp2.y - particleSize).put(tmp2.z).put(w).put(-1).put(-1);
                    num++;
                }
            }
        }
        particleVertices.flip();
        if (num > 0) {
            glUseProgram(particleProgram);
            glDepthMask(false);
            glEnable(GL_BLEND);
            glVertexPointer(4, GL_FLOAT, 6*4, particleVertices);
            particleVertices.position(4);
            glTexCoordPointer(2, GL_FLOAT, 6*4, particleVertices);
            particleVertices.position(0);
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            glDrawArrays(GL_TRIANGLES, 0, num * 6);
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
            glDisable(GL_BLEND);
            glDepthMask(true);
        }
    }

    private boolean narrowphase(FloatBuffer data, double x, double y, double z, float scale, Vector3d pOld, Vector3d pNew, Vector3d intersectionPoint, Vector3f normal) {
        tmp2.set(tmp.set(pOld).sub(x, y, z)).div(scale);
        tmp3.set(tmp.set(pNew).sub(x, y, z)).div(scale);
        data.clear();
        boolean intersects = false;
        while (data.hasRemaining() && !intersects) {
            float v0X = data.get();
            float v0Y = data.get();
            float v0Z = data.get();
            float v1X = data.get();
            float v1Y = data.get();
            float v1Z = data.get();
            float v2X = data.get();
            float v2Y = data.get();
            float v2Z = data.get();
            if (Intersectionf.intersectLineSegmentTriangle(tmp2.x, tmp2.y, tmp2.z, tmp3.x, tmp3.y, tmp3.z, v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z, 1E-6f, tmp2)) {
                intersectionPoint.x = tmp2.x * scale + x;
                intersectionPoint.y = tmp2.y * scale + y;
                intersectionPoint.z = tmp2.z * scale + z;
                GeometryUtils.normal(v0X, v0Y, v0Z, v1X, v1Y, v1Z, v2X, v2Y, v2Z, normal);
                intersects = true;
            }
        }
        data.clear();
        return intersects;
    }

    private static boolean broadphase(double x, double y, double z, float boundingRadius, float scale, Vector3d pOld, Vector3d pNew) {
        return Intersectiond.testLineSegmentSphere(pOld.x, pOld.y, pOld.z, pNew.x, pNew.y, pNew.z, x, y, z, boundingRadius * boundingRadius * scale * scale);
    }

    private void updateParticles(float dt) {
        for (int i = 0; i < particlePositions.length; i++) {
            Vector4d particleVelocity = particleVelocities[i];
            if (particleVelocity.w <= 0.0f)
                continue;
            particleVelocity.w += dt;
            Vector3d particlePosition = particlePositions[i];
            newPosition.set(particleVelocity.x, particleVelocity.y, particleVelocity.z).mul(dt).add(particlePosition);
            if (particleVelocity.w > maxParticleLifetime) {
                particleVelocity.w = 0.0f;
                continue;
            }
            particlePosition.set(newPosition);
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
    
    private void createSphere() throws IOException {
        sphere = new TriangleMesh();
        // TODO: set up sphere mesh
    }
    
    private static int createShader(String resource, int type) throws IOException {
        int shader = glCreateShader(type);
        ByteBuffer source = Utils.ioResourceToByteBuffer(resource, 1024);
        PointerBuffer strings = BufferUtils.createPointerBuffer(1);
        IntBuffer lengths = BufferUtils.createIntBuffer(1);
        strings.put(0, source);
        lengths.put(0, source.remaining());
        glShaderSource(shader, strings, lengths);
        glCompileShader(shader);
        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);
        String shaderLog = glGetShaderInfoLog(shader);
        if (shaderLog != null && shaderLog.trim().length() > 0) {
            System.err.println(shaderLog);
        }
        if (compiled == 0) {
            throw new AssertionError("Could not compile shader");
        }
        return shader;
    }

    private static int createProgram(int vshader, int fshader) {
        int program = glCreateProgram();
        glAttachShader(program, vshader);
        glAttachShader(program, fshader);
        glLinkProgram(program);
        int linked = glGetProgrami(program, GL_LINK_STATUS);
        String programLog = glGetProgramInfoLog(program);
        if (programLog != null && programLog.trim().length() > 0) {
            System.err.println(programLog);
        }
        if (linked == 0) {
            throw new AssertionError("Could not link program");
        }
        return program;
    }

    private void createCubemapProgram() throws IOException {
        int vshader = createShader("org/lwjgl/demo/game/cubemap.vs", GL_VERTEX_SHADER);
        int fshader = createShader("org/lwjgl/demo/game/cubemap.fs", GL_FRAGMENT_SHADER);
        int program = createProgram(vshader, fshader);
        glUseProgram(program);
        int texLocation = glGetUniformLocation(program, "tex");
        glUniform1i(texLocation, 0);
        cubemap_invViewProjUniform = glGetUniformLocation(program, "invViewProj");
        glUseProgram(0);
        cubemapProgram = program;
    }

    private void createParticleProgram() throws IOException {
        int vshader = createShader("org/lwjgl/demo/game/particle.vs", GL_VERTEX_SHADER);
        int fshader = createShader("org/lwjgl/demo/game/particle.fs", GL_FRAGMENT_SHADER);
        int program = createProgram(vshader, fshader);
        glUseProgram(program);
        particle_projUniform = glGetUniformLocation(program, "proj");
        glUseProgram(0);
        particleProgram = program;
    }

    private void createCubemapTexture() throws IOException {
        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, tex);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        ByteBuffer imageBuffer;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        String[] names = { "right", "left", "top", "bottom", "front", "back" };
        ByteBuffer image;
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_GENERATE_MIPMAP, GL_TRUE);
        for (int i = 0; i < 6; i++) {
            imageBuffer = Utils.ioResourceToByteBuffer("org/lwjgl/demo/space_" + names[i] + (i + 1) + ".jpg", 8 * 1024);
            if (!stbi_info_from_memory(imageBuffer, w, h, comp))
                throw new IOException("Failed to read image information: " + stbi_failure_reason());
            image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (image == null)
                throw new IOException("Failed to load image: " + stbi_failure_reason());
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB8, w.get(0), h.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            stbi_image_free(image);
        }
        if (RenderEngine.instance.getCaps().OpenGL32 || RenderEngine.instance.getCaps().GL_ARB_seamless_cube_map) {
            glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
        }
    }
    
    public int getCubemapProgram() {
    	return cubemapProgram;
    }
    
    public int getParticleProgram() {
    	return particleProgram;
    }
    
    public int getCubemapInvViewProjUniform() {
    	return cubemap_invViewProjUniform;
    }
    
    public int getParticleProjUniform() {
    	return particle_projUniform;
    }

    private void render() {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        drawCubemap();
        drawParticles();
    }
	
}
