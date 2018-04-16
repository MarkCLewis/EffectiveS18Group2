package engine;

import java.util.ArrayList;

public class Game {
  
	private Engine e;
	
	public Game() {
		e = Engine.getInstance();
	}
  
	public void addShapes(ArrayList<shapes.Shape> shapes) {
		e.changeShapes(shapes);
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
		ArrayList<shapes.Shape> rPs = new ArrayList<shapes.Shape>();
		for (int i = 0; i < 10; i++) {
			// Note: terrain is about 150 units high at the tallest, so try to keep your object's Y-coordinate above 150
			rPs.add(new shapes.RectangularPrism(0.2f, 0.2f, 0.2f, Engine.getRandomDouble(0,20), Engine.getRandomDouble(150, 300), Engine.getRandomDouble(0,20)));
		}
		g.start();
		g.addShapes(rPs);
	}
  
}