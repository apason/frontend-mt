package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *  A class responsible for the login dialog
 */
public class LoginDialog extends AppCompatActivity {

    private Dialog login = null;
    private View view;
    private TextView emailTV;
    private TextView passwordTV;
    private String url;
    private String email;
    private String password;
    private ArrayList<HashMap<String, String>> subUsers;
    
    private AsyncTask hp = null;

    private boolean triedCommunicatingAlready = false;

    

    /**
    * A listener that checks if the login was successful by calling authenticated-method and that is.
    */
    public class loginListener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            authenticated(response);
        }
    }

    // Checks if the registration was succesful
    public class registerListener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            registered(response);
        }
    }


    /**
    * Checks whether the loging's response is correct and acts accordingly.
    * If authentication response "cannot be computed" empty TextViews and notify user.
    * @param response the reponse from the server fo the API-call AuthenticateUser.
    */
    public void authenticated(String response) {
        Log.i("login response", response);
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            // Checks whether loging in was successful or not and acts accordingly.
            authenticationCheck();
        }
        else {
            // If authentication response "cannot be computed" empty TextViews and notify user.
            emailTV.setText("");
            passwordTV.setText("");
            Toast.makeText(this, "Ongelma varmentamisessa, kokeile uudestaan.",
            Toast.LENGTH_LONG).show();
        }
    }
    
    /**
    * Checks whether loging in was successful or not and acts accordingly.
    * It Shows the user if the logging worked out or if soemthing was wrong it says that either the email or password is wrong.
    */
    private void authenticationCheck() {
        //ArrayList<HashMap<String, String>> tasks = StatusService.StaticStatusService.jc.getObjects();
        //Log.i("status", StatusService.StaticStatusService.jc.getProperty("status"));
        if (StatusService.StaticStatusService.sc.checkStatus()) {
            StatusService.StaticStatusService.authToken = StatusService.StaticStatusService.jc.getProperty("auth_token");
            Log.i("token", "uusi" + StatusService.StaticStatusService.authToken);
            StatusService.StaticStatusService.fh.saveToken();
            Toast.makeText(this, "Kirjautuminen onnistui.",
                Toast.LENGTH_LONG).show();
            StatusService.setLoggedIn(true);
            String suburl = StatusService.StaticStatusService.sc.DescribeSubUsers();
            hp = new HTTPSRequester(new GotSubUsers()).execute(suburl);
            this.finish();
        } else {
            // If username or password is incorrect empty TextViews and notify user.
            emailTV.setText("");
            passwordTV.setText("");
            Toast.makeText(this, "Sähköpostiosoite tai salasana väärin.",
                Toast.LENGTH_LONG).show();
        }
    }


    /**
    * Checks whether registration was successful and acts accordingly.
    * @param response the reponse from the server fo the API-call CreateUser.
    */
    public void registered(String response) {
        Log.i("register response", response);
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            if (StatusService.StaticStatusService.sc.checkStatus()) {
                Toast.makeText(this, "Rekisteröityminen onnistui. Voit nyt kirjautua luomillasi tunnuksilla sisään.",
                        Toast.LENGTH_LONG).show();
            } else {
                // If there is a problem with registration, notify the user.
                emailTV.setText("");
                passwordTV.setText("");
                Toast.makeText(this, "Ongelma rekisteröitymisessä. Yritä uudelleen.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // Responsible for drawing the required components on screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openLoginDialog();
    }


    /**
    * A method that opens the login dialog
    */
    public void openLoginDialog() {
        login = new Dialog(this);
        // Set GUI of login screen
        login.setContentView(R.layout.login_fragment);
        login.setTitle("Kirjaudu mobiilitiedekerhoon");

        // Add TextView elements to the dialog
        emailTV = (EditText) login.findViewById(R.id.username);
        passwordTV = (EditText) login.findViewById(R.id.password);
        
        // Add buttons to the dialog and set onclicklisteners.
        createLoginButton();
        createRegisterButton();
        createClosePopupButton();

        // Force the dialog to the right size
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(login.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        login.show();
        login.getWindow().setAttributes(lp);
    }


	private void createLoginButton() {
        Button loginButton =
                (Button) login.findViewById(R.id.login_button);
            loginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // On click of login button get content from TextViews and check if they match a valid user using ServerCommunication.
                    email = emailTV.getText().toString();
                    password = passwordTV.getText().toString();
                    url = StatusService.StaticStatusService.sc.AuthenticateUser(email, password);
                    Log.i("auturli", url);
                    hp = new HTTPSRequester(new loginListener()).execute(url);
                }});
	}
    
	private void createRegisterButton() {
        Button registerButton =
                (Button) login.findViewById(R.id.register_button);
            registerButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = emailTV.getText().toString();
                    password = passwordTV.getText().toString();
                    url = StatusService.StaticStatusService.sc.CreateUser(email, password);
                    hp = new HTTPSRequester(new registerListener()).execute(url);
                    emailTV.setText("");
                    passwordTV.setText("");
                }
            });
	}
	
	// On click of cancel button close the dialog
    private void createClosePopupButton() {
        Button closePopupButton = (Button) login.findViewById(R.id.cancel_button);
        closePopupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }});
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(StatusService.StaticStatusService.dialogContext);
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
    
}