package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.graphics.Bitmap;
import android.os.AsyncTask;
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

    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {

        }
    }

    AsyncTask hp;

    /**
     * If there is saved the token for a user, loggedIn will be true.
     * Respectively authToken will become the user's auth_token.
     * @return true if there is a saved user.
     */
    public boolean checkIfValidToken(String response) throws FileNotFoundException{

        File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
        //This is due to a racing condition bug in <4.4 where .getFilesDir() may return really rarely the root directory "/".
        if(path.getAbsolutePath().equals("/")) {
            path = StatusService.StaticStatusService.context.getFilesDir(); //Just try again!
        }

        File file = new File(path, "token");

        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream stream = new FileInputStream(file);

        boolean existed = false;

        if (file.exists()) {
            try {
                stream.read(bytes);

                StatusService.StaticStatusService.authToken = new String(bytes);
                String url = StatusService.StaticStatusService.sc.S;
                hp = new HTTPSRequester(new listener()).execute(url);

                //StatusService.StaticStatusService.loggedIn = true;

                existed = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return existed;
    }

    /**
    * If there is saved the token for a user, loggedIn will be true.
    * Respectively authToken will become the user's auth_token.
    * @return true if there is a saved user.
    */
/*    public boolean CheckIfSavedToken() throws FileNotFoundException{

        File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
        //This is due to a racing condition bug in <4.4 where .getFilesDir() may return really rarely the root directory "/".
        if(path.getAbsolutePath().equals("/")) {
            path = StatusService.StaticStatusService.context.getFilesDir(); //Just try again!
        }
        
        File file = new File(path, "token");
        
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream stream = new FileInputStream(file);
        
        boolean existed = false;

        if (file.exists()) {
            try {
                stream.read(bytes);
                
                StatusService.StaticStatusService.authToken = new String(bytes);
                StatusService.StaticStatusService.loggedIn = true;

                existed = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return existed;
    }
*/
    /**
    * Save the user's token into a text file for future auto-login.
    */
    public void saveToken () throws FileNotFoundException {

        //SharedPreferences.Editor editor = getSharedPreferences(nick, MODE_PRIVATE).edit(); //MODE_PRIVATE is just: 0
        //editor.putString("token", token).commit();

        File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
        //This is due to a racing condition bug in <4.4 where .getFilesDir() may return really rarely the root directory "/".
        if(path.getAbsolutePath().equals("/")) {
            path = StatusService.StaticStatusService.context.getFilesDir(); //Just try again!
        }
        File file = new File(path, "token");
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write((StatusService.StaticStatusService.authToken).getBytes()); //Created file if didn't existed before.
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
    */

    public void saveImage(String name, Bitmap image) throws IOException{

        File path = StatusService.StaticStatusService.context.getFilesDir(); //The data directory of the application.
        //This is due to a racing condition bug in <4.4 where .getFilesDir() may return really rarely the root directory "/".
        if(path.getAbsolutePath().equals("/")) {
            path = StatusService.StaticStatusService.context.getFilesDir(); //Just try again!
        }
        File file = new File(path, name);
        FileOutputStream stream = new FileOutputStream(file); //Created file if didn't existed before.
        
        try {
            image.compress(Bitmap.CompressFormat.PNG, 100, stream); //Compress and save the image. TODO: Check if true?, that is if it worked out.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
   
//     public void saveImage(String name, Bitmap image) {
//         FileOutputStream stream = null;
//         try {
//              stream = StatusService.StaticStatusService.context.openFileOutput(Name, Context.MODE_PRIVATE); 
//              image.compress(Bitmap.CompressFormat.PNG, 100, stream); //Compress and save the image.
// 
//             /*if (!file.exists()) {
//                 file.createNewFile();
//             */
//            
//         } catch (IOException e) {
//             e.printStackTrace();
//         } finally {
//             try {
//                 stream.close();
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }
//     }

}
