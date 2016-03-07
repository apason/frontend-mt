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
            Log.i("autentikointi", StatusService.StaticStatusService.authToken);
            start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusService ss = new StatusService();
        /*
        if (StatusService.StaticStatusService.fh.CheckIfSavedUser()) {
            String urli = StatusService.StaticStatusService.sc.GetNewTokenIfOld(tokeni, ID);
            hp = new HTTPSRequester(new GotToken()).execute(url);
        } else {
        */
            String urli = StatusService.StaticStatusService.sc.AnonymousSession();
            hp = new HTTPSRequester(new GotToken()).execute(urli);

    }

    public void start() {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }


}
