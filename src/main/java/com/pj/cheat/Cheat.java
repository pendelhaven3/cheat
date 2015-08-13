package com.pj.cheat;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Cheat extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(createMainScene());
		primaryStage.setTitle("Cheat!");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private Scene createMainScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
		return new Scene(root, 550, 600);
	}

}