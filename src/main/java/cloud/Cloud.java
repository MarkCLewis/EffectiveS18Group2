package cloud;
import virtualworld.terrain.Perlin;

public class Cloud {
	
	public static void main (String[] args)
	{
		Cloud sample = new Cloud (20,10,30);
		sample.makeCloudArray();
		double [][] cloudArr = sample.getCloud();
		for (int y = 0; y < 20; y++){
			for (int x = 0; x< 10; x++){
				System.out.printf("%.5f ", cloudArr[y][x]);
			}
			System.out.println();
		}
	}
	
	public Cloud (int newWidth, int newLength, int newHeight)
	{
		width = newWidth;
		length = newLength;
		height = newHeight;
		func = Perlin.getInstance();	
		dimArr = new double[width][length];
		makeCloudArray();
	}
	//determine how big the cloud is 
	private final int width;
	private final int length;
	private final int height;
	Perlin func;
	private double[][] dimArr; //actual representation of the cloud, might have to change later 
			
	private void makeCloudArray()
	{
		//2d array to represent 2d cloud
		double yOff = 0;
		for (int y = 0; y < width; y++){
			double xOff = 0;
			for (int x = 0; x< length; x++){
				dimArr[y][x] = func.noise2D(xOff, yOff);
				xOff += 0.01;
			}
			yOff += 0.01;
		}
	}
	
	public double[][] getCloud (){
		return dimArr;
	}
	
}




