package shapes;

public class VectorCylinder extends Shape{
	private double[] startPos = new double[3];
	private double[] endPos = new double[3];
	private float radius;
	/**
	 * Cylinder that takes a start point and an end point and draws
	 * the cylinder between those two points.
	 * @param radius the radius of the cylinder
	 * @param sX the starting location's x-coordinate (the bottom of the cylinder)
	 * @param sY the starting location's y-coordinate (the bottom of the cylinder)
	 * @param sZ the starting location's z-coordinate (the bottom of the cylinder)
	 * @param eX the ending location's x-coordinate (the top of the cylinder)
	 * @param eY the ending location's y-coordinate (the top of the cylinder)
	 * @param eZ the ending location's z-coordinate (the top of the cylinder)
	 */
	public VectorCylinder(float radius,
	                      double sX, double sY, double sZ,
	                      double eX, double eY, double eZ) {
		super((sX + eX)/2f,(sY + eY)/2f,(sZ + eZ)/2f);
		this.radius = radius;
		this.startPos[0] = sX;
		this.startPos[1] = sY;
		this.startPos[2] = sZ;
		this.endPos[0] = eX;
		this.endPos[1] = eY;
		this.endPos[2] = eZ;
		
	}
	
	public double[] getStartPos() {
		return this.startPos.clone();
	}
	
	public double[] getEndPos() {
		return this.endPos.clone();
	}
	
	public float getRadius() {
		return this.radius;
	}
}
