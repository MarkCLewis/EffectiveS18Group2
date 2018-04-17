package shapes;

public class Shape {
	/**
	 * Location of shape's center in world coordinates.
	 */
	private double[] center;
	/**
	 * Euler rotation vector of shape in radians.
	 * {xRotation, yRotation, zRotation}
	 */
	private float[] rotation;
	
	public Shape(double xPos, double yPos, double zPos, float xRot, float yRot, float zRot) {
		this.center = new double[] {xPos, yPos, zPos};
		this.rotation = new float[] {xRot, yRot, zRot};
	}
	
	public double[] getCenter() {
		return new double[]  {center[0],center[1],center[2]};
	}
	
	public float[] getRotation() {
		return new float[] {rotation[0],rotation[1],rotation[2]};
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
