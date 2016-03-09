package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for managing all kind of file saving and loading. WIP
 * TODO: Should this use SharedPreferences?. Many users supported? Specially many sub-users extra stuff?
 */
public class FileHandling {

    /**
    * If there is saved the token for a user, loggedIn will be true.
    * Respectively authToken will become the user's auth_token.
    * @return true if there is a saved user.
    */
    public boolean CheckIfSavedToken() {
        File path = Environment.getDataDirectory(); //The data directory of the application.
        File file = new File(path, "token.txt");

        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String token = br.readLine();
                br.close();
                
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
    * Save the user's token into a text file for future auto-login.
    */
    public void saveToken() {
        //SharedPreferences.Editor editor = getSharedPreferences(nick, MODE_PRIVATE).edit(); //MODE_PRIVATE is just: 0
        //editor.putString(nick, token).commit();

        /*BUGI: StatusService.StaticStatusService.authToken ei käy
        FileOutputStream stream = null;
        try {
            File path = Environment.getDataDirectory(); //The data directory of the application.
            File file = new File(path, "token.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            stream = new FileOutputStream(file);
            //BUGI: StatusService.StaticStatusService.authToken ei käy
            //stream.write(StatusService.StaticStatusService.authToken).getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
    }

}
