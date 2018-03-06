
package towns;



import virtualworld.terrain.Pair;



public class Towns {
        private douhle radius, center;
	private double [] middle;
	private Pair Shopcenter, Schoolcenter, Housecenter, Parkcenter;

//DrawRectangle takes a radius of rectangle and center
	
	public static Town(Pair<Double, Double> cent, Double townsize) {
		radius= townsize;
		center= cent;
		double [] middle= GenerateCenters(center, radius); 
		Shopscenter= middle[0];
		Housecenter= middle [1];
		Parkcenter= middle [2];
		Schoolcenter= middle [3];
		
		Shops=DrawRectangle(Shopscenter, r/6);         
		House=DrawRectangle(Housecenter, r/6);
		Park=DrawRectangle(Parkcenter, r/4);
		School=DrawRectangle(Schoolcenter, r/6);
		
		}
  
        public static {Pair [] GenerateCenters (Pair<Double, Double> cent, Double rad){
		Pair shop= Pair(cent.left-sqrt(Math.pow(rad,2)/2), cent.right-sqrt(Math.pow(rad,2)/2)); 
		//shopCenter SOUTHWEST
		Pair house= Pair(cent.left+sqrt(Math.pow(rad,2)/2),cent.left+sqrt(rad^2)/2));
		//houseCenter NORTHEAST
		Pair park=Pair(cent.left-sqrt(Math.pow(rad,2)/2),cent.left+sqrt(rad^2)/2));
		//parkCenter NORTHWEST
		Pair school= Pair(cent.left+sqrt(Math.pow(rad,2)/2),cent.left-sqrt(rad^2)/2));
		//schoolCenter SOUTHEAST
		Pair [] pass = new Pair[]{shop,house,park,school}; 
		return (pass);
	}
	//public createShops(cent, rad)
	//public House(cent,rad)
	//public Park(cent,rad)
	//public School(cent,rad)
	 


}
