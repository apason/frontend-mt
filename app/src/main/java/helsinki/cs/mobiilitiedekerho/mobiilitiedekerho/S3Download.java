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
 * A class for downloading the needed graphics from S3.
 * Give as many urls (strings) pointing to pictures in S3 as you wish.
 */
public class S3Download extends AsyncTask<String, Void, String> {
//TODO: No idea yet of what to do!
    private TaskCompleted act;

    /**
    * Constructor for S3Download.
    * @param act a interface for being able to pass the response for the calling ativity.
    */
    public S3Download(TaskCompleted act){
        this.act = act;
    }
    
    
    protected String doInBackground(String... urli) {
        //Some way of loading data from S3.
        //After that save the retrievded data to the APP's private storage-area, just a directory actually. (Internal Storage).
        return "success";
    }
    
    protected void onPostExecute(String result) {
        act.taskCompleted(result);
    }
    
}
