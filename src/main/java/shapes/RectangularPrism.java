package shapes;

public class RectangularPrism extends Shape {
	private float[] dimensions;
	
	public RectangularPrism(
			float xSize, float ySize, float zSize, 
			double xPos, double yPos, double zPos) {
		super(xPos, yPos, zPos);
		this.dimensions = new float[] {xSize, ySize, zSize};
	}
	
	public RectangularPrism(
			float xSize, float ySize, float zSize, 
			double xPos, double yPos, double zPos,
			boolean isImmobile) {
		super(xPos, yPos, zPos, isImmobile);
		this.dimensions = new float[] {xSize, ySize, zSize};
	}
	
	public RectangularPrism(
			float xSize, float ySize, float zSize, 
			double xPos, double yPos, double zPos,
			float xRot, float yRot, float zRot) {
		super(xPos, yPos, zPos, xRot, yRot, zRot);
		this.dimensions = new float[] {xSize, ySize, zSize};
	}
	
	public RectangularPrism(
			float xSize, float ySize, float zSize, 
			double xPos, double yPos, double zPos,
			float xRot, float yRot, float zRot,
			boolean isImmobile) {
		super(xPos, yPos, zPos, xRot, yRot, zRot, isImmobile);
		this.dimensions = new float[] {xSize, ySize, zSize};
	}
	
	public RectangularPrism(
			float xSize, float ySize, float zSize, 
			double xPos, double yPos, double zPos,
			float xRot, float yRot, float zRot,
			boolean isImmobile, PivotLocation pivot) {
		super(xPos, yPos, zPos, xRot, yRot, zRot, isImmobile, pivot);
		this.dimensions = new float[] {xSize, ySize, zSize};
	}
	
	public float[] getDimensions() {
		return dimensions.clone();
	}
}
