package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.media.MediaPlayer;
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


public class AnswerVideoFragment extends Fragment implements View.OnClickListener {

    View view;
    VideoView videoView;

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

        return view;
    }

    @Override
    public void onClick(View v) {
        //task_id from TaskActivity.java:
        String id = getArguments().getString("task");
        Log.i("taskId", id);
        String taskVideo ="http://download.wavetlan.com/SVV/Media/HTTP/MP4/ConvertedFiles/Media-Convert/Unsupported/test7.mp4";
        //taskVideo = ServiceCommunication.describeAnswer(id);
        ((TaskActivity) getActivity()).playback(taskVideo);
    }
}


