package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import java.util.HashMap;


public class AnswerVideoFragment extends Fragment implements View.OnClickListener {
/*
    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            answers(response);
        }
    }
*/
    View view;
    VideoView videoView;
    AsyncTask hp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.answer_video_fragment, container, false);

        //Set onclick listeners for the task video buttons
        Button button1 =
                (Button) view.findViewById(R.id.button1);
        button1.setOnClickListener(this);

        Button button2 =
                (Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(this);

        //task_id from TaskActivity.java:
        String id = getArguments().getString("task");
        Log.i("taskId", id);

        String url = StatusService.StaticStatusService.sc.DescribeTaskAnswers(id);
  //      hp = new HTTPSRequester(new listener()).execute(url);

        return view;
    }

    @Override
    public void onClick(View v) {

        String a = "video";
        String taskVideo ="https://d3kto7252bccha.cloudfront.net/" + a +".mp4";
        //taskVideo = ServiceCommunication.describeAnswer(id);
        ((TaskActivity) getActivity()).playback(taskVideo);
    }
/*KESKEN
    private HashMap answers(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
        HashMap<String, String> answers = StatusService.StaticStatusService.jc.getObject();

    }
*/
}