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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoTextFragment extends Fragment implements View.OnClickListener {

    private Dialog info = null;
    private AsyncTask hp;
    ImageButton infoButton;
    View view;
    TextView textView;
    String title;

    public class InfoTextLoaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            openLoginDialog(response);
        }
    }

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

    public void setTitle(String s) {
        title = s;
    }

    // When loginButton is pressed call method openLoginDialog
    @Override
    public void onClick(View v) {
        if (!getArguments().getString("task").equals("0")) {
            String url = StatusService.StaticStatusService.sc.DescribeTask(getArguments().getString("task"));
            hp = new HTTPSRequester(new InfoTextLoaded()).execute(url);
        }
    }

    public void openLoginDialog(String response) {
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            String taskInfo = StatusService.StaticStatusService.jc.getProperty("info");
            info = new Dialog(InfoTextFragment.this.getActivity());
            // Set GUI of login screen
            info.setContentView(R.layout.info_text_fragment);
            info.setTitle(title);
            textView = (TextView) info.findViewById(R.id.taskText);
            textView.setTextSize(20);
            
            //Checks whether the task has info defined or not. If not it sets "Ei ole kuvausta tehtävälle." as the task's description.
            if (taskInfo == null) {
                textView.setText("Ei ole kuvausta tehtävälle.");
            }
            textView.setText(taskInfo);
        }

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
