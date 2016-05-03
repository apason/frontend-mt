package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Activity responsible for the user info screen.
 */
public class UserActivity extends AppCompatActivity {


    private AsyncTask hp;
    private boolean triedCommunicatingAlready = false;
    private String eula;
    Button subUser1;
    Button subUser2;
    Button subUser3;
    private ArrayList<HashMap<String, String>> subUsers;

    /**
    * A listener that checks if it returned response contains a valid token for the user.
    */
    public class GotToken implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                StatusService.StaticStatusService.authToken = StatusService.StaticStatusService.jc.getProperty("auth_token");
                afterLogout();
            }
            //Kopioin vaan mainista.
            else {
                if (triedCommunicatingAlready) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(UserActivity.this);
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
    * A listener that checks if it returned response contains a valid EULA.
    * If it is valid it sets it to the local variable eula otherwise it sets it to "Ongelma käyttöehtojen lataamisessa."
    */
    public class EULAListener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            Log.i("voi eula", response);
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                eula = StatusService.StaticStatusService.jc.getProperty("eula");
            }
            else eula = "Ongelma käyttöehtojen lataamisessa."; //Jotta ei crassha null viitteeseen.
        }
    }

    /**
    * A listener that checks if saving the privacy level worked out and notifies the user of the result.
    */
    public class PrivacyListener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            Log.i("subia", response);
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                Toast.makeText(UserActivity.this, "Muutokset käyttöoikeuksiin tallennettu.",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(UserActivity.this, "Yhteyttä palvelimeen ei saatu. Tekemiäsi muutoksia ei tallennettu.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // Draw content of user_activity.xml to the screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusService.StaticStatusService.context = getApplicationContext(); //needed for saving files to internal memory.

        StatusService.StaticStatusService.cc.conMgr();

        setContentView(R.layout.user_activity);

        String url = StatusService.StaticStatusService.sc.GetEULA();
        hp = new HTTPSRequester(new UserActivity.EULAListener()).execute(url);

        // The radiobuttons responsible for changing the privacy level
        RadioButton a = (RadioButton) findViewById(R.id.onlyme);
        RadioButton b = (RadioButton) findViewById(R.id.registered);
        RadioButton c = (RadioButton) findViewById(R.id.anyone);

        // Check which usage rights the user has determined for his/her videos and set the corresponding RadioButton checked.
        switch(StatusService.StaticStatusService.fh.getUsageRights()) {
            case 1:
                a.setChecked(true);
                break;
            case 2:
                b.setChecked(true);
                break;
            case 3:
                c.setChecked(true);
                break;
        }
        // The buttons responsible for changing the sub-user
        subUser1 = (Button) findViewById(R.id.subUser1);

        subUser2 = (Button) findViewById(R.id.subUser2);

        subUser3 = (Button) findViewById(R.id.subUser3);


        // Add OnClickListener to the logout button
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusService.setLoggedIn(false);
                Toast.makeText(getApplication(), "Olet nyt kirjautunut ulos Mobiilitiedekerhosta", Toast.LENGTH_LONG).show();

                //Removes token from SharedPreferences.
                StatusService.StaticStatusService.context.getSharedPreferences("mobiilitiedekerho", Context.MODE_PRIVATE).edit().clear().commit();

                String url = StatusService.StaticStatusService.sc.AnonymousSession();
                hp = new HTTPSRequester(new GotToken()).execute(url);
            }
        });

        Button userAgreementButton = (Button) findViewById(R.id.user_agreement_button);
        userAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserAgreement();
            }

        });

        Button subUserButton = (Button) findViewById(R.id.addsubuserbutton);
        subUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SubUserActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
    * Shows the user agreement
    */
    public void showUserAgreement() {
        AlertDialog.Builder alert = new AlertDialog.Builder(UserActivity.this);
        alert.setTitle("Mobiilitiedekerhon käyttöehdot");
        Log.i("eula", eula);
        alert.setMessage(eula);

        final TextView input = new TextView(UserActivity.this);
        alert.setView(input);

        alert.setNegativeButton("Sulje", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    /**
    * After the user has looged out call this, it pass the crrent activity to MainActivity.
    */
    public void afterLogout() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

    /**
    * Method for handling changes in usage rights the user makes.
    */
    public void radioButtonOnClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.onlyme:
                if (checked)
                    setPrivacyLevel(1);
                    break;
            case R.id.registered:
                if (checked)
                    setPrivacyLevel(2);
                    break;
            case R.id.anyone:
                if (checked)
                    setPrivacyLevel(3);
                    break;
        }
    }

    /**
    * Sets the privacy level for the current user
    * @param i the userights code.
    */
    public void setPrivacyLevel(int i) {
    
        //Saves the privacy level for later use.
        StatusService.setUsageRights(i);
        StatusService.StaticStatusService.fh.saveUsageRights(i);
    
        String url = StatusService.StaticStatusService.sc.SetPrivacyLevel(Integer.toString(i));
        Log.i("vika", url);
        hp = new HTTPSRequester(new UserActivity.PrivacyListener()).execute(url);
    }

    /**
    * A listener that checks the response for DescribeSubUsers.
    * If it is successful, draw an alertdialog on screen which the user can use to select the desired sub-user.
    * TODO: Slice this to pieces!
    */
    public class GotSubUsers implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked) {
                subUsers = StatusService.StaticStatusService.jc.getObjects();
                if (!subUsers.isEmpty()) {
                    Log.i("subit", "saatiin");
                    subUser1.setText(subUsers.get(0).get("nick"));
                    if(subUsers.get(0).get("id").equals(StatusService.getSubUser())) {
                        subUser1.setEnabled(false);
                    }
                    subUser1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StatusService.setSubUser(subUsers.get(0).get("id"));
                            subUser1.setEnabled(false);
                            subUser2.setEnabled(true);
                            subUser3.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Käyttäjä valittu.", Toast.LENGTH_LONG).show();
                        }
                    });

                    Log.i ("subilista", subUsers.toString());

                    if(subUsers.size() >= 2) {
                        subUser2.setText(subUsers.get(1).get("nick"));
                        if(subUsers.get(1).get("id").equals(StatusService.getSubUser())) {
                            subUser2.setEnabled(false);
                        }
                        subUser2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StatusService.setSubUser(subUsers.get(1).get("id"));
                                subUser1.setEnabled(true);
                                subUser2.setEnabled(false);
                                subUser3.setEnabled(true);
                                Toast.makeText(getApplicationContext(), "Käyttäjä valittu.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        subUser2.setText("EI LUOTU");
                        subUser2.setEnabled(false);
                        subUser3.setText("EI LUOTU");
                        subUser3.setEnabled(false);
                    }
                    if(subUsers.size() == 3) {
                        subUser3.setText(subUsers.get(2).get("nick"));
                        if(subUsers.get(2).get("id").equals(StatusService.getSubUser())) {
                            subUser3.setEnabled(false);
                        }
                        subUser3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StatusService.setSubUser(subUsers.get(2).get("id"));
                                subUser1.setEnabled(true);
                                subUser2.setEnabled(true);
                                subUser3.setEnabled(false);
                                Toast.makeText(getApplicationContext(), "Käyttäjä valittu.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        subUser3.setText("EI LUOTU");
                        subUser3.setEnabled(false);
                    }

                }
            }
        }
    }

    // Do this when the user first starts this activity.
    @Override
    protected void onStart() {
        super.onStart();
        String suburl = StatusService.StaticStatusService.sc.DescribeSubUsers();
        hp = new HTTPSRequester(new GotSubUsers()).execute(suburl);
    }
}