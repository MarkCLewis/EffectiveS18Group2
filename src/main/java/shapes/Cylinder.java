package shapes;

public class Cylinder extends Shape {
	private float height;
	private float radius;
	
	public Cylinder(
			float _height, float _radius, 
			double xPos, double yPos, double zPos,
			float xRot, float yRot, float zRot) {
		super(xPos, yPos, zPos, xRot, yRot, zRot);
		this.height = _height;
		this.radius = _radius;
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
