package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.Service;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

/**
 * A class for making urls for communicating with the back-end server via HTTPS.
 * Use the public methods for creating API-call urls to the server.
 * Also has checkStatus() for checking if response is allright.
 * Please note: Method descriptions are the descriptions of what the HTTP call will do, NOT what this actual method does.
 */
public class ServerCommunication  {

    public class anonymous implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            StatusService.StaticStatusService.jc.newJson(response);

            //this.checkStatus();

            StatusService.StaticStatusService.authToken = StatusService.StaticStatusService.jc.getProperty("auth_token");
        }
    }

    AsyncTask hp = null;

    /**
     * Creates a new HttPService class and gets a new anonymous token for use in API calls.
     */
    public ServerCommunication() {
        //TODO: Task must be done before doing anything else in the app!
        String url = StartSession();
        hp = new HTTPSRequester(new anonymous()).execute(url);
        //CheckIfSavedUser(); //Important note: The protected 'global' variable loggedIn will be changed to true if there is a saved user.
    }


    /**
     * private
     * This method returns the url to be used to communicate with the server.
     * TODO: Problem handling.
     * @param API_call: That is the API call to be executed.
     * @param paramsAndValues: Parameter and value pair, odd ones are the parameters and even ones the values.
     * Note: It does add automatically the user's token except if API_call is getAnonymiousHash.
     * @return the url which can be used to communicate with the server.
     */
    private String getResponse(String API_call, String... paramsAndValues) {

            //Creates the query to be added to the URL, that is the parameters of the API call.
            String query = "";
            for (int i = 0 ; i < paramsAndValues.length ; i+= 2) {
                query += paramsAndValues[i] + "=" + paramsAndValues[i+1];
                if (i < paramsAndValues.length -2) query += "&";
            }

            //Creates the textual representation of the url.
            StringBuilder sb = new StringBuilder();
            if (API_call == "GetAuthToken" && paramsAndValues.length == 0) sb.append(StatusService.StaticStatusService.urli + API_call);
            else if (API_call == "GetAuthToken") sb.append(StatusService.StaticStatusService.urli + API_call + "?" + query);
            else sb.append(StatusService.StaticStatusService.urli + API_call + "?" + "auth_token=" + StatusService.StaticStatusService.authToken + "&" + query);
            
            return sb.toString();
    }

    
    
    /**
    * Check if the status in response is success, that is if everything went allright.
    * TODO: If not, then what?
    * @return true if success is the status in the response. 
    */
    public boolean checkStatus() {
        String state = StatusService.StaticStatusService.jc.getProperty("status");
        return state == "Success";
    }
    
    
    /**
     * Notices the server so that a anonymous token would be linked to this client.
     */
    public String StartSession() {
        return getResponse("GetAuthToken");
    }


    /**
     * This sign up to the server with the corresponding email and password.
     * @param email: The user's email address.
     * @param password: The user's password.
     */
    public String CreateUser(String email, String password) {
        return getResponse("GetAuthToken", "email", email, "password", password);
    }

    /**
     * This does authenticate the user and get a hash for it.
     * @param email: The user's email address.
     * @param password: The user's password.
     */
    public String AuthenticateUser(String email, String password) {
        return getResponse("GetAuthToken", "email", email, "password", password);
    }

    /**
     * Gets the description of the desired task (a task video that is).
     * @param taskId: The task's id of which description is to be retrieved.
     */
    public String DescribeTask(String taskId) {
        return getResponse("DescribeTask", "task_id", taskId);
    }

    /**
     * Gets the information necessary to start uploading a video to S3 and notices the back-end server about the uploading so that it would be possible.
     * @param taskId: All answers does link to a certain task -> taskId is the task's id of the task to be answered.
     */
    public String StartAnswerUpload(String taskId) {
        return getResponse("StartAnswerUpload", "task_id", taskId);

    }

    /**
     * Notice the server that the video upload to S3 has been accomplished/failed.
     * @param answerId: The id of the answer that has been uploading.
     * @param uploadStatus: Whether it succeeded or not, "success" if succeeded.
     */
    public String EndAnswerUpload(String answerId, String uploadStatus) {
        return getResponse("EndAnswerUpload", "answer_id", answerId, "upload_status", uploadStatus);
    }

    /**
     * Gets the description of the desired answer (that is a user-uploaded video).
     * @param answerId: The answer's id of which the description is to be retrieved.
     */
    public String DescribeAnswer(String answerId) {
        return getResponse("DescribeAnswer", "answer_id", answerId);
    }

    /**
     * Gets all the info of all answers related to the task.
     * @param taskId: The task's id of which answers are to be retrieved.
     */
    public String DescribeTaskAnswers(String taskId) {
       return getResponse("DescribeTaskAnswers", "task_id", taskId);

    }

    /**
     * Get all the info of the wanted category.
     * @param categoryId the id of the category which info is wanted to be retrieved.
     */
    public String DescribeCategory(String categoryId) {
        return getResponse("DescribeCategory", "category_id", categoryId);
    }

   /**
     * Get all the ids from all the categories. Note: This method is supposed to be used at Main Menu.
     */

    public String DescribeCategories() {
        return getResponse("DescribeCategories");
    }
    
    /**
    * Get all tasks that are part of to the category.
    * @param categoryId the id of the category which task belonging to it are wanted to be retrieved.
    */
    public String DescribeTasksByCategory(String categoryId) {
        return getResponse("DescribeTasksByCategory", "category_id", categoryId);

    }

}
