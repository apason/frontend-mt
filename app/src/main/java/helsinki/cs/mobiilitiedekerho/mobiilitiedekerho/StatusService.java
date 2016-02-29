package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


public class StatusService {

    public static class StaticStatusService {
    
        protected static final String urli = "https://mobiilitiedekerho.duckdns.org:27461/"; //The IP of the back-end server, it is needed to add parameters to it to be able to comunivate with it. Hard-coded.
    
        protected static boolean loggedIn;
        protected static String nick;
        protected static String token;
        
        private final JsonConverter jc = new JsonConverter();
        private final ServerCommunication sc = new ServerCommunication();
        private final FileHandling fh = new FileHandling();
        
    }

}


