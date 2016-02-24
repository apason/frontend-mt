package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoScreen extends Fragment {

    View view;
    VideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.video_view_fragment, container, false);
        // Add functionality to the VideoView
        videoView = (VideoView) view.findViewById(R.id.viewTaskVideo);
        MediaController mediaController = new MediaController(VideoScreen.this.getActivity());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);

        return view;
    }

    public void playVideo(String uri) {

        Uri videoUri = Uri.parse(uri);
        Log.i ("videoosoite" , uri);
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
    }
}
