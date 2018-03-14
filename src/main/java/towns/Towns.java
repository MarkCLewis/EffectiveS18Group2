
package towns;



import java.util.ArrayList;

import virtualworld.terrain.Pair;



public class Towns {
    private double radius;
	private ArrayList<Pair<Double,Double>> middle;
	private Pair<Double,Double> Shopcenter, Schoolcenter, Housecenter, Parkcenter, center;

//DrawSqure takes a radius of Square and center of the rectangle Once implemented
	
	public Towns(Pair<Double,Double> cent, Double townsize) {
		radius= townsize;
		center= cent;
		middle= GenerateCenters(center, radius); 
		Shopcenter= middle.get(0);
		Housecenter= middle.get(1);
		Parkcenter= middle.get(2);
		Schoolcenter= middle.get(3);
		
		//Shops=DrawSquare(Shopscenter, r/6);         
		//House=DrawSquare(Housecenter, r/6);
		//Park=DrawSquare(Parkcenter, r/4);
		//School=DrawSquare(Schoolcenter, r/6);
		
	}
  
    public static ArrayList<Pair<Double,Double>> GenerateCenters (Pair<Double,Double> cent, Double rad){
		Pair<Double,Double> shop = new Pair<Double,Double>(cent.getLeft() - Math.sqrt(Math.pow(rad,2)/2), cent.getRight() - Math.sqrt(Math.pow(rad,2)/2)); 
		//shopCenter SOUTHWEST
		Pair<Double,Double> house = new Pair<Double,Double>(cent.getLeft() + Math.sqrt(Math.pow(rad,2)/2),cent.getLeft() + Math.sqrt(Math.pow(rad,2))/2);
		//houseCenter NORTHEAST
		Pair<Double,Double> park = new Pair<Double,Double>(cent.getLeft() - Math.sqrt(Math.pow(rad,2)/2),cent.getLeft() + Math.sqrt(Math.pow(rad,2))/2);
		//parkCenter NORTHWEST
		Pair<Double,Double> school = new Pair<Double,Double>(cent.getLeft() + Math.sqrt(Math.pow(rad,2)/2),cent.getLeft() - Math.sqrt(Math.pow(rad,2))/2);
		//schoolCenter SOUTHEAST
		ArrayList<Pair<Double,Double>> pass = new ArrayList<Pair<Double,Double>>();
		pass.add(shop);
		pass.add(house);
		pass.add(park);
		pass.add(school);
		return (pass);
	}
    
	//public createShops(cent, rad)
	//public House(cent,rad)
	//public Park(cent,rad)
	//public School(cent,rad)
	 


}
