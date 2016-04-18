package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

/**
 * Activity responsible for the user info screen
 */
public class UserActivity extends AppCompatActivity {


    private AsyncTask hp;
    private boolean triedCommunicatingAlready = false;
    private String eula;


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
        setContentView(R.layout.user_activity);

        String url = StatusService.StaticStatusService.sc.GetEULA();
        hp = new HTTPSRequester(new UserActivity.EULAListener()).execute(url);

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

        ImageButton subUser1 = (ImageButton) findViewById(R.id.subUser1);
        subUser1.setBackgroundResource(R.drawable.sub_user_icon_placeholder);

        ImageButton subUser2 = (ImageButton) findViewById(R.id.subUser2);
        subUser2.setBackgroundResource(R.drawable.sub_user_icon_placeholder);

        ImageButton subUser3 = (ImageButton) findViewById(R.id.subUser3);
        subUser3.setBackgroundResource(R.drawable.sub_user_icon_placeholder);

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

    public void setPrivacyLevel(int i) {
    
        //Saves the privacy level for later use.
        StatusService.setUsageRights(i);
        StatusService.StaticStatusService.fh.saveUsageRights(i);
    
        String url = StatusService.StaticStatusService.sc.SetPrivacyLevel(Integer.toString(i));
        Log.i("vika", url);
        hp = new HTTPSRequester(new UserActivity.PrivacyListener()).execute(url);
    }
}