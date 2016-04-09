package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;

public class UserVideosFragment extends Fragment implements View.OnClickListener {

    private Dialog list = null;

    public class FetchUserVideos implements TaskCompleted {
        @Override
        public void taskCompleted(String response) { openUserVideoDialog(response); }
    }

    String url;
    View view;
    String email;
    String password;
    AsyncTask hp = null;
    ImageButton userVideosButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_videos_button_fragment, null);
        userVideosButton =
                (ImageButton) view.findViewById(R.id.user_videos_button);
        userVideosButton.setOnClickListener(this);
        return view;
    }

    // When loginButton is pressed call method openLoginDialog
    @Override
    public void onClick(View v) {
        String url = StatusService.StaticStatusService.sc.DescribeSubUserAnswers(StatusService.StaticStatusService.currentSubUserID);
        Log.i("urli", url);
        hp = new HTTPSRequester(new FetchUserVideos()).execute(url);
    }

    public void openUserVideoDialog(String response) {
        Log.i("viesti", StatusService.StaticStatusService.jc.getProperty("status"));
        if(StatusService.StaticStatusService.currentSubUserID == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Videolaatikko on tyhjä");
            alert.setMessage("Voidaksesi katsella omia vastauksiasi, sinun on kirjauduttava ja/tai luotava käyttäjätunnus");
            alert.setNegativeButton("Sulje", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }

        list = new Dialog(UserVideosFragment.this.getActivity());
        // Set GUI of login screen
        list.setContentView(R.layout.user_videos_fragment);
        list.setTitle("ALIKÄYTTÄJÄN VIDEOT :-)");

        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            ArrayList<HashMap<String, String>> uservideos = StatusService.StaticStatusService.jc.getObjects();
            Log.i("koko", String.valueOf(uservideos.size()));
            if (!uservideos.isEmpty()) {
                for (int i = 0; i < uservideos.size(); i++) {
                    String uservideo = uservideos.get(i).get("uri");
                    Log.i("uservideo", uservideo);
                }
            }

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
}
