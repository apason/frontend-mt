package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;



public class StatusService {

    public static class StaticStatusService {
    
        protected static final String urli = "https://mobiilitiedekerho.duckdns.org:27461/"; //The IP of the back-end server, it is needed to add parameters to it to be able to comunicate with it. Hard-coded.
    
        protected static boolean loggedIn;
        protected static String nick;
        protected static String authToken;
        
        protected static final JsonConverter jc = new JsonConverter();
        protected static final ServerCommunication sc = new ServerCommunication();
        protected static final FileHandling fh = new FileHandling();

        public static void setAuthToken (String at) {
            authToken = at;
        }

        public static String getAuthToken() {
            return authToken;
        }
    }

}


