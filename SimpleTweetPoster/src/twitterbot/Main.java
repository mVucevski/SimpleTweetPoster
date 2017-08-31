package twitterbot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("TweetPosterNewbieBot");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(p->{
           byte confirm = ConfirmBox.display("Are you sure you want to exit?","After closing this window all of your tweets will be lost.\nMake sure you save (export) them before closing.","Export","Exit");
            if(confirm==1){
                TextFileImportExport.saveFile(Controller.listTweets);
                Platform.exit();
            }
            else if(confirm==-1){
                Platform.exit();
            }
            else{
                p.consume();
                System.out.println("FUCK");
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
