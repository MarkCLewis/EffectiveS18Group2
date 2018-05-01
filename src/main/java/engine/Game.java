package engine;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.geom.Shape;

import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;
import worldmanager.WorldManager;

public class Game {
  
	private Engine e;
	
	public Game(float waterHeight) {
		e = Engine.getInstance(waterHeight);
	}
  
	public void addShapes(List<shapes.Shape> shapes) {
		e.addShapes(shapes);
	}
	
	public void addShape(shapes.Shape shape) {
		e.addShape(shape);
	}
	
	public void replaceShapes(List<shapes.Shape> shapes) {
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
		Game g = new Game(400f);
		g.start();
		Point center = new Point(0,0);
		
		//WorldManager world = new WorldManager(center, 5000);		
		WorldManager world = WorldManager.getInstance();
		
		Terrain t = Terrain.forFeilds(center, 2048, 16);

		Terrain[] ters = t.split();
		List<shapes.Shape> shapes = new ArrayList<shapes.Shape>();
		for(int i = 0; i < ters.length; i++) {
			Terrain[] tmpters = ters[i].split();
			g.addShape(tmpters[0].getHeightMapSurface());
			g.addShape(tmpters[1].getHeightMapSurface());
			g.addShape(tmpters[2].getHeightMapSurface());
			g.addShape(tmpters[3].getHeightMapSurface());
		}
	}
  
}