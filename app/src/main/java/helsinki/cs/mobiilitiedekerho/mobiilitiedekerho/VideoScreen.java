package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import name.cpr.VideoEnabledWebChromeClient;
import name.cpr.VideoEnabledWebView;


/**
 * This class (activity) is for showing inside a inline webview (task/asnwer)-videos and answer-images to the user.
 * Uses VideoEnabledWebView wrapper/extension of Webview.
 */
public class VideoScreen extends Activity {


    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;
    
    
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        // Save the web view
        webView = (VideoEnabledWebView)findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup)findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView); // See all available constructors... //TODO: Maybe another one fits better?
//         {
//             // Subscribe to standard events, such as onProgressChanged()...
//             @Override
//             public void onProgressChanged(WebView view, int progress)
//             {
//                 // Your code...
//             }
//         };
        
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                //For now this just hides the title bar.
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


        // This code is for making the video to start palying without user interaction, works only in API level 17 or higher (that is android ver. >=4.2).
        // (Older ones actually (usually) doesn't need this either.) 
        // If it is preferable to not autoplay, setting in the html <video> preload="true" makes it start loading before playing. Also then check: http://stackoverflow.com/a/30692993
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }



        // Video loading code from intent message: URL.
        // It must contain a axtension telling which file-type it is.
        String url = StatusService.StaticStatusService.url;

        // From file's' extension it does determine whether it is showing a video or a image.
        //MimeTypeMap mime = MimeTypeMap.getSingleton();
        //String ext = url.substring(url.lastIndexOf(".")).toLowerCase(); //toLowerCase in the (odd) case of the extension being in UpperCase, else MimeType may not recognize it.
        //String type = mime.getMimeTypeFromExtension(ext);
        String type = null;
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        if (ext != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }

        String html_text = null;
        if (type == null) { //No file extension or doesn't match any known by the MimeType-class.
            // Let's guess that it is a video! TODO: hmmm...
            html_text = StatusService.StaticStatusService.VideoPlay_HtmlTemplate.replace("#video_src#", url);

            Log.i("videoscreen", "null-type");
        }
        else {
            if (type.contains("video")) {
                html_text = StatusService.StaticStatusService.VideoPlay_HtmlTemplate.replace("#video_src#", url);
                Log.i("videoscreen-video", html_text);
            }
            else if (type.contains("image")) {
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                html_text = "<html><img src='" + url + "' /></html>";
            }
            else ; //TODO: Wrong file extension! Should not happen ever thought.
        }
        
        //NOTE: Only "US-ASCII charset" is allowed/works in the html_text actually (android bug).
        webView.loadData(html_text, "text/html; charset=utf-8", "UTF-8");
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
                // Standard back button implementation (for example this could close the app).
                super.onBackPressed();
            }
        }
    }

    // Stops showing and downloading the video streamuig if back button is pressed.
    // And 'resets' the webview.
    @Override
    public void onPause() {
        super.onPause();
        webView.stopLoading();
        webView.destroy();
    }


}
