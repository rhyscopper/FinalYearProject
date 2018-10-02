/*
    this is the class which runs the actual program. 
 */
package conversationanalyser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;  

/**
 *
 * @author Rhys
 */
public class ConversationAnalyser extends Application {
     
    //the start method which is called to start the program.
    @Override
    public void start(Stage stage) throws Exception {
      
        // the Home.fxml is set to the root
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        // a new scene instance is created passing the root as it parameters
        Scene scene = new Scene(root); 
        // sets the program to be unresiable
        stage.setResizable(false);
        //the new scene is set to the stage and displayed.
        stage.setScene(scene);   
        stage.show();
    
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
     
}
