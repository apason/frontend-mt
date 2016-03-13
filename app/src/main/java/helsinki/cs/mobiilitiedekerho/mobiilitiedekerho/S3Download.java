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
 * After downloading, the retrieved data will be saved to the APP's private storage-area. (Internal Storage).
 * Give as many urls (strings) pointing to pictures in S3 as you wish.
 */
public class S3Download extends AsyncTask<String, Void, String> {

    private TaskCompleted act;
    private ArrayList<Bitmap> bitmaps;
	private String[] imageNames;

    /**
    * Constructor for S3Download.
    * @param act a interface for being able to pass the response for the calling activity.
    * @param imageNames the names of the images to be downloaded, note that they are the names which how they are saved to memory.
    * 		 They must match the order of "urls".
    * 		 If their length doesn't match, async will stop and return "'Image names' and 'urls' don't match in size" (to avoid crashing)
    */
    public S3Download(TaskCompleted act, String... imageNames){
        this.act = act;
        this.imageNames = imageNames;
        bitmaps = new ArrayList<Bitmap>();
    }
    
    HttpURLConnection urlConnection = null;
    
    protected String doInBackground(String... urls) {
    	if(urls.length != imageNames.length) 
    		return "'Image names' and 'urls' don't match in size";
    	
        for (int i = 0 ; i < urls.length ; i++) {
            try {
                URL url = new URL(urls[i]);
                urlConnection = (HttpURLConnection) url.openConnection();
                bitmaps.add(BitmapFactory.decodeStream(urlConnection.getInputStream()));
            } catch (MalformedURLException e) {
                Log.i("MalformedURLException", urls[i].toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("IOException", urls[i].toString());
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        }
        
        return "success";
    }
    
    
    protected void onPostExecute(String result) {
    	if (!result.equals("success"))
    		act.taskCompleted(result);
    	
        for (int i = 0 ; i < imageNames.length ; i++) {
            StatusService.StaticStatusService.fh.saveImage(imageNames[i], bitmaps.get(i));
        }
        
        act.taskCompleted(result);
    }
    
}
