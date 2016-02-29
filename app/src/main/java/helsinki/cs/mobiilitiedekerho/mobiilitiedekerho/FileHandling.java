package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Class for managing all kind of file saving and loading. WIP
 */
public class FileHandling {
    
    /**
    * If there is saved the data of a user, it does return the url of AuthenticateUser if not then YOUR PROBLEM.
    * (TODO: Encrypted/etc file loading.)
    */
    public String CheckIfSavedUser() {
        File path = Environment.getDataDirectory(); //The data directory of the application.
        File file = new File(path, "user.txt");

        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                //Fort now:
                String email = br.readLine();
                String password = br.readLine();
                br.close();

                return StatusService.StaticStatusService.sc.AuthenticateUser(email, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else return "YOUR PROBLEM";

    }

    /**
    * Save the needed data into a text file for future auto-login.
    * (TODO:  Encryption / better way to save data.)
    */
    public void saveUser(String email, String password) {
        FileOutputStream stream = null;
        try {
            File path = Environment.getDataDirectory(); //The data directory of the application.
            File file = new File(path, "user.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            stream = new FileOutputStream(file);

            stream.write((email + "\n" + password).getBytes());
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
