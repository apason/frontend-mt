package fi.helsinki.cs.mobiilitiedekerho.frontend.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.StringReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonConverter {

    //private String status; //duplikaatti. mitä tehdään?
    
    private ArrayList<HashMap<String, String>> objects;
    private HashMap<String, String> properties;

    public JsonConverter(String json) {
	properties = new HashMap<String, String>();
	objects = new ArrayList<HashMap<String, String>>();
	JsonReader reader = new JsonReader(new StringReader(json));
	parseJson(reader);
    }

    private void parseJson(JsonReader reader) throws IOException
    {
        reader.beginObject();
        while (reader.hasNext()) {

            JsonToken token = reader.peek();
	    if (token.equals(JsonToken.BEGIN_ARRAY))
                handleArray(reader);
	    else
		handleProperty(reader, token);
        }
 
    }

    private void handleArray(JsonReader reader) throws IOException {
	reader.beginArray();
        while (true) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
                break;
	    }
	    else if (token.equals(JsonToken.BEGIN_OBJECT)) {
		HashMap<String, String> objn = new HashMap<String, String>();
                handleArrayObject(reader, objn);
		objects.add(objn);
	    }
	    else if (token.equals(JsonToken.END_OBJECT))
                reader.endObject();
        }
    }
	
    private void handleArrayObject(JsonReader reader, HashMap<String, String> objn)
	throws IOException {
	
	reader.beginObject();
	while(reader.hasNext()){
	    JsonToken token = reader.peek();
	    String key = reader.nextName();
	    
	    if(token.equals(JsonToken.NUMBER)){
		Integer value = reader.nextInt();
		objn.put(key, "" + value);
	    }
	    else if(token.equals(JsonToken.STRING))
		objn.put(key, reader.nextString());
		
	}
    }

    private void handleProperty(JsonReader reader, JsonToken token)
	throws IOException {

	String key = reader.nextName();

	if(token.equals(JsonToken.STRING))
	    properties.put(key, reader.nextString());
        else if (token.equals(JsonToken.NUMBER))
	    properties.put(key, "" + reader.nextInt());
    }


    public String getProperty(String key){
	return properties.get(key);
    }
    public Objects getObjects(){
	return objects;
    }
    public HashMap<String, String> getObject(){
	return objects.get(1);
    }
}
