package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity responsible for the user info screen
 */
public class UserActivity extends AppCompatActivity {

    AsyncTask hp;
    boolean triedCommunicatingAlready = false;

    public class GotToken implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            StatusService.StaticStatusService.jc.newJson(response);

            if (StatusService.StaticStatusService.sc.checkStatus()) {
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

    // Draw content of user_activity.xml to the screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        RadioButton a = (RadioButton) findViewById(R.id.onlyme);
        RadioButton b = (RadioButton) findViewById(R.id.registered);
        RadioButton c = (RadioButton) findViewById(R.id.anyone);

        switch(StatusService.getUsageRights()) {
            case 0:
                a.setChecked(true);
            case 1:
                b.setChecked(true);
            case 2:
                c.setChecked(true);
        }

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
                SubUserFragment suf = new SubUserFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.sub_user_fragment, suf);
                ft.commit();
            }
        });


    }

    public void showUserAgreement() {
        AlertDialog.Builder alert = new AlertDialog.Builder(UserActivity.this);
        alert.setTitle("Mobiilitiedekerhon käyttöehdot");
        alert.setMessage("Tähän voidaan kirjoittaa palvelun käyttöehdot");

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
                    StatusService.setUsageRights(0);
                    break;
            case R.id.registered:
                if (checked)
                    StatusService.setUsageRights(1);
                    break;
            case R.id.anyone:
                if (checked)
                    StatusService.setUsageRights(2);
                    break;
        }
    }
}

