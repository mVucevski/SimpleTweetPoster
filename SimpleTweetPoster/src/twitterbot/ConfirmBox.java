package twitterbot;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    //Create variable
    static byte answer;

    public static byte display(String title, String message,String yes, String no) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);
        Label label = new Label();
        label.setText(message);

        //Create two buttons
        Button yesButton = new Button(yes);
        Button noButton = new Button(no);

        answer = 0;

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            answer = 1;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = -1;
            window.close();
        });

        VBox layout = new VBox(10);
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(yesButton,noButton);
        hbox.setAlignment(Pos.CENTER);
        //Add buttons
        layout.getChildren().addAll(label, hbox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
       // window.setOnCloseRequest(p->window.close());
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

}