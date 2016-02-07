package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.util.List;
import java.util.LinkedList; //???


private url = mobiilitiedekerho.duckdns.org; //The IP of the back-end server, it is needed to add parameters to it to be able to do the GETttooooo stufff. Hard-coded.
private userHash; //NO mihinpä tämä tallennetaan oikeasti.



/**
* This method returns the HttpResponse of the wanted API call.
*/
private HttpResponse getResponse(String API_call, String... params, String... values)) {
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
* This method parses the response dynamically, what it parses depends of which parameters are to be parsed.. The responseParams contains the params to be parsed (status is parsed by default).
*/
private LinkedList<String> parseResponse(HttpResponse resp, String... responseParams)) {
    Response response = resp.parseAs(Response.class);
    
    if (response.getStatus != "Succes") throws("Critical failure, nothing is to be done for now"); //No anyways jollain tavalla on huomioitava tämä.
    
    
    LinkedList<String> list = new LinkedList<String>();
    
    for (int i = 0 ; i < responseParams.size ; i++) {
	String getIt = "get" + responseParams[i];
	list += response.getIT; //Tiedän, tiedän, ei toimi, mutta onko mahdollista nyt tehdä tätä jollain tavalla? Pakko olla.
    }
    
    return list;
}


/**
* This does authenticate the user.
* @param email: The user's email adrres.
* @param passwd: The user's password.
*/
public void AuthenticateUser(String email, String passwd) {
    HttpResponse response = getResponse("AuthenticateUser", email, passwd);
    LinkedList<String> list = parseResponse(response, "User_hash");
    //TODO: Miten/mihin tallentaa se userHash?
}
