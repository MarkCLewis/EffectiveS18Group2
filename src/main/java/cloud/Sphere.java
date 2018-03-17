package cloud;

public class Sphere {
    public Sphere (double x, double y, double z, double radius)
    {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    	this.radius = radius;
    }
    
    private final double x;
    private final double y;
    private final double z;
    private final double radius;
    
    public double getX() {
    	return x;
    }
    
    public double getY() {
    	return y;
    }
    
    public double getZ() {
    	return z;
    }
    
    public double getRadius() {
    	return radius;
    }
}
