package shapes;

public class RectangularPrism extends Shape {
	private float[] dimensions;
	
	public RectangularPrism(float xSize, float ySize, float zSize, double xPos, double yPos, double zPos) {
		super(xPos, yPos, zPos);
		this.dimensions = new float[] {xSize, ySize, zSize};
	}
	
	public float[] getDimensions() {
		return dimensions.clone();
	}
}
