package shapes;

public class Shape {
	private double[] center;
	
	public Shape(double xPos, double yPos, double zPos) {
		this.center = new double[] {xPos, yPos, zPos};
	}
	
	public double[] getCenter() {
		return center.clone();
	}
}
