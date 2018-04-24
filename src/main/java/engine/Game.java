package engine;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.geom.Shape;

import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;
import worldmanager.WorldManager;

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
		
		WorldManager world = new WorldManager(center, 5000);		

		Terrain t = Terrain.forMountains(center, 10000, 10);
		Terrain[] ters = t.split();
		world.addEntity(t);
		List<shapes.Shape> shapes = t.getShapes();
		//List<shapes.Shape> shapes2 = world.getGeometry(center);
		List<shapes.Shape> shapes2 = ters[0].getShapes();
		shapes2.addAll(ters[1].getShapes());
		shapes2.addAll(ters[2].getShapes());
		shapes2.addAll(ters[3].getShapes());
		g.start();
		g.addShapes(shapes2);
		//g.addShapes(ters[0].getShapes());
		//g.addShapes(ters[1].getShapes());
		//g.addShapes(ters[2].getShapes());
		//g.addShapes(ters[3].getShapes());
		
		//g.addShapes(shapes);
	}
  
}