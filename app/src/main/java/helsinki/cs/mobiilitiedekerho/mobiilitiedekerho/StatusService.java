package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.content.Context;

import java.util.ArrayList;

public class StatusService {

    public static class StaticStatusService {

        protected static final String urli = "https://mobiilitiedekerho.duckdns.org:27461/"; //The DNS name of the back-end server. Hard-coded and will always be.
        protected static String url = "url";

        protected static String s3Location = "https://s3-eu-central-1.amazonaws.com/";
        protected static String taskBucket = "mobiilitiedekerho-tasks";
        protected static String answerBucket = "mobiilitiedekerho-answers";
        protected static String graphicsBucket = "mobiilitiedekerho-graphics";

        //protected static final String VideoPlay_HtmlTemplate = "<!DOCTYPE html><head><script>var elem = document.getElementById(\"video\");if (elem.requestFullscreen) {elem.requestFullscreen();} else if (elem.msRequestFullscreen) {elem.msRequestFullscreen();} else if (elem.mozRequestFullScreen) {elem.mozRequestFullScreen();} else if (elem.webkitRequestFullscreen) {elem.webkitRequestFullscreen();}elem.play();</script></head><body><link href=\"//vjs.zencdn.net/5.8/video-js.min.css\" rel=\"stylesheet\"><script src=\"//vjs.zencdn.net/5.8/video.min.js\"></script><video controls id=\"video\" class=\"video-js vjs-default-skin\" width=\"100%\" height=\"100%\" preload=\"auto\" data-setup=\"{}\" autoplay><source src=\"#video_src#\"></video></body>";
        protected static final String VideoPlay_HtmlTemplate = "<!DOCTYPE html><head><script>var elem = document.getElementById(\"video\");if (elem.requestFullscreen) {elem.requestFullscreen();} else if (elem.msRequestFullscreen) {elem.msRequestFullscreen();} else if (elem.mozRequestFullScreen) {elem.mozRequestFullScreen();} else if (elem.webkitRequestFullscreen) {elem.webkitRequestFullscreen();}elem.play();</script></head><body><video controls id=\"video\" width=\"100%\" height=\"100%\" preload=\"auto\" data-setup=\"{}\" autoplay><source src=\"#video_src#\"></video></body>";
        

        protected static boolean loggedIn;
        protected static String authToken;

        protected static String currentNick; //the not-unique name of the current sub-user.
        protected static String currentSubUserID;
    
        protected static String[] availableSubUsers; //The IDs of the available sub-users for the current user. TODO ?: String[] -> ???

        //Note: These variables declare the resolution of the screen in pixels, in this APP is the landscape orientation (width is the larger).
        protected static int screenWidth;
        protected static int screenHeight;

        // Keeps track of the RadioButtons in UserActivity. Defines who can view the user's videos.
        // 0 = Only the user himself, 1 = Only registered users, 2 = Anyone
        protected static int usageRights;
        
        
        protected static ArrayList<Integer> categories;
        protected static ArrayList<ArrayList<Integer>> category;
    
    
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

    public static void setUsageRights(int i) {
        StaticStatusService.usageRights = i;
        FileHandling fh = new FileHandling();
        fh.saveUsageRights(i);
    }

    public static int getUsageRights() {
        FileHandling fh = new FileHandling();
        return fh.getUsageRights();
    }
}


