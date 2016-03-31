package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity responsible for the user info screen
 */
public class UserActivity extends AppCompatActivity {

    // Draw content of user_activity.xml to the screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        // Add OnClickListener to the logout button
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusService.setLoggedIn(false);
                Toast.makeText(getApplication(), "Olet nyt kirjautunut ulos Mobiilitiedekerhosta", Toast.LENGTH_LONG).show();
                StatusService.StaticStatusService.authToken = null;
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
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
}

