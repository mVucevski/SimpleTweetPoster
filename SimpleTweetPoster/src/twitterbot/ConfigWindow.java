package twitterbot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfigWindow {
    Parent root;
    Stage stage;



    public void display() throws IOException {
        root = FXMLLoader.load(getClass().getResource("ConfigTwitter.fxml"));
        stage = new Stage();
        stage.setTitle("Twitter's Configuration");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }


}
