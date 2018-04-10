
package towns;



import java.util.ArrayList;

//import virtualworld.terrain.Pair;
import virtualworld.terrain.Point;
//OLD, WILL PUSH NEW TONIGHT

public class Towns extends Entity {
        private double radius;
	private ArrayList<Point> middle;
	private Point Shopcenter, Schoolcenter, Housecenter, Parkcenter, center;
	private RectangularPrism Shops, House, Park, School;
	

//DrawSqure takes a radius of Square and center of the rectangle Once implemented
	
	public Towns(Point cent) {
		seed= math.random() *
		radius= 50* mthpnt (cent);
		center= cent;
		middle= GenerateCenters(center, radius); 
		Shopcenter= middle.get(0);
		Housecenter= middle.get(1);
		Parkcenter= middle.get(2);
		Schoolcenter= middle.get(3);
		
		Shops=DrawSquare((2*radius/6), 0.3, (2*radius/6), Shopscenter.getX(), getHeightAt(center), ShopCenter.getY());         
		House=DrawSquare((2*radius/6), 0.5, (2*radius/6), Shopscenter.getX(), getHeightAt(center), ShopCenter.getY());
		Park=DrawSquare(Parkcenter, 0.7, radius/4);
		School=DrawSquare(Schoolcenter, 0.9, radius/6);
		
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
    private int town ratios
	//public createShops(cent, rad)
	//public House(cent,rad)
	//public Park(cent,rad)
	//public School(cent,rad)
	 

*/
}
