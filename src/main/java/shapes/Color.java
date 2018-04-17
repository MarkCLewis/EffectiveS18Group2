package shapes;

public class Color {
	private int R;
	private int G;
	private int B;
	private int A;
	
	public Color(int r, int g, int b, int a) {
		R = r;
		G = g;
		B = b;
		A = a;
	}
	
	public Color(Color c) {
		this.R = c.getRed();
		this.G = c.getGreen();
		this.B = c.getBlue();
		this.A = c.getAlpha();
	}
	
	public int getRed() {
		return R;
	}
	
	public int getGreen() {
		return G;
	}
	
	public int getBlue() {
		return B;
	}
	
	public int getAlpha() {
		return A;
	}
	
	public void setRed(int r) {
		R = r;
	}
	
	public void setGreen(int g) {
		G = g;
	}
	
	public void setBlue(int b) {
		B = b;
	}
	
	public void setAlpha(int a) {
		A = a;
	}
}
