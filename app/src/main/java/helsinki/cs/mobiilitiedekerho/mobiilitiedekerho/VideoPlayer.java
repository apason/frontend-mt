package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer {

    private String uri;
    private VideoView videoView;

    public VideoPlayer(VideoView vv, String uri) {
        this.uri = uri;
        this.videoView = vv;

    }

        public void playVideo() {
            Uri videoUri = Uri.parse(uri);
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
