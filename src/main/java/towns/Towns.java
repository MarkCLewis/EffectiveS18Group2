
package towns;



import java.util.ArrayList;

//import virtualworld.terrain.Pair;
import virtualworld.terrain.Point;


public class Towns extends Entity {
        private double radius, seed;
	private ArrayList<Point> middle;
	private Point Shopcenter, Schoolcenter, Housecenter, Parkcenter, center;
	private RectangularPrism Shops, House, Park, School;
	

//DrawSqure takes a radius of Square and center of the rectangle Once implemented
	
	public Towns (Point cent) {
		seed= math.random()*50;
		radius= seed* mthpnt (cent);
		center= cent;
		middle= GenerateCenters(center, radius); 
		Shopcenter= middle.get(0);
		Housecenter= middle.get(1);
		Parkcenter= middle.get(2);
		Schoolcenter= middle.get(3);
		
		Shops=DrawSquare((2*radius/6), 0.5, (2*radius/6), Shopscenter.getX(), getHeightAt(center), ShopCenter.getY());         
		House=DrawSquare((2*radius/6), 0.5, (2*radius/6), Housecenter.getX(), getHeightAt(center), HouseCenter.getY());
		Park=DrawSquare((2*radius/4), 0.5, (2*radius/4), Parkcenter.getX(), getHeightAt(center), Parkcenter.getY());
		School=DrawSquare((2*radius/6), 0.5, (2*radius/6), Parkcenter.getX(), getHeightAt(center), Schoolcenter.getY());
		// lays out a base for each quadrant on at 0.5 thick block
		// some sections occupy a different amount of their respective quadrant, for example,
		// the park occupies from edge to edge the size of its quadrant
	}
	public existingTown (Point cent, double entryseed){
	        seed=entryseed;
		radius= seed* mthpnt (cent);
		center= cent;
		middle= GenerateCenters(center, radius); 
		Shopcenter= middle.get(0);
		Housecenter= middle.get(1);
		Parkcenter= middle.get(2);
		Schoolcenter= middle.get(3);
		
		Shops=DrawSquare((2*radius/6), 0.5, (2*radius/6), Shopscenter.getX(), getHeightAt(center), ShopCenter.getY());         
		House=DrawSquare((2*radius/6), 0.5, (2*radius/6), Housecenter.getX(), getHeightAt(center), HouseCenter.getY());
		Park=DrawSquare((2*radius/4), 0.5, (2*radius/4), Parkcenter.getX(), getHeightAt(center), Parkcenter.getY());
		School=DrawSquare((2*radius/6), 0.5, (2*radius/6), Parkcenter.getX(), getHeightAt(center), Schoolcenter.getY());
	}
  
    public static ArrayList<Point> GenerateCenters (Point cent, Double rad){
		Point shop = new Point(cent.getLeft() - Math.sqrt(Math.pow(rad,2)/2), cent.getRight() - Math.sqrt(Math.pow(rad,2)/2)); 
		//shopCenter SOUTHWEST
		Point house = new Point(cent.getLeft() + Math.sqrt(Math.pow(rad,2)/2),cent.getLeft() + Math.sqrt(Math.pow(rad,2))/2);
		//houseCenter NORTHEAST
		Point park = new Point (cent.getLeft() - Math.sqrt(Math.pow(rad,2)/2),cent.getLeft() + Math.sqrt(Math.pow(rad,2))/2);
		//parkCenter NORTHWEST
		Point school = new Point (cent.getLeft() + Math.sqrt(Math.pow(rad,2)/2),cent.getLeft() - Math.sqrt(Math.pow(rad,2))/2);
		//schoolCenter SOUTHEAST
		ArrayList<Point> pass = new ArrayList<Point>();
		pass.add(shop);
		pass.add(house);
		pass.add(park);
		pass.add(school);
		return (pass);
	}
    private int mthpnt (Point k){
    	 	int x= k.getX;
		int y= k.getY;
		if ((x/y)<1.0){
			return (4+(x/y));
		}else{
			return (x/y);
		}
    }
    public Point getCenter (Towns p){ 
    		return middle;
    }
    public double getSeed (Towns p){ 
    		return seed; 
    }
    //private int town ratios
	//public createShops(cent, rad)
	//public House(cent,rad)
	//public Park(cent,rad)
	//public School(cent,rad)
	 

*/
}
