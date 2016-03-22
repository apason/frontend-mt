package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
//import android.view.KeyEvent;
//import android.util.Log;

import helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.VideoEnabledWebView; //???!!

public class VideoScreen extends Activity {

    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_example); //Oli jo siellä, tarpeellinen?

        // Save the web view
        webView = (VideoEnabledWebView)findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup)findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors... //OK ehkä pitää säätää.
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
                // Mennä sitten sanomaan tarvitaanko tätä mitenkään.
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                // Mitä tää oiken tekee?
                if (fullscreen)
                {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    //noinspection all
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                }
                else
                {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    //noinspection all
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        // Call private class InsideWebViewClient
        webView.setWebViewClient(new InsideWebViewClient());
        
        //Lisäys jotta video autoplayais, (vain jos android >=4.2):
        //Jos mieluiten ei autoplayaa, niin tämä on varteenotettava asia mitä lisätä HTML tageihin: preload="true" (jos toimii webviewelle)
        //Tai sitten seuraava ratkaisu: http://stackoverflow.com/questions/30692883/html5-video-slow-to-start-playing-on-android-phonegap/30692993#30692993
       // if (android.os.Build.VERSION.SDK_INT >= 17) {
       //     WebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        //}
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        // Force links to be opened inside WebView and not in Default Browser
        // Thanks http://stackoverflow.com/a/33681975/1815624
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    
    @Override
    public void onBackPressed()
    {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        if (!webChromeClient.onBackPressed())
        {
            if (webView.canGoBack())
            {
                webView.goBack();
            }
            else
            {
                // Standard back button implementation (for example this could close the app) VOI VATTU!
                super.onBackPressed();
            }
        }
    }
    
    //Meidän projektin metodi.
    public void playVideo() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(TaskActivity.EXTRA_MESSAGE_URL);
        
        // Navigate anywhere you want, but consider that this classes have only been tested on YouTube's mobile site NO VOI VITSI!
        webView.loadUrl("https://s3.eu-central-1.amazonaws.com/p60v4ow30312-tasks/videotag.html");
        
        //webView.loadUrl(message);
    }
    
    
    //Onko tarvetta tälle oikeasti? (Vanhaa tavaraa)
//     @Override
//     public void onStop() {
//         super.onStop();
//         webView.stopLoading();
//         //Or something else?
//     }

//     //HMMMMMMMMMM
//     // Stops showing and downloading of a video downloading if back button is pressed.
//     @Override
//     public void onPause() {
//         super.onPause();
//         webView.stopLoading();
//         webView.destroy();
//     }




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
