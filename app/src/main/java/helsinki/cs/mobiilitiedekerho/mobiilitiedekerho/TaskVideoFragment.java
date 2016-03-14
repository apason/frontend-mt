package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskVideoFragment extends Fragment implements View.OnClickListener {

    public class Listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            task(response);
        }
    }

    View view;
    VideoView videoView;
    AsyncTask hp;
    String taskURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.task_video_fragment, container, false);

        Button tbutton =
            (Button) view.findViewById(R.id.taskbutton);
        tbutton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        //task_id from TaskActivity.java:
        String id = getArguments().getString("task");

        // String taskVideo = ServiceCommunication.DescribeTask(id);
        String url = StatusService.StaticStatusService.sc.DescribeTask(id);
        Log.i("virheurl ", url);
        hp = new HTTPSRequester(new Listener()).execute(url);
        //String taskURL = "http://download.wavetlan.com/SVV/Media/HTTP/H264/Talkinghead_Media/H264_test1_Talkinghead_mp4_480x360.mp4";

    }

    private void task(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
        ArrayList<HashMap<String, String>> task = StatusService.StaticStatusService.jc.getObjects();
        taskURL = "https://s3.eu-central-1.amazonaws.com/p60v4ow30312-tasks/"+task.get(0).get("uri");
        Log.i("taskURL ", taskURL);
        ((TaskActivity) getActivity()).playback(taskURL);
    }
}
