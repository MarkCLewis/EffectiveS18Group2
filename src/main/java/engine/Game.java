package engine;

import java.util.ArrayList;
import java.util.List;

public class Game {
  
	private Engine e;
	
	public Game() {
		e = Engine.getInstance();
	}
  
	public void addShapes(List<shapes.Shape> shapes) {
		e.changeShapes(shapes);
	}
	
	public void start() {
		e.start();
	}
  
	/**
	 * Example application
	 * First you need a Game instance and then
     * you can add your shapes.
     * The "Game.addShapes" function takes an ArrayList of Shapes, and adds all of them to the renderer.
     * @param args
     */
	public static void main(String[] args) {
		Game g = new Game();
		ArrayList<shapes.Shape> rPs = new ArrayList<shapes.Shape>();
		for (int i = 0; i < 10; i++) {
			// Note: terrain is about 150 units high at the tallest, so try to keep your object's Y-coordinate above 150
			rPs.add(new shapes.Quad(5f, new float[] {1,1,-1,-1}, Engine.getRandomDouble(0,20), Engine.getRandomDouble(150, 300), Engine.getRandomDouble(0,20)));
			rPs.add(new shapes.Cylinder(5f, 2f, Engine.getRandomDouble(0,20), Engine.getRandomDouble(150, 300), Engine.getRandomDouble(0,20), Engine.getRandomFloat(0,20), Engine.getRandomFloat(150, 300), Engine.getRandomFloat(0,20)));
		}
		g.start();
		g.addShapes(rPs);
	}
  
}