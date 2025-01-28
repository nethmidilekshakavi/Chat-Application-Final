package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/ServerMsg.fxml"))));
        stage.setTitle("Server Form");
        stage.centerOnScreen();
        stage.show();

        Stage stage1 = new Stage();
        stage1.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/ClientMsg01.fxml"))));
        stage1.setTitle("Client 01 Form");
        stage1.centerOnScreen();
        stage1.show();


        Stage stage2 = new Stage();
        stage2.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/CilentMsg02.fxml"))));
        stage2.setTitle("Client 02 Form");
        stage2.centerOnScreen();
        stage2.show();
    }
}
