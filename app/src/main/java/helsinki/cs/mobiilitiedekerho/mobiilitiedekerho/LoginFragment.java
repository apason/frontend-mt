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

public class LoginFragment extends Fragment implements View.OnClickListener {

    View view;
    private PopupWindow loginPopup;

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

        // Add buttons to the popup and set onclicklisteners
        Button loginButton =
                (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(login_listener);

        Button closePopupButton =
                (Button) view.findViewById(R.id.cancel_button);
        closePopupButton.setOnClickListener(close_popup_listener);

    }

    private OnClickListener close_popup_listener = new OnClickListener() {
        public void onClick(View v) {
            loginPopup.dismiss();
        }
    };

    private OnClickListener login_listener = new OnClickListener() {
        public void onClick(View v) {
            // Login code here
        }
    };
}