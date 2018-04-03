package engine;

public class Game {
  
	private Engine e;
	
	public Game() {
		e = Engine.getInstance();
	}
  
	public void addShape(shapes.Shape shape) {
		e.addShape(shape);
	}
	
	public void start() {
		e.start();
	}
  
	/**
	 * Example application
	 * First you need a Game instance and then
     * you can add your shapes
     * @param args
     */
	public static void main(String[] args) {
		Game g = new Game();
		shapes.RectangularPrism rP = new shapes.RectangularPrism(1, 1, 1, 5, 5, 0);
		g.addShape(rP);
		g.start();
	}
  
}