package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashMap;

/**
* Json converter class which to parse JSON Strings. It stores the retrieved data for later use.
* Use newJson for assigning a new JSON from which to retrieve data, and use get*(...) for getting the info actually.
*/
public class JsonConverter {

    private ArrayList<HashMap<String, String>> objects;
    private HashMap<String, String> properties;

    //this is for first level properties and object list names only!
    private String key;

    /**
    * Creates new JsonConverter.
    * Call newJson for start with new JSON.
    */
    public JsonConverter() {}
    
    /**
    * Parses the wanted JSON string and stores the retrieved data for later use.
    * Note: Cleares old data if exists.
    * @param json: JSON String to be parsed.
    */
    public void newJson(String json) {
        try {
        properties = new HashMap<String, String>();
            objects = new ArrayList<HashMap<String, String>>();
            JsonReader reader = new JsonReader(new StringReader(json));
            parseJson(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    

    private void parseJson(JsonReader reader) throws IOException
    {
        //json object allways starts with "{"
        reader.beginObject();

        while (reader.hasNext()) {

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
            properties.put(key, s);
        }
        else if (token.equals(JsonToken.NUMBER))
            properties.put(key, "" + reader.nextInt());
        //'else-part' isn't' actually needed?
        else
            reader.skipValue();
    }


    /**
     * Returns the value of the parameter/field status.
     * Example usage: jc.getProperty("status")
     * @return returns the value of the field if exists(that is the parameter responded from the server),
     * otherwise null is returned
     */
    public String getProperty(String key){
        return properties.get(key);
    }
    /**
     * @return Arraylist of HashMaps containing all OBJECTS
     * (see the JsonResponse convention) of the json.
     * Note that return value can be an empty list.
     */
    public ArrayList<HashMap<String, String>> getObjects(){
        return objects;
    }
    
    /**
     * Returns first (or only) returned object.
     * Note that return value can be null.
     */
    public HashMap<String, String> getObject(){
        return objects.get(0);
    }
}


