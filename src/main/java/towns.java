
package towns;



import virtualworld.terrain.Pair;



public class Towns {


	
	public Town(Pair<Double, Double> cent, Double rad) {
		radius= rad;
		center= cent;
		Arr= GenerateCenters(center, radius); 

	}
  
        public GenerateCenters (Pair<Double, Double> cent, Double rad){
		shop= {Pair(cent.left-sqrt(Math.pow(rad,2)/2), cent.right-sqrt(Math.pow(rad,2)/2))};
		house= {Pair(cent.left+sqrt(Math.pow(rad,2)/2),cent.left+sqrt(rad^2)/2))};
		park={Pair(cent.left-sqrt(Math.pow(rad,2)/2),cent.left+sqrt(rad^2)/2))};
		school= {Pair(cent.left+sqrt(Math.pow(rad,2)/2),cent.left-sqrt(rad^2)/2))};
	}
	//public Shops(cent,rad)
	//public House(cent,rad)
	//public Park(cent,rad)
	//public School(cent,rad)
	//sin(theta)= 


	

}