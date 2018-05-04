package virtualworld.terrain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import engine.Engine;
import entity.Entity;
import javafx.geometry.Point3D;
import shapes.HeightMapSurface;
import shapes.RenderMaterial;
import shapes.Shape;
import worldmanager.WorldManager;

//Use Static Factories to set up the different types of terrain and create Root Terrain with it	

public class Terrain implements Entity {
	
	public static void main(String[] args) {
	   
		Terrain t = Terrain.forWorld(new Point(0.0,0.0), 500, 100);
		double[][] render = t.renderBaseHeights();
		
		System.out.println("rendered top level terrain");
		for (double[] a : render) {
			for (double height : a) {
				System.out.print(height + " ");
			}
			System.out.print("\n");
		}
		
		/*List<Point3D> trees = t.getTrees();
		
		if (trees.isEmpty()) {
			System.out.println("No trees");
		} else {
			System.out.println("Not empty");
		}
		
		for (Point3D tree: trees) {
			System.out.print(tree.toString() + " ");
		}*/
		
	}
	
    // (x, z) coordinate of the terrain center
	//private final Pair<Double, Double> center;
	private final double[][] heightMap;
	private final Point center;
	private TerrainHeightAlgorithm noise;
	private final int pointsPerSide;
	private boolean mapIsSet;
	private boolean active = true;
	private double worldSize;
	
    // length the area that the terrain covers and the height seed between each point.
    private final double length;
    
    
	
	public Terrain(Point c, double len, int points, TerrainHeightAlgorithm noiseAlgorithm) {
		center = c;
        length = len;
		pointsPerSide = points;
        heightMap = new double[points][points];
        noise = noiseAlgorithm;
        mapIsSet = false;
        worldSize = WorldManager.getInstance().getSize();
	}
	
	//Static Constructors. LOD stand for game units covered per Quad

	public static Terrain forWorld(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = new WorldHeightAlgorithm(c, length);
		return new Terrain(c, length, pointsFromLOD(length, lod) , ta);
	}
	
	public static Terrain forIsland(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = new IslandHeightAlgorithm(c, length, NormalHeightAlgorithm.forMountains(length));
		return new Terrain(c, length, pointsFromLOD(length, lod) , ta);
	}
	
	public static Terrain forFields(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = NormalHeightAlgorithm.forFields(length);
		return new Terrain(c, length, pointsFromLOD(length, lod) , ta);
	}
	
	public static Terrain forHills(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = NormalHeightAlgorithm.forHills(length);
		return new Terrain(c, length, pointsFromLOD(length, lod) ,  ta);
	}
	
	public static Terrain forMountains(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = NormalHeightAlgorithm.forMountains(length);
		return new Terrain(c, length, pointsFromLOD(length, lod), ta);
	}
	
	public static Terrain forMountainValley(Point c, double length, int lod) {
		TerrainHeightAlgorithm ta = ValleyHeightAlgorithm.forNormalValley(length);
		return new Terrain(c, length, pointsFromLOD(length, lod) , ta);
	}
	
	private static int pointsFromLOD(double length, int lod) {
		return (int) (Math.pow(2, lod) + 1);
		
	}

    //returns a pair that represents the center of the terrain object
    public Point getCenter() {
        return center;
    }

    // splits the terrain object into four separate terrain objects
    public Terrain[] split() {

    	  active = false;

        return new Terrain[] {
        	
        		//0 Top-left
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getZ() + (length/4)),
                length/2,
                pointsPerSide,
                noise),
            //1  Top-right
            new Terrain(
                new Point(
                		center.getX() + (length/4), 
                		center.getZ() + (length/4)),
                length/2,
                pointsPerSide,
                noise),
            //2 bottom-left
            new Terrain(
                new Point(
                		center.getX() - (length/4), 
                		center.getZ() - (length/4)),
                length/2,
                pointsPerSide,
                noise),
            //3 bottom-right
            new Terrain(
                new Point(
                		center.getX() + (length/4), 
                		center.getZ() - (length/4)),
                length/2,
                pointsPerSide,
                noise),
        };   
    }
    
    public double[][] renderBaseHeights() {
    	Point topLeft = new Point(center.getX() - (length / 2), center.getZ() + (length / 2));
    	double increment = length / (pointsPerSide - 1);
    	
    	for (int r = 0; r < pointsPerSide; r++) {
    		double nr = (topLeft.getZ() - (increment * r))/worldSize - 0.5;
			for (int c = 0; c < pointsPerSide; c++) {
				double nc = (topLeft.getX() + (increment * c))/worldSize - 0.5;
					heightMap[r][c] = noise.generateHeight(nc, nr);
			}
    	}
    	
    	return heightMap;
    }
    
    public double getHeightAt(Point spot) {
    	
    	double worldZ = spot.getZ()/worldSize - 0.5;
		double worldX = spot.getX()/worldSize - 0.5;
		
		return noise.generateHeight(worldX, worldZ);
		
    }
    
    public List<Point3D> getTrees() {
    	Point topLeft = new Point(center.getX() - (length / 2), center.getZ() + (length / 2));
    	double increment = 5;
    	ArrayList<Point3D> trees = new ArrayList<Point3D>();
    	
    	for (int r = 0; r < pointsPerSide; r++) {
    		double nr = (topLeft.getZ() - (increment * r))/length - 0.5;
			for (int c = 0; c < pointsPerSide; c++) {
				double nc = (topLeft.getX() + (increment * c))/length - 0.5;
				Point3D p = noise.placeTree(nc, nr);
				if (p != null) {
					trees.add(p);
				}
			}
    	}
    	
		return trees;
    	
    }
     
     
	@Override
	public double getSize() {
		return length;
	}

	@Override
	public void distFromCamera(double dist) {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public List<Shape> getShapes() {
		
		if (!mapIsSet) renderBaseHeights();
		
		Point topLeft = new Point(center.getX() - (length / 2), center.getZ() + (length / 2));
    	double increment = length / (pointsPerSide -1);
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
	}*/
	
	// (added by Kayla (for testing height map shape)
	public HeightMapSurface getHeightMapSurface() {
		if(!mapIsSet) renderBaseHeights();
		Point topLeft = new Point(center.getX() - (length/2), center.getZ() + (length/2));
		float[] convertedHeightMap = new float[heightMap.length * heightMap[0].length];
		// assuming row-major, turn height map into flat array of floats
		for(int r = 0; r < heightMap.length; r++) {
			for(int c = 0; c < heightMap[r].length; c++) {
				int idx = (c + (r * heightMap.length));
				convertedHeightMap[idx] = (float)heightMap[r][c];
			}
		}
		int patchSize = (int)((pointsPerSide-1) / 4) + 1;
		float scale = (float) length / (pointsPerSide - 1);
		Engine.logInfo("Terrain.HeightMapSurface: scale: " + scale);
		HeightMapSurface hms = new HeightMapSurface(pointsPerSide, patchSize, convertedHeightMap, scale, 1f, scale, center.getX(), 0, center.getZ());
		return hms;
	}
	
	@Override
	public List<Shape> getShapes() {
		RenderMaterial hmsMat = new RenderMaterial();
		hmsMat.setUseTexture(true);
		hmsMat.setTextureDiffusePath("Textures/Terrain/splat/grass.jpg");
		hmsMat.setTextureNormalPath("Textures/Terrain/splat/grass_normal.jpg");
		List<Shape> shapes = new ArrayList<Shape>();
		HeightMapSurface hms = getHeightMapSurface();
		hms.setMaterial(hmsMat);
		shapes.add(hms);
		return shapes;
	}
	
	@Override
	public boolean isActive() {
		return active;
	}

}

