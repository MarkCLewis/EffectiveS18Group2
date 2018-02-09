package virtualworld.terrain;

public class Terrain {
	
    // (x, z) coordinate of the terrain center
	private final Pair<double, double> center;

    // length and width of the area that the terrain covers.
    private final double length;
    private final double width;
	
	public Terrain(Pair<double, double> c, double len, double w) {
		center = c;
        length = len;
        width = w; 
	}

    //returns a pair that represents the center of the terrain object
    public Pair<double, double> getCenter {
        return center;
    }

    // splits the terrain object into four seperate terrin objects
    public Terrian[] split() {
        return new Terrain[] {
            new Terrain(
                Pair(center.getLeft() - (width/4), center.getRight() - (length/4),
                length, 
                width/2)),
            new Terrain(
                Pair(center.getLeft() + (width/4), center.getRight() - (length/4),
                length, 
                width/2)),
            new Terrain(
                Pair(center.getLeft() - (width/4), center.getRight() + (length/4),
                length, 
                width/2)),
            new Terrain(
                Pair(center.getLeft() + (width/4), center.getRight() + (length/4),
                length, 
                width/2))
        }

    }

}
