package roads;

import java.util.ArrayList;
import java.util.List;

import engine.Game;
import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Point;
import towns.Towns;

public class Road implements Entity {
    
     private final Point startPoint;
     private final Point endPoint;
    private final ArrayList<Point> path;

    public Road(Point start) {
        startPoint = start;
        endPoint = findEnd(startPoint);
        path = findPath(startPoint, endPoint);
    }
    
    
	//gets the center of the closest town and makes that the endpoint
	Point findEnd(Point start) {
		Point tmp = new Point(20.0,25.0); //temporary value until I figure out WorldManager
		return (tmp);
	}
	
	//creates a list of points on the line
	private ArrayList<Point> findPath(Point start, Point end) {
		ArrayList<Point> tmp = new ArrayList<Point>();
		
		double distance = Math.sqrt((start.getX()-end.getX())*(start.getX()-end.getX()) 
						+ (start.getZ()-end.getX())*(start.getZ()-end.getZ()));
    	double m = (end.getZ()-start.getZ())/(end.getX()-start.getX());
    	
    	double b = end.getZ() - (m * end.getX());
        for(int i=0; i < distance; i++) {
            tmp.add(new Point(0.0, 0.0));
        }
        return tmp;
    }
	
	int gcd(int a, int b)
	{
	    if (b == 0) return a;
	    return gcd(b, a%b);
	}
	
	public Point getEnd(Towns towns) {
		//endPoint = towns.getCenter();
		return endPoint;
	}
	
	public ArrayList<Point> getPath() {
		return findPath(startPoint, endPoint);
	}
    @Override
    public Point getCenter() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public double getSize() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public void distFromCamera(double dist) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public List<Shape> getShapes() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean isActive() {
    	return true;
    }

}

