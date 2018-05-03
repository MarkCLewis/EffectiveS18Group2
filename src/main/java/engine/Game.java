package engine;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.geom.Shape;

import shapes.Cylinder;
import shapes.HeightMapSurface;
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
		world.updateMaxView(10000);
		
		

		Terrain t = Terrain.forWorld(center, 2048, 16);


		RenderMaterial hmsMat = new RenderMaterial();
		hmsMat.setUseTexture(true);
		hmsMat.setTextureDiffusePath("Textures/Terrain/splat/grass.jpg");
		hmsMat.setTextureNormalPath("Textures/Terrain/splat/grass_normal.jpg");
		Terrain[] ters = t.split();
		System.out.println("Initial Height: " + ters[3].getHeightAt(new Point(200,-200)));
		for(int i = 0; i < ters.length-1; i++) {
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
			g.addShape(hms0);
			g.addShape(hms1);
			g.addShape(hms2);
			g.addShape(hms3);
			//g.addShapes(allShapes);
		}
		/*for(Terrain ter: ters) {
			HeightMapSurface hms = ter.getHeightMapSurface();
			hms.setMaterial(hmsMat);
			g.addShape(hms);
		}*/
		Terrain[] temp = ters[3].split();
		System.out.println("after split height: " + ters[3].getHeightAt(new Point(200,-200)));
		System.out.println("split height: " + temp[0].getHeightAt(new Point(200,-200)));
		HeightMapSurface hms0 = temp[0].getHeightMapSurface();
		world.addEntity(temp[0]);
		HeightMapSurface hms1 = temp[1].getHeightMapSurface();
		world.addEntity(temp[1]);
		HeightMapSurface hms2 = temp[2].getHeightMapSurface();
		world.addEntity(temp[2]);
		HeightMapSurface hms3 = temp[3].getHeightMapSurface();
		world.addEntity(temp[3]);
		g.addShape(hms0);
		g.addShape(hms1);
		g.addShape(hms2);
		g.addShape(hms3);
		HeightMapSurface hms = ters[3].getHeightMapSurface();
		hms.setMaterial(hmsMat);
		g.addShape(hms);
	}
  
}