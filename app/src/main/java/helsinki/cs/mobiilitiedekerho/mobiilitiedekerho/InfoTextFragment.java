package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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

/**
 *  A class responsible for showing the info-texts
 */
public class InfoTextFragment extends Fragment implements View.OnClickListener {

    private Dialog info = null;
    private AsyncTask hp;
    private String taskId;
    private ImageButton infoButton;
    private View view;
    private TextView textView;
    private String title;
    private String instructionsText;

    
    // A method that checks if we got the info-text
    public class InfoTextLoaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            openInfoDialog(response);
        }
    }

    // A method that checks if we got the instructions for this app
    public class instructionsListener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                instructionsText = StatusService.StaticStatusService.jc.getProperty("instructions");
            }
            else instructionsText = "Ongelma ohjeiden lataamisessa.";
        }
    }

    // A method that draws the needed objects onscreen
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.info_button_fragment, null);
        // Add onClickListener to the login button
        infoButton =
            (ImageButton) view.findViewById(R.id.info_button);
        infoButton.setOnClickListener(this);
        taskId = getArguments().getString("task");
        
        String url = StatusService.StaticStatusService.sc.GetInstructions();
        hp = new HTTPSRequester(new instructionsListener()).execute(url);
        
        return view;
    }

    // A method for setting the title for an info-box
    public void setTitle(String s) {
        title = s;
    }

    // A listener for clicks that loads the info text for a given task
    @Override
    public void onClick(View v) {
        String url = StatusService.StaticStatusService.sc.DescribeTask(taskId);
        hp = new HTTPSRequester(new InfoTextLoaded()).execute(url);
    }

    // A method for opening a dialog that shows the info-text for the given task
    public void openInfoDialog(String response) {
        info = new Dialog(InfoTextFragment.this.getActivity());
        info.setContentView(R.layout.info_text_fragment);
        info.setTitle(title);
        textView = (TextView) info.findViewById(R.id.taskText);
        textView.setTextSize(20);

        // If we got the info-text, then show it
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            ArrayList<HashMap<String, String>> task = StatusService.StaticStatusService.jc.getObjects();
            String taskInfo = task.get(0).get("info");

            //Checks whether the task has info defined or not. If not it sets "Ei ole kuvausta tehtävälle." as the task's description.
            if (taskInfo == null) textView.setText("Tehtävälle ei ole kuvausta.");
            else textView.setText(Html.fromHtml(taskInfo));
        }
        //This is for user info - taskId should be set to -1 in MainActivity.java
        if (taskId.equals("-1")) {
            info.setTitle("Mobiilitiedekerhon Käyttöohjeet");
            textView.setText(instructionsText);
        }

        // On click of cancel button close the dialog
        Button closePopupButton =
            (Button) info.findViewById(R.id.cancel_button);
        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.dismiss();
            }});

        // Force the dialog to the right size
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(info.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        info.show();
        info.getWindow().setAttributes(lp);
    }

}
