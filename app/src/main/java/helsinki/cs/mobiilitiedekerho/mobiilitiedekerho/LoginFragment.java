package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment implements View.OnClickListener {

    View view;
    PopupWindow loginPopup;
    TextView emailTV;
    TextView passwordTV;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_button_fragment, container, false);
        // Add onClickListener to the login button
        ImageButton loginButton =
                (ImageButton) view.findViewById(R.id.login_icon_button);
        loginButton.setOnClickListener(this);

        return view;
    }

    // When loginButton is pressed call method openLoginPopup
    @Override
    public void onClick(View v) {
        openLoginPopup();
    }

    private void openLoginPopup() {
        // Inflate the popup_layout.xml
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.login_fragment, null);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.login_fragment);

        // Request loginPopup to be drawn to the center of the screen
        loginPopup = new PopupWindow(layout);
        loginPopup.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Add TextView elements to the screen.
        emailTV = (TextView) view.findViewById(R.id.username);
        passwordTV = (TextView) view.findViewById(R.id.password);

        // Add buttons to the popup and set onclicklisteners
        Button loginButton =
                (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(login_listener);

        Button closePopupButton =
                (Button) view.findViewById(R.id.cancel_button);
        closePopupButton.setOnClickListener(close_popup_listener);

    }

    private OnClickListener login_listener = new OnClickListener() {
        public void onClick(View v) {
            // On click of login button get content from TextViews and check if they match a valid
            // user using ServerCommunication.
            String email = emailTV.getText().toString();
            String password = passwordTV.getText().toString();
            ServerCommunication servcom = new ServerCommunication();
            if (servcom.AuthenticateUser(email, password) == true) {
                // TODO Sign in user
                Toast.makeText(LoginFragment.this.getActivity(), "Kirjautuminen onnistui!",
                        Toast.LENGTH_LONG).show();
                loginPopup.dismiss();
            }
            // If username or password is incorrect empty TextViews and notify user.
            emailTV.setText(""); passwordTV.setText("");
            Toast.makeText(LoginFragment.this.getActivity(), "Sähköpostiosoite tai salasana väärin!",
                    Toast.LENGTH_LONG).show();
        }
    };

    // On click of close button close the popup.
    private OnClickListener close_popup_listener = new OnClickListener() {
        public void onClick(View v) {
            loginPopup.dismiss();
        }
    };




}