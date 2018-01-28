package virtualworld;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

/**
 * This is just a starter, place holder for the group.
 */
public class Main extends Application {
	public static void main(String[] args) {
		System.out.println("Virtual world goes here.");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Virtual World");
		Group mainGroup = new Group();
		Scene scene = new Scene(mainGroup, 1280, 720, true);
		Camera camera = new PerspectiveCamera(true);
		scene.setCamera(camera);
		Group cameraGroup = new Group();
		cameraGroup.getChildren().add(camera);
		mainGroup.getChildren().add(cameraGroup);

		Sphere sphere = new Sphere(2);
		Material mat = new PhongMaterial(Color.BLUE);
		sphere.setMaterial(mat);
		sphere.setTranslateZ(10);
		mainGroup.getChildren().add(sphere);
		// TODO Your stuff goes here.

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
