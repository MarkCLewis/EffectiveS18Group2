package roads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jme3.material.Material;

import entity.Entity;
import shapes.RectangularPrism;
import shapes.RenderColor;
import shapes.RenderMaterial;
import shapes.Shape;
import virtualworld.terrain.Pair;
import virtualworld.terrain.Point;

public class Road implements Entity {

	public int north;
	public int south;
	public int east;
	public int west;
	private final float len;
	private final Point topLeft;
	private final Point topRight;
	private final Point bottomLeft;
	private final Point bottomRight;
	boolean activeness = false;
	private final Point center;

	// initial size should be size of world
	public Road(Point cent, double d) {
		north = 262000;
		south = 262000;
		east = 262000;
		west = 262000;
		len = (float) d;
		topLeft = new Point(cent.getX() - (d / 2), cent.getZ() - (d / 2));
		topRight = new Point(cent.getX() + (d / 2), cent.getZ() - (d / 2));
		bottomLeft = new Point(cent.getX() - (d / 2), cent.getZ() + (d / 2));
		bottomRight = new Point(cent.getX() + (d / 2), cent.getZ() + (d / 2));
		center = cent;
	}

	// splits the road into four quads
	public Road[] split() {
		Pair<Integer, Integer> northPair = splitLine(topLeft, topRight, north);
		Pair<Integer, Integer> southPair = splitLine(bottomLeft, bottomRight, south);
		Pair<Integer, Integer> westPair = splitLine(topLeft, bottomLeft, west);
		Pair<Integer, Integer> eastPair = splitLine(topRight, bottomRight, east);
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
		int middleTop = findNum(westPair.getRight(), eastPair.getRight());
		int middleBottom = findNum(westPair.getLeft(), eastPair.getLeft());
		int middleLeft = findNum(northPair.getLeft(), southPair.getLeft());
		int middleRight = findNum(northPair.getRight(), southPair.getRight());
		// topLeft
		boolean active0 = true;
		roadArray[0].north = northPair.getLeft();
		roadArray[0].south = middleLeft;
		roadArray[0].east = middleTop;
		roadArray[0].west = westPair.getRight();
		if (northPair.getLeft() > 1 || middleLeft > 1 || middleTop > 1 || westPair.getRight() > 1) {
			active0 = false;
		}
		roadArray[0].activeness = active0;

		/// topRight
		boolean active1 = true;
		roadArray[1].north = northPair.getRight();
		roadArray[1].south = middleRight;
		roadArray[1].east = eastPair.getRight();
		roadArray[1].west = middleTop;
		if (northPair.getRight() > 1 || middleRight > 1 || eastPair.getRight() > 1 || middleTop > 1) {
			active1 = false;
		}
		roadArray[1].activeness = active1;

		// bottomLeft
		boolean active2 = true;
		roadArray[2].north = middleLeft;
		roadArray[2].south = southPair.getLeft();
		roadArray[2].east = middleBottom;
		roadArray[2].west = westPair.getLeft();
		if (middleLeft > 1 || southPair.getLeft() > 1 || middleBottom > 1 || westPair.getLeft() > 1) {
			active2 = false;
		}
		roadArray[2].activeness = active2;

		// bottomRight
		boolean active3 = true;
		roadArray[3].north = middleRight;
		roadArray[3].south = southPair.getRight();
		roadArray[3].east = eastPair.getLeft();
		roadArray[3].west = middleBottom;
		if (middleRight > 1 || southPair.getRight() > 1 || eastPair.getLeft() > 1 || middleBottom > 1) {
			active3 = false;
		}
		roadArray[3].activeness = active3;
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
	private int findNum(int side2, int side1) {
		if ((side1 == 0 && side2 == 1) || (side2 == 0 && side1 == 1)) {
			return 1;
		}
		
		int val = (side1 + side2) / 2;
		return val;
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

		List<Shape> shapes = new ArrayList<Shape>();
		
		if (north == 1) {
			if (east == 0) {
				RectangularPrism col = new RectangularPrism(10, 1400, 10, topLeft.getX(), 0, topLeft.getZ());
				shapes.add(col);
			}
			RectangularPrism road = new RectangularPrism(50, 10, len / 4, center.getX()-(len/2), 1400, center.getZ() - len / 4);
			road.setMaterialColor(RenderColor.Black);
			shapes.add(road);
			RectangularPrism line = new RectangularPrism(5, 7, len/8-4, center.getX()-(len/2), 1410, center.getZ()- len/4);
			line.setMaterialColor(RenderColor.White);
			shapes.add(line);
			
		}
		if (south == 1) {/*
			if (west == 0) {
				RectangularPrism col = new RectangularPrism(10, 1400, 10, bottomLeft.getX(), 0, bottomLeft.getZ());
				shapes.add(col);
			}*/
			RectangularPrism road = new RectangularPrism(50, 10, len / 4, center.getX()-(len/2), 1400, center.getZ() + len / 4);
			shapes.add(road);
			road.setMaterialColor(RenderColor.Black);
			RectangularPrism line = new RectangularPrism(5, 7, len/8-4, center.getX()-(len/2), 1410, center.getZ()+len/4);
			line.setMaterialColor(RenderColor.White);
			shapes.add(line);
		}
		if (west == 1) {
			if (south == 0) {
				RectangularPrism col = new RectangularPrism(10, 1400, 10, topLeft.getX(), 0, topLeft.getZ());
				shapes.add(col);
			}
			RectangularPrism road = new RectangularPrism(len / 4, 10, 50, center.getX() + len / 4, 1400, center.getZ()-(len/2));
			shapes.add(road);
			road.setMaterialColor(RenderColor.Black);
			RectangularPrism line = new RectangularPrism(len/8-4, 7, 5, center.getX()+(len/4), 1410, center.getZ()-len/2);
			line.setMaterialColor(RenderColor.White);
			shapes.add(line);
		}
		if (east == 1) {/*
			if (north == 0) {
				RectangularPrism col = new RectangularPrism(10, 1400, 10, topRight.getX(), 0, topRight.getZ());
				shapes.add(col);
			}*/
			RectangularPrism road = new RectangularPrism(len / 4, 10, 50, center.getX() - len / 4, 1400, center.getZ()-(len/2));
			shapes.add(road);
			road.setMaterialColor(RenderColor.Black);
			RectangularPrism line = new RectangularPrism(len/8-4, 7, 5, center.getX()-(len/4), 1410, center.getZ()-len/2);
			line.setMaterialColor(RenderColor.White);
			shapes.add(line);
		}/*
		if (north ==1 && south == 0) {
			RectangularPrism col = new RectangularPrism(10, 1400, 10, topLeft.getX(), 0, topLeft.getZ());
			shapes.add(col);
		}*/
		return shapes;

	}

	@Override
	public boolean isActive() {
		return activeness;
	}

}
