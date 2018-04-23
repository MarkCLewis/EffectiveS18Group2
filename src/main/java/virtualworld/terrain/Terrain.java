package virtualworld.terrain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import engine.Engine;
import entity.Entity;
import shapes.Shape;
import shapes.Quad;

//Use Static Factories to set up the different types of terrain and create Root Terrain with it	

public class Terrain implements Entity {
	
	public static void main(String[] args) {
	   
		double length = 5000;
		int seed = 500;
	    int points = 61;
		double[][] heightMap = {{0.0}};
		
		TerrainHeightAlgorithm ta = new NormalHeightAlgorithm(4, 3, 2, 150, 400); 
		
		//Terrain t = new Terrain(new Point(0.0,0.0), length, points, heightMap, ta);
		Terrain t = Terrain.forMountainValley(new Point(0.0,0.0), length, 100);
		double[][] render = t.renderBaseHeights();
		
		System.out.println("rendered top level terrain");
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
	private TerrainHeightAlgorithm noise;
	private final int pointsPerSide;
	private boolean mapIsSet;

    // length the area that the terrain covers and the height seed between each point.
    private final double length;
    
    
	
	public Terrain(Point c, double len, int points, double[][] constructionArray, TerrainHeightAlgorithm noiseAlgorithm) {
		center = c;
        length = len;
		pointsPerSide = points;
        heightMap = expandArray(constructionArray);
        noise = noiseAlgorithm;
        mapIsSet = false;
	}
	
	//Static Constructors. LOD stand for game units covered per Quad
	
	public static Terrain forFeilds(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = new NormalHeightAlgorithm(2, 2, 2, 150, 500);
		double [][] placeholderArray = {{0.0}};
		return new Terrain(c, length, pointsFromLOD(length, lod) , placeholderArray, ta);
	}
	
	public static Terrain forHills(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = new NormalHeightAlgorithm(5, 3, 3, 150, 500);
		double [][] placeholderArray = {{0.0}};
		return new Terrain(c, length, pointsFromLOD(length, lod) , placeholderArray, ta);
	}
	
	public static Terrain forMountains(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = new NormalHeightAlgorithm(8, 3, 3, 150, 600);
		double [][] placeholderArray = {{0.0}};
		return new Terrain(c, length, pointsFromLOD(length, lod) , placeholderArray, ta);
	}
	
	public static Terrain forMountainValley(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = new ValleyHeightAlgorithm(8, 3, 3, 150, 600, 1.5);
		double [][] placeholderArray = {{0.0}};
		return new Terrain(c, length, pointsFromLOD(length, lod) , placeholderArray, ta);
	}
	
	private static int pointsFromLOD(double length, int lod) {
		int possiblePoints = (int)(length/lod);
		if (possiblePoints % 2 ==0) {
			return possiblePoints + 1;
		} else {
			return possiblePoints;
		}
		
	}

    //returns a pair that represents the center of the terrain object
    public Point getCenter() {
        return center;
    }

    // splits the terrain object into four separate terrain objects
    public Terrain[] split() {
    	
        double[][][] subTerrains = splitArray();
        return new Terrain[] {
        	
        		//0 Top-left
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getZ() + (length/4)),
                length/2,
                pointsPerSide,
                subTerrains[0],
                noise),
            //1  Top-right
            new Terrain(
                new Point(
                		center.getX() + (length/4), 
                		center.getZ() + (length/4)),
                length/2,
                pointsPerSide,
                subTerrains[1],
                noise),
            //2 bottom-left
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getZ() - (length/4)),
                length/2,
                pointsPerSide,
                subTerrains[2],
                noise),
            //3 bottom-right
            new Terrain(
                new Point(
                		center.getX() + (length/4), 
                		center.getZ() - (length/4)),
                length/2,
                pointsPerSide,
                subTerrains[3],
                noise),
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
    	
    	if(!mapIsSet) renderBaseHeights();
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
    	Point topLeft = new Point(center.getX() - (length / 2), center.getZ() +(length / 2));
    	double increment = length / pointsPerSide;
    	
    	for (int r = 0; r < pointsPerSide; r++) {
    		double nr = (topLeft.getZ() + (increment * r))/length - 0.5;
			for (int c = 0; c < pointsPerSide; c++) {
				double nc = (topLeft.getX() + (increment * c))/length - 0.5;
				if (heightMap[r][c] == -1.0) {
					//heightMap[r][c] = 150 + perlNoise.noise2D(nc, nr) * heightSeed;
					heightMap[r][c] =  noise.generateHeight(nc, nr);
				}
				
			}
    	}
    	
    	return heightMap;
    }
    
    public double[][] renderBaseHeights() {
    	Point topLeft = new Point(center.getX() - (length / 2), center.getZ() + (length / 2));
    	double increment = length / pointsPerSide;
    	
    	for (int r = 0; r < pointsPerSide; r++) {
    		double nr = (topLeft.getZ() - (increment * r))/length - 0.5;
			for (int c = 0; c < pointsPerSide; c++) {
				double nc = (topLeft.getX() + (increment * c))/length - 0.5;
					//heightMap[r][c] = 150 + perlNoise.noise2D(7 * nc, 7 * nr) * heightSeed;
					heightMap[r][c] = noise.generateHeight(nc, nr);
			}
    	}
    	
    	return heightMap;
    }
    
    public double getHeightAt(Point spot) {
    	
    	double worldZ = spot.getZ()/length - 0.5;
		double worldX = spot.getX()/length - 0.5;
		
		//return perlNoise.noise2D(worldX, worldZ) * heightSeed;
		return noise.generateHeight(worldX, worldZ);
		
    }
     
     
	@Override
	public double getSize() {
		return length;
	}

	@Override
	public void distFromCamera(double dist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Shape> getShapes() {
		
		if (!mapIsSet) renderBaseHeights();
		
		Point topLeft = new Point(center.getX() - (length / 2), center.getZ() + (length / 2));
    	double increment = length / pointsPerSide;
    	List<Shape> quads = new ArrayList<Shape>();
    	
    	for (int r = 0; r < heightMap.length -1; r++) {
    		double quadZCoordinate = topLeft.getZ() -((increment * r) + (increment/2));
			for (int c = 0; c < heightMap.length-1; c++) {
				double quadXCoordinate = topLeft.getX() + (increment * c) + (increment/2);
				float[] corners = {(float)heightMap[r+1][c], (float)heightMap[r+1][c+1], (float)heightMap[r][c+1], (float)heightMap[r][c]};
				 quads.add(new Quad((float)increment, corners, quadXCoordinate, 0.0, quadZCoordinate));
				}
				
			}
    	
		return quads; 
	}
	
	@Override
	public boolean isActive() {
		return true;
	}

}

