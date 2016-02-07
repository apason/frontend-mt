package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho; //Pitää säädellä kenties.

//Onko kaikki?
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

private url = mobiilitiedekerho.duckdns.org; //The IP of the back-end server, it is needed to add parameters to it to be able to do the GETttooooo stufff. Hard-coded.
private userHash; //MITEN TUO NYT ANNTETAAN TÄLLE LUOKALLE?

//# If needed to tell the server what character encoding to use, then uncomment the below and implement what needed in the code.
//private charset = java.nio.charset.StandardCharsets.UTF_8.name();




/**
 * Priva tavaraa. :D
 * Aika selvää tavaraa, käyetään kaikissa HTTP GET metodeissa. Palauttaa JSONin input streamina.
 * Params on parametrien nimet ja values niitä vastaavat arvot.
 * Input stream on nimensä mukaan juuri sitä.
 */
private InputStream getResponse(String API_call, String... params, String... values) {
    String query = "";
    for (int i = 0 ; i < params.size ; i++) {
	query += params[i] + "=" + values[i];
	if (i < params.size -1) query += "&";
    }

    URLConnection connection = new URL(url + API_call + "?" + userHash  + query).openConnection());
    return connection.getInputStream();
}

/**
* JSONObject jassoni = new JSONObject(response); jos haluaa parsata sen objektina. ^
*/




//Heitin vaann tuon JSONArrayn palautuksen noihin.

public JSONArray AuthenticateUser(String... params, String... values) {
    InputStream response = getResponse("AuthenticateUser", params, values);
}


public JSONArray DescribeTask(String... params, String... values) {
    InputStream response = getResponse("DescribeTask", params, values);
}


public JSONArray DescribeAnswer(String... params, String... values) {
    InputStream response = getResponse("DescribeAnswer", params, values);
}


public JSONArray StartAnswerUpload(String... params, String... values) {
    InputStream response = getResponse("StartAnswerUpload", params, values);
}


public JSONArray EndAnswerUpload(String... params, String... values) {
    InputStream response = getResponse("EndAnswerUpload", params, values);
}




//Seuraavien sprinntien:

public JSONArray DescribeTaskAnswers(String... params, String... values) {
    InputStream response = getResponse("DescribeTaskAnswers", params, values);
}


public JSONArray DescribeTaskRecommendedAnswers(String... params, String... values) {
    InputStream response = getResponse("DescribeTaskRecommendedAnswers", params, values);
}








/**
* Juu tämä on luonnos siitä jos käyttäis sitä Google-http-clienttia.
* En jakasanut ajatella oikein, on sinne päin rakenneeltaan. Roskasti rakenneltu mutta tarpeeksi antamaan tarvittavan kuvan toteutuksesta.
*/
private EI_HAJUAKAAN josKäyttäisiSitäGoogleJuttua(String API_call, String... params, String... values)) {
    try {
	HttpClient client = new DefaultHttpClient(); //???
	
	String query = "";
	for (int i = 0 ; i < params.size ; i++) {
	    query += params[i] + "=" + values[i];
	    if (i < params.size -1) query += "&";
	}

	HttpGet request = new HttpGet(url + API_call + "?" + userHash  + query).openConnection());
	HttpResponse response = client.execute(request);
	
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    //TODO: Sitten ei kun https://developers.google.com/api-client-library/java/google-http-java-client/json#example
    //Sittem koska servu palauttaa mitä kysyttiin voi joku luokka hoitaa sen, että mitä datalla tehdään, kun siitä näkee mitä kysytiinkiin.
    
}