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
 * After downloading, the retrieved images will be saved to the APP's private storage-area as PNG. (Internal Storage).
 * Give as many imageNames (strings) pointing to pictures in S3 as you wish.
 */
public class S3Download extends AsyncTask<String, Void, String> {

    private TaskCompleted act;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> imageNames;

    /**
    * Constructor for S3Download.
    * @param act a interface for being able to pass the response for the calling activity.
    * @param imageNames the names of the images to be downloaded, note that they are the names which how they are saved to memory and how saved to S3.
    */
    public S3Download(TaskCompleted act, ArrayList<String> imageNames){
        this.act = act;
        this.imageNames = imageNames;
        bitmaps = new ArrayList<Bitmap>();
    }
    
    
    protected String doInBackground(String... urls) {
        HttpURLConnection urlConnection = null;
        
        try {
            for (int i = 0 ; i < imageNames.size() ; i++) {
                URL url = new URL(StatusService.StaticStatusService.s3Location + StatusService.StaticStatusService.graphicsBucket + "/" + imageNames.get(i));
                urlConnection = (HttpURLConnection) url.openConnection();
                bitmaps.add(BitmapFactory.decodeStream(urlConnection.getInputStream()));
            }
            return "success";
        } catch (MalformedURLException e) {
            Log.i("MalformedURLException", "");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.i("SomeError", "");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        
        return "failure";
    }
    
    
    protected void onPostExecute(String result) {
    	if (!result.equals("success"))
    		act.taskCompleted(result);
    	
        for (int i = 0 ; i < imageNames.size() ; i++) {
            StatusService.StaticStatusService.fh.saveImage(imageNames.get(i), bitmaps.get(i)); //If returns false didn't worked out, => ????
        }
        
        act.taskCompleted(result);
    }
    
}
