package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.IntentService;
import android.content.Intent;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
* A class for communicating with the back-end server via HTTP.
* Use the public methods for making API calls to the server, all methods returns the response as JSON string.
* Note: JSON string contains status='succes' if succeeded OR "problem econtered" -string if there has happened a problem with the calling of the API.
*/
public class HttpService extends IntentService {

    private String urli = "mobiilitiedekerho.duckdns.org"; //The IP of the back-end server, it is needed to add parameters to it to be able to comunivate with it. Hard-coded.

//     public IBinder onBind(Intend intend) {
// 	
//     }


    public HttpService() {
	super("HttpService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
	//En tiedä mitä tänne laittaa!
    }
    
    
    //Pitkä luokka, voi paloitella tarvittaessa, mutta ei siitä kyllä ole hyötyä kun kaikki tämä on tehtävä putkeen + jaettu jo metodissa kokonaisuuksiin.
    /**
    * This method returns the JSON response as String of the wanted API call.
    * @param API_call: That is the API call to be executed.
    * @param paramsAndValues: Parameter and value pair, odd ones are the parameters and even ones the values.
    * Note: It does add automatically the user's hash except when the API call in question is the "AuthenticateUser".
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

	    //Creates a URL connection, always has the user's hash with it except when the call is the authentication call.
	    URL url;
	    if (API_call == "AuthenticateUser") url = new URL(urli + API_call + "?" + query);
	    //else url = new URL(urli + API_call + "?" + userHash + query);
	    //urlConnection = (HttpURLConnection) url.openConnection();

	    //Creates a string (for GSON to be parsed) from the connection's inputStream.
	    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null) {
		sb.append(line+"\n");
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
	//Miten huomioida status virheilmoitus: //if (response.getStatus != "Success") throws("Critical failure, nothing is to be done for now"); //No anyways jollain tavalla on huomioitava tämä. Ja se mitä se oikeati palautti.
    }
    


    /**
    * This does authenticate the user and get a hash for it.
    * The returned hash must be used on all other API calls.
    * @param email: The user's email adress.
    * @param password: The user's password.
    * @return the user's hash to be used inside a JSON string.
    */
    public String AuthenticateUser(String email, String password) {
	return getResponse("AuthenticateUser", "email", email, "password", password);
    }

    /**
    * Gets the description of the desired task (a task video that is).
    * @param taskId: The task's id of which description is to be retrieved.
    * @return JSON string containing the description of the task. (Contains: "task_id", "uri", "loaded")
    */
    public String DescribeTask(String taskId) {
	return getResponse("DescribeTask", "task_id", taskId);
    }

    /**
    * Gets the information necessary to start uploading a video to S3 and notices the back-end server about the uploading so that it would be possible.
    * @param taskId: All answers does link to a certain task -> taskId is the task's id of the task to be answered.
    * @return A JSON string containing needed information for uploading a video to S3: "task_id" (useless?), the video's id to be: "answer_id", the "uri" to upload in S3.
    */
    public String StartAnswerUpload(String taskId) {
	return getResponse("StartAnswerUpload", "task_id", taskId);
    }

    /**
    * Notice the server that the video upload to S3 has been accomplished/failed.
    * @param answerId: The id of the answer that has been uploading.
    * @param uploadStatus: Whether it succeeded or not,	success if succeeded.
    * @return  nothing of importance for what comes to other that succeeding info.
    */
    public String EndAnswerUpload(String answerId, String uploadStatus) {
	return getResponse("EndAnswerUpload", "answer_id", answerId, "upload_status", uploadStatus);
    }


    /**
    * Gets the description of the desired answer (that is a user-uploaded video). EI 1-sprintissä! On nyt void.
    * @param answerId: The answer's id of which the description is to be retrieved.
    * @return Sitä ei kukaan tiedä mitä!
    */
    public String DescribeAnswer(String answerId) {
	return getResponse("DescribeAnswer", "answer_id", answerId);
    }

}