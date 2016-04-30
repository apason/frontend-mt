package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
* Main activity of the app.
* It does initialize the process.
* And also servers as the Main-menu.
* TODO: A separate class for the initialization?
*/
public class MainActivity extends AppCompatActivity {

    private AsyncTask hp = null;
    private boolean triedCommunicatingAlready = false;
    private ArrayList<HashMap<String, String>> subUsers;
    private boolean askedForSubUser = false;


    /**
     * A listener that checks if the client got the anonymous token right.
     * If it went wrong after trying again it does notify the user about a problem with communication with the server.
     */
    public class GotToken implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                StatusService.StaticStatusService.authToken = StatusService.StaticStatusService.jc.getProperty("auth_token");
                StatusService.StaticStatusService.fh.saveToken();
                start();
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
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked) {
                if (!StatusService.StaticStatusService.jc.getProperty("status").equals("authenticated")) {
                    String url = StatusService.StaticStatusService.sc.AnonymousSession();
                    hp = new HTTPSRequester(new GotToken()).execute(url);
                }
                else {
                    StatusService.setLoggedIn(true);
                    start();
                }
            }
            //else {String url = StatusService.StaticStatusService.sc.AnonymousSession(); hp = new HTTPSRequester(new GotToken()).execute(url);} //???
        }
    }

    /**
     * A listener that checks the response for DescribeSubUsers.
     * If it is successful, draw an alertdialog on screen which the user can use to select the desired sub-user.
     */
    public class GotSubUsers implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked) {
                subUsers = StatusService.StaticStatusService.jc.getObjects();
                if (!subUsers.isEmpty()) {
                    Log.i("subit", "saatiin");
                    //List<String> subList = new ArrayList<String>();
                    //for (int i = 0; i < subUsers.size(); i++) {
                    //    subList.add(subUsers.get(i).get("nick"));
                    //}
                    //CharSequence subs[] = subList.toArray(new CharSequence[subList.size()]);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Valitse käyttäjä");
                    StatusService.StaticStatusService.currentSubUserID = subUsers.get(0).get("id");
                    builder.setPositiveButton(subUsers.get(0).get("nick"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StatusService.setSubUser(subUsers.get(0).get("id"));
                            Log.i("Current sub = ", StatusService.StaticStatusService.currentSubUserID);
                        }
                    });
                    Log.i ("subilista", subUsers.toString());
                    if(subUsers.size() >= 2) {
                        builder.setNeutralButton(subUsers.get(1).get("nick"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StatusService.setSubUser(subUsers.get(1).get("id"));
                                Log.i("Current sub = ", StatusService.StaticStatusService.currentSubUserID);
                            }
                        });
                    }
                    if(subUsers.size() == 3) {
                        builder.setNegativeButton(subUsers.get(2).get("nick"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StatusService.setSubUser(subUsers.get(2).get("id"));
                                Log.i("Current sub = ", StatusService.StaticStatusService.currentSubUserID);
                            }
                        });
                    }

                    builder.show();
                }
            }
        }
    }

    // A method that draws the required objects on screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new ConnectionCheck().conMgr(this);


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

    // This is executed every time the user accessess this activity
    @Override
    protected void onStart() {
        super.onStart();
        Log.i ("Kysytty subeista", Boolean.toString(askedForSubUser));
        if(askedForSubUser == false) {
            String suburl = StatusService.StaticStatusService.sc.DescribeSubUsers();
            hp = new HTTPSRequester(new GotSubUsers()).execute(suburl);
            askedForSubUser = true;
            Log.i ("Kysytty subeista", Boolean.toString(askedForSubUser));
        }
    }

    // This is executed every time the user returns to this activity e.g. onBackPress
    @Override
    public void onResume() {  // Refreshes screen when returning to this page, after eg. logging in or out
        super.onResume();
        new ConnectionCheck().conMgr(this);
        drawScreen();
    }

    // This initiates the drawScreen method
    // Can be used also to do "extra-stuff" before drawing.
    public void start() {
        drawScreen();
    }

    /**
    * Draws the needed components to the screen.
    */
    public void drawScreen() {
        setContentView(R.layout.main_activity);
        LoginFragment lf = new LoginFragment();
        UserVideosFragment uvf = new UserVideosFragment();
        InfoTextFragment itf = new InfoTextFragment();
        
        //taskId is -1 so InfoTextFragment displays user info
        Bundle bundle = new Bundle();
        bundle.putString("task", "-1");
        itf.setArguments(bundle);

        NextPageFragment npf = new NextPageFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.login_button_fragment, lf);
        transaction.add(R.id.user_videos_button_fragment, uvf);
        transaction.add(R.id.info_button_fragment, itf);
        transaction.add(R.id.next_button_fragment, npf);
        transaction.commit();
    }


    /**
    * Starts the CategoriesActivity.
    */
    public void startCategories() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    /**
    * Starts the UserActivity.
    */
    public void startUserActivity() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }


    /**
    * A method for taking care to notify VideoScreen the right type of the media to be played.
    * And starts it.
    * Note: it uses StatusService's variables for this.
    * @param uri Uri to be streamed or well readed by the WebView.
    * @param mediaTypee the media-type of the stuff, "video" or "image".
    */
    public void playback(String uri, String mediaTypee) {
        Intent intent = new Intent(this, VideoScreen.class);
        StatusService.StaticStatusService.url = uri;
        StatusService.StaticStatusService.mediaTypee = mediaTypee;
        startActivity(intent);
    }
}