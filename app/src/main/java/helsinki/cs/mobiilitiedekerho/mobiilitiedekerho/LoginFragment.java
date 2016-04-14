package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private Dialog login = null;

    String url;
    View view;
    ImageButton loginIconButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_button_fragment, null);
        // Add onClickListener to the login button
        loginIconButton =
                (ImageButton) view.findViewById(R.id.login_icon_button);
        if(StatusService.loggedIn()) {

            loginIconButton.setBackgroundResource(R.drawable.sub_user_icon_placeholder);
        }else {
            loginIconButton.setBackgroundResource(R.drawable.login_icon);
        }
        loginIconButton.setOnClickListener(this);

        return view;
    }

    // When loginButton is pressed call method openLoginDialog
    @Override
    public void onClick(View v) {
        if(StatusService.loggedIn()) { ((MainActivity) getActivity()).startUserActivity();
        }
        else {
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginDialog.class);
            startActivity(intent);
        }
    }
}