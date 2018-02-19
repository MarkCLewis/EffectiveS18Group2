package cloud;

public class Cloud {
	
	public Cloud (int newWidth, int newLength, int newHeight)
	{
		width = newWidth;
		length = newLength;
		height = newHeight;
	}
	//determine how big the cloud is 
	private final int width;
	private final int length;
	private final int height;
	
	private double[][] dimArr; //actual representation of the cloud, might have to change later 
			
	private void makeCloudArray()
	{
		//todo
		//call perlin noise to change dimArr
	}
	
	public double[][] getCloud (){
		return dimArr;
	}
	
}




