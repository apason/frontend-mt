package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    AsyncTask hp = null;

    public class GotToken implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            StatusService.StaticStatusService.jc.newJson(response);

            //this.checkStatus();

            StatusService.StaticStatusService.authToken = StatusService.StaticStatusService.jc.getProperty("auth_token");
            StatusService.StaticStatusService.fh.saveToken();
            start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusService ss = new StatusService();
        
        //Either saved token will be used (user auto-login) or an 'anonymous' one is retrieved for use.
        if (StatusService.StaticStatusService.fh.CheckIfSavedToken()) {
            start();
        } else {
            String urli = StatusService.StaticStatusService.sc.AnonymousSession();
            hp = new HTTPSRequester(new GotToken()).execute(urli);
            
        /*
        if (StatusService.StaticStatusService.fh.CheckIfSavedToken()) {
            String urli = StatusService.StaticStatusService.sc.GetNewTokenIfOld(tokeni, ID);
            hp = new HTTPSRequester(new GotToken()).execute(url);
        } else {
            String urli = StatusService.StaticStatusService.sc.AnonymousSession();
            hp = new HTTPSRequester(new GotToken()).execute(urli);
        */
    }

    public void start() {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }


}
