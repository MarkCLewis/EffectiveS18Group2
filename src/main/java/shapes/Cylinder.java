package shapes;

public class Cylinder extends Shape {
	private float height;
	private float radius;
	
	public Cylinder(
			float height, float radius, 
			double xPos, double yPos, double zPos) {
		super(xPos, yPos, zPos);
		this.height = height;
		this.radius = radius;
	}
	
	public Cylinder(
			float height, float radius, 
			double xPos, double yPos, double zPos,
			boolean isImmobile) {
		super(xPos, yPos, zPos, isImmobile);
		this.height = height;
		this.radius = radius;
	}
	
	public Cylinder(
			float height, float radius, 
			double xPos, double yPos, double zPos,
			float xRot, float yRot, float zRot) {
		super(xPos, yPos, zPos, xRot, yRot, zRot);
		this.height = height;
		this.radius = radius;
	}
	
	public Cylinder(
			float height, float radius, 
			double xPos, double yPos, double zPos,
			float xRot, float yRot, float zRot, 
			boolean isImmobile) {
		super(xPos, yPos, zPos, xRot, yRot, zRot, isImmobile);
		this.height = height;
		this.radius = radius;
	}
	
	public float getHeight() {
		float hcopy = height;
		return hcopy;
	}
	
	public float getRadius() {
		float rcopy = radius;
		return rcopy;
	}
}
