/*
    this is the instance method which is to set the path of the xml and media files
    from the home page and get them from the main page.
 */
package conversationanalyser;

/**
 *
 * @author Rhys
 */
public class HomeInstanceMethod {
    
    // the instance of the clas and xml and media files string variables are created
    private static final HomeInstanceMethod instance = new HomeInstanceMethod();
    public String xmlPath = "";
    public String mediaPath = "";
    
    // this is the instance method of the class.
    public static HomeInstanceMethod getInstance() {
        return instance;
    }
    
    // this is the getter method of the xml path
    public String getXmlPath() {
        return xmlPath;
    }

    // this is the setter method of the xml path
    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    // this is the getter method of the media path
    public String getMediaPath() {
        return mediaPath;
    }

    // this is the setter method of the media path
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
}
