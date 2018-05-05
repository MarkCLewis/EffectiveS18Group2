package virtualworld.terrain;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import virtualworld.terrain.Point;

public class JFXWorldTerrain extends Application {
	
	private int height = 800;
	private int width  = 800;
	private double x = 400; 
	private double z = 400;
	private Point p = new Point(x,z);
	private WorldHeightAlgorithm world = new WorldHeightAlgorithm(p, 800);
	
	
	@Override public void start(Stage stage) {
		Group group = new Group();
		Scene scene = new Scene(group,height,width,Color.WHITE);
		
		Rectangle center = new Rectangle();
		center.setX(0);
		center.setY(0);
		center.setWidth(5);
		center.setHeight(5);
		center.setFill(Color.BLUE);
		center.setStrokeWidth(3);
		group.getChildren().add(center);
		
		Rectangle t = new Rectangle();
		t.setX(0);
		t.setY(0);
		t.setWidth(height);
		t.setHeight(width);
		t.setFill(Color.TRANSPARENT);
		t.setStroke(Color.BLUE);
		t.setStrokeWidth(3);
		group.getChildren().add(t);
		
		List<Region> regions = world.getRegions();
		Line line1 = new Line(world.l1t.getX(), world.l1t.getZ(),world.l1b.getX(), world.l1b.getZ());
		Line line2 = new Line(world.l2t.getX(), world.l2t.getZ(),world.l2b.getX(), world.l2b.getZ());
		line1.setStrokeWidth(5);
		line1.setFill(Color.GREEN);
		line2.setStrokeWidth(5);
		line2.setFill(Color.RED);
		group.getChildren().add(line1);
		group.getChildren().add(line2);
		int colorInc = 0;
		for(Region r: regions) {
			Rectangle rect = new Rectangle();
			Point c = r.getTopLeft();
			System.out.println("TopLeft " + c.getX() + " " + (c.getZ() - 800));
			rect.setX(c.getX());
			if (c.getZ() - 800 < 0) {
				rect.setY(c.getZ());
			} else {
				rect.setY(c.getZ() - 800);
			}
			rect.setWidth(r.getWidth());
			rect.setHeight(r.getHeight()); 
			rect.setFill(Color.TRANSPARENT);
			if (colorInc == 0) {
				rect.setStroke(Color.ORANGE);	
			} else if (colorInc ==1) {
				rect.setStroke(Color.PURPLE);
			} else {
				rect.setStroke(Color.AQUAMARINE);
			}
			rect.setStrokeWidth(3);
			group.getChildren().add(rect);
			colorInc += 1;
		}
		//Canvas canvas = new Canvas(width,height);
		//group.getChildren().add(canvas);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch();
	}

}
