package shapes;

public class VectorCylinder extends Cylinder {
	/**
	 * Cylinder that takes a start point and an end point and draws
	 * the cylinder between those two points. If the length is less
	 * than the distance between the points, then the cylinder is
	 * drawn with its center at the midpoint between the start and end
	 * points given. If the length is greater than the distance between the
	 * two points, then the cylinder is shrunk to fit between the start and end points.
	 * @param length the length (or height, if the cylinder is vertical) of the cylinder
	 * @param radius the radius of the cylinder
	 * @param sX the starting location's x-coordinate (the bottom of the cylinder)
	 * @param sY the starting location's y-coordinate (the bottom of the cylinder)
	 * @param sZ the starting location's z-coordinate (the bottom of the cylinder)
	 * @param eX the ending location's x-coordinate (the top of the cylinder)
	 * @param eY the ending location's y-coordinate (the top of the cylinder)
	 * @param eZ the ending location's z-coordinate (the top of the cylinder)
	 */
	public VectorCylinder(float length, float radius,
	                      double sX, double sY, double sZ,
	                      double eX, double eY, double eZ) {
		super(length, radius, ((sX + eX) / 2), ((sY + eY) / 2f), ((sZ + eZ) / 2f));
		// anything else???
	}
}
