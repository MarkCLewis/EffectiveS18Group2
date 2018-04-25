package shapes;

public class HeightMapSurface extends Shape {
	
	private int sideLength;
	private int patchSize;
	private float[] heightMap;
	private float scaleX;
	private float scaleY;
	private float scaleZ;
	
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
	public HeightMapSurface(int sideLength, int patchSize, float[] heightMap, double xPos, double yPos, double zPos) {
		super(xPos,yPos,zPos,0,0,0,true); // Height maps are always immobile
		boolean checkSideLengthIsPowTwoPlusOne = (sideLength-1) > 0 && (((sideLength-1) & ((sideLength-1) - 1)) == 0);
		boolean checkPatchSizeIsPowTwoPlusOne = ((patchSize-1) > 0) && (((patchSize-1) & ((patchSize-1) - 1)) == 0);
		if(heightMap.length != sideLength * sideLength) {
			throw new IllegalArgumentException("Height map was not the correct length (height map length was " + heightMap.length + "); height map must have length L^2, where L is the side length.\nLength given was " + sideLength);
		}
		else if(!checkSideLengthIsPowTwoPlusOne) {
			throw new IllegalArgumentException("Side length must be 2^N + 1. (" + sideLength + " given)");
		}
		else if(!checkPatchSizeIsPowTwoPlusOne) {
			throw new IllegalArgumentException("Patch size must be 2^N + 1. (" + patchSize + " given)");
		}
		this.sideLength = sideLength;
		this.patchSize = patchSize;
		this.heightMap = heightMap.clone();
	}
	
	public HeightMapSurface(int sideLength, int patchSize, float[] heightMap, float scaleX, float scaleY, float scaleZ, double xPos, double yPos, double zPos) {
		super(xPos,yPos,zPos,0,0,0,true); // Height maps are always immobile
		boolean checkSideLengthIsPowTwoPlusOne = (sideLength-1) > 0 && (((sideLength-1) & ((sideLength-1) - 1)) == 0);
		boolean checkPatchSizeIsPowTwoPlusOne = ((patchSize-1) > 0) && (((patchSize-1) & ((patchSize-1) - 1)) == 0);
		if(heightMap.length != sideLength * sideLength) {
			throw new IllegalArgumentException("Height map was not the correct length (height map length was " + heightMap.length + "); height map must have length L^2, where L is the side length.\nLength given was " + sideLength);
		}
		else if(!checkSideLengthIsPowTwoPlusOne) {
			throw new IllegalArgumentException("Side length must be 2^N + 1. (" + sideLength + " given)");
		}
		else if(!checkPatchSizeIsPowTwoPlusOne) {
			throw new IllegalArgumentException("Patch size must be 2^N + 1. (" + patchSize + " given)");
		}
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		this.sideLength = sideLength;
		this.patchSize = patchSize;
		this.heightMap = heightMap.clone();
	}
	
	public float getHeightAt(int idx) {
		if(idx >= this.heightMap.length)
		{
			throw new IllegalArgumentException("Index exceeded height map length.");
		}
		return this.heightMap[idx];
	}
	
	public float[] getHeightMap() {
		return this.heightMap.clone();
	}
	
	public int getPatchSize() {
		return this.patchSize;
	}
	
	public int getSideLength() {
		return this.sideLength;
	}
	
	public float getScaleX() {
		return this.scaleX;
	}
	
	public float getScaleY() {
		return this.scaleY;
	}
	
	public float getScaleZ() {
		return this.scaleZ;
	}
}
