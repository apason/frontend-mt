package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


public class StatusService {

    public static class StaticStatusService {
    
        protected static final String urli = "https://mobiilitiedekerho.duckdns.org:27461/"; //The IP of the back-end server, it is needed to add parameters to it to be able to comunivate with it. Hard-coded.
    
        protected static boolean loggedIn;
        protected static String nick;
        protected static String token;
        
        protected final JsonConverter jc = new JsonConverter();
        protected final ServerCommunication sc = new ServerCommunication();
        protected final FileHandling fh = new FileHandling();
        
    }

}


