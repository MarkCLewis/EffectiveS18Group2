package shapes;

public class Sphere extends Shape {
	private float radius;
	
	public Sphere(
			float _radius, 
			double xPos, double yPos, double zPos) {
		super(xPos, yPos, zPos);
		this.radius = _radius;
	}
	
	public Sphere(
			float _radius, 
			double xPos, double yPos, double zPos, 
			boolean isImmobile) {
		super(xPos, yPos, zPos, isImmobile);
		this.radius = _radius;
	}
	
	public float getRadius() {
		float rcopy = radius;
		return rcopy;
	}
}
