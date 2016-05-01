package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.content.Context;

import java.util.ArrayList;


// This is for 'wrapping' the static-class, android does not let declare a static class directly.
public class StatusService {

    /**
     *  A class that saves important states needed for the operation of this app.
     */
    public static class StaticStatusService {

        //The DNS name of the back-end server. Hard-coded.
        protected static final String urli = "https://mobiilitiedekerho.duckdns.org:27461/";


        // This is the html-template to be used for streaming videos from S3. #Video_src# is programatically replaced with the url of the stream.
        // It is used so that autoplay would work and auto-fullscreen too. (Android doesn't let this to be done programatically otherwise.)
        // TODO: Auto-fullscreen does not work for some reason.
        // TODO: It may be better to not hard-code it like this.
        protected static final String VideoPlay_HtmlTemplate = "<!DOCTYPE html><head><script>var elem = document.getElementById(\"video\");if (elem.requestFullscreen) {elem.requestFullscreen();} else if (elem.msRequestFullscreen) {elem.msRequestFullscreen();} else if (elem.mozRequestFullScreen) {elem.mozRequestFullScreen();} else if (elem.webkitRequestFullscreen) {elem.webkitRequestFullscreen();}elem.play();</script></head><body><video controls id=\"video\" width=\"100%\" height=\"100%\" preload=\"auto\" data-setup=\"{}\" autoplay><source src=\"#video_src#\"></video></body>";


        /* These are variables storing info about the current user. */
        // Is the user logged in
        protected static boolean loggedIn;

        // The current authentication token
        protected static String authToken;

        // The not-unique name of the current sub-user.
        protected static String currentNick;

        // The id of the current sub-user
        protected static String currentSubUserID;

        //The IDs of the available sub-users for the current user.
        protected static String[] availableSubUsers;


        // Keeps track of the RadioButtons in UserActivity. Defines who can view the user's videos.
        // 1 = Only the user itself, 2 = Only registered users, 3 = Anyone
        protected static int usageRights;
        /* */
        
        //Note: These variables declare the resolution of the screen in pixels, in this APP is the landscape orientation (width is the larger).
        protected static int screenWidth;
        protected static int screenHeight;


        /*  These are references to class instances to be used all over the program.
            They contain methods to be called from activities.
            Note: See each class documetation for further information about them.   */
        // The JSON parser class that is used to parse the response from the server.
        protected static final JsonConverter jc = new JsonConverter();

        // A class for creating urls to make calls for the server.
        protected static final ServerCommunication sc = new ServerCommunication();

        // A class for saving data to the devices persistent memory.
        protected static final FileHandling fh = new FileHandling();
        /* */
        // This is for the 'helper-classes' since the apllication's context is needed for saving data, etc.
        protected static Context context;


        // For VideoScreen-activity, they are like parameters for what to show:
        // (Note supposed to be setted only in MainActivity's playVideo()-method)
        protected static String url;
        protected static String mediaTypee;


        // Originally meant for 'data-preloading' from the server, could store IDs and based on them show icons, etc.
        // NOT in use!
        // protected static ArrayList<Integer> categories;
        // protected static ArrayList<ArrayList<Integer>> category;
    }


    // All kind of methods for setting variables in StaticStatusService that cannot be accesed directly.
    
    // Returns true if the user has logged in, otherwise false
    public static boolean loggedIn() {
        return StaticStatusService.loggedIn;
    }

    // Sets the logged in state for the current user
    public static void setLoggedIn(boolean loggedIn) {
        StaticStatusService.loggedIn = loggedIn;
    }

    // Returns the device's screen width
    public static int StagetScreenWidth() {
        return StaticStatusService.screenWidth;
    }

    // Returns the device's screen width
    public static void setScreenWidth(int screenWidth) {
        StaticStatusService.screenWidth = screenWidth;
    }

    // Returns the device's screen height
    public static int getScreenHeight() {
        return StaticStatusService.screenHeight;
    }

    // Set the screen height to be used
    public static void setScreenHeight(int screenHeight) {
        StaticStatusService.screenHeight = screenHeight;
    }

    // Set the privacy level for the user. 1 = Just me, 2 = Registered users, 3 = Anyone
    public static void setUsageRights(int i) {
        StaticStatusService.usageRights = i;
    }

    // Get the privacy level of the user. 1 = Just me, 2 = Registered users, 3 = Anyone
    public static int getUsageRights() {
        return StaticStatusService.usageRights;
    }

    // Set the current sub-user
    public static void setSubUser(String s) {
        StaticStatusService.currentSubUserID = s;
    }

    // Get the current sub-user
    public static String getSubUser() {
        return StaticStatusService.currentSubUserID;
    }
}
