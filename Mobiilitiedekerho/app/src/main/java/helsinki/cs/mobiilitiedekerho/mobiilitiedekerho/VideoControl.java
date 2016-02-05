package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;


public class VideoControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videobrowser);
        Button playButton = (Button) findViewById(R.id.playButton);


    }

    public void playButtonOnClick(View view) {
        VideoView videoView = (VideoView)findViewById(R.id.viewTaskVideo);
        String taskVideo = "https://s3.eu-central-1.amazonaws.com/p60v4ow30312-tasks/VID_20160201_150600.mp4";
        Uri videoUri = Uri.parse(taskVideo);
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }
}
