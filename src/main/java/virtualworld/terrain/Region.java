package virtualworld.terrain;

public class Region {
	private Point topLeft;
	private Point bottomRight;
	public TerrainHeightAlgorithm noise;
	
	Region(Point tl, Point br, TerrainHeightAlgorithm ta) {
		topLeft = tl;
		bottomRight = br;
		noise = ta;
	}
	
	public boolean containsPoint(double x, double z) {
		if ((x >= topLeft.getX() && x <= bottomRight.getX()) && (z <= topLeft.getZ() && z >= bottomRight.getZ())) {
			return true;
		} else {
			return false;
		}
	}
	
	public Point getTopLeft() {
		return topLeft;
	}
	
	public double getWidth() {
		return bottomRight.getX() - topLeft.getX(); 
	}

	public double getHeight() {
		return topLeft.getZ() - bottomRight.getZ();
	}
}
