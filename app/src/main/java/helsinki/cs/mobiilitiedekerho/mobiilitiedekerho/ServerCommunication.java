package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


/**
 * A class for making urls for communicating with the back-end server via HTTPS.
 * All classes returns String contained the formed url corresponding to the API call query.
 * Use the public methods for creating API-call urls to the server.
 * Also has checkStatus() for checking if response is allright.
 * Please note: Method descriptions are the descriptions of what the HTTP call will do, NOT what this actual method does.
 */
public class ServerCommunication {

    /**
     * private
     * This method returns the url to be used to communicate with the server.
     * TODO: Problem handling.
     *
     * @param API_call:        That is the API call to be executed.
     * @param paramsAndValues: Parameter and value pair, odd ones are the parameters and even ones the values.
     *                         Note: It does add automatically the user's token except if API_call is getAnonymiousHash.
     * @return the url which can be used to communicate with the server.
     */
    private String getResponse(String API_call, String... paramsAndValues) {

        //Creates the query to be added to the URL, that is the parameters of the API call.
        String query = "";
        for (int i = 0; i < paramsAndValues.length; i += 2) {
            query += paramsAndValues[i] + "=" + paramsAndValues[i + 1];
            if (i < paramsAndValues.length - 2) query += "&";
        }
        //Creates the textual representation of the url.
        StringBuilder sb = new StringBuilder();
        if (API_call == "GetAuthToken" && paramsAndValues.length == 0)
            sb.append(StatusService.StaticStatusService.urli + API_call);
        else if (API_call == "GetAuthToken")
            sb.append(StatusService.StaticStatusService.urli + API_call + "?" + query);
        else
            sb.append(StatusService.StaticStatusService.urli + API_call + "?" + "auth_token=" + StatusService.StaticStatusService.authToken + "&" + query);
        return sb.toString();
    }


    /**
     * Check if the status in response is success, that is if everything went alright.
     * TODO: If not, then what?
     *
     * @return true if success is the status in the response.
     */
    public boolean checkStatus() {
        String state = StatusService.StaticStatusService.jc.getProperty("status");
        return state.equals("Success");
    }

    

    /**
     * Notices the server so that a anonymous token would be linked to this client.
     */
    public String AnonymousSession() {
        return getResponse("GetAuthToken");
    }

    /**
     * This sign up to the server with the corresponding email and password.
     *
     * @param email:    The user's email address.
     * @param password: The user's password.
     */
    public String CreateUser(String email, String password) {
        return getResponse("CreateUser", "user_email", email, "user_password", password);
    }

    /**
     * This does authenticate the user and get a hash for it.
     *
     * @param email:    The user's email address.
     * @param password: The user's password.
     */
    public String AuthenticateUser(String email, String password) {
        return getResponse("GetAuthToken", "email", email, "password", password);
    }
        
    /**
     * Check if the token is a valid one that is it hasn't expired.
     * @param token the auth_token to check.
     */
    public String CheckTokenIntegrity(String token) {
        return getResponse("CheckTokenIntegrity", "auth_token", token);
    }
    
    
    /**
     * Get all the info of all the categories. Note: This method is supposed to be used at the Category Menu.
     */
    public String DescribeCategories() {
        return getResponse("DescribeCategories");
    }
    
    /**
     * Get all the info of the wanted category.
     *
     * @param categoryId the id of the category which info is wanted to be retrieved.
     */
    public String DescribeCategory(String categoryId) {
        return getResponse("DescribeCategory", "category_id", categoryId);
    }

    /**
     * Get all tasks that are part of to the category.
     *
     * @param categoryId the id of the category which task belonging to it are wanted to be retrieved.
     */
    public String DescribeCategoryTasks(String categoryId) {
        return getResponse("DescribeCategoryTasks", "category_id", categoryId);
    }
    
    /**
     * Gets the description of the desired task (a task video that is).
     *
     * @param taskId: The task's id of which description is to be retrieved.
     */
    public String DescribeTask(String taskId) {
        return getResponse("DescribeTask", "task_id", taskId);
        //return getResponse("DescribeTask", "task_id", taskId);
    }
    
    /**
     * Gets all the info of all answers related to the task.
     *
     * @param taskId: The task's id of which answers are to be retrieved.
     */
    public String DescribeTaskAnswers(String taskId) {
        return getResponse("DescribeTaskAnswers", "task_id", taskId);

    }
    
    /**
     * Gets the description of the desired answer (that is a user-uploaded video).
     *
     * @param answerId: The answer's id of which the description is to be retrieved.
     */
    public String DescribeAnswer(String answerId) {
        return getResponse("DescribeAnswer", "answer_id", answerId);
    }

    /**
     * Gets the information necessary to start uploading a video to S3 and notices the back-end server about the uploading so that it would be possible.
     *
     * @param taskId:    All answers does link to a certain task -> taskId is the task's id of the task to be answered.
     * @param subUserID: The subUser's id which answer is to be uploaded.
     */
    public String StartAnswerUpload(String taskId, String subUserID, String fileType) {
        return getResponse("StartAnswerUpload", "task_id", taskId, "subuser_id", subUserID, "file_type", fileType);
    }

    /**
     * Notice the server that the video upload to S3 has been accomplished/failed.
     *
     * @param answerId:     The id of the answer that has been uploading.
     * @param uploadStatus: Whether it succeeded or not, "success" if succeeded.
     */
    public String EndAnswerUpload(String answerId, String uploadStatus) {
        return getResponse("EndAnswerUpload", "answer_id", answerId, "upload_status", uploadStatus);
    }

    
    /**
     * Creates a new sub user for the current user.
     *
     * @param nick the nick of the subUser to be created.
     */
    public String CreateSubUser(String nick) {
        return getResponse("CreateSubUser", "subuser_nick", nick);
    }

    /**
     * Deletes a sub-user pointing to the current user.
     *
     * @param subUserId the id of the subuser to be deleted.
     */
    public String DeleteSubUser(String subUserId) {
        return getResponse("DeleteSubUser", "subuser_id", subUserId);
    }

    /**
     * Gets the info of all sub-users of this current user.
     */
    public String DescribeSubUsers() {
        return getResponse("DescribeSubUsers");
    }

    /**
     * Get all the answers info of the desired subuser.
     *
     * @param subUserId the id of the sub-user which answers belonging to it are wanted to be retrieved.
     */
    public String DescribeSubUserAnswers(String subUserId) {
        return getResponse("DescribeSubUserAnswers", "subuser_id", subUserId);
    }

    
//     /**
//      * Get all the likes of a task.
//      *
//      * @param taskId the id of the task of which likes are to be described.
//      */
//     public String DescribeTaskLikes(String taskId) {
//         return getResponse("DescribeTaskLikes", "task_id", taskId);
//     }

    /**
     * Get all the likes of a answer.
     *
     * @param answerId the id of the answer of which likes are to be described.
     */
    public String DescribeAnswerLikes(String answerId) {
        return getResponse("DescribeAnswerLikes", "answer_id", answerId);
    }

    /**
     * Get all the likes of the desired subuser.
     *
     * @param subUserId the id of the subuser which likes belonging to it are wanted to be retrieved.
     */
    public String DescribeSubUserLikes(String subUserId) {
        return getResponse("DescribeSubUserLikes", "subuser_id", subUserId);
    }

//     /**
//      * Like the pointed task
//      *
//      * @param taskId the id of the task to be liked.
//      */
//     public String LikeTask(String taskId) {
//         return getResponse("LikeTask", "task_id", taskId);
//     }

    /**
     * Like the pointed answer
     *
     * @param answerId the id of the answer to be liked.
     */
    public String LikeAnswer(String answerId) {
        return getResponse("LikeAnswer", "answer_id", answerId);
    }

    /**
     * Delete the pointed like.
     *
     * @param likeId the id of the like to be deleted.
     */
    public String DeleteLike(String likeId) {
        return getResponse("DeleteLike", "like_id", likeId);
    }
    
    /**
     * Gets all the addresses of the S3 buckets and its location.
     */
    public String GetBuckets() {
        return getResponse("GetBuckets");
    }
    
    /**
     * Notices the server to change user's privacy-level settings.
     * @param privacyLevel the privacyLevel to become the users privacyLevel.
     */
    public String SetPrivacyLevel(String privacyLevel) {
        return getResponse("SetPrivacyLevel", "privacy_level", privacyLevel);
    }
    
    /**
     * Notices the server to change user's pin or set it if there wasn't one already.
     * @param pin the pin to become the users pin. To stop children from messing up some things.
     */
    public String SetPin(String pin) {
        return getResponse("SetPrivacyLevel", "pin", pin);
    }
    
    /**
    * Gets the EULA as a String from the server.
    */
    public String GetEULA() {
        return getResponse("GetEULA");
    }

    /**
    * Gets the instructions as a String from the server.
    */
    public String GetInstructions() {
        return getResponse("GetInstructions");
    }
}

