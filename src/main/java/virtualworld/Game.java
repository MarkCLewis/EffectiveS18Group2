package virtualworld;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4d;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import javafx.scene.shape.Mesh;
import javafx.scene.shape.TriangleMesh;

import static org.lwjgl.opengl.ARBSeamlessCubeMap.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {
	/* 
	 * This class is a singleton
	 * No public constructors are provided
	 * 
	 * Access class members by referencing Game.instance
	 */

	public static final String title = "VirtualWorld";
	/* Singleton instance is null initially */
	public static final Game instance = new Game();
	
	private Game() {
		try {
			this.init();
    	} catch (IOException e) {
    		System.out.println(e.toString());
    	}
    }

	private long lastTime = System.nanoTime();

    
    private static float maxParticleLifetime = 1.0f;
    private static float particleSize = 1.0f;
    private static final int maxParticles = 4096;

    private int cubemapProgram;
    private int cubemap_invViewProjUniform;

    private int particleProgram;
    private int particle_projUniform;

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

	/* --------
	 * Private Methods
	 * --------
	 */

    private void init() throws IOException, IllegalStateException {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        /* Create all needed GL resources */
        createCubemapTexture();
        createFullScreenQuad();
        createCubemapProgram();
        createParticleProgram();
        createSphere();
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
        if (RenderEngine.instance.getCaps().OpenGL32 || Display.RenderEngine.instance.getCaps().GL_ARB_seamless_cube_map) {
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

    private void update() {
        long thisTime = System.nanoTime();
        float dt = (thisTime - lastTime) / 1E9f;
        lastTime = thisTime;
        updateParticles(dt);
        updateControls(dt);
    }

    private void updateControls(float dt) {
        Input.instance.update(dt);
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
                float x = (float) (particlePosition.x - cam.position.x);
                float y = (float) (particlePosition.y - cam.position.y);
                float z = (float) (particlePosition.z - cam.position.z);
                if (frustumIntersection.testPoint(x, y, z)) {
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

    private void render() {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        drawCubemap();
        drawParticles();
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glViewport(0, 0, fbWidth, fbHeight);
            update();
            render();
            glfwSwapBuffers(window);
        }
    }

    private void run() {
        try {
            init();
            loop();

            if (debugProc != null)
                debugProc.free();

            keyCallback.free();
            cpCallback.free();
            mbCallback.free();
            fbCallback.free();
            wsCallback.free();
            glfwDestroyWindow(window);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            glfwTerminate();
        }
    }

}
