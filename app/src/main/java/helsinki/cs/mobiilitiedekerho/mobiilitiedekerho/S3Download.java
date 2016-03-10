package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.os.AsyncTask;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class for downloading the needed graphics from S3.
 * Give as many urls (strings) pointing to pictures in S3 as you wish.
 */
public class S3Download extends AsyncTask<String, Void, String> {

    private TaskCompleted act;
    private ArrayList<Bitmap> bitmaps;
    private String... imageNames;

    /**
    * Constructor for S3Download.
    * @param act a interface for being able to pass the response for the calling ativity.
    * @param imageNames the names of the images to be downloaded, note that they are the names which how they are saved to memory. (must mach urli order.)
    */
    public S3Download(TaskCompleted act, string... imageNames){
        this.act = act;
        this.imageNames = imageNames;
        bitmaps = new ArrayList<Bitmap>();
    }
    
    HttpURLConnection urlConnection = null;
    
    protected String doInBackground(String... urls) {
        //Some way of loading data from S3.
        //After that save the retrievded data to the APP's private storage-area, just a directory actually. (Internal Storage).
        for (int i = 0 ; i < urls.length ; i++) {
            try {
                URL url = new URL(requestUrl);
                urlConnection = url.openConnection();
                bitmaps.add(BitmapFactory.decodeStream(urlConnection.getInputStream()));
            } catch (MalformedURLException e) {
                Log.i("MalformedURLException", urli.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("IOException", urli.toString());
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        }
        
        return "success";
    }
    
    
    protected void onPostExecute(String result) {
        for (int i = 0 ; i < imageNames.length ; i++) {
            StatusService.StaticStatusService.fh.saveImage(imageNames[i], bitmaps.get(i));
        }
        
        act.taskCompleted(result);
    }
    
}
