package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.IntentService;
import android.content.Intent;



/**
* Class for communicating with the server, uses other classes for retrieving data.
* A wrap class designed for easing the use of the "communication-classes" in the android aplication from the UI.
*/
public class ServerCommunication extends IntentService {

    private HttpService hp = new HttpSevice();
    private JsonConverter jc = new JsonConverter();
    

    
    public ServerCommunication() {
	super("ServerCommunication");
	getAnonymiousHash();
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
	//En vieläkään tiedä mitä tänne laittaa!
    }
    
//     public IBinder onBind(Intend intend) {
// 	
//     }
    
    
    
    private getAnonymiousHash() {
	String hash = Null;
	
	//No one knows yet.
	//Hand-shake with server (via another class?) and then API call to get anonymious hash.
	
	hp.setHash(jc.getProperty(hash);
    }
    
    private checkstatus() {
	String state = jc.getProperty("status");
	if (state != "succes") {
	    //Something. Check which error actually happened at the server.
	}
    }
    
    
    /**
    * This does authenticate the user and get a hash for it.
    * The returned hash must be used on all other API calls.
    * @param email: The user's email adress.
    * @param password: The user's password.
    */
    public void AuthenticateUser(String email, String password) {
	String response = hp.AuthenticateUser(String email, String password);
	if (response = "problem encountered") {
	    //Something. Either couldn't acces the server or response cannot be read.
	}
	
	jc.newJson(response);
	
	this.checkstatus();
	
	hp.setHash(jc.getProperty("user_hash"));
    }

    /**
    * Gets the description of the desired task (a task video that is).
    * @param taskId: The task's id of which description is to be retrieved.
    * @return String containing the uri of the task.
    */
    public String DescribeTask(String taskId) {
	String response =  hp.DescribeTask(String taskId);
	if (response = "problem encountered") {
	    //Something. Either couldn't acces the server or response cannot be read.
	}
	
	jc.newJson(response);
	
	this.checkstatus();
	
	return jc.getProperty("uri");
    }

    /**
    * Gets the information necessary to start uploading a video to S3 and notices the back-end server about the uploading so that it would be possible.
    * @param taskId: All answers does link to a certain task -> taskId is the task's id of the task to be answered.
    * @return A string containing needed information for uploading a video to S3: the 'uri'' to upload in S3...That's all.
    */
    public String StartAnswerUpload(String taskId) {
	String response = hp.StartAnswerUpload(String taskId);
	if (response = "problem encountered") {
	    //Something. Either couldn't acces the server or response cannot be read.
	}
	
	jc.newJson(response);
	
	this.checkstatus();
	
	return jc.getProperty("uri");
    }

    /**
    * Notice the server that the video upload to S3 has been accomplished/failed.
    * @param answerId: The id of the answer that has been uploading.
    * @param uploadStatus: Whether it succeeded or not,	success if succeeded.
    */
    public void EndAnswerUpload(String answerId, String uploadStatus) {
	String response = hp.EndAnswerUpload(String answerId, String uploadStatus);
	if (response = "problem encountered") {
	    //Something. Either couldn't acces the server or response cannot be read.
	}
	
	jc.newJson(response);
	
	this.checkstatus();
    }
    
}
