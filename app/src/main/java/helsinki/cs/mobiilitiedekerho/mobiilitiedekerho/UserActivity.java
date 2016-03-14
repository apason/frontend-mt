package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Activity responsible for the user info screen
 */
public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    // Draw content of user_activity.xml to the screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        // Add OnClickListener to the logout button
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this);
    }

    // On click of the logout_button log out the user and notify him about it. Return to MainActivity.
    @Override
    public void onClick(View v) {
        StatusService.setLoggedIn(false);
        Toast.makeText(this, "Olet nyt kirjautunut ulos Mobiilitiedekerhosta", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
