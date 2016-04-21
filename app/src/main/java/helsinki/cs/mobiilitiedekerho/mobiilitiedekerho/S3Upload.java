package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A class for uploading videos to S3.
 * When calling execute pass as String... urli the signed url to be used for upload the file.
 */
public class S3Upload extends AsyncTask<String, Void, String> {

    private TaskCompleted act;
    private File selectedFile;
    
    /**
    * Constructor for S3Upload.
    * @param act a interface for being able to pass the response for the calling activity.
    * @param selectedFile the file to be uploaded
    */
    public S3Upload(TaskCompleted act, File selectedFile){
        this.act = act;
        this.selectedFile = selectedFile;
    }
    
    
    protected String doInBackground(String... urli) {
        try {
        	URL url = new URL(urli[0]);
        	
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            
            //# Sets the metadata to the file to be uploaded.
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            //TODO: There is no need for the extension part ".type" actually (except for Windows) so possibly there could be a viable file without its type written in the file's name.
            //Also the extension may or may not tell the truth about the file's type. (No need to take into account?)
            //In android it is possible to use "android.media.MediaMetadataRetriever" for reading the file's metadata and get the real type. TODO: Do this to the File selectedFile, Note: this is not very favored for some reason, why?
            String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".")).toLowerCase(); //toLowerCase in the (odd) case of the extension being in UpperCase, else MimeType may not recognize it.
            String type = mime.getMimeTypeFromExtension(ext); //Gets the Mime type corresponding to the extension. E.G: mp4 -> video/mp4
            connection.setRequestProperty("Content-Type", type); // Very important ! It won't work without adding this!
            connection.setRequestProperty("Content-Disposition", "inline");
            
            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(selectedFile));
            int i;
            // read byte by byte until end of stream
            while ((i = bis.read()) > -1) {
                bos.write(i);
            }
            bos.close();
            
            connection.disconnect();
            
            return "succes";
        } catch (Exception e) {
            return "failure";
        }
        //TODO: Check response code, connection.getResponseCode()
    }
    
    protected void onPostExecute(String result) {
        act.taskCompleted(result);
    }
    
}