package virtualworld.terrain;
import java.util.Arrays;
import entity.Entity;;

//Use Static Factories to set up the different types of terrain and create Root Terrain with it	

public class Terrain implements Entity {
	
	public static void main(String[] args) {
	   
		double length = 100;
		int seed = 10;
	    int points = 9;
		double[][] heightMap = {{0.25, 0.0 , 1.0, 0.0, 0.35},
								{0.0, 0.0 , 0.0, 0.0, 0.0},
								{0.0, 0.0 , 1.0, 0.0, 0.0},
								{0.0, 0.0 , 0.0, 0.0, 0.0},
								{0.5, 0.0 , 1.0, 0.0, 0.25}};
	
		Terrain t = new Terrain(new Point(0.0,0.0), length, seed, points, heightMap);
		
		double[][] render = t.renderHeights();
		
		System.out.println("rendered top level terrain");
		for (double[] a : render) {
			for (double height : a) {
				System.out.print(height + " ");
			}
			System.out.print("\n");
		}
		
		Terrain[] split = t.split();
		
		Terrain topLeft = split[0];
		
		double[][] render2 = topLeft.renderHeights();
	    
		System.out.println("rendered top left terrain");
		for (double[] a : render2) {
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
	private final int pointsPerSide;
	private boolean mapIsSet;

    // length the area that the terrain covers and the height seed between each point.
    private final double length;
    private final double heightSeed;
    
    
	
	public Terrain(Point c, double len, double heightS, int points, double[][] constructionArray) {
		center = c;
        length = len;
        heightSeed = heightS;
		pointsPerSide = points;
        heightMap = expandArray(constructionArray);
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
                		center.getY() + (length/4)),
                length,
                heightSeed,
                pointsPerSide,
                subTerrains[0]),
            new Terrain(
                new Point(
                		center.getX() + (length/4), 
                		center.getY() + (length/4)),
                length,
                heightSeed,
                pointsPerSide,
                subTerrains[1]),
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getY() - (length/4)),
                length,
                heightSeed,
                pointsPerSide,
                subTerrains[2]),
            new Terrain(
                new Point(
                		center.getX() + (length/4), 
                		center.getY() - (length/4)),
                length,
                heightSeed,
                pointsPerSide,
                subTerrains[3]),
        };   
    }
    
    private double[][] expandArray(double[][] init) {
    	
    	double[][] ans = new double[pointsPerSide][pointsPerSide];
    	
    	for (double[] row: ans)
    	    Arrays.fill(row, -1.0);
    	
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
    	//Integer division so will always round down
    	int splitSize = (pointsPerSide + 1)/2;
    	
    	double[][][] split = new double[4][splitSize][splitSize];
    	
    	for(int r = 0; r < heightMap.length; r++) {
    		if (r < splitSize -1) {
    			split[0][r] = Arrays.copyOfRange(heightMap[r], 0, splitSize); 
    			split[1][r] = Arrays.copyOfRange(heightMap[r], splitSize -1, heightMap[r].length); 
    		} else if (r > splitSize -1) {
    			split[2][r % splitSize] = Arrays.copyOfRange(heightMap[r], 0, splitSize); 
    			split[3][r % splitSize] = Arrays.copyOfRange(heightMap[r], splitSize -1, heightMap[r].length); 
    		} else {
    			split[0][r] = Arrays.copyOfRange(heightMap[r], 0, splitSize); 
    			split[1][r] = Arrays.copyOfRange(heightMap[r], splitSize -1, heightMap[r].length); 
    			split[2][r] = Arrays.copyOfRange(heightMap[r], 0, splitSize); 
    			split[3][r] = Arrays.copyOfRange(heightMap[r], splitSize -1, heightMap[r].length); 
    		}
    	}
    	
    	return split;
    	
    }
    
    public double[][] renderHeights() {
    	Point topLeft = new Point(center.getX() - (length / 2), center.getY() + (length / 2));
    	double increment = length / pointsPerSide;
    	
    	for (int r = 0; r < pointsPerSide; r++) {
    		double nr = (topLeft.getY() + (increment * r))/length - 0.5;
			for (int c = 0; c < pointsPerSide; c++) {
				double nc = (topLeft.getX() + (increment * c))/length - 0.5;
				if (heightMap[r][c] == -1.0) {
					heightMap[r][c] = perlNoise.noise2D(nc, nr) * heightSeed;
				}
				
			}
    	}
    	
    	return heightMap;
    }
    
    public double getHeightAt(Point spot) {
    	
    	double worldY = spot.getY()/length - 0.5;
		double worldX = spot.getX()/length - 0.5;
		
		return perlNoise.noise2D(worldX, worldY) * heightSeed;
		
    }
     
     
    //generates a height map for terrain square

	@Override
	public double getSize() {
		return length;
	}

	@Override
	public void distFromCamera(double dist) {
		// TODO Auto-generated method stub
		
	}

}
