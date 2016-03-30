package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.content.Context;

import java.util.ArrayList;

public class StatusService {

    public static class StaticStatusService {

        protected static final String urli = "https://mobiilitiedekerho.duckdns.org:27461/"; //The DNS name of the back-end server. Hard-coded and will always be.
        
        protected static final String s3Location = "https://s3.eu-central-1.amazonaws.com/"; //TODO: Not hard-coded but from the back-end server.
        protected static final String taskBucket = "p60v4ow30312-tasks"; //TODO: Not hard-coded but from the back-end server.
        protected static final String answerBucket = "p60v4ow30312-answers"; //TODO: Not hard-coded but from the back-end server.
        protected static final String graphicsBucket = "not used yet"; //TODO: Not hard-coded but from the back-end server.
        

        protected static boolean loggedIn;
        protected static String authToken;
        protected static int [] categories;
        protected static ArrayList [] task;

        protected static String currentNick; //the not-unique name of the current subuser.
        protected static String currentSubUserID;
    
        protected static String[] aviableSubUsers; //The IDs of the aviable subusers for the current user. TODO ?: String[] -> ???

        //Note: These variables declare the resolution of the screen in pixels, in this APP is the landscape orientation (width is the larger).
        protected static int screenWidth;
        protected static int screenHeight;
    
    
        protected static final JsonConverter jc = new JsonConverter();
        protected static final ServerCommunication sc = new ServerCommunication();
        protected static final FileHandling fh = new FileHandling();
        
        protected static Context context;
    }
    

    public static boolean loggedIn() {
        return StaticStatusService.loggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        StaticStatusService.loggedIn = loggedIn;
    }

    public static int StagetScreenWidth() {
        return StaticStatusService.screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        StaticStatusService.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return StaticStatusService.screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        StaticStatusService.screenHeight = screenHeight;
    }
}


