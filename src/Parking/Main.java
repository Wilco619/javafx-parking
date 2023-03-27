package Parking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard1.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("Dashboard1.fxml"));
        stage.setTitle("Find Parking Space");
        stage.setResizable(false);
        stage.setScene(new Scene(root,1280,700));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

