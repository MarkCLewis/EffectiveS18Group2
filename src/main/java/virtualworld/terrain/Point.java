package virtualworld.terrain;

public class Point {
	
  private final double x;
  private final double z;

  public Point(double x, double z) {
    this.x = x;
    this.z = z;
  }

  public double getX() { return x; }
  public double getZ() { return z; }



  public boolean equals(Point o) {
    return this.getX() == o.getX() &&
           this.getZ() == o.getZ();
  }

}
