package towns;
import worldmanager.Node;
import java.lang.Object;
import worldmanager.WorldManager;
import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;
import entity.Entity;
import shapes.RectangularPrism;
import shapes.Shape;
import shapes.Cylinder;
import shapes.RenderMaterial;
//import virtualworld.terrain.Pair;

public class Towns implements Entity {
    private float radius, seed;
	private ArrayList<Point> middle;
	private ArrayList<RectangularPrism> shopStalls, completeListofRec, houselist;
	private Point Shopcenter, Schoolcenter, Housecenter, Parkcenter, center;
	private RectangularPrism Shops;
	private RectangularPrism House;
	private RectangularPrism Park;
	private RectangularPrism School; 
	private RectangularPrism Townwallnorth;
	private RectangularPrism Townwallsouth;
	private RectangularPrism Townwalleast; 
	private RectangularPrism Townwallwest;
	private Cylinder Towntower;
	private ArrayList<Cylinder> grass, completeListofCyc;
	private double houseseed, schoolseed ;
	
//DrawSqure takes a radius of Square and center of the rectangle Once implemented

	
	
	public Towns (Point cent, float ... a) {
		for (float i: a){
		    seed=i;
		}
		
		seed= (float) (Math.random()*50);
		radius= seed* mthpnt (cent);
		center= cent;
		middle= GenerateCenters(center, radius); 
		main holdingtower= middle(0);
		//BASES
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
		
		//WALLS
	    Townwallnorth= new RectangularPrism ((float)0.5, (float) (radius*2), (float) 20, center.getX()+radius, 
	    		WorldManager.getInstance().getHeight(center), center.getZ());
	    Townwallsouth= new RectangularPrism ((float)0.5, (float) (radius*2), (float) 20, center.getX()-radius, 
	    		WorldManager.getInstance().getHeight(center), center.getZ());
	    Townwalleast= new RectangularPrism ((float)0.5, (float) (radius*2), (float) 20, center.getX(),
	    		WorldManager.getInstance().getHeight(center), center.getZ()+radius);
	    Townwallwest= new RectangularPrism ((float)0.5, (float) (radius*2), (float) 20, center.getX(), 
	    		WorldManager.getInstance().getHeight(center), center.getZ());
	    //Tower
	    Towntower=new Cylinder((float)(20-seed), (float) 3, center.getX(),center.getZ(),
	    		WorldManager.getInstance().getHeight(center)); 
		
	    //Grass
	    grass=Makegrass(Parkcenter, (radius/2));
	   
	    //School generation
	    //House generation
	    //Both will rely on seed to determine the amount of school buildings and houses
	    //set to a maximum of10 and must be greater than one
	    houseseed=Math.floor(seed/5);
	    double housesizes= Math.floor(Math.floor(radius)/(houseseed));
	    houselist= MakeHouses((int) houseseed, (int) housesizes, Housecenter,radius/3);
	    schoolseed=Math.floor(seed/6);
	    
	    double schoolsizes=Math.floor(Math.floor(seed/6)/(radius/3))-1;
	    
	    //makeHouses= MakeHouse(Housecenter,radius /2 ))
	    
	 
	    
	    
	    
		
		
	}
	public ArrayList<RectangularPrism> MakeHouses(int amount, int sizeofhouses, Point housecentr, double rad){
		ArrayList<RectangularPrism> house= new ArrayList<RectangularPrism>();
		int temp=0;
		Double stop =housecentr.getX()+rad;
		Point Constr=new Point (housecentr.getX()-rad, housecentr.getZ()-rad);
		Point Sentinel= new Point (housecentr.getX()-rad, housecentr.getZ()-rad);
		while (temp< amount){
			double shiftY=WorldManager.getInstance().getHeight(Sentinel);
			house.add(new RectangularPrism((float) sizeofhouses, (float) sizeofhouses+8,(float) sizeofhouses, Sentinel.getX(), 
					shiftY, Sentinel.getZ()));
			double newX=Sentinel.getX()+ sizeofhouses + 3;
			temp=temp+1;
			//double newZ=Starter.getY+.2;
			if (newX>stop&&Sentinel.getZ()<Constr.getZ()+rad){
				Sentinel=new Point (Constr.getX(),Sentinel.getZ()+3);
			}else{
				Sentinel=new Point(newX,Sentinel.getZ());
						
			}
		}
		return house;
		
	}
//public destroyTown{}
	public ArrayList<Cylinder> Makegrass(Point grasseed, double Patchrad){
		//dont need two points if im just doing a square patch
		//radius will serve as both dimensions
		ArrayList<Cylinder> grass= new ArrayList<Cylinder>();
		Double temp=grasseed.getX()-Patchrad; 
		Double stop =grasseed.getX()+Patchrad;
		Point Constr=new Point (grasseed.getX()-Patchrad, grasseed.getZ()-Patchrad);
		Point Sentinel= new Point (grasseed.getX()-Patchrad, grasseed.getZ()-Patchrad);
		while (temp< stop){
			double shiftY=WorldManager.getInstance().getHeight(grasseed);
			grass.add(new Cylinder ( (float)0.3, (float).0125, Sentinel.getX(),shiftY, Sentinel.getZ()));
			double newX=Sentinel.getX()+.2;
			//double newZ=Starter.getY+.2;
			if (newX>stop&&Sentinel.getZ()<Constr.getZ()+Patchrad){
				Sentinel=new Point (Constr.getX(),Sentinel.getZ()+.2);
			}else{
				Sentinel=new Point(newX,Sentinel.getZ());
						
			}
		}
		return grass;
	}
	private void resetGShifter(Point seed, double PatchR){
		//return Point(seed.getX()-PatchR, PatchR) 
	}
	public ArrayList<RectangularPrism> StallGenerator(Point a, float base, float dist, float stallsize, float sed){
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
		Point ArrondTowee = new Point(cent.getX() - r); 
		
		
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
		//using cloud's method
		double close= 3000;
		double farther = 6000;
		
		if (dist < close){
			color("normal");
		}
		else {
			color("far");
		}

	@Override
	public List<Shape> getShapes() {

		Shape [] arrC=completeListofCyc.toArray(new Shape [completeListofCyc.size()]);
		Shape [] arrR=completeListofRec.toArray(new Shape [completeListofRec.size()]);
		ArrayList<Shape> work=new ArrayList<Shape>();
		for(int i = 0; i<arrC.length;i++){
			work.add(arrC[i]);
		}
		int o= work.size();
		for(int i = 0; i<arrR.length;i++){
			work.add(arrR[i]);
		}
		Shape [] gd=work.toArray(new Shape [work.size()]);
		List<Shape> list=Arrays.asList(gd);
		return list;
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
