package engine;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.geom.Shape;

import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;

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
		Point center = new Point(0,0);
		double[][] heightMap = {{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0},
				{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0},
				{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0},
				{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0},
				{0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0}};
		Terrain t = new Terrain(center, 5000, Engine.getRandomDouble(400, 600), 61, heightMap);
		List<shapes.Shape> shapes = t.getShapes();
		g.start();
		g.addShapes(shapes);
	}
  
}