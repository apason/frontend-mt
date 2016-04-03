package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private AsyncTask hp = null;
    private boolean triedCommunicatingAlready = false;


    public class GotToken implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            StatusService.StaticStatusService.jc.newJson(response);
            Log.i("täällä", "täällä");
            if (StatusService.StaticStatusService.sc.checkStatus()) {
                StatusService.StaticStatusService.authToken = StatusService.StaticStatusService.jc.getProperty("auth_token");
                getBuckets();
            }
            else {
                if (triedCommunicatingAlready) {
                    //Show the user an alert dialog notifying there is a problem with connecting to the server.
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Tietoliikennevirhe");
                    alert.setMessage("Valitettavasti Mobiilitiedekerho ei saa tällä hetkellä yhteyttä palvelimeen. Yritä myöhemmin uudestaan.");
                    alert.setNegativeButton("Sulje", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
                else {
                    //Try again just in case.
                    triedCommunicatingAlready = true;
                    String url = StatusService.StaticStatusService.sc.AnonymousSession();
                    hp = new HTTPSRequester(new GotToken()).execute(url);
                }
            }
        }
    }

    /**
     * A listener that checks the response about token integrity.
     * If it is "no good", then it gets an anonymous one.
     */
    public class CheckToken implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            StatusService.StaticStatusService.jc.newJson(response);
            Log.i("täällä2", "täällä");
            if (!StatusService.StaticStatusService.sc.checkStatus()) {
                String url = StatusService.StaticStatusService.sc.AnonymousSession();
                hp = new HTTPSRequester(new GotToken()).execute(url);
            }
            else {
                StatusService.setLoggedIn(true);
                getBuckets();
            }
        }
    }
    
    
    /**
     * A listener that checks if receiving bucket names worked out and takes them to memory for use.
     */
    public class GotBuckets implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            Log.i("bucketit", response);
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                StatusService.StaticStatusService.s3Location = StatusService.StaticStatusService.jc.getProperty("s3Location")
                StatusService.StaticStatusService.taskBucket = StatusService.StaticStatusService.jc.getProperty("taskBucket")
                StatusService.StaticStatusService.answerBucket = StatusService.StaticStatusService.jc.getProperty("answerBucket")
                StatusService.StaticStatusService.graphicsBucket = StatusService.StaticStatusService.jc.getProperty("graphicsBucket")
            }
            //else just uses the hard-coded ones.
            start();
        }
    }
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checks if there is an internet connection available. Note:  isConnectedOrConnecting () is true if connection is being established, but hasn't already.
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean internetConnectionAvailable = conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
        if (!internetConnectionAvailable) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Tietoliikennevirhe");
            alert.setMessage("Laite ei ole yhteydessä internetiin. Suurinta osaa Mobiilitiedekerhon toiminnoista ei voi käyttää ilman toimivaa verkkoyhteyttä");
            alert.setNegativeButton("Sulje", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }

        new StatusService();
        StatusService.StaticStatusService.context = getApplicationContext(); //needed for saving files to internal memory.

        
        //Saves the screen resolution for being able to show correct sized images.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        StatusService.StaticStatusService.screenWidth = metrics.widthPixels;
        StatusService.StaticStatusService.screenHeight = metrics.heightPixels;

        
        //Either saved token will be used (user auto-login) or an anonymous-token is retrieved for use. Token validity is also checked.
        if (StatusService.StaticStatusService.fh.CheckIfSavedToken()) {
            String url = StatusService.StaticStatusService.sc.CheckTokenIntegrity(StatusService.StaticStatusService.authToken);
            hp = new HTTPSRequester(new CheckToken()).execute(url);
        } else {
            String url = StatusService.StaticStatusService.sc.AnonymousSession();
            hp = new HTTPSRequester(new GotToken()).execute(url);
        }
        
    }


    @Override
    public void onResume() {  // Refreshes screen when returned to the front page, after eg. logging in or out
        super.onResume();
        drawScreen();
    }

    public void getBuckets() {
        String url = StatusService.StaticStatusService.sc.GetBuckets();
        hp = new HTTPSRequester(new GotBuckets()).execute(url);
    }
    
    public void start() {
        // TODO: Maybe some kind of data-preloading.
        drawScreen();
    }

    public void drawScreen() {
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
