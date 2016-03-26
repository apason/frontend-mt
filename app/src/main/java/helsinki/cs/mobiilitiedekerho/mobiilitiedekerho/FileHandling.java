package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Class for managing all kind of file saving and loading. WIP
 * TODO: Should token saving use SharedPreferences?. Many users supported? Specially many sub-users extra stuff?
 */
public class FileHandling {


    /**
    * If there is saved the token for a user, loggedIn will be true.
    * Respectively authToken will become the user's auth_token.
    * @return true if there is saved a token for an user and reading it worked out.
    */
    public boolean CheckIfSavedToken() {
    	
    	boolean existed = false;

        FileInputStream stream  = null;
        try {
            File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
            //This is due to a racing condition bug in <4.4 where .getFilesDir() may return really rarely the root directory "/".
            if(path.getAbsolutePath().equals("/")) {
                path = StatusService.StaticStatusService.context.getFilesDir(); //Just try again!
            }
            
            File file = new File(path, "token");
            if (file.exists()) {
               	int length = (int) file.length();
               	byte[] bytes = new byte[length];
               	stream = new FileInputStream(file);
               
               	stream.read(bytes);
               
               	StatusService.StaticStatusService.authToken = new String(bytes);
               	StatusService.StaticStatusService.loggedIn = true;
               	existed = true;
            }
        } catch (Exception e) {
        	Log.i("Problem while reading token.", "");
            e.printStackTrace();
        }
        finally {
            try {
               	if (stream != null) stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return existed;
    }

    /**
    * Save the user's token into a text file for future auto-login.
    * @return true if saving the token worked out.
    */
    public boolean saveToken () {
    	
    	boolean workedOut = false;

    	FileOutputStream stream = null;
        try {
            File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
            //This is due to a racing condition bug in <4.4 where .getFilesDir() may return really rarely the root directory "/".
            if(path.getAbsolutePath().equals("/")) {
                path = StatusService.StaticStatusService.context.getFilesDir(); //Just try again!
            }
            File file = new File(path, "token");
            stream = new FileOutputStream(file);
            stream.write((StatusService.StaticStatusService.authToken).getBytes()); //Created file if didn't existed before.
            workedOut = true;
        } catch (Exception e) {
        	Log.i("Problem while saving token", "");
            e.printStackTrace();
        } finally {
            try {
            	if (stream != null) stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return workedOut;

    }
    
    /**
    * Checks whether the pointed image (or any file...) exist in applications allocated Internal Storage.
    * @param name the file's' name to be checked if exists..
    * @return true if exists.
    */
    public boolean checkIfImageExists(String name) {
    	
        File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
        //This is due to a racing condition bug in <4.4 where .getFilesDir() may return really rarely the root directory "/".
        if(path.getAbsolutePath().equals("/")) {
            path = StatusService.StaticStatusService.context.getFilesDir(); //Just try again!
        }
        File file = new File(path, name);

        if (file.exists()) {
            return true;
        } else {
            return false;
        }
        
    }
    
    /**
    * Saves the wanted image to the Applications data directory.
    * @param name the file name.
    * @param image the image to be saved.
    * @return true if saving the image worked out.
    */
    public boolean saveImage(String name, Bitmap image) {
    	
    	boolean workedOut = false;
    	
    	FileOutputStream stream = null;
        try {
            File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
            //This is due to a racing condition bug in <4.4 where .getFilesDir() may return really rarely the root directory "/".
            if(path.getAbsolutePath().equals("/")) {
                path = StatusService.StaticStatusService.context.getFilesDir(); //Just try again!
            }
            File file = new File(path, name);
            stream = new FileOutputStream(file); //Created file if didn't existed before.
            image.compress(Bitmap.CompressFormat.PNG, 100, stream); //Compress and save the image. TODO: Check if true?, that is if it worked out.
            workedOut = true;
        } catch (Exception e) {
        	Log.i("Problem while saving image", name);
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return workedOut;
    }

}
