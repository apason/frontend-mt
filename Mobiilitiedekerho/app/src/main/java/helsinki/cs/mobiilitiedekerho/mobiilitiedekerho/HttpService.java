package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.gson.*; //pitää säätää

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class HttpService extends IntentService {

    private String urli = "mobiilitiedekerho.duckdns.org"; //The IP of the back-end server, it is needed to add parameters to it to be able to do the GETttooooo stufff. Hard-coded.
    private String userHash; //For now it is saved inside this class. TODO: Save persistently, etc.


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
    * This method returns the JSON as String of the wanted API call.
    * @param API_call: That is the API call to be executed.
    * @param paramsAndValues: Parameter and value pair, odd ones are the parameters and even ones the values.
    * Note: It does add automatically the user's hash except when the API call in question is the "AuthenticateUser".
    * @return a HashMap (returned as abstraction Map) containing the response from the server. Keys are parameters' names and values are well the parameters values.
    * (Note value field as Object so that also numbers can be passed from the server directly.)
    */
    private Map <String, Object> getResponse(String API_call, String... paramsAndValues) {
	
	HttpURLConnection urlConnection;
	try {
	    //Creates the query to be added to the URL, that is the parameters of the API call.
	    String query = "";
	    for (int i = 0 ; i < params.length ; i+= 2) {
		query += paramsAndValues[i] + "=" + paramsAndValues[i+1];
		if (i < params.length -2) query += "&";
	    }
	    
	    //Creates a URL connection, always has the user's hash with it except when the call is the authentication call.
	    URL url;
	    if (API_call == "AuthenticateUser") url = new URL(urli + API_call + "?" + query);
	    else url = new URL(urli + API_call + "?" + userHash + query);
	    urlConnection = (HttpURLConnection) url.openConnection();
	    
	    //Creates a string (for GSON to be parsed) from the connection's inputStream.
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
		sb.append(line+"\n");
            }
            br.close();
            
            //Creates the response Map and returns it to the caller.
            Map <String, Object> response = new HashMap <String, Object>();
            response = (Map <String, Object>) gson.fromJson(sb.toString(), response.getClass());
            return response;
                
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    urlConnection.disconnect();
	}
	//Miten huomioida status virheilmoitus: //if (response.getStatus != "Success") throws("Critical failure, nothing is to be done for now"); //No anyways jollain tavalla on huomioitava tämä. Ja se mitä se oikeati palautti.
    }
    


    /**
    * This does authenticate the user and get a hash for it.
    * This call is special since it does not have the user's hash with it. Has coded inside the retrieving of the response because of this. Toki vois sen toteuttaa if chekkauksena tossa metodissa...
    * @param email: The user's email adress.
    * @param password: The user's password.
    */
    public void AuthenticateUser(String email, String password) {
	Map <String, Object> response = getResponse("AuthenticateUser", "email", email, "password", password);
	userHash = (String) response.get("user_hash");
    }

    /** NYT tämä palauttaa vaan URIn Stringina, no 1-rpintille OK.
    * Gets the description of the desired task.
    * @param taskId: The task's id of which description is to be retrieved.
    * @return The task-video URI as string.
    */
    public String DescribeTask(String taskId) {
	Map <String, Object>  response = getResponse("DescribeTask", "task_id", taskId); //Contains: "task_id", "uri", "loaded"
	return (String) response.get("uri");
    }

    /**
    * Gets the information necessary to start uploading a video to S3 and notices the back-end server about the uploading so that it would be possible.
    * @param taskId: All answers does link to a certain task -> taskId is the task's id of the task to be answered.
    * @return HashMap in which elements (key names are the following, as string, values as objects): "task_id" (useless?), the video's id to be: "answer_id", the "uri" to upload in S3.
    */
    public Map <String, Object> StartAnswerUpload(String taskId) {
	Map <String, Object>  response = getResponse("StartAnswerUpload", "task_id", taskId); //Contains: "task_id", "answer_id", "uri"
	return response; //TODO: Better as a simple array.
    }

    /**
    * Notice the server that the video upload to S3 has been accomplished/failed.
    * @param answerId: The id of the answer that has been uploading.
    * @param uploadStatus: Whether it succeeded or not,	success if succeeded.
    */
    public void EndAnswerUpload(String answerId, String uploadStatus) {
	getResponse("EndAnswerUpload", "answer_id", answerId, "upload_status", uploadStatus);
    }


    /**
    * Gets the description of the desired answer (that is a user-uploaded video). EI 1-sprintissä! On nyt void.
    * @param answerId: The answer's id of which the description is to be retrieved.
    */
    public void DescribeAnswer(String answerId) {
	Map <String, Object>  response = getResponse("DescribeAnswer", "answer_id", answerId);
    }

}