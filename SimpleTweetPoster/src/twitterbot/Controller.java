package twitterbot;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import twitter4j.TwitterException;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML private ListView<Quote> listLines;
    @FXML private Button buttonDelete;
    @FXML private Button buttonStop;
    @FXML private Button buttonStart;
    @FXML private Label labelParts;
    @FXML private Label labelLength;
    @FXML private Label labelActive;
    @FXML private TextArea textArea;
    @FXML private Spinner<Integer> spinnerPosition;
    @FXML private AnchorPane tweetOrderControl;
    @FXML private CheckBox checkboxRandomOrder;
    @FXML private Label labelNextTweetTime;
    @FXML private Label statusbarTweetsLabel;
    @FXML private Spinner<Integer> spinnerTime;
    @FXML private Spinner<Integer> spinnerRandomTimeStart;
    @FXML private Spinner<Integer> spinnerRandomTimeEnd;
    public static LinkedList<Quote> listTweets;
    private LocalDateTime date;
    private boolean postOn;
    private Timeline mainPostingTimeer;
    private int timerMin;
    private boolean checkBoxRandomPost;
    private boolean checkBoxRandomPeriod;
    private  DateTimeFormatter dateFormater;
    private DateTimeFormatter timeFormater;
    private LogWindow tmp;
    public static ObservableList<Log> logList;
    public Label labelConntected;
    public static BooleanProperty configConnected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tmp = null;
        configConnected = new SimpleBooleanProperty(false);
       // date = LocalDateTime.now();
        postOn = false;
        checkBoxRandomPost = false;
        dateFormater = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");
        timeFormater = DateTimeFormatter.ofPattern("HH:mm:ss");

        initControls();
        updateItemsRealTime();
        updateStatusBar();
        checkNumberInput();

        System.out.println(LocalDateTime.now().format(dateFormater) + " :: Main view is now loaded!");
        logList = FXCollections.observableArrayList(new Log(LocalDateTime.now().format(dateFormater),"Load","Main View"));
    }

    //Initialization of the controls on start
    private void initControls(){
        checkboxRandomOrder.setTooltip(new Tooltip("UnChecked - The tweets are posted in the order they are shown on the list." +
                "\n" +
                "Checked - The tweets are posted in a random order."));
        loadListView();
        initTimeControls();

        configConnected.addListener((o,oldValue,newValue)->{
            if(newValue){
                labelConntected.setText("Connected");
                labelConntected.setTextFill(Paint.valueOf("#13d350"));
                System.out.println("WORKS");
                buttonStart.setDisable(false);
            }else{
                labelConntected.setText("Not Connected");
                labelConntected.setTextFill(Paint.valueOf("#e80b0b"));
                System.out.println("AAAAAAAAAAAAA");
                buttonStart.setDisable(true);
            }
        });
    }

    private void initTimeControls(){

        spinnerTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,1));
        spinnerRandomTimeEnd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2,Integer.MAX_VALUE,2));
        spinnerRandomTimeStart.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,1,1));
        spinnerRandomTimeEnd.valueProperty().addListener((obs,old,newValue)->{
            Integer tmp = spinnerRandomTimeStart.getValue();

            if(newValue<=tmp) tmp-=1;
            spinnerRandomTimeStart.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,newValue-1,tmp));
        });

    }

    // Not sure if it is the correct approach
    public void startnewApp() throws Exception {
        Main newApp = new Main();
        Stage newStage = new Stage();
        newApp.start(newStage);
    }

    //Popravi ima bug koga ke se zatvori posle ne se otvara
    public void openLogHistoryView() throws IOException {

        if(tmp==null) {
            tmp = new LogWindow();
            tmp.display();
        }
        else{

            tmp.focus();
        }
    }

    private void loadListView(){
        ObservableList<Quote> items = FXCollections.observableArrayList (
                new Quote("Hello world!"));
        listLines.setItems(items);
        listTweets = new LinkedList<Quote>();
        listLines.getItems().addListener((ListChangeListener<? super Quote>) p->{
            listTweets.add(listLines.getItems().get(listLines.getItems().size()-1));
        });
    }


    private void updateOrderControl(){
        Quote selectedTweet = listLines.selectionModelProperty().get().getSelectedItem();

        if(selectedTweet!=null) {
            tweetOrderControl.setDisable(false);
            spinnerPosition.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,listLines.getItems().size(),listLines.selectionModelProperty().get().getSelectedIndex()+1));
        }
        else{
            tweetOrderControl.setDisable(true);
        }
    }

    public void clickListItemPositionChange(){
        int selectedIndex = listLines.selectionModelProperty().get().getSelectedIndex();
        int spinnerIndex;

                try {
                    spinnerIndex = Integer.parseInt(spinnerPosition.getEditor().getText());
                }
                catch(Exception ex){
                   // ex.printStackTrace();

                    SpinnerValueFactory<Integer> valueFactory = spinnerPosition.getValueFactory();

                    spinnerIndex = selectedIndex+1;
                    valueFactory.setValue(selectedIndex);
                    AlertBox.display("Input Error","Your input isn't a number. Please try again.");
                }

                if(spinnerIndex>listLines.getItems().size()){
                    spinnerIndex = listLines.getItems().size();
                }
                else if(spinnerIndex<1){
                    spinnerIndex = 1;
                }

        listItemPosChange(selectedIndex,spinnerIndex);

    }

    private void listItemPosChange(int selectedIndex, int spinnerIndex){
        if(selectedIndex!=spinnerIndex-1) {

            Quote tmp = listLines.selectionModelProperty().get().getSelectedItem();
            listLines.getItems().remove(selectedIndex);
            listLines.getItems().add(spinnerIndex-1, tmp);
            listLines.selectionModelProperty().get().select(spinnerIndex-1);

        }
    }

    private void updateSelectedTweet(){
        Quote quote = listLines.selectionModelProperty().get().getSelectedItem();


        if(quote!=null) {
            updateSelectedTweetInfo(quote.toString(),quote.getTweetlenght(),quote.getParts(),true);
        }
        else{
            updateSelectedTweetInfo("",0,0,false);
        }

        updateOrderControl();

    }

    private void updateSelectedTweetInfo(String text, int tweetLenght, int tweetParts, boolean isTweetSelected){
        textArea.textProperty().set(text);
        labelLength.setText("" + tweetLenght);
        labelParts.setText("" + tweetParts);
        buttonDelete.setDisable(!isTweetSelected);
    }

    public void saveButtonAction(){
        Quote quote = listLines.selectionModelProperty().get().getSelectedItem();
        if(quote!=null) quote.setText(textArea.getText());
        else saveAsButtonAction();
        listLines.refresh();
    }

    public void saveAsButtonAction(){
        Quote newQuote = new Quote(textArea.getText());
        listLines.getItems().add(newQuote);
    }

    private void updateItemsRealTime(){
        listLines.getSelectionModel().selectedItemProperty().addListener(p->updateSelectedTweet());
        textArea.textProperty().addListener(p->{
            labelLength.setText(textArea.getText().length()+"");
            labelParts.setText(1+textArea.getText().length()/140+"");
        });

    }

    public void deleteSelectedTweet(){
        deleteTweet(listLines.selectionModelProperty().get().getSelectedItem());
    }

    private void deleteTweet(Quote tweet){
        if(tweet!=null){
            listLines.getItems().remove(tweet);
        }
    }

    public void newTweet(){
        listLines.getSelectionModel().clearSelection();
        updateSelectedTweet();
    }

    public void importLinesfromTextFile(){
        TextFileImportExport.readLines(listLines);
    }

    public void exportLinesfromTextFile(){
        TextFileImportExport.saveFile(listLines.getItems());
    }

    public void closeProgram(){
        Platform.exit();

    }

    public void onClickButtonRandomPosition(){
        if(listLines.getItems().size()>1) {
            int selectedIndex = listLines.selectionModelProperty().get().getSelectedIndex();
            int randomPos = Helper.randInt(1, listLines.getItems().size());

            while(randomPos-1==selectedIndex){
                randomPos = Helper.randInt(1, listLines.getItems().size());
            }

            listItemPosChange(selectedIndex, randomPos);
        }
    }

    public void onClickButtonRandomizeList(){
        FXCollections.shuffle(listLines.getItems());
    }

    private int postTime(){
        if(checkBoxRandomPeriod){
            return Helper.randInt(spinnerRandomTimeStart.getValue(),spinnerRandomTimeEnd.getValue());
        }
        else{
            return spinnerTime.getValue();
        }
    }

    // Post the Tweet Fucntion on Button Click
    public void postButtonAction(){
        timerMin = postTime();

        mainPostingTimeer = new Timeline(new KeyFrame(
                Duration.minutes(timerMin),
                ae -> postTweet()));
        mainPostingTimeer.setCycleCount(Animation.INDEFINITE);

        postingStartsControls();
        nextTweetAt(((LocalTime.now().plusMinutes(timerMin)).format(timeFormater)).toString());

       // postTweet();
    }

    // Stops the posting process
    public void stopButtonAction(){
        postingStopControls();
    }

    private void nextTweetAt(String time){
        if(postOn)
        labelNextTweetTime.setText(time);
        else
            labelNextTweetTime.setText("not defined");
    }

    //Control's values when the posting stops
    private void postingStopControls(){
        System.out.println(LocalDateTime.now().format(dateFormater) + " :: Stopped posting!");
        logList.add(new Log(LocalDateTime.now().format(dateFormater),  "Terminate","The posting has been stopped."));
        postOn = false;
        mainPostingTimeer.stop();
        labelActive.setText("OFF");
        labelActive.setTextFill(Paint.valueOf("#e80b0b"));
        buttonStop.setDisable(true);
        buttonStart.setDisable(false);
        labelNextTweetTime.setText("not defined");
    }

    private void postingStartsControls(){
        System.out.println(LocalDateTime.now().format(dateFormater) + " :: Started posting!");
        logList.add(new Log(LocalDateTime.now().format(dateFormater),  "Start","The posting has been started."));
        postOn = true;
        mainPostingTimeer.play();
        labelActive.setText("ON");
        labelActive.setTextFill(Paint.valueOf("#13d350"));
        buttonStop.setDisable(false);
        buttonStart.setDisable(true);
    }

    /*
    * Posts the first tweet from the listView (listLines)
    */
    private void postTweet(){
        date = LocalDateTime.now();
        if(listLines.getItems().size()>0) {
            Quote tweet;
            if(checkBoxRandomPost) {
                tweet = listLines.getItems().get(Helper.randInt(0, listLines.getItems().size() - 1));
            }else {
                tweet = listLines.getItems().get(0);
            }

            if (tweet != null) {

                printTweetParts(tweet);
                deleteTweet(tweet);
            }
        }
        else{
            System.out.println(date.format(dateFormater) + " :: All Tweets are Posted.");
            postingStopControls();
        }


    }

    private void printTweetParts(Quote tweet){
        int duration = 0;
       for(String s : tweet.getTweets()){
           //If the tweet has more than 1 part than wait 5 sec between parts.
           Timeline timebetweenTweetParts = new Timeline(new KeyFrame(
                   Duration.millis(duration+=5000),
                   ae ->{
                       try {
                           TwitterConfig.postTwitter(s);
                       } catch (TwitterException e) {
                           e.printStackTrace();
                       }
                       System.out.println(LocalDateTime.now().format(dateFormater) + " :: POSTING: " + s);
                            logList.add(new Log(LocalDateTime.now().format(dateFormater),  "Posting",s));
                   }));
           timebetweenTweetParts.play();
       }
        nextTweetAt(LocalTime.now().plusMinutes(timerMin).format(timeFormater));
       }

    public void checkboxRandomOrderAction(){
        checkBoxRandomPost = !checkBoxRandomPost;
        //System.out.println(checkBoxRandomPost);

    }

    public void checkBoxRandomPeriodAction(){
        checkBoxRandomPeriod = !checkBoxRandomPeriod;

            spinnerTime.setDisable(checkBoxRandomPeriod);
        spinnerRandomTimeStart.setDisable(!checkBoxRandomPeriod);
        spinnerRandomTimeEnd.setDisable(!checkBoxRandomPeriod);

    }

    private void updateStatusBar(){
        updateTweetCount();
    }

    private void updateTweetCount(){
        statusbarTweetsLabel.setText(String.format("%d",listLines.getItems().size()));
        listLines.itemsProperty().getValue().addListener((ListChangeListener)(s)->statusbarTweetsLabel.setText(String.format("%d",listLines.getItems().size())));
    }

    //Spinner (numUpDown) - Checks if the there is a text input instead of a number and deletes it
    private void checkNumberInput(){
      /* spinnerPosition.getEditor().textProperty().addListener((ob,oldValue,newValue)-> {
                if (!newValue.matches("\\d*")) {
                    spinnerPosition.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
                }

                //TODO - Try to fix this problem. If you try to delete the text of the textfiled it auto fills it with the selected item's index
                if(spinnerPosition.getEditor().getText().length()<1)  spinnerPosition.getEditor().setText((listLines.getSelectionModel().getSelectedIndex()+1)+"");
            }
        ); */
        checkIfNumberonSpinner(spinnerPosition,1);
        checkIfNumberonSpinner(spinnerTime,1);
        checkIfNumberonSpinner(spinnerRandomTimeStart,1);
        checkIfNumberonSpinner(spinnerRandomTimeEnd,2);

    }

    private void checkIfNumberonSpinner(Spinner<Integer> spinner, int defValue){
        spinner.getEditor().textProperty().addListener((ob,oldValue,newValue)-> {
                    if (!newValue.matches("\\d*")) {
                        spinner.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
                    }

                    //TODO - Try to fix this problem. If you try to delete the text of the textfiled it auto fills it with the selected item's index
                    if(spinner.getEditor().getText().length()<1)  spinner.getEditor().setText(defValue+"");
                }
        );
    }

    public void menuItemEditConfi() throws IOException {
        ConfigWindow cw = new ConfigWindow();
        cw.display();

    }

}
