package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * A class for communicating with the back-end server via HTTPS.
 * Give the wanted url(parameters included) for making API calls to the server.
 */
public class HTTPSRequester extends AsyncTask<String, String, String> {

    private TaskCompleted act;

    /**
    * Constructor for HTTPSRequester.
    * @param act a interface for being able to pass the response for the calling ativity.
    */
    public HTTPSRequester(TaskCompleted act){
        this.act = act;
    }

    HttpURLConnection urlConnection = null;

    //TODO: Problem handling.
    protected String doInBackground(String... urli) {
        try {

            URL url = new URL(urli[0]);
        
            urlConnection = (HttpURLConnection) url.openConnection();
        
            //Creates a string (for JsonConverter to be parsed) from the connection's inputStream.
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();
        
        
        } catch (MalformedURLException e) {
            Log.i("MalformedURLException", urli.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException", urli.toString());
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        
        return "{\"status\":\"CommunicationWithServerError\"}"; //A problem has been encountered while either calling the API or the response its damaged in some way (strange if data checking...) => Some special precautions to take?
    }
    
    
    protected void onPostExecute(String result) {
        act.taskCompleted(result);
    }

}