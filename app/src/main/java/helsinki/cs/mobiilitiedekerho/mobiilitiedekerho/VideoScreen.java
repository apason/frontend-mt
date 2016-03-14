package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Activity;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
//import android.webkit.WebView;
//import android.widget.MediaController;
//import android.widget.VideoView;

public class VideoScreen extends Activity {
    View view;
    HTML5WebView vView;
//     VideoView videoView;
//     WebView webview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vView = new HTML5WebView(this);

        //Ainoastaan jos halutaan, että jos avataan sama video toiste, muistaisi missä oltiin.
        //if (savedInstanceState != null) {
        //    vView.restoreState(savedInstanceState);
        //} 

        Intent intent = getIntent();
        String message = intent.getStringExtra(TaskActivity.EXTRA_MESSAGE_URL);
        vView.loadUrl("https://s3.eu-central-1.amazonaws.com/p60v4ow30312-tasks/videotag.html");
        setContentView(vView.getLayout());
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.video_view_fragment, container, false);
//         // Add functionality to the VideoView
//         videoView = (VideoView) view.findViewById(R.id.viewTaskVideo);
//         MediaController mediaController = new MediaController(VideoScreen.this.getActivity());
//         mediaController.setAnchorView(videoView);
//         mediaController.setMediaPlayer(videoView);
//         videoView.setMediaController(mediaController);
//
//        return view;
//    }

    //Onko samaa varten kun se tarkistus?, Ainoastaan jos halutaan, että jos avataan sama video toiste, muistaisi missä oltiin.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        vView.saveState(outState);
    }

    public void playVideo() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(TaskActivity.EXTRA_MESSAGE_URL);
        vView.loadUrl("https://s3.eu-central-1.amazonaws.com/p60v4ow30312-tasks/videotag.html");
        //setContentView(vView.getLayout());
//         Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
//         Uri data = Uri.parse(uri);
//         intent.setDataAndType(data, "video/mp4");
//         startActivity(intent); //???
    }

    @Override
    public void onStop() {
        super.onStop();
        vView.stopLoading();
        //Or something else?
    }


//Mitä tämä edes tekee? Näyttää lähtevän pois videonäkymästä ja piilottaa vaan sen.    
//     @Override
//     public boolean onKeyDown(int keyCode, KeyEvent event) {
// 
//         if (keyCode == KeyEvent.KEYCODE_BACK) {
//             if (mWebView.inCustomView()) {
//                 mWebView.hideCustomView();
//             //  mWebView.goBack();
//                 //mWebView.goBack();
//                 return true;
//             }
// 
//         }
//         return super.onKeyDown(keyCode, event);
//     }
}