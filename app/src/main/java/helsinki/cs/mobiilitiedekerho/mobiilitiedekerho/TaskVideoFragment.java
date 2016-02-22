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
import android.widget.VideoView;


public class TaskVideoFragment extends Fragment {

    View view;
    VideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.task_video_fragment, container, false);

        /*
        //get message (task_id) from category activity:
        String strtext = getArguments().getString("taskidmessage");
        Log.i("TASKMESSAGE ", strtext);
        */

        /*
        Button button =
            (Button) view.findViewById(R.id.taskbutton);
        button.setOnClickListener(this);

        videoView = (VideoView) view.findViewById(R.id.viewTaskVideo);
        MediaController mediaController = new MediaController(TaskVideoFragment.this.getActivity());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        */
        return view;
    }

    /*
    @Override
    public void onClick(View v) {
        String taskVideo = "https://s3.eu-central-1.amazonaws.com/p60v4ow30312-tasks/VID_20160201_150600.mp4";
        VideoPlayer vp = new VideoPlayer(videoView, taskVideo);
        vp.playVideo();
    }
    */

}
