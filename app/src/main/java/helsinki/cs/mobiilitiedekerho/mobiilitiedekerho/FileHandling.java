package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for managing all kind of file saving and loading. WIP
 * TODO: Should this use SharedPreferences?. Many users supported.
 */
public class FileHandling {

    /**
    * If there is saved the data of a user, loggedIn will be true.
    * Respectively nick will be the user's nick and authToken the user's auth_token.
    * @return true if there is a saved user.
    */
    public boolean CheckIfSavedUser() {
        File path = Environment.getDataDirectory(); //The data directory of the application.
        File file = new File(path, "user.txt");

        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                //Fort now:
                String nick = br.readLine();
                String token = br.readLine();
                br.close();
                
                StatusService.StaticStatusService.nick = nick;
                StatusService.StaticStatusService.authToken = token;
                StatusService.StaticStatusService.loggedIn = true;

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
    * Save the needed data into a text file for future auto-login.
    * @param nick the user's nick which auth_token is to be saved.
    * @param token the auth_token to be saved.
    */
    public void saveUser(String nick, String token) {
        //SharedPreferences.Editor editor = getSharedPreferences(nick, MODE_PRIVATE).edit(); //MODE_PRIVATE is just: 0
        //editor.putString(nick, token).commit();
    
        FileOutputStream stream = null;
        try {
            File path = Environment.getDataDirectory(); //The data directory of the application.
            File file = new File(path, "user.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            stream = new FileOutputStream(file);

            stream.write((nick + "\n" + token).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
