package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.LocalDate;

public class Main extends Application {
    Singleton singleton = Singleton.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/admin/adminHome.fxml"));
        singleton.setMainStage(primaryStage);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
        String test[] = test();
        System.out.println(test==null);
    }

    public String[] test() {
        return null;
    }


    public static void main(String[] args) { launch(args); }
}
