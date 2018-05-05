package shapes;

public class RenderColor {
	
	public static RenderColor Red = new RenderColor(255,0,0,255);
	public static RenderColor Yellow = new RenderColor(255,255,0,255);
	public static RenderColor Green = new RenderColor(0,255,0,255);
	public static RenderColor Blue = new RenderColor(0,0,255,255);
	public static RenderColor Black = new RenderColor(0,0,0,255);
	public static RenderColor White = new RenderColor(255,255,255,255);
	public static RenderColor LightGrey = new RenderColor(200,200,200,255);
	public static RenderColor MediumGrey = new RenderColor(100,100,100,255);
	public static RenderColor DarkGrey = new RenderColor(40,40,40,255);
	public static RenderColor VeryDarkGrey = new RenderColor(5,5,5,255);
	
	private int R;
	private int G;
	private int B;
	private int A;
	
	public RenderColor(int r, int g, int b, int a) {
		R = r;
		G = g;
		B = b;
		A = a;
	}
	
	public RenderColor(RenderColor c) {
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
