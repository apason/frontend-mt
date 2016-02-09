package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import com.amazonaws.http.HttpRequest;
import com.amazonaws.http.HttpResponse;

import java.io.IOException;

// TESTILUOKKA KOPIOITU TÄÄLTÄ:
// https://developers.google.com/api-client-library/java/google-http-java-client/unit-testing

public class HttpTest {
    /*
    HttpService transport = new MockHttpTransport();
    HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
    HttpResponse response = request.execute();
    */

    /*
    HttpService transport = new MockHttpTransport() {
        @Override
        public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
            return new MockLowLevelHttpRequest() {
                @Override
                public LowLevelHttpResponse execute() throws IOException {
                    MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                    response.addHeader("custom_header", "value");
                    response.setStatusCode(404);
                    response.setContentType(Json.MEDIA_TYPE);
                    response.setContent("{\"error\":\"not found\"}");
                    return response;
                }
            };
        }
    };
    HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
    HttpResponse response = request.execute();
    */
}
