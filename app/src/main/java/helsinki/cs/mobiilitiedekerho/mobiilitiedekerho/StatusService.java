package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.content.Context;

import java.util.ArrayList;

public class StatusService {

    /**
     *  A class that saves important states needed for the operation of this app
     */
    public static class StaticStatusService {

        //The DNS name of the back-end server. Hard-coded and will always be.
        protected static final String urli = "https://mobiilitiedekerho.duckdns.org:27461/";

        protected static final String VideoPlay_HtmlTemplate = "<!DOCTYPE html><head><script>var elem = document.getElementById(\"video\");if (elem.requestFullscreen) {elem.requestFullscreen();} else if (elem.msRequestFullscreen) {elem.msRequestFullscreen();} else if (elem.mozRequestFullScreen) {elem.mozRequestFullScreen();} else if (elem.webkitRequestFullscreen) {elem.webkitRequestFullscreen();}elem.play();</script></head><body><video controls id=\"video\" width=\"100%\" height=\"100%\" preload=\"auto\" data-setup=\"{}\" autoplay><source src=\"#video_src#\"></video></body>";
        
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

        //Note: These variables declare the resolution of the screen in pixels, in this APP is the landscape orientation (width is the larger).
        protected static int screenWidth;
        protected static int screenHeight;

        // Keeps track of the RadioButtons in UserActivity. Defines who can view the user's videos.
        // 1 = Only the user itself, 2 = Only registered users, 3 = Anyone
        protected static int usageRights;
        
        
        protected static ArrayList<Integer> categories;
        protected static ArrayList<ArrayList<Integer>> category;
    
    
        protected static final JsonConverter jc = new JsonConverter();
        protected static final ServerCommunication sc = new ServerCommunication();
        protected static final FileHandling fh = new FileHandling();
        
        protected static Context context;

        //For VideoScreen:
        protected static String url;
        protected static String mediaTypee;
    }
    
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


