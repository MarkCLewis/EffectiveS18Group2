package roads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.Entity;
import shapes.RectangularPrism;
import shapes.Shape;
import virtualworld.terrain.Pair;
import virtualworld.terrain.Point;

public class Road implements Entity {

	private int north;
	private int south;
	private int east;
	private int west;
	private final double len;
	private final Point topLeft;
	private final Point topRight;
	private final Point bottomLeft;
	private final Point bottomRight;
	boolean activeness;
	private final Point center;

	// initial size should be size of world
	public Road(Point cent, double size) {
		north = 1;
		south = 1;
		east = 2;
		west = 1;
		len = size;
		topLeft = new Point(cent.getX() - (size / 2), cent.getZ() - (size / 2));
		topRight = new Point(cent.getX() + (size / 2), cent.getZ() - (size / 2));
		bottomLeft = new Point(cent.getX() - (size / 2), cent.getZ() + (size / 2));
		bottomRight = new Point(cent.getX() + (size / 2), cent.getZ() + (size / 2));
		center = cent;
		activeness = false;
	}

	// splits the road into four quads
	public Road[] split() {

		Pair<Integer, Integer> northPair = splitLine(topLeft, topRight, north);
		Pair<Integer, Integer> southPair = splitLine(bottomLeft, bottomRight, south);
		Pair<Integer, Integer> westPair = splitLine(topLeft, bottomLeft, west);
		Pair<Integer, Integer> eastPair = splitLine(topRight, bottomRight, east);
		activeness = false;
		Road[] roadArray = new Road[] {
				// topLeft
				new Road(new Point(center.getX() - (len / 4), center.getZ() - (len / 4)), len / 2),
				// topRight
				new Road(new Point(center.getX() + (len / 4), center.getZ() - (len / 4)), len / 2),
				// bottomLeft
				new Road(new Point(center.getX() - (len / 4), center.getZ() + (len / 4)), len / 2),
				// bottomRight
				new Road(new Point(center.getX() + (len / 4), center.getZ() + (len / 4)), len / 2),

		};
		// topLeft
		roadArray[0].north = northPair.getLeft();
		roadArray[0].south = findNum(northPair.getLeft(), southPair.getLeft());
		roadArray[0].east = findNum(westPair.getRight(), eastPair.getRight());
		roadArray[0].west = westPair.getRight();
		roadArray[0].activeness = isActive();

		/// topRight
		roadArray[1].north = northPair.getRight();
		roadArray[1].south = findNum(northPair.getRight(), southPair.getRight());
		roadArray[1].east = eastPair.getRight();
		roadArray[1].west = findNum(westPair.getRight(), eastPair.getRight());
		roadArray[1].activeness = isActive();

		// bottomLeft
		roadArray[2].north = findNum(northPair.getLeft(), southPair.getLeft());
		roadArray[2].south = southPair.getLeft();
		roadArray[2].east = findNum(westPair.getLeft(), eastPair.getLeft());
		roadArray[2].west = westPair.getLeft();
		roadArray[2].activeness = isActive();

		// bottomRight
		roadArray[3].north = findNum(northPair.getRight(), southPair.getLeft());
		roadArray[3].south = southPair.getRight();
		roadArray[3].east = eastPair.getLeft();
		roadArray[3].west = findNum(westPair.getLeft(), eastPair.getLeft());
		roadArray[3].activeness = isActive();
		return roadArray;
	}

	// find the number of roads on each side of the splitLine
	private Pair<Integer, Integer> splitLine(Point p1, Point p2, int card) {
		int seed = (int) Math.hypot(p1.getX() - p2.getX(), p1.getZ() - p2.getZ());
		Random rand = new Random(seed);
		int mid = card / 2;
		int lowerBound = mid - 5;
		if (mid <= 10) {
			lowerBound = mid - 2;
		}
		if (mid == 1) {
			mid = card;
			lowerBound = 0;
		}
		if (card == 1) {
			return new Pair<Integer, Integer>(0, 1);
		} else if (card == 2) {
			return new Pair<Integer, Integer>(1, 1);
		}
		int split1 = rand.nextInt(mid - lowerBound) + lowerBound;
		int split2 = card - split1;
		if ((card <= 10) && (split1 == 0 || split2 == 0)) {
			if (split1 == 0) {
				split1 += 1;
				split2 -= 1;
			} else {
				split2 += 1;
				split1 -= 1;
			}
		}
		return new Pair<Integer, Integer>(split1, split2);
	}

	// for testing purposes
	public String splitString(Pair<Integer, Integer> pair) {
		return ("(" + pair.getLeft() + ", " + pair.getRight() + ")");
	}

	public String pointString(Point p) {
		return ("(" + p.getX() + ", " + p.getZ() + ")");
	}

	// finds a semi-random number
	// right now it's inbetween two numbers but i'll change it if i have time
	private int findNum(int side2, int side1) {
		if (side1 == 0 || side2 == 0) {
			return 1;
		}
		Random rand = new Random();
		int upperBound;
		int lowerBound;
		if (side1 > side2) {
			upperBound = side1;
			lowerBound = side2;
		} else {
			upperBound = side2;
			lowerBound = side1;
		}
		int val = rand.nextInt((upperBound - lowerBound) + lowerBound);
		return val;
	}

	private boolean isDone(int card) {
		if (card == 0 || card == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Point getCenter() {
		return center;
	}

	@Override
	public double getSize() {
		return len;
	}

	@Override
	public void distFromCamera(double dist) {
		dist = center.getX() - dist;
	}

	// RectangularPrism road = new RectangularPrism(xsize(width),ysize(height),
	// zsize(length), xpos, //ypos (goes up), zpos);
	@Override
	public List<Shape> getShapes() {
		float size = (float) len;
		List<Shape> shapes = new ArrayList<Shape>();

		if (north == 1 && south == 1) {
			RectangularPrism road = new RectangularPrism(100f, 400f, size, topRight.getX() + (size / 2), 200,
					topRight.getZ() + size);
			shapes.add(road);
		} else if (north == 1 && south == 0) {
			RectangularPrism road = new RectangularPrism(100f, 400f, (size / 2), topRight.getX() + (size / 2), 200,
					topRight.getZ() + size / 2);
			shapes.add(road);
		} else if (north == 0 && south == 1) {
			RectangularPrism road = new RectangularPrism(100f, 400f, (size / 2), topRight.getX() + (size / 2), 200,
					topRight.getZ() + size + size / 2);
			shapes.add(road);
		}
		if (west == 1 && east == 1) {
			RectangularPrism road = new RectangularPrism(size, 400f, 100f, topRight.getX() + size / 2, 200,
					topRight.getZ() + (size));
			shapes.add(road);
		} else if (west == 1 && east == 0) {
			RectangularPrism road = new RectangularPrism((size / 2), 400f, 100f, topRight.getX() + size, 200,
					topRight.getZ() + size);
			shapes.add(road);
		} else if (west == 0 && east == 1) {
			RectangularPrism road = new RectangularPrism((size / 2), 400f, 100f, topRight.getX(), 200,
					topRight.getZ() + size);
			shapes.add(road);
		}

		return shapes;
	}

	@Override
	public boolean isActive() {
		if (isDone(north) && isDone(south) && isDone(east) && isDone(west)) {
			return true;
		} else {
			return false;
		}
	}

}
