package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class UserInfoFragment extends Fragment implements View.OnClickListener {

    private Dialog info = null;
    ImageButton infoButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.info_button_fragment, null);
        // Add onClickListener to the login button
        infoButton =
            (ImageButton) view.findViewById(R.id.info_button);
        infoButton.setOnClickListener(this);

        return view;
    }

    // When loginButton is pressed call method openLoginDialog
    @Override
    public void onClick(View v) {
        openLoginDialog();
    }

    public void openLoginDialog() {
        info = new Dialog(UserInfoFragment.this.getActivity());
        // Set GUI of login screen
        info.setContentView(R.layout.user_info_fragment);
        info.setTitle("KÄYTTÖEHDOT JA OHJEET TÄHÄN");

        // On click of cancel button close the dialog
        Button closePopupButton =
            (Button) info.findViewById(R.id.cancel_button);
        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.dismiss();
            }

        });

        // Force the dialog to the right size
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(info.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        info.show();
        info.getWindow().setAttributes(lp);
    }
}
