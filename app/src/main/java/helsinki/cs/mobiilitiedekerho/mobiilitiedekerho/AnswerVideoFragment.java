package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Bundle;
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

        // Add functionality to the VideoView
        videoView = (VideoView) view.findViewById(R.id.viewTaskVideo);
        MediaController mediaController = new MediaController(AnswerVideoFragment.this.getActivity());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);

        return view;
    }


    @Override
    public void onClick(View v) {
        String taskVideo;
        switch (v.getId()) {
            // Play video related to button 1
            case R.id.button1:
                taskVideo = "https://s3.eu-central-1.amazonaws.com/p60v4ow30312-tasks/VID_20160201_150600.mp4";
                Uri videoUri = Uri.parse(taskVideo);
                videoView.setVideoURI(videoUri);
                videoView.start();

                // Empty VideoView after playback has finished
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        videoView.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                    }
                });
                break;
            // Play video related to button 2
            case R.id.button2:
                //if (view.getId() == R.id.button2) taskVideo = "https://s3.eu-central-1.amazonaws.com/p60v4ow30312-answers/20160207_221819%5B1%5D.mp4";
                taskVideo = "http://download.wavetlan.com/SVV/Media/HTTP/H264/Talkinghead_Media/H264_test1_Talkinghead_mp4_480x360.mp4";
                Uri videoUri2 = Uri.parse(taskVideo);
                videoView.setVideoURI(videoUri2);
                videoView.start();

                // Empty VideoView after playback has finished
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        videoView.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }
}


