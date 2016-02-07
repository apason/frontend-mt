package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.util.List;
import java.util.LinkedList; //???


public class HttpService extends IntentService {

    private String url = mobiilitiedekerho.duckdns.org; //The IP of the back-end server, it is needed to add parameters to it to be able to do the GETttooooo stufff. Hard-coded.
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
    
    
    
    /**
    * This method returns the HttpResponse of the wanted API call.
    * @param paramsAndValues: Parameter and value pair, odd ones are the parameters and even ones the values.
    */
    private HttpResponse getResponse(String API_call, String... paramsAndValues)) {
	try {
    
	    HttpClient client = new DefaultHttpClient();
	
	    String query = "";
	    for (int i = 0 ; i < params.size ; i++) {
		query += params[i] + "=" + values[i];
		if (i < params.size -1) query += "&";
	    }

	    HttpGet request = new HttpGet(url + API_call + "?" + userHash  + query).openConnection());
	    return client.execute(request);
	
	} catch (ClientProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    
    }


    /**
    * This method parses the response dynamically, what it parses depends of which parameters are to be parsed.
    * @param responseParams: contains the params to be parsed (status is parsed by default).
    */
    private LinkedList<String> parseResponse(HttpResponse resp, String... responseParams)) {
	Response response = resp.parseAs(Response.class);
	
	if (response.getStatus != "Succes") throws("Critical failure, nothing is to be done for now"); //No anyways jollain tavalla on huomioitava tämä. Ja se mitä se oikeati palautti.
	
	
	LinkedList<String> list = new LinkedList<String>();
	
	for (int i = 0 ; i < responseParams.size ; i++) {
	    list += response.get(responseParams[i]);
	}
    
	return list;
    }


    /**
    * This does authenticate the user and get a hash for it.
    * This call is special since it does not have the user's hash with it. Has coded inside the retrieving of the reponse because of this. Toki vois sen toteuttaa if chekkauksena tossa metodissa...
    * @param email: The user's email adrres.
    * @param passwd: The user's password.
    */
    public void AuthenticateUser(String email, String passwd) {
	HttpResponse response = new DefaultHttpClient().execute(new HttpGet(url + API_call + "?" + email + "&" + psswd).openConnection()));
	LinkedList<String> list = parseResponse(response, "user_hash");
	userHash = list.getFirst();
    }

    /** NYT tämä palauttaa vaan URI, no 1-rpintille OK.
    * Gets the description of the desired task.
    * @param taskId: The task's id of which description is to be retrieved.
    */
    public String DescribeTask(String taskId) {
	HttpResponse response = getResponse("DescribeTask", "task_id", taskId);
	LinkedList<String> list = parseResponse(response, "task_id", "uri", "loaded");
	
	return list.element(1);
    }

    /**
    * Starts uploading a video to S3 relating to a task.
    * @param taskId: All answers does link to a certain task -> taskId is the task's id of the task to be answered.
    * @return linked list in which first element is the task_id (useless?), the second the video's id, and the third is the uri to upload in S3.
    */
    public LinkedList<String> StartAnswerUpload(String taskId) {
	HttpResponse response = getResponse("StartAnswerUpload", "task_id", taskId);
	LinkedList<String> list = parseResponse(response, 	"task_id", "answe_id", "uri");
	return list;
    }

    /**
    * Notice the server that the video upload to S3 has been accomplished/failed.
    * @param answerId: The id of the answer that has been uploading.
    * @param uploadStatus: Whether it succeeded or not,	succes if succeeded.
    */
    public void EndAnswerUpload(String taskId, String uploadStatus) {
	HttpResponse response = getResponse("EndAnswerUpload", "answer_id", answerId, "upload_status", uploadStatus);
	LinkedList<String> list = parseResponse(response);
    }


    /**
    * Gets the description of the desired task. EI 1-sprintissä! On nyt void.
    * @param answerId: The answer's id of which description is to be retrieved.
    */
    public void DescribeAnswer(String answerId) {
	HttpResponse response = getResponse("DescribeAnswer", "answer_id", taskId);
	LinkedList<String> list = parseResponse(response, "answer_id", "jne", "jne");
    }

}