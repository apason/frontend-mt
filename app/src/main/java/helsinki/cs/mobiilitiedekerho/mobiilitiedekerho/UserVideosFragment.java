package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class UserVideosFragment extends Fragment implements View.OnClickListener {

    private Dialog list = null;

    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            listUserVideos(response);
        }
    }

    String url;
    View view;
    String email;
    String password;
    AsyncTask hp = null;
    ImageButton userVideosButton;


    public void listUserVideos(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_videos_button_fragment, null);

        /* TODO: DescribeSubUserAnswers(StatusService.StaticStatusService.sc.currentSubUserID)
        String url = StatusService.StaticStatusService.sc.DescribeSubUserAnswers(StatusService.StaticStatusService.sc.currentSubUserID);
        hp = new HTTPSRequester(new listener()).execute(url);
        */
        return view;
    }

    // When loginButton is pressed call method openLoginDialog
    @Override
    public void onClick(View v) {
        openLoginDialog();
    }

    public void openLoginDialog() {
        list = new Dialog(UserVideosFragment.this.getActivity());
        // Set GUI of login screen
        list.setContentView(R.layout.user_videos_fragment);
        list.setTitle("LISTAUS KÄYTTÄJÄN VIDEOISTA TÄHÄN");

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
