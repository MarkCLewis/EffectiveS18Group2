package engine;

import java.util.List;

import org.joml.Vector3d;

import roads.Road;
import shapes.Cylinder
import virtualworld.terrain.Point;
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

		Game g = new Game(100f);
		g.start();
		Point center = new Point(0,0);
		
		//WorldManager world = new WorldManager(center, 5000);		
		WorldManager world = WorldManager.getInstance();

		world.updateMaxView(10000);
		
		/*Road road = new Road(center,200000);
		System.out.println(road.isActive());
		Road[] roadarray = road.split();
		for(Road r: roadarray) {
			g.addShapes(r.getShapes());
			System.out.println(r.isActive());
		}*/
		WorldManager.initializeWorld();
		List<shapes.Shape> allShapes = world.getGeometry(g.getLocation());
		g.addShapes(allShapes);
		
		System.out.println("allShapes size: " + allShapes.size());

		/*Terrain t = Terrain.forMountains(center, 2048, 6);
		world.addEntity(t);*/
		/*RectangularPrism r = new RectangularPrism(50, 50, 50, 1026, 250,-1026); 
		RectangularPrism r1 = new RectangularPrism(50, 50, 50, 1026, 250, 1026);
		RectangularPrism r2 = new RectangularPrism(50, 50, 50, -1026, 250, -1026);
		RectangularPrism r3 = new RectangularPrism(50, 50, 50, -1026, 250, 1026);
		RectangularPrism r4 = new RectangularPrism(50, 50, 50, 0, 250, 0);
		

		RenderMaterial hmsMat = new RenderMaterial();
		hmsMat.setUseTexture(true);
		hmsMat.setTextureDiffusePath("Textures/Terrain/splat/grass.jpg");
		hmsMat.setTextureNormalPath("Textures/Terrain/splat/grass_normal.jpg");
		Terrain[] ters = t.split();
		//System.out.println("Initial Height: " + ters[3].getHeightAt(new Point(200,-200)));
		//g.addShape(r);
		//g.addShape(r1);
		//g.addShape(r2);
		//g.addShape(r3);
		//g.addShape(r4);
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
		
		RenderMaterial cloudMat = new RenderMaterial();
		cloudMat.setSpecularColor(RenderColor.MediumGrey);
		cloudMat.setDiffuseColor(RenderColor.MediumGrey);
		cloudMat.setAmbientColor(RenderColor.MediumGrey);
		cloudMat.setUseTexture(true);
		cloudMat.setShininess(0.01f);
		cloudMat.setTextureDiffusePath("Textures/Terrain/splat/clouddiffuse.jpg");
		cloudMat.setTextureAlphaPath("Textures/Terrain/splat/cloudalpha.jpg");
		cloudMat.setUseTransparency(true);
		cloudMat.setColorAlphas(0.8f);
		float iX = 0;
		float iY = 450;
		float iZ = 0;
		for(int i = 0; i < 10; i++) {
			float x = iX + (i * 20);
			float y = iY + (i * 20);
			float z = iZ + (i * 20);
			VectorCylinder c = new VectorCylinder(
					10, 
					x, 
					y, 
					z, 
					x + 10, 
					y + 10, 
					z + 10);
			g.addShape(c);
			
			Sphere s = new Sphere(10,x,(y+50),z);
			s.setMaterial(cloudMat);
			g.addShape(s);
		}
		
		/*WorldManager.initializeWorld();
		List<shapes.Shape> allShapes = world.getGeometry(center);
		g.addShapes(allShapes);*/
	}

  
}
