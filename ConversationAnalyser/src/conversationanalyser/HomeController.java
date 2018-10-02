/*
    This is the controller of the home page of the proman. in this it deals with
    the details of the files inputted in by the user.
 */
package conversationanalyser;
 
import java.awt.EventQueue; 
import java.io.File;
import java.io.IOException;
import java.net.URL; 
import java.util.ResourceBundle; 
import java.util.logging.Level;
import java.util.logging.Logger; 
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; 
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane; 
import javafx.stage.Stage;
import javafx.stage.FileChooser; 
import javax.swing.JOptionPane;
import javafx.scene.Node; 
import javafx.scene.text.Text;

/**
 *
 * @author Rhys
 */
public class HomeController implements Initializable {
    
    private Stage stage;    
    
    // the fxml button object used to load the xml file
    @FXML
    private Button xmlBtn;

    // the fxml button object used to load the media file
    @FXML
    private Button mediaBtn;
    
    // the fxml button object used to create the submit button
    @FXML
    private Button submit;
    
    // the fxml TextField object that is used to disaply the path of the xml file to the user
    // when they have selected it
    @FXML
    private TextField xmlText;
    
    // the fxml TextField object that is used to display the path of the media file to the user
    // when they have selected it
    @FXML
    private TextField mediaText;
    
    // the fxml pane object that stores the xmlError
    @FXML
    private Pane xmlError;
    
    // the fxml pane object that stores the media error
    @FXML
    private Pane mediaError;
    
    // the fxml pane object taht stores a submit error
    @FXML
    private Text submitError;
    
    void init(Stage stage) {
        this.stage = stage;
    }
    // strings that are going to be used to store the xml and media file names/
    String xmlFileName=""; 
    String mediaFileName="";

    // two strings that are going to change i
    String extensionXml;  
    String extensionMedia;  
            
    //this creates the instance variable of the HomeInstanceMethod class
    private HomeInstanceMethod instance;
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // this gets the intnace of the instnace method 
        instance = HomeInstanceMethod.getInstance();
        
        // a set on mouse clicked event is added to the xml button.
        xmlBtn.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                // sets the xmlError pane to invisible.
                xmlError.setVisible(false);
                //crates a new file chooser instance which allows a user to select a file
                FileChooser fch = new FileChooser();
                fch.setInitialDirectory(new File("."));
                File f = fch.showOpenDialog(null);
                //checks if the file is not equal to null meaning a file was choosen.
                if(f != null){
                    // the path to the file is collected and set the the textField xmlText
                    xmlText.setText(f.getAbsolutePath());
                    
                    //gets the full name of the xml file.
                    String a = f.getName(); 
                    // splits the name at the occurence of a "."
                    String[] parts = a.split("\\.");
                    // stores the names in the string xmlFieldName
                    xmlFileName = parts[parts.length-2];
                    // stores the extension of the xml file in the new strnig extension
                    String extension = parts[parts.length-1]; 
                    // checks if the extension is not equal to xml meaning, it is a incorrect
                    // file type.
                    if(!extension.equalsIgnoreCase("xml")){
                        // as it is a incorrect file type the xmlError pane is displayed
                        // the extensionXML string is set to false the the instance method updates
                        // the current xml path to blank.
                        xmlError.setVisible(true);
                        extensionXml = "false";
                        instance.setXmlPath("");
                    }
                    else{
                        // if the else is ran it means the extension is .xml
                        // so the extensionXml is set to true and the setter of the xml
                        // path in the instance method iss set to the path of the file 
                        // inputed by the user.
                        extensionXml = "true";
                        instance.setXmlPath(f.getAbsolutePath());
                    }
                }
                else{
                    xmlText.setText(""); 
                    instance.setXmlPath("");
                }
            }
        });   
        
        // same process as the previous mouse clicked button but for the meida button instead
        mediaBtn.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                mediaError.setVisible(false);
                FileChooser fch = new FileChooser();
                fch.setInitialDirectory(new File("."));
                File f = fch.showOpenDialog(null);
                if(f != null){
                    mediaText.setText(f.getAbsolutePath());
                    String a = f.getName(); 
                    String[] parts = a.split("\\.");
                    mediaFileName = parts[parts.length-2];
                    String extension = parts[parts.length-1]; 
                    if(extension.equalsIgnoreCase("mp4")){
                        mediaError.setVisible(false);
                        extensionMedia = "true";
                        instance.setMediaPath(f.getAbsolutePath());
                    }
                    else if(extension.equalsIgnoreCase("mp3")){
                        mediaError.setVisible(false);
                        extensionMedia = "true";
                        instance.setMediaPath(f.getAbsolutePath());
                    }
                    else{
                        mediaError.setVisible(true);
                        extensionMedia = "false";
                        instance.setMediaPath("");
                    }
                }
                else{
                    mediaText.setText(""); 
                    instance.setMediaPath("");
                }
            }
        });
        
        // a set on mouse click event handler is set to the submit button.
        submit.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                // when this handler is activated it first chekcs if eithre of the file name are empty
                // if they are this means there was no file paths entered by the user, so the error messaeg
                // is displayed to the user.
                if(xmlFileName.equalsIgnoreCase("") || mediaFileName.equalsIgnoreCase("")){
                    submitError.setText("Error: one or more of you're file paths are empty.");
                }
                // the handler next checks if both of the file extensisions are correct if they are
                // the else if is ran
                else if(extensionXml.equalsIgnoreCase("true") && extensionMedia.equalsIgnoreCase("true")){
                    // the program then checks if both the xml file name and media file name are equal
                    if(xmlFileName.equalsIgnoreCase(mediaFileName)){ 
                        // if they are the if statment is run and loads the main page of the program.
                        Parent Submit_parent;
                        try {
                            Submit_parent = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                            Scene Submit_scene = new Scene(Submit_parent);
                            Stage Submit_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Submit_stage.setScene(Submit_scene);
                            //Submit_stage.setResizable(false);
                            Submit_stage.show(); 
                        } catch (IOException ex) {
                            //Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
 
                    else{
                        // if the else is run this means that they have different names, the main page 
                        // is still ran as sometimes file may have different names but a warning message
                        // is given to the user showing telling them the files have different names
                        ShowMessage("Warning: Selected files have different names");
                        Parent Submit_parent;
                        try {
                            Submit_parent = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                            Scene Submit_scene = new Scene(Submit_parent);
                            Stage Submit_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Submit_stage.setScene(Submit_scene);
                            //Submit_stage.setResizable(false);
                            Submit_stage.show();
                        } catch (IOException ex) {
                            //Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            
                    } 
                }
                // the else is ran if one of the files that has been selected by the user is the 
                // wrong file type. and error message is displayed to the user
                else{ 
                    submitError.setText("Error: one or more of the file types are incorrect");
                }
            }
        });
    }
    
    // this method is run to display the warning message to the user if the
    // names of there files are different.
    private void ShowMessage(String message){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                JOptionPane.showMessageDialog(null, message,"File name error",JOptionPane.WARNING_MESSAGE); 
                
            }
        }); 
    }
}
