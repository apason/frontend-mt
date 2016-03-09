package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    AsyncTask hp = null;

    public class GotToken implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            StatusService.StaticStatusService.jc.newJson(response);

            //this.checkStatus();

            StatusService.StaticStatusService.authToken = StatusService.StaticStatusService.jc.getProperty("auth_token");
            start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusService ss = new StatusService();
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
        setContentView(R.layout.main_activity);
        LoginFragment lf = new LoginFragment();
        NextPageFragment npf = new NextPageFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.login_button_fragment, lf);
        transaction.add(R.id.next_button_fragment, npf);
        transaction.commit();
    }

    public void startCategories() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }
}
