package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Class for managing all kind of file saving and loading.
 * TODO: Specially many sub-users extra stuff?
 */
public class FileHandling extends AppCompatActivity {


    private String token = "token";
    private String usageRights = "usagerights";


    /**
    * If there is saved the token for a user, loggedIn will be true.
    * Respectively authToken will become the user's auth_token.
    * @return true if there is a saved token for an user and reading it worked out.
    */
    public boolean CheckIfSavedToken() {
        SharedPreferences sharedPref = StatusService.StaticStatusService.context.getSharedPreferences("mobiilitiedekerho",Context.MODE_PRIVATE);
        String tokenValue = sharedPref.getString(token, "Tokenia ei löydy");
        Log.i("tokeni", tokenValue);
        if (tokenValue.equals("Tokenia ei löydy"))
            return false;
        else {
            StatusService.StaticStatusService.authToken = tokenValue;
            return true;
        }

    }

    /**
    * Save the user's token into SharedPreferences with key token for future auto-login.
    * @return true if saving the token worked out.
    */
    public boolean saveToken () {
        SharedPreferences sharedPref = StatusService.StaticStatusService.context.getSharedPreferences("mobiilitiedekerho",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(token, StatusService.StaticStatusService.authToken);
        Log.i("tokeni", StatusService.StaticStatusService.authToken);
        return editor.commit();
    }


    public boolean saveUsageRights(int i) {
        SharedPreferences sharedPref = StatusService.StaticStatusService.context.getSharedPreferences("mobiilitiedekerho", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(usageRights, Integer.toString(StatusService.StaticStatusService.usageRights));
        Log.i("usagerights", Integer.toString(StatusService.StaticStatusService.usageRights));
        return editor.commit();
    }

    public int getUsageRights() {
        SharedPreferences prefs = StatusService.StaticStatusService.context.getSharedPreferences("mobiilitiedekerho", Context.MODE_PRIVATE);
        String rights = prefs.getString(usageRights, null);
        if (rights == null) {
            saveUsageRights(1);
            return 1;
        } else
        return Integer.parseInt(rights);
    }


    /**
    * Checks whether the pointed image (or any file...) exist in applications allocated Internal Storage.
    * @param name the file's' name to be checked if exists.
    * @return true if the asked file exists.
    */
    public boolean checkIfImageExists(String name) {

        File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
        //This is due to a racing condition bug in <4.4 where .getFilesDir() may return realy rarely the root directory "/".
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
    * @param name the to-be-saved file's name.
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
            if(file.exists()) StatusService.StaticStatusService.context.deleteFile(name); //Deletes the existing file just in case. Otherwise truncating may mess up things.
            file.createNewFile();

            stream = new FileOutputStream(file);

            boolean savedAccomplished = image.compress(Bitmap.CompressFormat.PNG, 100, stream); //Compress and save the image as png.
            //Sometimes it is worth to try again. This does remove the previous file and create a new FileOutputStream which is used to save (again) the image.
            if (!savedAccomplished) {
                StatusService.StaticStatusService.context.deleteFile(name);
                file.createNewFile();
                stream = new FileOutputStream(file);
                savedAccomplished = image.compress(Bitmap.CompressFormat.PNG, 100, stream);

            }
            workedOut = savedAccomplished;

        } catch (Exception e) {
            Log.e("Problem saving image", e.toString());
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                Log.e("Problem saving image", e.toString());
            }
        }

        return workedOut;
    }

}