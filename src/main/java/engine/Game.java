package engine;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.geom.Shape;

import shapes.Cylinder;
import shapes.HeightMapSurface;
import shapes.RenderMaterial;
import shapes.Shape.PivotLocation;
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

		RenderMaterial hmsMat = new RenderMaterial();
		hmsMat.setUseTexture(true);
		hmsMat.setTextureDiffusePath("Textures/Terrain/splat/grass.jpg");
		hmsMat.setTextureNormalPath("Textures/Terrain/splat/grass_normal.jpg");
		Terrain[] ters = t.split();
		for(int i = 0; i < ters.length; i++) {
			Terrain[] tmpters = ters[i].split();
			
			HeightMapSurface hms0 = tmpters[0].getHeightMapSurface();
			hms0.setMaterial(hmsMat);
			HeightMapSurface hms1 = tmpters[1].getHeightMapSurface();
			hms1.setMaterial(hmsMat);
			HeightMapSurface hms2 = tmpters[2].getHeightMapSurface();
			hms2.setMaterial(hmsMat);
			HeightMapSurface hms3 = tmpters[3].getHeightMapSurface();
			hms3.setMaterial(hmsMat);
			g.addShape(hms0);
			g.addShape(hms1);
			g.addShape(hms2);
			g.addShape(hms3);
		}
		
		float rx = 0;
		float ry = 0;
		float rz = 0;
		for(int i = 0; i < 10; i++) {
			float radius = 20f;
			float height = 12f;
			double x = 0;
			double y = 450.0 + (i * height);
			double z = 0;
			
			Cylinder c = new Cylinder(height, radius, x - (radius*2.5), y, z, rx, ry, rz, true, PivotLocation.Bottom);
			Cylinder c1 = new Cylinder(height, radius, x + (radius*2.5), y, z, rx, ry, rz, true, PivotLocation.Top);
			Cylinder c2 = new Cylinder(height, radius, x, y, z, rx, ry, rz, true, PivotLocation.Center);
			g.addShape(c);
			g.addShape(c1);
			g.addShape(c2);
			
			ry = ry + ((float)Math.PI / 15f);
			rz = i * (2f * (float)(Math.PI) / 15f);
		}
	}
  
}