package shapes;

public class Quad extends Shape {
	/**
	 * size of the sides of the Quad
	 * (It's a square)
	 */
	private float size;
	/**
	 * Corner heights of the quad.
	 * Index 0 is the top left corner
	 * Index 1 is the top right corner
	 * Index 2 is the bottom right corner
	 * Index 3 is the bottom left corner
	 */
	private float[] cornerHeights;
	
	/**
	 * Construct a new Quad shape
	 * @param size the side length (it's a square)
	 * @param cornerHeights the heights of the four corners of the quad.
	 * The array must only be four elements long (runtime exception is thrown if not).
	 * Index 0: top left,
	 * Index 1: top right,
	 * Index 2: bottom right,
	 * Index 3: bottom left.
	 * @param xPos world x coordinate
	 * @param yPos world y coordinate
	 * @param zPos world z coordinate
	 */
	public Quad(float size, float[] cornerHeights, double xPos, double yPos, double zPos) {
		super(xPos,yPos,zPos);
		this.size = size;
		this.cornerHeights = cornerHeights.clone();
	}
	
	public float[] getCornerHeights() {
		return this.cornerHeights.clone();
	}
	
	public float getSize() {
		return this.size;
	}
}
