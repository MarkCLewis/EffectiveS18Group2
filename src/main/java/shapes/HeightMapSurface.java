package shapes;

public class HeightMapSurface extends Shape {
	
	private float sideLength;
	private int pointsPerSide;
	private float[] heightMap;
	
	/**
	 * Construct a new Height Map Surface.
	 * @param sideLength the length of one side of the surface (in world units)
	 * @param patchSize the size of one "patch" section in the surface (in world units)
	 * @param heightMap the heights of the surface, in world units<br>
	 * Example:<br>
	 * <pre>   {  A, B, C,
	 *      D, E, F,
	 *      G, H, I }</pre>
	 * In this example, pointsPerSide = 3<br>
	 * Each capital letter represents some float height value.<br>
	 * The first "patch" of the surface (patches are squares)
	 * would be...
	 * <pre> A -- B
	 * |    |
	 * D -- E</pre>
	 * the second would be...<br>
	 * <pre> B -- C
	 * |    |
	 * E -- F</pre>
	 * and so on.
	 * @param xPos world x coordinate
	 * @param yPos world y coordinate
	 * @param zPos world z coordinate
	 */
	public HeightMapSurface(float sideLength, int pointsPerSide, float[] heightMap, double xPos, double yPos, double zPos) {
		super(xPos,yPos,zPos,0,0,0,true); // Height maps are always immobile
		if(heightMap.length != pointsPerSide*pointsPerSide) {
			throw new IllegalArgumentException("Height map was not the correct length; height map must have length p^2, where p is the number of points per side.");
		}
		this.sideLength = sideLength;
		this.pointsPerSide = pointsPerSide;
		this.heightMap = heightMap.clone();
	}
	
	public float[] getHeightMap() {
		return this.heightMap.clone();
	}
	
	public float getPatchSize() {
		return (this.sideLength / (pointsPerSide-1));
	}
	
	public int getPointsPerSide() {
		return this.pointsPerSide;
	}
	
	public float getSideLength() {
		return this.sideLength;
	}
}
