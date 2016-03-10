package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.media.Image;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class UserVideosFragment extends Fragment implements View.OnClickListener {

    private Dialog list = null;
/*
    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            authenticated(response);
        }
    }
*/

    String url;
    View view;
    String email;
    String password;
    AsyncTask hp = null;
    ImageButton userVideosButton;

    /*
    public void authenticated(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
        //ArrayList<HashMap<String, String>> tasks = StatusService.StaticStatusService.jc.getObjects();
        Log.i("urli", url);
        Log.i("markkeri", "");
        Log.i("status", StatusService.StaticStatusService.jc.getProperty("status"));
        if (StatusService.StaticStatusService.sc.checkStatus()) {
            Toast.makeText(LoginFragment.this.getActivity(), "Kirjautuminen onnistui!",
                Toast.LENGTH_LONG).show();
            StatusService.setLoggedIn(true);
            loginIconButton.setBackgroundResource(R.drawable.logout_icon);
            login.dismiss();
        } else {
            // If username or password is incorrect empty TextViews and notify user.
            emailTV.setText("");
            passwordTV.setText("");
            Toast.makeText(LoginFragment.this.getActivity(), "Sähköpostiosoite tai salasana väärin!",
                Toast.LENGTH_LONG).show();
        }
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_videos_button_fragment, null);
        // Add onClickListener to the login button
        userVideosButton =
            (ImageButton) view.findViewById(R.id.user_videos_button);
        userVideosButton.setOnClickListener(this);

        return view;
    }

    // When loginButton is pressed call method openLoginDialog
    @Override
    public void onClick(View v) {
        /*
        if(StatusService.getLoggedIn()) {
            StatusService.setLoggedIn(false);
            Toast.makeText(LoginFragment.this.getActivity(), "Olet nyt kirjautunut ulos Mobiilitiedekerhosta",
                Toast.LENGTH_LONG).show();
            loginIconButton.setBackgroundResource(R.drawable.login_icon);

        } else { openLoginDialog(); }

        */
        openLoginDialog();
    }

    public void openLoginDialog() {
        list = new Dialog(UserVideosFragment.this.getActivity());
        // Set GUI of login screen
        list.setContentView(R.layout.user_videos_fragment);
        list.setTitle("LISTAUS KÄYTTÄJÄN VIDEOISTA TÄHÄN");

/*
        Button registerButton =
            (Button) login.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailTV.getText().toString();
                password = passwordTV.getText().toString();
                url = StatusService.StaticStatusService.sc.AuthenticateUser(email, password);
                hp = new HTTPSRequester(new listener()).execute(url);

                Toast.makeText(LoginFragment.this.getActivity(), "Tunnuksesi on nyt rekisteröity ja voit kirjautua sillä sisään",
                    Toast.LENGTH_LONG).show();
                emailTV.setText("");
                passwordTV.setText("");
            }
        });
*/
        // On click of cancel button close the dialog
        Button closePopupButton =
            (Button) list.findViewById(R.id.cancel_button);
        closePopupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                list.dismiss();
            }

        });

        // Force the dialog to the right size
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(list.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        list.show();
        list.getWindow().setAttributes(lp);
    }
}
