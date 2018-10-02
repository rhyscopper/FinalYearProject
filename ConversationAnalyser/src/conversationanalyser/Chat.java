
/*
    this is the Chat class which stores a list of the participants and the paragraphs.
*/
package conversationanalyser;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Rhys
 */
public class Chat {
    
    // the lists created of the types Participant and Paragraph
    private List<Participant> participants;
    private List<Paragraph> paragraphs;
    
    // the constructor method of the class that initialises the variables
    public Chat(){
        participants = new ArrayList<Participant>();
        paragraphs = new ArrayList<Paragraph>();
    }
    
    // the setter(add) method of the participants
    public void addParticipant(Participant p){
        participants.add(p);
    }
    
    // the setter(add) method of the parapgrahs
    public void addParagraph(Paragraph p){
        paragraphs.add(p);
    }

    // the getter method of the participants
    public List<Participant> getParticipants() {
        return participants;
    }

    // the getter mehtod of the paragraphs.
    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }
}
