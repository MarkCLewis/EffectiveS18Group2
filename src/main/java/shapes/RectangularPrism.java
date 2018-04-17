package shapes;

public class RectangularPrism extends Shape {
	private float[] dimensions;
	
	public RectangularPrism(
			float xSize, float ySize, float zSize, 
			double xPos, double yPos, double zPos,
			float xRot, float yRot, float zRot) {
		super(xPos, yPos, zPos, xRot, yRot, zRot);
		this.dimensions = new float[] {xSize, ySize, zSize};
	}
	
	public float[] getDimensions() {
		return dimensions.clone();
	}
}
