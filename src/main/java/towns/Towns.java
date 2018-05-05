
package towns;
import worldmanager.Node;
import java.lang.Object;
import worldmanager.WorldManager;
import java.util.ArrayList;
import java.util.List;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;
import entity.Entity;
import shapes.RectangularPrism;
import shapes.Shape;
import shapes.Cylinder;

//import virtualworld.terrain.Pair;


public class Towns implements Entity {
    private float radius, seed;
	private ArrayList<Point> middle;
	private ArrayList<RectangularPrism> shopStalls;
	private Point Shopcenter, Schoolcenter, Housecenter, Parkcenter, center;
	private RectangularPrism Shops, House, Park, School, 
			Townwallnorth, Townwallsouth, Townwalleast, Townwall, Townwallwest;
	private Cylinder Towntower;
	
//DrawSqure takes a radius of Square and center of the rectangle Once implemented
	
	public Towns (Point cent, float ... a) {
		for (float i: a){
		    seed=i;
		}
		seed= (float) (Math.random()*50);
		radius= seed* mthpnt (cent);
		center= cent;
		middle= GenerateCenters(center, radius); 
		Shopcenter= middle.get(0);
		Housecenter= middle.get(1);
		Parkcenter= middle.get(2);
		Schoolcenter= middle.get(3);
		
		Shops=new RectangularPrism((2*radius/6), (float) 0.5, (2*radius/6), Shopcenter.getX(), 
				WorldManager.getInstance().getHeight(center), Shopcenter.getZ()); 	
		House=new RectangularPrism((2*radius/6), (float) 0.5, (2*radius/6), Housecenter.getX(), 
				WorldManager.getInstance().getHeight(center), Housecenter.getZ());
		Park=new RectangularPrism((2*radius/4), (float) 0.5, (2*radius/4), Parkcenter.getX(), 
				WorldManager.getInstance().getHeight(center), Parkcenter.getZ());
		School=new RectangularPrism((2*radius/6), (float) 0.5, (2*radius/6), Parkcenter.getX(), 
				WorldManager.getInstance().getHeight(center), Schoolcenter.getZ());
		// lays out a base for each quadrant on at 0.5 thick block
		// some sections occupy a different amount of their respective quadrant, for example,
		// the park occupies from edge to edge the size of its quadrant
		
	    Townwallnorth= new RectangularPrism ((float)0.5, (float) (radius*2), (float) 20, center.getX()+radius, 
	    		WorldManager.getInstance().getHeight(center), center.getZ());
	    Townwallsouth= new RectangularPrism ((float)0.5, (float) (radius*2), (float) 20, center.getX()-radius, 
	    		WorldManager.getInstance().getHeight(center), center.getZ());
	    Townwalleast= new RectangularPrism ((float)0.5, (float) (radius*2), (float) 20, center.getX(),
	    		WorldManager.getInstance().getHeight(center), center.getZ()+radius);
	    Townwallwest= new RectangularPrism ((float)0.5, (float) (radius*2), (float) 20, center.getX(), 
	    		WorldManager.getInstance().getHeight(center), center.getZ());
	    
	    Towntower=new Cylinder((float)(20-seed), (float) 3, center.getX(),center.getZ(),WorldManager.getInstance().getHeight(center)); 
		
		
		
	}

  

	public static ArrayList<RectangularPrism> StallGenerator(Point a, float base, float dist, float stallsize, float sed){
        float z=dist;
        ArrayList<RectangularPrism> arr= new ArrayList <RectangularPrism>();
	    while(z<dist){
	    	arr.add(new RectangularPrism(sed/6, sed/12, 1, a.getX(), base, z+a.getZ()));
	    	z=z+stallsize;
	    } 
	    return arr;
    }
	/*public static Point shovePoint (Point a){
		double xPos=a.getX();
		double zPost=a.getZ();
		double
	}*/
	
	public static Cylinder shoveCylinder(Cylinder a){
		double xPos= a.getXPos();
		double yPos= a.getYPos()-1;
		double zPos= a.getZPos();
		a.setPosition(xPos, yPos, zPos);
		return a;
	}
	
	public static RectangularPrism shoveRect(RectangularPrism a){
		double xPos=a.getXPos();
		double yPos=a.getYPos()-1;
		double zPos=a.getZPos();
		a.setPosition(xPos, yPos, zPos);
		return a;
	}
	
    public static ArrayList<Point> GenerateCenters (Point cent, float rad){
		Point shop = new Point(cent.getX() - Math.sqrt(Math.pow(rad,2)/2), cent.getZ() - Math.sqrt(Math.pow(rad,2)/2)); 
		//shopCenter SOUTHWEST
		Point house = new Point(cent.getX() + Math.sqrt(Math.pow(rad,2)/2),cent.getZ() + Math.sqrt(Math.pow(rad,2))/2);
		//houseCenter NORTHEAST
		Point park = new Point (cent.getX() - Math.sqrt(Math.pow(rad,2)/2),cent.getZ() + Math.sqrt(Math.pow(rad,2))/2);
		//parkCenter NORTHWEST
		Point school = new Point (cent.getX() + Math.sqrt(Math.pow(rad,2)/2),cent.getZ() - Math.sqrt(Math.pow(rad,2))/2);
		//schoolCenter SOUTHEAST
		ArrayList<Point> pass = new ArrayList<Point>();
		pass.add(shop);
		pass.add(house);
		pass.add(park);
		pass.add(school);
		return (pass);
	}
	
    private int mthpnt (Point k){
    	 	double x= k.getX();
		    double y= k.getZ();
		if ((x/y)<1.0){
			return (int) Math.floor(4+(x/y));
		}else{
			return (int) Math.floor(x/y);
		}
    }
    public Point getCenter (Towns p){ 
    		return center;
    }
    public double getSize (Towns p){ 
    		return (double) seed; 
    }
    //private int town ratios
	//public createShops(cent, rad)
	//public House(cent,rad)
	//public Park(cent,rad)
	//public School(cent,rad)

	


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
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public Point getCenter() {
		// TODO Auto-generated method stub
		return center;
	}



	@Override
	public double getSize() {
		// TODO Auto-generated method stub
		return (double) seed;
	}



	

    
}
