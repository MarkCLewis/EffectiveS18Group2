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
	 */

	public static final String title = "VirtualWorld";
	/* Singleton instance is null initially */
	private static final Game INSTANCE = new Game();
	
	public static Game getInstance() {
		return Game.INSTANCE;
	}
	
	private Game() {
		this.init();
    }

    private void init() {
    	/* Initialize the RenderEngine implicitly */
        RenderEngine.getInstance();
    }
    
    private void run() {
        RenderEngine.getInstance().run();
    }

}
