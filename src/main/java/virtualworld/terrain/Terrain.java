package virtualworld.terrain;

public class Terrain {
	
	public static void main(String[] args) {
	   
		double length = 100;
		int seed = 10;
		double[][] heightMap = {{0.0, 0.0 , 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0}};
	
		Terrain t = new Terrain(new Point(0.0,0.0), length, seed, heightMap);
		
		double[][] render = t.renderHeights();
		
		for (double[] a : render) {
			for (double height : a) {
				System.out.print(height + " ");
			}
			System.out.print("\n");
		}
	    
	}
	
    // (x, z) coordinate of the terrain center
	//private final Pair<Double, Double> center;
	private final double[][] heightMap;
	private final Point center;
	private final Perlin perlNoise = Perlin.getInstance();
	private boolean mapIsSet;

    // length the area that the terrain covers and the height seed between each point.
    private final double length;
    private final double heightSeed;
    
    
	
	public Terrain(Point c, double len, double heightS, double[][] constructionArray) {
		center = c;
        length = len;
        heightMap = expandArray(constructionArray);
        heightSeed = heightS;
        mapIsSet = false;
	}

    //returns a pair that represents the center of the terrain object
    public Point getCenter() {
        return center;
    }

    // splits the terrain object into four separate terrain objects
    public Terrain[] split() {
    	
        double[][][] subTerrains = splitArray();
        return new Terrain[] {
        	
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getY() - (length/4)),
                length,
                heightSeed,
                subTerrains[0]),
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getY() - (length/4)),
                length,
                heightSeed,
                subTerrains[1]),
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getY() - (length/4)),
                length,
                heightSeed,
                subTerrains[2]),
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getY() - (length/4)),
                length,
                heightSeed,
                subTerrains[3]),
        };   
    }
    
    private double[][] expandArray(double[][] init) {
    	
    	double[][] ans = new double [(init.length*init.length) - 1][(init.length*init.length) - 1];
    	
    	for (int r = 0; r < init.length; r++) {
			for (int c = 0; c < init.length; c++) {
				ans[r*2][c*2] = init[r][c];
			}
    	}
    	
    	return ans;
    }
    
    //function takes in a 0,1,2,3 for the 4 different parts of the terrain
    private double[][][] splitArray() {
    	
    	if(!mapIsSet) renderHeights();
    	
    	double[][][] split = new double[4][heightMap.length/2][heightMap.length/2];

    	//for(int f = 0; f < split.length; f++) {
    	//}
    	
    	return split;
    	
    }
    
    //generates a height map for terrain square
    public double[][] renderHeights() {
    	// height difference seed
    	
    	for (int r = 0; r < heightMap.length; r+= 2) {
    		double xOffset = (center.getY() + length/2) / heightSeed;
			for (int c = 0; c < heightMap.length; c+= 2) {
				//average of all of the perlin generated heights from surrounding heights
				heightMap[r][c] = ((perlNoise.noise2D(xOffset, heightMap[r-1][c-1])*heightSeed) +
									(perlNoise.noise2D(xOffset, heightMap[r+1][c-1])*heightSeed) +
									(perlNoise.noise2D(xOffset, heightMap[r-1][c+1])*heightSeed) +
									(perlNoise.noise2D(xOffset, heightMap[r+1][c+1])*heightSeed)) / 2;
    			xOffset += 0.1;
			}
    	}
    	
    	
    	for (int r = 0; r < heightMap.length; r++) {
    		double xOffset = (center.getY() + length/2) / 10;
			for (int c = 0; c < heightMap.length; c++) {
				if (heightMap[r][c] != 0) {
					if (r % 2 == 0) {
						//vertical triangle case
						heightMap[r][c] = ((perlNoise.noise2D(xOffset, heightMap[r][c-1])*heightSeed) +
											(perlNoise.noise2D(xOffset, heightMap[r][c+1])*heightSeed) +
											(perlNoise.noise2D(xOffset, heightMap[r+1][c])*heightSeed)) / 2;
					} else {
						//horizontal triangle case
						heightMap[r][c] = ((perlNoise.noise2D(xOffset, heightMap[r+1][c])*heightSeed) +
											(perlNoise.noise2D(xOffset, heightMap[r-1][c])*heightSeed) +
											(perlNoise.noise2D(xOffset, heightMap[r][c+1])*heightSeed)) / 2;
						
					}
					xOffset += 0.1;
			}
		}
    	}
    	
    	return heightMap;
    	
    }

}
