package virtualworld.terrain;

public class Region {
	

	public static void main(String[] args) {
		
		double size = 2084;
		
		TerrainHeightAlgorithm fieldHeight = NormalHeightAlgorithm.forFields(2084);
		TerrainHeightAlgorithm mountainHeight = ValleyHeightAlgorithm.forNormalValley(2084);
		TerrainHeightAlgorithm hillHeight = NormalHeightAlgorithm.forHills(2084);
		
		Point topLeft = new Point(-1024, 1024);
		Point bottomRight = new Point(1024, -1024);
		Point center = new Point(0,0);
		
		double randomPoint = 437;
		double randomPoint2 = -361;
		
		Point line1Top = new Point(randomPoint, center.getZ() + (size/2));
		Point line1Bottom = new Point(randomPoint, center.getZ() - (size/2));
		
		Point line2Top = new Point(randomPoint, randomPoint2);
		Point line2Bottom = new Point(center.getX() + (size/2), randomPoint2);
		
		Region m = new Region(topLeft, line1Bottom, mountainHeight);
		Region h = new Region(line1Top, line2Bottom, hillHeight);
	    Region f = new Region(line2Top, bottomRight , fieldHeight);
	    
	    //fields
	    System.out.println("FIELD");
	    System.out.println("fields should contain:" + f.containsPoint(537, -380));
	    System.out.println("mountains should not contain:" + m.containsPoint(537, -380));
	    System.out.println("hills should not contain:" + h.containsPoint(537, -380));
	    
	    //hills
	    System.out.println("HILL");
	    System.out.println("hills should contain:" + h.containsPoint(537, 380));
	    System.out.println("fields should not contain:" + f.containsPoint(537, 380));
	    System.out.println("mountains should not contain:" + m.containsPoint(537, 380));
	    
	    //Mountains
	    System.out.println("MOUNTAINS");
	    System.out.println("mountains should contain:" + m.containsPoint(-100, 100));
	    System.out.println("hills should not contain:" + h.containsPoint(-100, 100));
	    System.out.println("fields should not contain:" + f.containsPoint(-100, 100));
	}
	
	
	private Point topLeft;
	private Point bottomRight;
	public TerrainHeightAlgorithm noise;
	
	
	
	Region(Point tl, Point br, TerrainHeightAlgorithm ta) {
		topLeft = tl;
		bottomRight = br;
		noise = ta;
	}
	
	public boolean containsPoint(double x, double z) {
		if ((x >= topLeft.getX() && x <= bottomRight.getX()) && ( z >= bottomRight.getZ() && z <= topLeft.getZ() )) {
			return true;
		} else {
			return false;
		}
	}
	
	public Point getTopLeft() {
		return topLeft;
	}
	public Point getBottomRight() {
		return bottomRight;
	}
	
	public double getWidth() {
		return bottomRight.getX() - topLeft.getX(); 
		//return topLeft.getX() - bottomRight.getX(); 
	}
	
	public Point getCenter() {
		double x = ((topLeft.getX() + bottomRight.getX()) / 2);
		double z = ((topLeft.getZ() + bottomRight.getZ()) / 2);
		return new Point(x, z);
	}

	public double getHeight() {
		//return bottomRight.getZ() - topLeft.getZ(); 
		return topLeft.getZ() - bottomRight.getZ();
	}
}
