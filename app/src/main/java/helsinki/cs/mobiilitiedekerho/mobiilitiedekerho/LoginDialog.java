package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.media.Image;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginDialog extends AppCompatActivity {

    private Dialog login = null;

    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            authenticated(response);
        }
    }

    String url;
    View view;
    TextView emailTV;
    TextView passwordTV;
    String email;
    String password;
    AsyncTask hp = null;
    ImageButton loginIconButton;

    public void authenticated(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
        //ArrayList<HashMap<String, String>> tasks = StatusService.StaticStatusService.jc.getObjects();
        //Log.i("status", StatusService.StaticStatusService.jc.getProperty("status"));
        if (StatusService.StaticStatusService.sc.checkStatus()) {
            Toast toast = new Toast(this);
            Toast.makeText(this, "Kirjautuminen onnistui!",
                Toast.LENGTH_LONG).show();
            StatusService.setLoggedIn(true);
            this.finish();
            //loginIconButton.setBackgroundResource(R.drawable.logout_icon);
        } else {
            // If username or password is incorrect empty TextViews and notify user.
            emailTV.setText("");
            passwordTV.setText("");
            Toast.makeText(this, "Sähköpostiosoite tai salasana väärin!",
                Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_fragment);
        // Add onClickListener to the login button
        openLoginDialog();
    }

    public void openLoginDialog() {
        login = new Dialog(this);
        // Set GUI of login screen
        login.setContentView(R.layout.login_fragment);
        login.setTitle("Kirjaudu mobiilitiedekerhoon");

        // Add TextView elements to the dialog
        emailTV = (EditText) login.findViewById(R.id.username);
        passwordTV = (EditText) login.findViewById(R.id.password);

        // Add buttons to the dialog and set onclicklisteners
        Button loginButton =
            (Button) login.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // On click of login button get content from TextViews and check if they match a valid user using ServerCommunication.
                email = emailTV.getText().toString();
                password = passwordTV.getText().toString();
                url = StatusService.StaticStatusService.sc.AuthenticateUser(email, password);
                hp = new HTTPSRequester(new listener()).execute(url);
            }});

        Button registerButton =
            (Button) login.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailTV.getText().toString();
                password = passwordTV.getText().toString();
                url = StatusService.StaticStatusService.sc.CreateUser(email, password);
                hp = new HTTPSRequester(new listener()).execute(url);

                Toast.makeText(getApplicationContext(), "Tunnuksesi on nyt rekisteröity ja voit kirjautua sillä sisään",
                    Toast.LENGTH_LONG).show();
                emailTV.setText("");
                passwordTV.setText("");
            }
        });

        // On click of cancel button close the dialog
        Button closePopupButton =
            (Button) login.findViewById(R.id.cancel_button);
        closePopupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //login.dismiss();
            }

        });

        // Force the dialog to the right size
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(login.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        login.show();
        login.getWindow().setAttributes(lp);
    }

}