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
            StatusService.StaticStatusService.fh.saveToken();
            start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new StatusService();

        //Either saved token will be used (user auto-login) or an 'anonymous' one is retrieved for use.
        if (StatusService.StaticStatusService.fh.CheckIfSavedToken()) {
            start();
        } else {
            String url = StatusService.StaticStatusService.sc.AnonymousSession();
            hp = new HTTPSRequester(new GotToken()).execute(url);
        }
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
