package shapes;

/**
 * Shape is an abstract class representing an in-game object.
 * @author kayla
 *
 */
public class Shape {

	/**
	 * Location of shape's center in world coordinates.
	 */
	private double[] center;
	
	/**
	 * Euler rotation vector of shape in radians.<br>
	 * {xRotation, yRotation, zRotation}<br>
	 * Default is {0,0,0}
	 */
	private float[] rotation;
	
	/**
	 * Defines the color, transparency, shininess, 
	 * and other rendering options of this shape.
	 */
	private RenderMaterial mat;
	
	/**
	 * If true, then this object won't be affected by gravity and
	 * will not be pushed out of the way by sheep/the player.<br>
	 * True by default.
	 */
	private boolean isImmobile;

	/**
	 * Shape is an abstract class representing an in-game object.
	 * @param xPos x-coordinate of object's center
	 * @param yPos y-coordinate of object's center
	 * @param zPos z-coordinate of object's center
	 */
	public Shape(double xPos, double yPos, double zPos) {
		this.center = new double[] {xPos, yPos, zPos};
		this.rotation = new float[] {0,0,0};
		this.isImmobile = true;
		this.mat = new RenderMaterial();
	}
	
	/**
	 * Shape is an abstract class representing an in-game object.
	 * @param xPos x-coordinate of object's center
	 * @param yPos y-coordinate of object's center
	 * @param zPos z-coordinate of object's center
	 * @param isImmobile if true, object will not move but mobile objects will collide with it. 
	 * (For example, a tree in GTA is immobile)
	 */
	public Shape(
			double xPos, double yPos, double zPos, 
			boolean isImmobile) {
		this.center = new double[] {xPos, yPos, zPos};
		this.rotation = new float[] {0,0,0};
		this.isImmobile = isImmobile;
		this.mat = new RenderMaterial();
	}
	
	/**
	 * Shape is an abstract class representing an in-game object.
	 * @param xPos x-coordinate of object's center
	 * @param yPos y-coordinate of object's center
	 * @param zPos z-coordinate of object's center
	 * @param xRot Rotation about the x-axis in radians
	 * @param yRot Rotation about the y-axis in radians
	 * @param zRot Rotation about the z-axis in radians
	 */
	public Shape(
			double xPos, double yPos, double zPos, 
			float xRot, float yRot, float zRot) {
		this.center = new double[] {xPos, yPos, zPos};
		this.rotation = new float[] {xRot, yRot, zRot};
		this.isImmobile = true;
		this.mat = new RenderMaterial();
	}
	
	/**
	 * Shape is an abstract class representing an in-game object.
	 * @param xPos x-coordinate of object's center
	 * @param yPos y-coordinate of object's center
	 * @param zPos z-coordinate of object's center
	 * @param xRot Rotation about the x-axis in radians
	 * @param yRot Rotation about the y-axis in radians
	 * @param zRot Rotation about the z-axis in radians
	 * @param isImmobile if true, object will not move but mobile objects will collide with it. 
	 * (For example, a tree in GTA is immobile)
	 */
	public Shape(
			double xPos, double yPos, double zPos, 
			float xRot, float yRot, float zRot, 
			boolean isImmobile) {
		this.center = new double[] {xPos, yPos, zPos};
		this.rotation = new float[] {xRot, yRot, zRot};
		this.isImmobile = isImmobile;
		this.mat = new RenderMaterial();
	}

	public double[] getCenter() {
		return new double[]  {center[0],center[1],center[2]};
	}
	
	public float[] getRotation() {
		return new float[] {rotation[0],rotation[1],rotation[2]};
	}
	
	public double getXPos() {
		return this.center[0];
	}
	
	public double getYPos() {
		return this.center[1];
	}
	
	public double getZPos() {
		return this.center[2];
	}
	
	public float getXRot() {
		return this.rotation[0];
	}
	
	public float getYRot() {
		return this.rotation[1];
	}
	
	public float getZRot() {
		return this.rotation[2];
	}
	
	public RenderMaterial getMaterial() {
		return this.mat;
	}
	
	public boolean isImmobile() {
		return this.isImmobile;
	}
	
	/**
	 * Set the Shape's rotation. <br>
	 * If you change the shape's rotation after adding the shape to the game, 
	 * then this will have no effect.
	 * @param xRot Rotation about the x-axis in radians
	 * @param yRot Rotation about the y-axis in radians
	 * @param zRot Rotation about the z-axis in radians
	 */
	public void setRotation(float xRot, float yRot, float zRot) {
		rotation[0] = xRot;
		rotation[1] = yRot;
		rotation[2] = zRot;
	}
	
	/**
	 * Set the Shape's position relative to the world origin.<br>
	 * If you change the shape's position after adding the shape to the game,
	 * then this will have no effect.
	 * @param xPos x-coordinate of object's center
	 * @param yPos y-coordinate of object's center
	 * @param zPos z-coordinate of object's center
	 */
	public void setPosition(double xPos, double yPos, double zPos) {
		center[0] = xPos;
		center[1] = yPos;
		center[2] = zPos;
	}
	
	/**
	 * Set the immobility of the Shape.
	 * @param isImmobile if true, object will not move but mobile objects will collide with it. 
	 * (For example, a tree in GTA is immobile)
	 */
	public void setIsImmobile(boolean isImmobile) {
		this.isImmobile = isImmobile;
	}
	
	public void setMaterial(RenderMaterial mat) {
		this.mat = mat;
	}
	
	public void setMaterialColor(RenderColor col) {
		this.mat.setDiffuseColor(col);
	}

}
