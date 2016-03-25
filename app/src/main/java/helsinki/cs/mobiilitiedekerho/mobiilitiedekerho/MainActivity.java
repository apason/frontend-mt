package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    AsyncTask hp = null;

    public class GotToken implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            StatusService.StaticStatusService.jc.newJson(response);

            if (StatusService.StaticStatusService.sc.checkStatus()) {
                StatusService.StaticStatusService.authToken = StatusService.StaticStatusService.jc.getProperty("auth_token");
                StatusService.StaticStatusService.fh.saveToken();
            }
            else {
                //TODO: Problem getting an anonymous token from the server => ???
            }
            
            start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new StatusService();
        StatusService.StaticStatusService.context = getApplicationContext(); //needed for saving files to internal memory.
        
        //Saves the screen resolution for being able to show correct sized images.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        StatusService.StaticStatusService.screenWidth = metrics.widthPixels;
        StatusService.StaticStatusService.screenHeight = metrics.heightPixels;

        //Either saved token will be used (user auto-login) or an 'anonymous' one is retrieved for use.
        if (StatusService.StaticStatusService.fh.CheckIfSavedToken()) {
            start();
        } else {
            String url = StatusService.StaticStatusService.sc.AnonymousSession();
            hp = new HTTPSRequester(new GotToken()).execute(url);

    }


    @Override
    public void onResume()
    {  // Refreshes screen when returned to the front page, after eg. loggin in or out
        super.onResume();
        start();
    }

    public void start() {
        setContentView(R.layout.main_activity);
        LoginFragment lf = new LoginFragment();
        UserVideosFragment uvf = new UserVideosFragment();
        InfoTextFragment uif = new InfoTextFragment();
        uif.setTitle("Käyttöehdot ja ohjeet tähän");
        NextPageFragment npf = new NextPageFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.login_button_fragment, lf);
        transaction.add(R.id.user_videos_button_fragment, uvf);
        transaction.add(R.id.info_button_fragment, uif);
        transaction.add(R.id.next_button_fragment, npf);
        transaction.commit();
    }

    public void startCategories() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    public void startUserActivity() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
}
