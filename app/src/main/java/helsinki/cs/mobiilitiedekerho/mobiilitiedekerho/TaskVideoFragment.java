package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskVideoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private AsyncTask hp;
    private String taskURL;

    
    public class Listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            task(response);
        }
    }

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
        hp = new HTTPSRequester(new Listener()).execute(url);
        //String taskURL = "http://download.wavetlan.com/SVV/Media/HTTP/H264/Talkinghead_Media/H264_test1_Talkinghead_mp4_480x360.mp4";

    }

    private void task(String response) {
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            ArrayList<HashMap<String, String>> task = StatusService.StaticStatusService.jc.getObjects();
            taskURL = StatusService.StaticStatusService.s3Location + StatusService.StaticStatusService.taskBucket + "/" + task.get(0).get("uri");
            ((TaskActivity) getActivity()).playback(taskURL);
        }
        //TODO: else?
    }
}
