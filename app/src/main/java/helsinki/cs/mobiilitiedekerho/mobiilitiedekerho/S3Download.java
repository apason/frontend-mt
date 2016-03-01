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
 * A class for downloading (streaming down) videos from S3.
 */
protected class S3Download extends AsyncTask<String, void, String> {
//TODO: No idea yet of what to do!
    private TaskCompleted act;

    /**
    * Constructor for S3Download.
    * @param act a interface for being able to pass the response for the calling ativity.
    */
    public S3Download(TaskCompleted act){
        this.act = act;
    }
    
    
    protected void doInBackground(String urli) {
        
    }
    
    protected void onPostExecute(String result) {
        act.taskCompleted(result);
    }
    
}