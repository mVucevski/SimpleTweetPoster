package twitterbot;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class logHistoryController implements Initializable {

    @FXML private TableView<Log> tableviewHistoryLog;
    @FXML private TableColumn<Log,String> datetimeColumn;
    @FXML private TableColumn<Log,String> actionColumn;
    @FXML private TableColumn<Log,String> contentColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        datetimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

        tableviewHistoryLog.getItems().addAll(Controller.logList);


            Controller.logList.addListener((ListChangeListener) p ->{
        if(Controller.logList.size()>0) {
                    tableviewHistoryLog.getItems().add(Controller.logList.get(Controller.logList.size() - 1));
        }});

        System.out.println("LogHistory view is now loaded!");
    }

    public void clearButtonClick(){
        tableviewHistoryLog.getItems().clear();
        Controller.logList.clear();
    }

    public void exportButtonClick(){
        List<Log> tmpList = Controller.logList;
        TextFileImportExport.saveFile(tmpList);
    }


}
