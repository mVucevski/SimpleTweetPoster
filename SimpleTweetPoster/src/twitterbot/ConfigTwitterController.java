package twitterbot;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigTwitterController implements Initializable {

    public TextField textFieldAPIKey;
    public TextField textFieldAPISecret;
    public TextField textFieldToken;
    public TextField textFieldTokenSecret;

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        System.out.println("Config's view is now loaded!");
    }

    public void saveButtonClick(){
        boolean validConfig = TwitterConfig.saveTwitterConfig(textFieldAPIKey.getText(),textFieldAPISecret.getText(),textFieldToken.getText(),textFieldTokenSecret.getText());
        if(validConfig){
            Controller.configConnected.setValue(true);
            // get a handle to the stage
            Stage stage = (Stage) textFieldAPIKey.getScene().getWindow();
            stage.close();
        }else{
            Controller.configConnected.setValue(false);
            AlertBox.display("Twitter's configuration error!","Your configuration isn't correct, please fix it before continuing.");
        }
    }

    public void loadConfigButtonClick(){
        List<String> config = TextFileImportExport.loadConfig();
        if(config!=null){
            textFieldAPIKey.setText(config.get(0));
            textFieldAPISecret.setText(config.get(1));
            textFieldToken.setText(config.get(2));
            textFieldTokenSecret.setText(config.get(3));
        }
    }

}
