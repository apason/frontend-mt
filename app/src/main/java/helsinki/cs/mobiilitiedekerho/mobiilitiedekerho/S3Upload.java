package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.os.AsyncTask;
// import android.util.Log;
// 
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.net.MalformedURLException;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.io.IOException;

/**
 * A class for uploading videos to S3.
 */
protected class S3Upload extends AsyncTask<String, void, String> {
//TODO: No idea yet of what to do!
    
    /**
    * Constructor for S3Upload.
    * @param act a interface for being able to pass the response for the calling ativity.
    */
    public S3Upload(TaskCompleted act){
        this.act = act;
    }
    
    
    protected void doInBackground(String urli) {
        
    }
    
    protected void onPostExecute(String result) {
        act.taskCompleted(result);
    }
}