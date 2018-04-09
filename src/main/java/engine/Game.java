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
			rPs.add(new shapes.RectangularPrism(1, 1, 1, Engine.getRandomDouble(0, 10), Engine.getRandomDouble(0, 10), Engine.getRandomDouble(0, 10)));
		}
		g.start();
		g.addShapes(rPs);
	}
  
}