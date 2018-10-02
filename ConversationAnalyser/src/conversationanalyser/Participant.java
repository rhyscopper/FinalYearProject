/*
    this is the participants class which is used to store the details of the participants.
    in this both the names and ids of the individuals in the conversation are stored.
*/
package conversationanalyser;

/**
 *
 * @author Rhys
 */
public class Participant {
    
    // the two strings are created to store the user id and name
    private String uid;
    private String name;
    
    // this is the constructer of the program which initialises the variables
    public Participant(String uid, String name){
        this.uid = uid;
        this.name = name;
    }

    // this is the getter method of the user id
    public String getUid() {
        return uid;
    }

    // this is the setter method of the user id
    public void setUid(String uid) {
        this.uid = uid;
    }

    // this is the getter method of the name of the individual
    public String getName() {
        return name;
    }

    // this is the setter method of the name of the individual
    public void setName(String name) {
        this.name = name;
    }
}