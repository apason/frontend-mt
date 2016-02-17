package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.IntentService;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.os.Environment;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;

/**
 * A class for communicating with the back-end server via HTTP.
 * Use the public methods for making API calls to the server.
 */
public class ServerCommunication extends IntentService {

    private String urli = "mobiilitiedekerho.duckdns.org"; //The IP of the back-end server, it is needed to add parameters to it to be able to comunivate with it. Hard-coded.
    private String authToken;

    private JsonConverter jc = new JsonConverter();

//     public IBinder onBind(Intend intend) {
//      
//     }



    /**
     * Creates a new HttPService class and gets a new anonymous hash for use in API calls.
     */
    public ServerCommunication() {
        super("ServerCommunication");
        StartSession();
        CheckIfSavedUser();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //En vieläkään tiedä mitä tänne laittaa!
    }

    

    //Notices the server so that a anonymus session would be linked to this client.
    private void StartSession() {
        jc.newJson(getResponse("StartSession"));

        this.checkstatus();

        authToken = jc.getProperty("auth_token");
    }
    
    //If there is saved the login data of a user, it does AuthenticateUser().
    private void CheckIfSavedUser() {
        File path = Environment.getDataDirectory(); //The data directory of the application.
        File file = new File(path, "user.txt");
        
        this.checkStatus();

        authToken = jc.getProperty("auth_token");
    }

    //If there is saved the data of a user, it does AuthenticateUser.
    private void CheckIfSavedUser() {
        File path = Environment.getDataDirectory(); //The data directory of the application.
        File file = new File(path, "user.txt");

        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                //Fort now:
                String email = br.readLine();
                String password = br.readLine();
                br.close();

                this.AuthenticateUser(email, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    //Save the needed data into a text file for future auto-login. (TODO: Encryption)
    //Save the needed data into text file for future auto-login.
    private void saveUser(String email, String password) {
        FileOutputStream stream = null;
        try {
            File path = Environment.getDataDirectory(); //The data directory of the application.
            File file = new File(path, "user.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            stream = new FileOutputStream(file);

            stream.write((email + "\n" + password).getBytes());
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
    

    private boolean checkStatus() {
        String state = jc.getProperty("status");
        if (state != "success") {
            //Something. Check which error actually happened at the server.
            return false;
        }
        return true;
    }


    /**
     * private
     * This method returns the JSON response as String of the wanted API call.
     * TODO: Problem handling.
     * @param API_call: That is the API call to be executed.
     * @param paramsAndValues: Parameter and value pair, odd ones are the parameters and even ones the values.
     * Note: It does add automatically the user's hash except if API_call is getAnonymiousHash.
     * @return the response from the API call as a JSON string.
     */
    private String getResponse(String API_call, String... paramsAndValues) {

        HttpURLConnection urlConnection = null;

        try {
            //Creates the query to be added to the URL, that is the parameters of the API call.
            String query = "";
            for (int i = 0 ; i < paramsAndValues.length ; i+= 2) {
                query += paramsAndValues[i] + "=" + paramsAndValues[i+1];
                if (i < paramsAndValues.length -2) query += "&";
            }

            //Creates a URL connection.
            URL url;
            if (API_call == "GetAuthToken" && paramsAndValues.size() == 0) url = new URL(urli + API_call);
            else url = new URL(urli + API_call + "?" + authToken + query);
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
            
        return "Problem encountered"; //A problem has been encountered while either calling the API or the response its damaged in some way (strange if data checking...) => Some special precautions to take.
    }




    /**
     * This sign up to the server with the corresponding email and password.
     * @param email: The user's email address.
     * @param password: The user's password.
     */
    public void CreateUser(String email, String password) {
        jc.newJson(getResponse("GetAuthToken", "email", email, "password", password));

        this.checkStatus();

        authToken = jc.getProperty("auth_token");

        //save user data (if succeeded):
        saveUser(email, password);
    }

    /**
     * This does authenticate the user and get a hash for it.
     * @param email: The user's email address.
     * @param password: The user's password.
     * @return Whether authentication succeeded or not.
     */
    public boolean AuthenticateUser(String email, String password) {
        jc.newJson(getResponse("GetAuthToken", "email", email, "password", password));

        boolean fine = this.checkStatus();
        if (fine) {
            authToken = jc.getProperty("auth_token");
            StatusService.StaticStatusService.loggedIn = true;
            return true;
        }
        else return false;
    }

    /**
     * Gets the description of the desired task (a task video that is).
     * @param taskId: The task's id of which description is to be retrieved.
     * @return String containing the uri of the task. TODO needs image too!
     */
    public String DescribeTask(String taskId) {
        jc.newJson(getResponse("DescribeTask", "task_id", taskId)); //JSON string containing the description of the task. (Contains: "task_id", "uri", "loaded")

        this.checkStatus();

        return jc.getProperty("uri");
    }

    /**
     * Gets the information necessary to start uploading a video to S3 and notices the back-end server about the uploading so that it would be possible.
     * @param taskId: All answers does link to a certain task -> taskId is the task's id of the task to be answered.
     * @return A string containing needed information for uploading a video to S3: the 'uri'' to upload in S3...That's all.
     */
    public String StartAnswerUpload(String taskId) {
        jc.newJson(getResponse("StartAnswerUpload", "task_id", taskId)); //A JSON string containing needed information for uploading a video to S3: "task_id" (useless?), the video's id to be: "answer_id", the "uri" to upload in S3.

        this.checkStatus();

        return jc.getProperty("uri");
    }

    /**
    * Notice the server that the video upload to S3 has been accomplished/failed.
    * @param answerId: The id of the answer that has been uploading.
    * @param uploadStatus: Whether it succeeded or not, "success" if succeeded.
    */
    public void EndAnswerUpload(String answerId, String uploadStatus) {
        jc.newJson(getResponse("EndAnswerUpload", "answer_id", answerId, "upload_status", uploadStatus));
        
        this.checkstatus();
    }


    /**
    * Gets the description of the desired answer (that is a user-uploaded video).
    * @param answerId: The answer's id of which the description is to be retrieved.
    * @return A HashMap<String, String> containing info about the answer, please do use as search key the parameter which value is to be retrived.
    * (Note: Useful ones: "uri". "enabled"; "task_id", "user_id")
    */
    public HashMap<String, String> DescribeAnswer(String answerId) {
        jc.newJson(getResponse("DescribeAnswer", "answer_id", answerId))
        
        this.checkstatus();
        
        return jc.getObject();
    }

}