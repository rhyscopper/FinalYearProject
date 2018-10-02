/*
    this is the controller of the main page of the program.
    This controller deals with all of the functonality of the video, transcripts
    and the analysis processes.
*/
package conversationanalyser;

import java.awt.Button; 
import java.io.BufferedReader;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException; 
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL; 
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle; 
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable; 
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList; 
import javafx.fxml.FXML;
import javafx.fxml.Initializable;  
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView; 
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;  
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer; 
import javafx.scene.media.MediaView;
import javafx.scene.text.Text; 
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Rhys
 */
public class MainPageController implements Initializable {
    
    private Stage stage; 
    //the variables used to store the location of the media and xml files.
    private static String mediafile = "";
    private static String xmlFile = "";
    //variables used to 
    private Duration duration;   
    private static final double INIT_VALUE = 0; 
    
    // the chat transcript loaded from XML
    Chat chat;
    
    // list of paragraphs in the chat
    List<Paragraph> paragraphs;
    int Counter = 0; 
    
    //all of the media from the fxml from
    @FXML
    private MediaView MediaView;
    private MediaPlayer MediaPlayer;
    private Media Media; 
    private Button PlayButton;
    
    //the fxml object of the volume slider
    @FXML
    Slider VolumeSlider;
    
    //the fxml object of the video slider
    @FXML
    Slider VideoSlider;
    
    //the fxml object of the speed slider
    @FXML
    Slider SpeedSlider;
    
    //the fxml object which stores the elapsed time and total time of the vidoe.
    @FXML
    Text StartTime;
    
    // the list video which store the list of the participants
    @FXML
    private ListView<String> participantsList;
     
    //the text area storing the conversation
    @FXML
    TextArea chatText;
    
    //the pane object from the fxml document used to store the graph onto.
    @FXML
    Pane grapharea;
    
    //creates the LineChart object.
    LineChart<String,Number> lineChart;
    
    // reads the emotions and stores them in an array
    String inputtedEmotions = ""; 
     
    // string array to store the names of 
    String[] LineName; 
    int Numberofemotions;
    
    // where are the classifier models?
    String installDir = "";
    
    // the classifier variable.
    SVMClassifier svm;
	
    // this is the arraylist that stores the results of the analysis
    private ArrayList<double[]> Data = new ArrayList<double[]>();
    
    // creates an arralist of the XY chart series, used to created the lines
    ArrayList<XYChart.Series> list = new ArrayList<XYChart.Series>();
        
    // this is the variables that store the max and min the user inputs
    private static Double MAX_DATA_POINTS;
    private static Double MIN_DATA_POINTS;
        
    // creates the instance of the yAxis
    private NumberAxis yAxis;
         
    // create anarraylist of type XYChart     
    //ArrayList<ConcurrentLinkedQueue> list2 = new ArrayList<ConcurrentLinkedQueue>();
     
    // initaialise the instance method of the HomeInstanceMethod which stores 
    // the media and xml path which is gained from the home page.
    private HomeInstanceMethod instance;
    
    double VideoTime;
    double ClickedTime;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // gets the insances of the HomeInstanceMethod
        instance = HomeInstanceMethod.getInstance();
        
        // use the get methods from the HomeInstanceMethod to get the media
        // and xml path.
        mediafile = instance.getMediaPath();
        xmlFile = instance.getXmlPath();
        
        // list view to display all participants 
        final ObservableList<String> participants = FXCollections.observableArrayList();
        // sets the observableList participants to the List view participantsList.
        participantsList.setItems(participants);
        // runs the parse method of the ChatParser class, passing the xmlFile path
        // as its parameters and stores the result in chat
        chat = ChatParser.parse(xmlFile);
        // gets all of the paragraphs stores in chat and stores in the list of paragraphs
        paragraphs = chat.getParagraphs();
        
        // makes sures there is nothing stores in participants, then adds to first line
        // showing list of participants.
        participants.clear();
        participants.add("-- List of Participants --");
        
        // gets all of the participants stores in chat and stores them in  List of type 
        // participants
        List<Participant> listOfParticipants = chat.getParticipants();
        
        // for each participant in the list of participants. it gets their userid and
        // their name in branckets and adds them to the participants.
        for(Participant p : listOfParticipants){
            participants.add(p.getUid()+" ("+p.getName()+")");
        }
        
        // creates a new media instance using the mediafile variable, which is passed
        // to a new instance of media player and the set to the MediaView.
        Media = new Media(new File(mediafile).toURI().toString());
        MediaPlayer = new MediaPlayer(Media);
        MediaView.setMediaPlayer(MediaPlayer);
            
        // this sets the initial values of the volume and video slider.
        VolumeSlider.setValue(MediaPlayer.getVolume() * 100); 
        VideoSlider.setValue(INIT_VALUE);  
 
        // on the media player a setOnReady intance is run with a runabble instance
        // that updates the media player time stored in duration and runs the updateValues method.
        MediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                duration = MediaPlayer.getMedia().getDuration();
                updateValues();
            }
        });
           
        // adds a listener onto the Speedslider which detects when change has occured
        // and sets the rate of the selected value.
        SpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                MediaPlayer.setRate(new_val.doubleValue());
            }
        });
        
        // adds a listener to the current time property of the media player,
        // which when activated runs the updateValues method.
        MediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                updateValues();
            }
        });   
        
        //creates a new filesteam variable.
        FileInputStream fstream;
        try {
            // creates a new filen input steam instance passing input.txt as its parameters
            // which is the names of the text file found in the program folder
            fstream = new FileInputStream("input.txt");
            // a new bufferedReader which reads the line of the file input stream.
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            // a new string instance is created
            String strLine;
            // a integer count is created set at 0 to itterate through the size of the
            // values read in.
            int Count = 0;
            /*
                As it is knows there is 5 lines in the text file, the first line is
                the inputed emotions, the second and third the maximum of the graph,
                the fourth is the classifier name and the 5th is the location of the
                models. each line is read one at a time, and depending on the line number
                the variable is set correlating to the values inputed.
            */ 
            while ((strLine = br.readLine()) != null)   {
                if(Count == 0){
                    strLine = strLine.substring(9, strLine.length());
                    strLine = strLine.replace(" ", "");
                    inputtedEmotions = strLine;
                }
                if(Count == 1){
                    strLine = strLine.substring(10, strLine.length());
                    strLine = strLine.replace(" ", "");
                    if(!strLine.equalsIgnoreCase("")){
                        MAX_DATA_POINTS = Double.parseDouble(strLine);
                    }
                }
                if(Count == 2){
                    strLine = strLine.substring(10, strLine.length());
                    strLine = strLine.replace(" ", "");
                    if(!strLine.equalsIgnoreCase("")){
                        MIN_DATA_POINTS = Double.parseDouble(strLine);
                    }
                }
                if(Count == 3){
                    //this section gets the names of the classifier, that might
                    //have been used to 
                    strLine = strLine.substring(11, strLine.length());
                    strLine = strLine.replace(" ", ""); 
                }
                if(Count == 4){
                    strLine = strLine.substring(18, strLine.length());
                    strLine = strLine.replace(" ", "");
                    installDir = strLine;
                }
                else{
                
                }
                // this adds 1 to count, so that it increases by one every time.
                 Count++;
            }
            // when the emotions are inputed they are seperated by ",", so the 
            // string of the inputted emotions is split at each , instance and
            // the results stored in LineName
            LineName = inputtedEmotions.split(","); 
            // this counts the number of emotions
            Numberofemotions = LineName.length;
            // this creates a new instance of the classifier passing the directory
            // of the models and list of emotions.
            svm = new SVMClassifier(installDir, LineName);
            //Close the buffer reader
            br.close();
        } 
        catch (FileNotFoundException ex) {
            
        } 
        catch (IOException ex) { 
            
        }
        
        // a new listener is set on the volume slider which gets the value the
        // user changes and sets this onto the Media player.
        VolumeSlider.valueProperty().addListener(new InvalidationListener(){
         
            @Override
            public void invalidated(Observable observable) {
                MediaPlayer.setVolume(VolumeSlider.getValue() / 100 ); 
            }

        }); 
        
        // a new listener is added to the video slider which seeks thought the video
        // when changed by the user.
        VideoSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (VideoSlider.isValueChanging()) {
                    MediaPlayer.pause();
                    // multiply duration by percentage calculated by slider position
                    MediaPlayer.seek(duration.multiply(VideoSlider.getValue() / 100.0));
                    MediaPlayer.play();
                }
            }
        });
        
        // set on mouse pressed method run on the video slider which sekks to the
        // time of the video selected by the user.
        VideoSlider.setOnMousePressed(e -> {
            MediaPlayer.pause();
            ClickedTime = (duration.multiply(VideoSlider.getValue() / 100.0)).toSeconds();
            MediaPlayer.seek(duration.multiply(VideoSlider.getValue() / 100.0));
            MediaPlayer.play();
        });
        
        // set on mouse released method set on the video slider which clears the data when
        // user releases the mouse on the video slider.
        VideoSlider.setOnMouseReleased(e -> {   
            MediaPlayer.pause();
            Data.clear(); 
            chatText.clear();
            Counter = 0; 
            MediaPlayer.seek(duration.multiply(VideoSlider.getValue() / 100.0));    
            MediaPlayer.play();           
        });
         
        //iniciates the x and y axis
        final CategoryAxis xAxis = new CategoryAxis(); 
        yAxis = new NumberAxis(); 
        
        /*
            this if statement is used to set the maximum and minimum data points
            of the graph. what this does is first checks if the max and min
            variables are both null, if they are this means the user didnt enter
            a maximum or minimum value in the text file, so the graph will run a 
            plain number axis which automatically adjust as data is added.
        */
        if(MIN_DATA_POINTS == null && MAX_DATA_POINTS == null){
            
        }
        // the if statement then checks if either one of the max or min are equal
        // to null if one of them is, if max, the min is automatically set to 0.0
        // if max is null it is automatically set to 1.0
        else if(MIN_DATA_POINTS == null){
            yAxis = new NumberAxis(0.0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        }
        else if(MAX_DATA_POINTS == null){
            yAxis = new NumberAxis(MIN_DATA_POINTS, 1.0, 1.0 / 10);
        }
        // if the else is run that means the user entered data for both the max
        // and minimum values of the graph, so this area run with the graph.
        else{
            yAxis = new NumberAxis(MIN_DATA_POINTS, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        }
        
        //sets the names of the collums to a constant.
        xAxis.setCategories(FXCollections.<String>observableArrayList("10","9","8","7","6","5","4","3","2","1"));
        // sets the name on the y axis
         xAxis.setLabel("number of utterances");
        //initialises the graph
        lineChart = new LineChart<String,Number>(xAxis,yAxis);
        // sets the title of the graph
        lineChart.setTitle("Emotion Analysis");
        
        //sets the size and height of the line graph.
        lineChart.setPrefHeight(325.0);
        lineChart.setMinHeight(325.0);
        lineChart.setMaxHeight(325.0);

        lineChart.setPrefWidth(500.0);
        lineChart.setMinWidth(500.0);
        lineChart.setMaxWidth(540.0);

        lineChart.setPrefSize(500.00, 325.00);
        lineChart.setMinSize(500.00, 325.00);
        lineChart.setMaxSize(500.00, 325.00);
        
        // creates a series for the number of emotions entered
        // for each emotion a new XYChart series is added and is given the name of that emotion.
        for(int i = 0; i < Numberofemotions; i++){
            
            list.add(new XYChart.Series()); 
            list.get(i).setName(LineName[i]); 
        
        } 
        //adds each line stored in list to the linechart
        for(int k = 0; k < Numberofemotions; k++){
            lineChart.getData().add(list.get(k));
        }
        
        //the line chart is set to the graph area.
        grapharea.getChildren().addAll(lineChart);

    }    
    
    void init(Stage stage) {
        this.stage = stage;
    }
    
    // this method is run to update the values being displayed in the program.
    protected void updateValues() {
        // this is used to get the current elapsed time of the media
        Duration currentTime = MediaPlayer.getCurrentTime();
        VideoTime = currentTime.toSeconds();
        // the if statement makes sure the start time and video slider are not equal to null
        // and that the current time is not equal to 0. if run a new runabble instnace initialised
        if (StartTime != null && VideoSlider != null && currentTime.toSeconds() != 0) {
            Platform.runLater(new Runnable() {
                public void run() {  
                    //this is what constantly dispalys the time of the elapsed video.
                    StartTime.setText(formatTime(currentTime, duration));
                    VideoSlider.setDisable(duration.isUnknown());
                    if (!VideoSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !VideoSlider.isValueChanging()) {
                        VideoSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                    } 
                    /*
                        this is the part of the program that is run to display the conversation
                        taking place in tet. the way it works, is at the beginning a counter is set to 0
                        and all of the paragraphs are stores in a list object called paragraphs. the
                        program starts by looking at the first element at location 0 of the list.
                        it checks if the current elapsed time of the video is greater than or equal to
                        the start time of the paragraph to be printed. if it is this means it is ready
                        to be printed.
                    */
                    Paragraph p = paragraphs.get(Counter);
                    if (MediaPlayer.getCurrentTime().toSeconds() >= p.getTime()) {  
                        // this adds the text of the paragraph to the program.
                        chatText.appendText(p.toString()+"\n");
                        // this passes the string representation of the paragraph to the analysis method
                        analysis(p.toString());
                        // increments the counter by one so the next value is looked at
                        Counter++;
                    }
                    
                }
            });
        }
    }
    
    /*
        this is the analysis method which is called from the updateValues method and passsed the current 
        sentence to be printed as it parameters, so that it can be analysed and its data displayed on a graph.
    */
    private void analysis(String sentence) {
        
        String[] sents = {sentence};
	for (int s=0; s<sents.length; s++) { 
	    
	    String sent = sents[s];  
	    
	    // classify
	    // get raw list of confidences for each emotion
	    double[] p = svm.predict(sent);
	    // scale to [0,1] and find max
	    double max = -1.0;
	    int maxind = -1;
	    for (int i=0; i<p.length; i++) {
		p[i] = 1.0 / (1.0 + Math.exp(-p[i]));
		if (p[i]>max) {
		    maxind = i;
		    max = p[i];
		}  
	    }
            // this is the part which ensures the data on the graph is not larger than
            // 10, it checks the size of the Data variable if it is 10 or more the first
            // elements is removed from it.
            if(Data.size() >= 10){
                
                        Data.remove(0);
                  
            }
            // new set of results are added to the data arraylist
	    Data.add(p); 
	}
        
        // gets the size of the arraylist data, so that it is known how many arrays
        // there is of data, so that the right number of utterances are printed.
        int a = Data.size();   
        for(int j = 0; j < Numberofemotions; j++){
            
            // calls each line stores in list and sets it to a name of the emotion,
            list.get(j).setName(LineName[j]); 
            list.get(j).getData().clear();
            
            // this for loop is run for the number of utterances stores in data.
            // the length of this is stores in variable a, which tells the program
            // the number of arrays of results there our which correlates to the number
            // of utterances.
            for(int c = 0; c < a ; c++){
                // each array is called and the data depending on the number it is, 
                // it is added to the graph.
                double[] data = Data.get(c);
                if(c==0){
                    list.get(j).getData().add(new XYChart.Data("1",data[j]));
                } 
                else if(c==1){
                    list.get(j).getData().add(new XYChart.Data("2",data[j]));
                } 
                else if(c==2){
                    list.get(j).getData().add(new XYChart.Data("3",data[j]));
                } 
                else if(c==3){
                    list.get(j).getData().add(new XYChart.Data("4",data[j]));
                } 
                else if(c==4){
                    list.get(j).getData().add(new XYChart.Data("5",data[j]));
                } 
                else if(c==5){
                    list.get(j).getData().add(new XYChart.Data("6",data[j]));
                } 
                else if(c==6){
                    list.get(j).getData().add(new XYChart.Data("7",data[j]));
                } 
                else if(c==7){
                    list.get(j).getData().add(new XYChart.Data("8",data[j]));
                } 
                else if(c==8){
                    list.get(j).getData().add(new XYChart.Data("9",data[j]));
                } 
                else if(c==9){
                    list.get(j).getData().add(new XYChart.Data("10",data[j]));
                }  
                else{
                
                }
            }
        } 
        // adds each line of data to the graph.
        for(int k = 0; k < Numberofemotions; k++){
            lineChart.getData().add(list.get(k));
        } 
    }
    
    // this is the method called by the text element which displays the elapsed time
    // of the media.
    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
    
    // this is the fxml method called to play the video
    @FXML
    private void PlayVideo(ActionEvent event)
    { 
        MediaPlayer.play();
        MediaPlayer.setRate(1);
    }
    // this is the fxml method called to pause the video
    @FXML
    private void PauseVideo(ActionEvent event)
    { 
        MediaPlayer.pause();
    }
    // this is the fxml method called to reaload the video
    @FXML
    private void reload(ActionEvent event)
    { 
        Data.clear(); 
        MediaPlayer.seek(MediaPlayer.getStartTime());
        chatText.clear();
        Counter = 0;  
        VideoSlider.setValue(INIT_VALUE);
        MediaPlayer.pause();
    }
    
    @FXML
    private void FastForward(ActionEvent event)
    { 
        MediaPlayer.seek(duration.multiply(VideoSlider.getValue() / 100.0 + 0.05));  
    }
    
     @FXML
    private void Rewind(ActionEvent event)
    { 
       Data.clear(); 
       chatText.clear();
       Counter = 0;   
       MediaPlayer.seek(duration.multiply(VideoSlider.getValue() / 100.0 - 0.05));   
    }
}