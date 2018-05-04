package engine;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.geom.Shape;

import shapes.Cylinder;
import shapes.HeightMapSurface;
import shapes.RectangularPrism;
import shapes.RenderMaterial;
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

		Game g = new Game(250f);
		g.start();
		Point center = new Point(0,0);
		
		//WorldManager world = new WorldManager(center, 5000);		
		WorldManager world = WorldManager.getInstance();
		world.updateMaxView(100000);
		
		

		/*Terrain t = Terrain.forMountains(center, 2048, 16);
		world.addEntity(t);


		RenderMaterial hmsMat = new RenderMaterial();
		hmsMat.setUseTexture(true);
		hmsMat.setTextureDiffusePath("Textures/Terrain/splat/grass.jpg");
		hmsMat.setTextureNormalPath("Textures/Terrain/splat/grass_normal.jpg");
		Terrain[] ters = t.split();
		System.out.println("Initial Height: " + ters[3].getHeightAt(new Point(200,-200)));
		for(int i = 0; i < ters.length; i++) {
			world.addEntity(ters[i]);
			Terrain[] tmpters = ters[i].split();
			
			HeightMapSurface hms0 = tmpters[0].getHeightMapSurface();

			hms0.setMaterial(hmsMat);
			world.addEntity(tmpters[0]);
			HeightMapSurface hms1 = tmpters[1].getHeightMapSurface();
			hms1.setMaterial(hmsMat);
			world.addEntity(tmpters[1]);
			HeightMapSurface hms2 = tmpters[2].getHeightMapSurface();
			hms2.setMaterial(hmsMat);
			world.addEntity(tmpters[2]);
			HeightMapSurface hms3 = tmpters[3].getHeightMapSurface();
			hms3.setMaterial(hmsMat);
			world.addEntity(tmpters[3]);
			List<shapes.Shape> allShapes = world.getGeometry(center);
			//g.addShape(hms0);
			//g.addShape(hms1);
			//g.addShape(hms2);
			//g.addShape(hms3);
			g.addShapes(allShapes);
		}*/
		WorldManager.initializeWorld();
		List<shapes.Shape> allShapes = world.getGeometry(center);
		g.addShapes(allShapes);
		
	}
  
}