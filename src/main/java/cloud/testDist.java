package cloud;

import engine.Game;

public class testDist {
	public static void main(String[] args) {
		Game g = new Game(100f);
		CloudFactory cFactory = CloudFactory.getInstance();
		
		int type = 0;
		double size = cFactory.getRandomSize(0);
		
		Cloud a = cFactory.getCloud(0, 0, 0, type, size);
		
		g.start();
		g.addShapes(a.getShapes());
		
	}
}
