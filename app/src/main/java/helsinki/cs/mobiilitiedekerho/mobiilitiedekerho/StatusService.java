package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;



public class StatusService {

    public static class StaticStatusService {
    
        protected static final String urli = "https://mobiilitiedekerho.duckdns.org:27461/"; //The IP of the back-end server, it is needed to add parameters to it to be able to comunicate with it. Hard-coded.
    
    
        protected static boolean loggedIn;
        protected static String authToken;
        
        protected static String currentNick; //the not-unique name of the curent subuser.
        protected static String currentSubUserID;
    
        protected static String[] aviableSubUsers; //The Ids of the aviable subusers for the current user. TODO ?: String[] -> ???
        
        //Note: These variables declare the resolution of the screen in pixels, in this APP is the landscape orientation (width is the larger).
        protected static int screenWidth;
        protected static int screenHeight;
    
    
        protected static final JsonConverter jc = new JsonConverter();
        protected static final ServerCommunication sc = new ServerCommunication();
        protected static final FileHandling fh = new FileHandling();
        
        //protected Context context;

    }

}


