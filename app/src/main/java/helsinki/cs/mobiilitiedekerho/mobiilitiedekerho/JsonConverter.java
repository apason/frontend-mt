package fi.helsinki.cs.mobiilitiedekerho.backend;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;


/**
* Json converter class which parses a JSON String and stores the retrieved data for later use.
* Use get*(...) for getting data.
*/
public class JsonConverter {

    private ArrayList<HashMap<String, String>> objects;
    private HashMap<String, String> properties;

    //this is for first level properties and object list names only!
    private String key;

    /**
    * Parses the wanted JSON string and stores the retrieved data for later use.
    * @param json: JSON String to be parsed.
    */
    public JsonConverter(String json) throws IOException {
	properties = new HashMap<String, String>();
	objects = new ArrayList<HashMap<String, String>>();
	JsonReader reader = new JsonReader(new StringReader(json));
	parseJson(reader);
    }

    private void parseJson(JsonReader reader) throws IOException
    {
	//json object allways starts with {
        reader.beginObject();

        while (reader.hasNext()) {

	    //there is at least one element so this is ok.
	    key = reader.nextName();

            JsonToken token = reader.peek();
	    if (token.equals(JsonToken.BEGIN_ARRAY))
                handleArray(reader);
	    else
		handleProperty(reader, token);
        }
 
    }

    private void handleArray(JsonReader reader) throws IOException {
	reader.beginArray();

	System.out.print("handleArray\n");

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

	System.out.print("handle array object\n");

	reader.beginObject();
	while(reader.hasNext()){

	    String key = reader.nextName();
	    JsonToken token = reader.peek();
	    
	    if(token.equals(JsonToken.NUMBER)){
		Integer value = reader.nextInt();
		objn.put(key, "" + value);
	    }
	    else if(token.equals(JsonToken.STRING))
		objn.put(key, reader.nextString());
	    else
		reader.skipValue();
		
	}
    }

    private void handleProperty(JsonReader reader, JsonToken token)
	throws IOException {

	token = reader.peek();

	if(token.equals(JsonToken.STRING)){
	    String s = reader.nextString();
	    System.out.print("\n" + key + " : " + s + "\n");
	    properties.put(key, s);
	}
        else if (token.equals(JsonToken.NUMBER))
	    properties.put(key, "" + reader.nextInt());
	//'else-part' isn't' actually needed
	else
	    reader.skipValue();
    }


    /**
     * Returns the value of the parameter/field status.
     * Example usage: jc.getProperty("status")
     * @return returns the value of the field (that is the parameter responded from the server).
     */
    public String getProperty(String key){
	return properties.get(key);
    }
    /**
    * 
    * @return Arraylist of HashMaps containing the data of all parameter-value -pairs of an object in the json. 
    */
    public ArrayList<HashMap<String, String>> getObjects(){
	return objects;
    }
    
    //returns first object in the list
    // D: what is this for actually, this is of no use!!!??
    public HashMap<String, String> getObject(){
	return objects.get(0);
    }
}


