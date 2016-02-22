package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
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

public class LoginFragment extends Fragment implements View.OnClickListener {

    View view;
    TextView emailTV;
    TextView passwordTV;
    String email;
    String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_button_fragment, null);
        // Add onClickListener to the login button
        ImageButton loginIconButton =
                (ImageButton) view.findViewById(R.id.login_icon_button);
        loginIconButton.setOnClickListener(this);

        return view;
    }

    // When loginButton is pressed call method openLoginDialog
    @Override
    public void onClick(View v) {
        openLoginDialog();
    }

    private void openLoginDialog() {
        final Dialog login = new Dialog(LoginFragment.this.getActivity());
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
                // TODO On click of login button get content from TextViews and check if they match a valid user using ServerCommunication.
                    email = emailTV.getText().toString();
                    password = passwordTV.getText().toString();

                ServerCommunication servcom = new ServerCommunication();
                if (servcom.AuthenticateUser(email, password)) {
                    Toast.makeText(LoginFragment.this.getActivity(), "Kirjautuminen onnistui!",
                            Toast.LENGTH_LONG).show();
                    login.dismiss();
                } else {
                    // TODO If username or password is incorrect empty TextViews and notify user.
                    emailTV.setText("");
                    passwordTV.setText("");
                    Toast.makeText(LoginFragment.this.getActivity(), "Sähköpostiosoite tai salasana väärin!",
                            Toast.LENGTH_LONG).show();
            }
        }});

        // On click of cancel button close the dialog
        Button closePopupButton =
                (Button) login.findViewById(R.id.cancel_button);
        closePopupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login.dismiss();
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