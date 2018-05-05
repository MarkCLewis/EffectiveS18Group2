package engine;

import java.util.List;

import shapes.HeightMapSurface;
import shapes.RectangularPrism;
import shapes.RenderColor;
import shapes.RenderMaterial;
import shapes.Sphere;
import shapes.VectorCylinder;
import virtualworld.terrain.Point;
import virtualworld.terrain.Terrain;
import worldmanager.WorldManager;

public class Game {
  
	public Engine e;
	
	public Game(float waterHeight) {
		e = Engine.getInstance();
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

		Game g = new Game(450f);
		g.start();
		
		/*Terrain t = Terrain.forMountains(new Point(0,0), 2048, 6);
		
		RenderMaterial hmsMat = new RenderMaterial();
		hmsMat.setUseTexture(true);
		hmsMat.setTextureDiffusePath("Textures/Terrain/splat/grass.jpg");
		hmsMat.setTextureNormalPath("Textures/Terrain/splat/grass_normal.jpg");
		Terrain[] ters = t.split();
		for(int i = 0; i < ters.length; i++) {
			Terrain[] tmpters = ters[i].split();
			HeightMapSurface hms0 = tmpters[0].getHeightMapSurface();
			hms0.setMaterial(hmsMat);
			g.addShape(hms0);
			HeightMapSurface hms1 = tmpters[1].getHeightMapSurface();
			hms1.setMaterial(hmsMat);
			g.addShape(hms1);
			HeightMapSurface hms2 = tmpters[2].getHeightMapSurface();
			hms2.setMaterial(hmsMat);
			g.addShape(hms2);
			HeightMapSurface hms3 = tmpters[3].getHeightMapSurface();
			hms3.setMaterial(hmsMat);
			g.addShape(hms3);
		}*/
		
		/*RenderMaterial cloudMat = new RenderMaterial();
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
			
			RectangularPrism rp = new RectangularPrism(10,10,10,x,y+100,z);
			g.addShape(rp);
		}*/
		
		/*WorldManager.initializeWorld();
		List<shapes.Shape> allShapes = world.getGeometry(center);
		g.addShapes(allShapes);*/
	}

  
}
