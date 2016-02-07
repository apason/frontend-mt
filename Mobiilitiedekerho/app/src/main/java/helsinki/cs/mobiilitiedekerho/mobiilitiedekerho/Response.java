package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import com.google.api.client.json.GenericJson;



public static class Response extends GenericJson {
    
    @key
    private String status;
    
    public String getstatus() {
	return status;
    }
    
    
    @key
    private String task_id;
    
    public String gettask_id() {
	return task_id;
    }
    
    
    @key
    private String answer_id;
    
    public String getanswer_id() {
	return answer_id;
    }

    
    @key
    private String uri;
    
    public String geturi() {
	return uri;
    }
    
    
    @key
    private String loaded;
    
    public String getloaded() {
	return loaded;
    }
    
    
    @key
    private String user_hash;
    
    public String getuser_hash() {
	return uri;
    }
}