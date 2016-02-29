package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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


public class TaskVideoFragment extends Fragment implements View.OnClickListener {

    View view;
    VideoView videoView;

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
        String taskVideo = "http://download.wavetlan.com/SVV/Media/HTTP/H264/Talkinghead_Media/H264_test1_Talkinghead_mp4_480x360.mp4";
        ((TaskActivity) getActivity()).playback(taskVideo);
    }

}
