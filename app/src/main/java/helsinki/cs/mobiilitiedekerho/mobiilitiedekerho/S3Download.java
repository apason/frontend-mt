package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A class for downloading the needed graphics from S3.
 * After downloading, the retrieved images will be saved to the APP's private storage-area as PNG. (Internal Storage).
 * Give as many urlss (strings) and the corresponding imageNames pointing to pictures in S3 as you wish.
 */
public class S3Download extends AsyncTask<String, Void, String> {

    private TaskCompleted act;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> imageNames;
    private ArrayList<String> urlss;

    /**
    * Constructor for S3Download.
    * @param act a interface for being able to pass the response for the calling activity.
    * @param imageNames the names of the images to be downloaded, note that they are the names which how they are saved to memory and how saved to S3.
    * @param urlss the urls pointing to images to be downloaded.
    *   "imageNames" must match the order of "urlss".
+   *   If their length doesn't match, async will stop and return "'Image names' and 'urls' don't match in size" (to avoid crashing)
    * Note (@return after executing): "succes" if all went right, "failure" communication with S3 failed and if only some images couldn't be saved then it returns their indexes in a string in format: "index:index:", indes is the index and ":" a separator.
    */
    public S3Download(TaskCompleted act, ArrayList<String> imageNames, ArrayList<String> urlss){
        this.act = act;
        this.imageNames = imageNames;
        this.urlss = urlss;
        bitmaps = new ArrayList<Bitmap>();
    }


    protected String doInBackground(String... urls) {
        if(urlss.size() != imageNames.size()) 
            return "'ImageNames' and 'urlss' don't match in size";
    
        HttpURLConnection urlConnection = null;

        try {
            for (int i = 0 ; i < urlss.size() ; i++) {
                Log.i("urli", StatusService.StaticStatusService.s3Location + StatusService.StaticStatusService.graphicsBucket + "/" + urlss.get(i));

                URL url = new URL(StatusService.StaticStatusService.s3Location + StatusService.StaticStatusService.graphicsBucket + "/" + urlss.get(i));
                Log.i("kuvaurli", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                bitmaps.add(BitmapFactory.decodeStream(urlConnection.getInputStream()));
            }
            return "success";
        } catch (MalformedURLException e) {
        Log.e("MalformedURLException", e.toString());
        } catch (IOException e) {
        Log.e("IOException", e.toString());
        } catch (Exception e) {
        Log.e("SomeError", e.toString());
        } finally {
        if (urlConnection != null) urlConnection.disconnect();
        }

        return "failure";
    }


    protected void onPostExecute(String result) {
        if (!result.equals("success"))
            act.taskCompleted(result);
        //TEST
        for (String name: imageNames) {
            Log.i("kuvanimiS3", name);
        }
        //TEST
        
        String problems = ""; //A 'list' of images with which saving didn't work out.

        for (int i = 0 ; i < imageNames.size() ; i++) {
            if (!StatusService.StaticStatusService.fh.saveImage(imageNames.get(i), bitmaps.get(i))){
                Log.i("feilasi", imageNames.get(i));
                problems = problems + i + ":";
                //If returns false then it didn't work out, => only ^ ???
            }
        }
        if (problems.equals("")) act.taskCompleted(result);
        else act.taskCompleted("problems");
    }

}