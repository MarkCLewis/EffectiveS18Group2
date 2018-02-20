
package towns;



import virtualworld.terrain.Pair;



public class Towns {


	
	public static Town(Pair<Double, Double> cent, Double rad) {
		radius= rad;
		center= cent;
		Genera= GenerateCenters(center, radius); 

	}
  
        public static double [] GenerateCenters (Pair<Double, Double> cent, Double rad){
		shop= {Pair(cent.left-sqrt(Math.pow(rad,2)/2), cent.right-sqrt(Math.pow(rad,2)/2))}; 
		//shopCenter
		house= {Pair(cent.left+sqrt(Math.pow(rad,2)/2),cent.left+sqrt(rad^2)/2))};
		//houseCenter
		park={Pair(cent.left-sqrt(Math.pow(rad,2)/2),cent.left+sqrt(rad^2)/2))};
		//parkCenter
		school= {Pair(cent.left+sqrt(Math.pow(rad,2)/2),cent.left-sqrt(rad^2)/2))};
		//schoolCenter
	}
	public createShops()
	public House(cent,rad)
	public Park(cent,rad)
	public School(cent,rad)
	 


	

}
