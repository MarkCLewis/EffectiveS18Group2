
package towns;



import virtualworld.terrain.Pair;



public class Towns {
        private douhle radius, center;
	private double [] middle;
	private Pair Shopcenter, Schoolcenter, Housecenter, Parkcenter;


	
	public static Town(Pair<Double, Double> cent, Double rad) {
		radius= rad;
		center= cent;
		double [] middle= GenerateCenters(center, radius); 
		Shopscenter= middle[0];
		Housecenter= middle [1];
		Parkcenter= middle [2];
		Schoolcenter= middle [3];
	}
  
        public static double [] GenerateCenters (Pair<Double, Double> cent, Double rad){
		Pair shop= {Pair(cent.left-sqrt(Math.pow(rad,2)/2), cent.right-sqrt(Math.pow(rad,2)/2))}; 
		//shopCenter
		Pair house= {Pair(cent.left+sqrt(Math.pow(rad,2)/2),cent.left+sqrt(rad^2)/2))};
		//houseCenter
		Pair park={Pair(cent.left-sqrt(Math.pow(rad,2)/2),cent.left+sqrt(rad^2)/2))};
		//parkCenter
		Pair school= {Pair(cent.left+sqrt(Math.pow(rad,2)/2),cent.left-sqrt(rad^2)/2))};
		//schoolCenter
		double[] passArray = new double[]{1,2,3,4,5,6,7,8,9,10}; 
		return (pass);
	}
	//public createShops(cent, rad)
	//public House(cent,rad)
	//public Park(cent,rad)
	//public School(cent,rad)
	 


}
