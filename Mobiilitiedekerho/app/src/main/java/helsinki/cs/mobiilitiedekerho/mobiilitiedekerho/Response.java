package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import com.google.api.client.json.gson.GsonFactory;



public static class Response extends GsonFactory { //Tai JsonFactory
    
    @key
    private String status;
    
    public String getStatus() {
	return status;
    }
    
    @key
    private String someID;
    
    public String getSomeID() {
	return someID;
    }

}