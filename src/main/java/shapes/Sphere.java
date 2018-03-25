package shapes;

public class Sphere extends Shape {
	private float radius;
	
	public Sphere(float _radius, double xPos, double yPos, double zPos) {
		super(xPos, yPos, zPos);
		this.radius = _radius;
	}
	
	public float getRadius() {
		float rcopy = radius;
		return rcopy;
	}
}
