package water;

import virtualworld.terrain.Point;

public class Ocean {

	public Ocean(Point cen, double leng) {
		center = cen;
		length = leng;
	}
	
	// coordinate for center of Ocean segment. Utilizing Ian's terrain pair.
	//TODO: figure out if ocean needs it's own unique pair
	private final Point center;
	
	//length the ocean extends
	private final Double length;
	
	//representation of ocean
	private Double[][] oceanArr;
	
	private void oceanArray() {
		//TODO
		//Perlin to make waves? (either this or create rivers)
	};
	
	public Double[][] getOcean() {
		return oceanArr;
	}

}
