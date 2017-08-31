package twitterbot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LogWindow {
    Parent root;
    Stage stage;


    public void display() throws IOException {
        root = FXMLLoader.load(getClass().getResource("LogHistory.fxml"));
        stage = new Stage();
        stage.setTitle("History Log");
        stage.setScene(new Scene(root,800,600));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }

    public void focus(){
        stage.requestFocus();
        if(!stage.isShowing()) stage.show();
    }

}
