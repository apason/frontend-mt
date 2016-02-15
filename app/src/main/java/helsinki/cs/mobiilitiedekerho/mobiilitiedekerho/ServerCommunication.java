package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.io.File;

/**
* A class for communicating with the back-end server via HTTP.
* Use the public methods for making API calls to the server.
*/
public class ServerCommunication extends IntentService {

    private String urli = "mobiilitiedekerho.duckdns.org"; //The IP of the back-end server, it is needed to add parameters to it to be able to comunivate with it. Hard-coded.
    private String userHash;
    
    private fi.helsinki.cs.mobiilitiedekerho.backend.JsonConverter jc = new fi.helsinki.cs.mobiilitiedekerho.backend.JsonConverter();

//     public IBinder onBind(Intend intend) {
// 	
//     }


    /**
    * Creates a new HttPService class and gets a new anonymious hash for use in API calls.
    */
    public ServerCommunication() {
	super("ServerCommunication");
	StartSession();
	//getAnonymiousHash(); //To be removed?
	CheckIfSavedUser();
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
	//En vieläkään tiedä mitä tänne laittaa!
    }
    
    
    //Notices the server so that a anonymus session would be linked to this client.
    private StartSession() {
	jc.newJson(getResponse("StartSession"));
	
	this.checkstatus();
	
	userHash = jc.getProperty("user_hash");
    }
    
    //If there is saved the data of a user, it does AuthenticateUser.
    private CheckIfSavedUser() {
	File path = context.getFilesDir(); //The data directory of the application.
	File file = new File(path, "user.txt");
	
	if (file.exists) {
	    BufferedReader br = new BufferedReader(new FileReader(file));
	    try {
		//Fort now:
		String email = br.readLine();
		String password = br.readLine();
		br.close();
		
		this.AuthenticateUser(email, password);
		//
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	
    }
    
    //Save the needed data into text file for future auto-login.
    private saveUser(String email, String password) {
	File path = context.getFilesDir(); //The data directory of the application.
	File file = new File(path, "user.txt");
	
	if (!file.exists) {
	    file.createNewFile();
	}
	
	FileOutputStream stream = new FileOutputStream(file);
	try {
	    stream.write((email + "\n" + password).getBytes());
	} finally {
	    stream.close();
	}
    }
    
    
//     private getAnonymiousHash() {
// 	//No one knows yet.
// 	//TODO: Hand-shake with server (via another class?) and then API call to get anonymious hash.
// 	
// 	
// 	jc.newJson(getResponse("getAnonymiousHash"));
// 	
// 	userHash = jc.getProperty("user_hash");
//     }
    
    
    private checkstatus() {
	String state = jc.getProperty("status");
	if (state != "succes") {
	    //Something. Check which error actually happened at the server.
	}
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
	    if (API_call == "getAnonymiousHash") url = new URL(urli + API_call);
	    else url = new URL(urli + API_call + "?" + userHash + query);
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
	//Something. Check which error actually happened at the server.
	return "Problem encountered"; //A problem has been encountered while either calling the API or the response its damaged in some way (strange if data checking...) => Some special precautions to take.
    }
    


    
    /**
    * This sign up to the server with the corresponding email and password.
    * @param email: The user's email adress.
    * @param password: The user's password.
    */
    public void CreateUser(String email, String password) {
	jc.newJson(getResponse("CreateUser", "email", email, "password", password));
	
	this.checkstatus();
	
	userHash = jc.getProperty("user_hash");
	
	//save user data (if succeeded):
	saveUser(email, password);
    }
    
    /**
    * This does authenticate the user and get a hash for it.
    * @param email: The user's email adress.
    * @param password: The user's password.
    */
    public void AuthenticateUser(String email, String password) {
	jc.newJson(getResponse("AuthenticateUser", "email", email, "password", password));
	
	this.checkstatus();
	
	userHash = jc.getProperty("user_hash");
    }

    /**
    * Gets the description of the desired task (a task video that is).
    * @param taskId: The task's id of which description is to be retrieved.
    * @return String containing the uri of the task.
    */
    public String DescribeTask(String taskId) {
	jc.newJson(getResponse("DescribeTask", "task_id", taskId)); //JSON string containing the description of the task. (Contains: "task_id", "uri", "loaded")
	
	this.checkstatus();
	
	return jc.getProperty("uri");
    }

    /**
    * Gets the information necessary to start uploading a video to S3 and notices the back-end server about the uploading so that it would be possible.
    * @param taskId: All answers does link to a certain task -> taskId is the task's id of the task to be answered.
    * @return A string containing needed information for uploading a video to S3: the 'uri'' to upload in S3...That's all.
    */
    public String StartAnswerUpload(String taskId) {
	jc.newJson(getResponse("StartAnswerUpload", "task_id", taskId)); //A JSON string containing needed information for uploading a video to S3: "task_id" (useless?), the video's id to be: "answer_id", the "uri" to upload in S3.
	
	this.checkstatus();
	
	return jc.getProperty("uri");
    }

    /**
    * Notice the server that the video upload to S3 has been accomplished/failed.
    * @param answerId: The id of the answer that has been uploading.
    * @param uploadStatus: Whether it succeeded or not,	success if succeeded.
    */
    public void EndAnswerUpload(String answerId, String uploadStatus) {
	jc.newJson(getResponse("EndAnswerUpload", "answer_id", answerId, "upload_status", uploadStatus));
	
	this.checkstatus();
    }


    /**
    * Gets the description of the desired answer (that is a user-uploaded video). EI 1-sprintissä! On nyt void.
    * @param answerId: The answer's id of which the description is to be retrieved.
    * @return Sitä ei kukaan tiedä mitä!
    */
    public void DescribeAnswer(String answerId) {
	jc.newJson(getResponse("DescribeAnswer", "answer_id", answerId));
	
	this.checkstatus();
    }

}