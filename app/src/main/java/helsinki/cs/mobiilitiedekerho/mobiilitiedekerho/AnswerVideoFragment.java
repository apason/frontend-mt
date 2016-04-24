package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;


public class AnswerVideoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private VideoView videoView;
    private AsyncTask hp;
    private String answerURL;

    
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

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.answer_video_fragment, container, false);

        //task_id from TaskActivity.java:
        String id = getArguments().getString("task");
        String url = StatusService.StaticStatusService.sc.DescribeTaskAnswers(id);
        Log.i("urli", url);
        hp = new HTTPSRequester(new AnswerListener()).execute(url);

        return view;
    }

    @Override
    public void onClick(View v) {
        int button = v.getId();
        String url = StatusService.StaticStatusService.sc.DescribeAnswer(Integer.toString(button));
        hp = new HTTPSRequester(new URLListener()).execute(url);
    }

    private void answers(String response) {
        Log.i("responssi", response);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.answers);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 15);
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            ArrayList<HashMap<String, String>> answers = StatusService.StaticStatusService.jc.getObjects();

            //draws chosen number of taskvideobuttons:
            Button[] answerbutton = new Button[10];
            for (int i = 0; i < answers.size(); i++) {
                if (!answers.get(i).isEmpty()) {

                    String id = answers.get(i).get("id");
                    //int imageID = getResources().getIdentifier(id, "drawable", getActivity().getApplicationContext().getPackageName());
                    answerbutton[i] = new Button(getContext());
                    //answerbutton[i].setImageResource(imageID);
                    //answerbutton[i].setScaleType(ImageView.ScaleType.FIT_CENTER); // skaalaa vastausvideon thumbnailin, jos erikokoisia
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
            }
        }
        //TODO else?
        
    }

    //Gets URL from database and calls activity's method to play video in VideoScreen object.
    private void answerURL (String response) {
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            ArrayList<HashMap<String, String>> answer = StatusService.StaticStatusService.jc.getObjects();
            //answerURL = StatusService.StaticStatusService.s3Location + StatusService.StaticStatusService.answerBucket + "/" + answer.get(0).get("uri");
            answerURL = answer.get(0).get("uri");
            ((TaskActivity) getActivity()).playback(answerURL);
        }
        //TODO else?
    }
}