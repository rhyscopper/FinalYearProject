/*
    this is the class that is called from the main page controller and is passed
    the xml file path as its parameters. the class returns a chat object back to
    the main page controller which contains all of the data.
*/ 
package conversationanalyser;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

/**
 *
 * @author Rhys
 */

public class ChatParser {
    
    public static Chat parse(String xmlFile){
        
        // a new chat object is creates which is going to used to store
        // the participants and the paragraphs.
        Chat chat = new Chat();
        
        try{
            // a new DocumentBuilderFactory instance that is used to get all
            // all the documents.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFile));
            Element root = doc.getDocumentElement();
            
            // a nodelist is created which stores all of the elements that are contain
            // in a participant tag.
            NodeList nodesParticipants = root.getElementsByTagName("participant");
            // the for loop iterates through each element returned back.
            for(int i=0; i < nodesParticipants.getLength(); i++){
                Element nodeParticipant = (Element)nodesParticipants.item(i);
                // the user id is extracted from the data
                String uid = nodeParticipant.getAttribute("id");
                // the string name is originally set to NoName incase the user has no name.
                String name="NoName";
                // the if is ran if the elements being looked at contains a name
                if(nodeParticipant.hasAttribute("name")){
                    // it gets the name of the user and stores it in name.
                    name = nodeParticipant.getAttribute("name");
                }
                // a new participant instnace is created, passing the user id and name
                // as its paramemets
                Participant p = new Participant(uid, name);
                // the addParticipant method is called on the chat which, adds
                // the new participant to the participant list.
                chat.addParticipant(p);
            }
            
            // gets all the elements surrounded by u tags.
            NodeList nodeParagraphs = root.getElementsByTagName("u"); 
                // the for loop iterates through each element passed back
            for(int i=0; i < nodeParagraphs.getLength(); i++){
                Element nodeParagraph = (Element)nodeParagraphs.item(i);  
                
                // the id of the individual speaking in this element and there userid are colelcted.
                String id = nodeParagraph.getAttribute("uID");
                String who = nodeParagraph.getAttribute("who"); 
                // a new paragraph instance is created passing the indivual speaking 
                // and their id as its parameters.
                Paragraph p = new Paragraph(who, id);
                
                // get all <w>/<blob> and media/internal=media tag of the current <u> tag
                NodeList childNodes = nodeParagraph.getChildNodes();        
                // iterate over all the <w>/<blob> and media/internal-media tags
                for(int j=0; j < childNodes.getLength(); j++){    
                    // gets each piece cof data one at a time
                    Node childNode = childNodes.item(j);
                    // stores the name of the element being manipulated now
                    String tagName = childNode.getNodeName();
                    
                    // if the tagName is equal to w or blob it is known that 
                    // between the tags it is going to contain a single word
                    if(tagName.equals("w") || tagName.equals("blob")){
                        // its gets the text content and add it to the paragraph object
                        String word = childNode.getTextContent();
                        p.addWord(word); 
                    }
                    
                    // if the tag is equal to internal-media or media, we know this stores
                    // the start time of the paragraph.
                    else if (tagName.equals("internal-media") || tagName.equals("media")) {
                        // the element and start value are collected
                        Element childNodeElem = (Element) childNode;                        
                        double start = Double.parseDouble(childNodeElem.getAttribute("start"));
                        // set the start time is set in the paragraph object
                        p.setTime(start);
                        // the paragraph is added to the chat object.
                        chat.addParagraph(p);

                        // empty the paragraph for the next trascript
                        p = new Paragraph(who, id);
                    }
                    else{
                    
                    }
                } 
            }    
        }
        catch(Exception e){
            
        }        
        return chat;
    }
}