package roads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.Engine;
import entity.Entity;
import shapes.Shape;
import virtualworld.terrain.Pair;
import virtualworld.terrain.Point;

public class Road implements Entity {

	private final int north;
	private final int south;
	private final int east;
	private final int west;
	private final Point topLeft;
	private final Point topRight;
	private final Point bottomLeft;
	private final Point bottomRight;
	boolean activeness;
	
	public Road(int n, int s, int e, int w,
				Point tl, Point tr, Point bl, Point br) {
		north = n;
		south = s;
		east = e;
		west = w;
		topLeft = tl;
		topRight = tr;
		bottomLeft = bl;
		bottomRight = br;
    }
    
	//splits the road into four qauds
	public Road[] split() {
		Pair<Integer, Integer> northPair = splitLine(topLeft, topRight, north);
		Pair<Integer, Integer> southPair = splitLine(bottomLeft, bottomRight, south);
		Pair<Integer, Integer> westPair = splitLine(topLeft, bottomLeft, west);
		Pair<Integer, Integer> eastPair = splitLine(topRight, bottomRight, east);
		activeness = false;
		Point center = midPoint(topLeft, bottomRight);
		return new Road[] {
				//top-left
				new Road(
					northPair.getLeft(),
					findNum(northPair.getLeft(), southPair.getLeft()),
					findNum(westPair.getRight(), eastPair.getRight()),
					westPair.getRight(),
					topLeft,
					midPoint(topLeft, topRight),
					midPoint(topLeft, bottomLeft),
					center),
				//top-right
				new Road(
					northPair.getRight(),
					findNum(northPair.getRight(), southPair.getRight()),
					eastPair.getRight(),
					findNum(westPair.getRight(), eastPair.getRight()),
					midPoint(topLeft, topRight),
					topRight,
					center,
					midPoint(topRight, bottomRight)),
				//bottom-left
				new Road(
					findNum(northPair.getLeft(), southPair.getLeft()),
					southPair.getLeft(),
					findNum(westPair.getLeft(), eastPair.getLeft()),
					westPair.getLeft(),
					midPoint(topLeft, bottomLeft),
					center,
					bottomLeft,
					midPoint(bottomLeft, bottomRight)),
				//bottom-right
				new Road(
					findNum(northPair.getRight(), southPair.getLeft()),
					southPair.getRight(),
					eastPair.getLeft(),
					findNum(westPair.getLeft(), eastPair.getLeft()),
					center,
					midPoint(topRight, bottomRight),
					midPoint(bottomLeft, bottomRight),
					bottomRight),
		};
	}
	
	// find the number of roads on each side of the splitLine
	//TODO change back to private
	public Pair<Integer, Integer> splitLine(Point p1, Point p2, int card) {
		int seed = (int) Math.hypot(p1.getX()-p2.getX(), p1.getZ()-p2.getZ());
		Random rand = new Random(seed);
		int mid = card/2;
		int lowerBound = mid-5;
		if (mid <= 10) {
			lowerBound = mid-2;
		}
		if (mid == 1) {
			mid = card;
			lowerBound = 0;
		}
		if (card == 1) {
			mid = 1;
			lowerBound = 0;
		}
		int split1 = rand.nextInt(mid-lowerBound) + lowerBound;
		int split2 = card - split1;
		return new Pair<Integer, Integer>(split1,split2);
	}
	//for testing purposes
	public String splitString(Pair<Integer, Integer> pair){
		return ("("+pair.getLeft()+", "+pair.getRight()+")"); 
	}  
	
	//finds a semi-random number 
	//right now it's inbetween two numbers but i'll change it if i have time
	//TODO change back to private
	public int findNum(int side1, int side2) {
		Random rand = new Random();
		int upperBound;
		int lowerBound;
		if (side1 > side2) {
			upperBound = side1;
			lowerBound = side2;
		}
		else {
			upperBound = side2;
			lowerBound = side1;
		}
		int val = rand.nextInt(upperBound-lowerBound) + lowerBound;
		return val;
	}
	
	//TODO create an initial road that is the size of the world
	Point midPoint(Point p1, Point p2) {
		Point tmp = new Point(
				(p1.getX()+p2.getX())/2,
				(p1.getX()+p2.getX())/2);
		return tmp;
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

