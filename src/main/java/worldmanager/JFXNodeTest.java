package worldmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.Entity;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import shapes.Shape;
import virtualworld.terrain.Point;

public class JFXNodeTest extends Application {
	
	private int height = 800;
	private int width  = 800;
	private double x = 400;
	private double z = 400;
	private Point p = new Point(x,z);
	private WorldManager world = WorldManager.getInstance();
	private List<Entity> entlist = new ArrayList<>();
	
	private void populate() {
		Random rand = new Random();
		for(int i = 0; i < 100; i++) {
			double x = rand.nextDouble() + rand.nextInt(799);
			double y = rand.nextDouble() + rand.nextInt(799);
			Point p = new Point(x,y);
			TestEntity tester = new TestEntity(p,rand.nextInt(30)+5, 0);
			entlist.add(tester);
			world.addEntity(tester);
		}
	}
	
	private List<Node> getNodes(Node root) {
		List<Node> nodelist = new ArrayList<>();
		nodelist.add(root);
		for(Node n: root.children) {
			nodelist.addAll(getNodes(n));
		}
		return nodelist;
	}
	
	@Override public void start(Stage stage) {
		Group group = new Group();
		Scene scene = new Scene(group,height,width,Color.WHITE);
		world.updateMaxView(150);
		world.updateCameraStep(101);
		populate();
		List<Node> nodelist = getNodes(world.rootNode);
		for(Node n: nodelist) {
			Rectangle rect = new Rectangle();
			rect.setX(n.center.getX()-n.getSize()/2);
			rect.setY(n.center.getZ()-n.getSize()/2);
			rect.setWidth(n.getSize());
			rect.setHeight(n.getSize());
			rect.setFill(Color.TRANSPARENT);
			rect.setStroke(Color.BLACK);
			rect.setStrokeWidth(.5);
			group.getChildren().add(rect);
		}
		for(Entity e: entlist) {
			Circle circ = new Circle();
			circ.setCenterX(e.getCenter().getX());
			circ.setCenterY(e.getCenter().getZ());
			circ.setRadius(e.getSize()/2);
			circ.setFill(Color.BLUE);
			group.getChildren().add(circ);
		}
		List<Shape> shapes = world.getGeometry(p);
		Circle camera1 = new Circle();
		camera1.setCenterX(p.getX());
		camera1.setCenterY(p.getZ());
		camera1.setRadius(world.maxView);
		camera1.setFill(Color.TRANSPARENT);
		camera1.setStroke(Color.RED);
		camera1.setStrokeWidth(2);
		group.getChildren().add(camera1);
		for(Shape s: shapes) {
			Circle circ = new Circle();
			circ.setCenterX(s.getXPos());
			circ.setCenterY(s.getYPos());
			circ.setRadius(6);
			circ.setFill(Color.RED);
			group.getChildren().add(circ);
		}
		p = new Point(x,z+100);
		shapes = world.getGeometry(p);
		Circle camera2 = new Circle();
		camera2.setCenterX(p.getX());
		camera2.setCenterY(p.getZ());
		camera2.setRadius(world.maxView);
		camera2.setFill(Color.TRANSPARENT);
		camera2.setStroke(Color.INDIGO);
		camera2.setStrokeWidth(2);
		group.getChildren().add(camera2);
		for(Shape s: shapes) {
			Circle circ = new Circle();
			circ.setCenterX(s.getXPos());
			circ.setCenterY(s.getYPos());
			circ.setRadius(2);
			circ.setFill(Color.INDIGO);
			group.getChildren().add(circ);
		}
		p = new Point(650,650);
		shapes = world.getGeometry(p);
		Circle camera3 = new Circle();
		camera3.setCenterX(p.getX());
		camera3.setCenterY(p.getZ());
		camera3.setRadius(world.maxView);
		camera3.setFill(Color.TRANSPARENT);
		camera3.setStroke(Color.DEEPPINK);
		camera3.setStrokeWidth(2);
		group.getChildren().add(camera3);
		for(Shape s: shapes) {
			Circle circ = new Circle();
			circ.setCenterX(s.getXPos());
			circ.setCenterY(s.getYPos());
			circ.setRadius(3);
			circ.setFill(Color.DEEPPINK);
			group.getChildren().add(circ);
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
