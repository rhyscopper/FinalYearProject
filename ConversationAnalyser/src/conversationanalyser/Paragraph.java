/*
    this is the class Paragaph which is used to store all of the information of each
    parapgraph to be printed. in this the person who speaks the paragraph, their id,
    the time it was spoken and the words that were spoken are stored.
*/
package conversationanalyser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rhys
 */
public class Paragraph {
    
    // the variables created to store the data..
    private String who;
    private String id;
    private double time;
    private List<String> words;
    
    // the construcrtor of the class that initialises the variabels.
    public Paragraph(String who, String id){
        this.who = who;
        this.id = id;
        this.time = 0;
        words = new ArrayList<String>();
    }
    
    // the method called to add words to the paragraph object.
    public void addWord(String w){
        words.add(w);
    }

    // the getter method used to get who was speaking. 
    public String getWho() {
        return who;
    }

    // the getter mthod to get the id of the individual
    public String getId() {
        return id;
    }
    
    // the getter method to get the time the paragraph was spoken
    public double getTime() {
        return time;
    }
    
    // Setter method to set the start time of the paragraph
    public void setTime(double time){
        this.time = time;
    }
    
    // getter method used to get the list of words.
    public List<String> getWords() {
        return words;
    }
    
    // string method used to get a string representation of the paragraph, which
    // stores who said it and the words they spoke.
    @Override
    public String toString(){
        String s = who+": ";
        for(String w : words){
            s += w + " ";
        }
        return s;
    }
}
