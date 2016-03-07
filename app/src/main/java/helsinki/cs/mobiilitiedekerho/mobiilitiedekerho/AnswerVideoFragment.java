package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;


public class AnswerVideoFragment extends Fragment implements View.OnClickListener {

    public class AnswerListener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            answers(response);
        }
    }

    public class URLListener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            answerURL(response);
        }
    }

    View view;
    VideoView videoView;
    AsyncTask hp;
    String answerURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.answer_video_fragment, container, false);

        //task_id from TaskActivity.java:
        String id = getArguments().getString("task");
        Log.i("tiedot", id);
        //TESTIKOMENTO:
        String url = StatusService.StaticStatusService.sc.DescribeCategoryTasks("1");
        //TÄLLÄ KOMENNOLLA PITÄISI LOPULLISESSA VERSIOSSA TOIMIA: String url = StatusService.StaticStatusService.sc.DescribeTaskAnswers(id);
        hp = new HTTPSRequester(new AnswerListener()).execute(url);

        return view;
    }

    @Override
    public void onClick(View v) {
        int button = v.getId();
        //String taskVideo ="https://d3kto7252bccha.cloudfront.net/" + a +".mp4";
        String url = StatusService.StaticStatusService.sc.DescribeAnswer(Integer.toString(button));
        hp = new HTTPSRequester(new URLListener()).execute(url);
    }

    private void answers(String response) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.answers);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 15);
        StatusService.StaticStatusService.jc.newJson(response);
        ArrayList<HashMap<String, String>> answers = StatusService.StaticStatusService.jc.getObjects();

        //draws chosen number of taskvideobuttons:
        Button[] answerbutton = new Button[10];
        for (int i = 0; i < answers.size(); i++) {
            if (!answers.get(i).isEmpty()) {

                String id = answers.get(i).get("id");
                Log.i("vastaus", id);
                //int imageID = getResources().getIdentifier(id, "drawable", getActivity().getApplicationContext().getPackageName());
                answerbutton[i] = new Button(getContext());
                //answerbutton[i].setImageResource(imageID);
                //answerbutton[i].setScaleType(ImageView.ScaleType.FIT_CENTER); // skaalaa vastausvideon thumbnailin
                answerbutton[i].setOnClickListener(this);
                answerbutton[i].setLayoutParams(lp);
                answerbutton[i].setBackgroundColor(Color.RED);
                answerbutton[i].setText("VASTAUS "+id);
                final float scale = getContext().getResources().getDisplayMetrics().density;
                answerbutton[i].setMinimumHeight((int) (50 * scale + 0.5f));
                answerbutton[i].setMinimumWidth((int) (100 * scale + 0.5f));

                ((ViewGroup.MarginLayoutParams) answerbutton[i].getLayoutParams()).leftMargin = 7;
                //TAI: ((MarginLayoutParams) answerbutton[i].getLayoutParams()).leftMargin = 7;
                answerbutton[i].setId(Integer.parseInt(answers.get(i).get("id")));
                ll.addView(answerbutton[i], lp);
        }
        

        /* NÄMÄ OMINAISUUDET PITÄISI SIIRTÄÄ IMAGEBUTTONEIHIN YLLÄ:
        <Button
        ***android:layout_width="wrap_content"
        ***android:layout_height="wrap_content"
        ***android:id="@+id/button1"
        android:text="PYSTYVIDEO"
        android:adjustViewBounds="false"
        android:baselineAlignBottom="false"
        android:cropToPadding="false"
        android:scrollIndicators="right"
        android:visibility="visible"
        ***android:minHeight="50dp"
        ***android:minWidth="100dp"
        ***android:background="#fd0101"
        ***android:layout_margin="7dp" />
*/
        }
    }

    private void answerURL (String response) {
        StatusService.StaticStatusService.jc.newJson(response);
        ArrayList<HashMap<String, String>> answer = StatusService.StaticStatusService.jc.getObjects();
        answerURL = "https://s3.eu-central-1.amazonaws.com/p60v4ow30312-answers/"+answer.get(0).get("uri");
        Log.i("answerURL", answerURL);
        ((TaskActivity) getActivity()).playback(answerURL);
    }
}