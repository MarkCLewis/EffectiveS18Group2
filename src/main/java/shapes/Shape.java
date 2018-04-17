package shapes;

public class Shape {
	/**
	 * Location of shape's center in world coordinates.
	 */
	private final double[] center = new double[3];
	/**
	 * Euler rotation vector of shape in radians.
	 * {xRotation, yRotation, zRotation}
	 */
	private final float[] rotation = new float[3];
	
	public Shape(double xPos, double yPos, double zPos, float xRot, float yRot, float zRot) {
		this.center[0] = xPos;
		this.center[1] = yPos;
		this.center[2] = xPos;
		this.rotation[0] = xRot;
		this.rotation[1] = yRot;
		this.rotation[2] = zRot;
	}
	
	public double[] getCenter() {
		return center.clone();
	}
	
	public float[] getRotation() {
		return rotation.clone();
	}
	
	public double getXPos() {
		return center[0];
	}
	
	public double getYPos() {
		return center[1];
	}
	
	public double getZPos() {
		return center[2];
	}
	
	public float getXRot() {
		return rotation[0];
	}
	
	public float getYRot() {
		return rotation[1];
	}
	
	public float getZRot() {
		return rotation[2];
	}
}
