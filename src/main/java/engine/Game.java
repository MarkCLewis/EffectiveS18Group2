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
		Game g = new Game(250f);
		Point center = new Point(0,0);
		
		WorldManager world = new WorldManager(center, 5000);		

		Terrain t = Terrain.forFeilds(center, 2048, 16);
		Terrain[] ters = t.split();
		List<shapes.Shape> shapes = new ArrayList<shapes.Shape>();
		for(int i = 0; i < ters.length; i++) {
			Terrain[] tmpters = ters[i].split();
			shapes.add(tmpters[0].getHeightMapSurface());
			shapes.add(tmpters[1].getHeightMapSurface());
			shapes.add(tmpters[2].getHeightMapSurface());
			shapes.add(tmpters[3].getHeightMapSurface());
		}
		g.start();
		g.addShapes(shapes);
	}
  
}