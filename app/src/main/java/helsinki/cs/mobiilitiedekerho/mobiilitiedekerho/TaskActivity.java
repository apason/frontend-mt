package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

//public class TaskActivity extends FragmentActivity {
/**
* No idea, guess that something about tasks... ???
*/
public class TaskActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE_URL = "helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.CATEGORY";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Draw components described in activity_main.xml on screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        new ConnectionCheck().conMgr(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        //get message (task_id) from category activity:
        Intent intent = getIntent();
        String taskId = intent.getStringExtra(CategoryActivity.EXTRA_MESSAGE);

        CameraFragment cf = new CameraFragment();
        AnswerVideoFragment avf = new AnswerVideoFragment();
        TaskVideoFragment tvf = new TaskVideoFragment();
        InfoTextFragment uif = new InfoTextFragment();
        //HomeButtonFragment hbf = new HomeButtonFragment();
        uif.setTitle("Tehtävän kuvaus");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.task_video_fragment, tvf);
        transaction.add(R.id.camera_fragment, cf);
        transaction.add(R.id.answer_video_fragment, avf);
        transaction.add(R.id.info_button_fragment, uif);
        //transaction.add(R.id.home_button_fragment, hbf);

        Bundle bundle = new Bundle();
        bundle.putString("task", taskId);
        cf.setArguments(bundle);
        avf.setArguments(bundle);
        tvf.setArguments(bundle);
        uif.setArguments(bundle);

        transaction.commit();
    }

    /**
    * ???
    */
    public void playback(String uri, String contentType) {
        Intent intent = new Intent(this, VideoScreen.class);
        StatusService.StaticStatusService.url = uri;
        StatusService.StaticStatusService.mediaTypee = contentType;
        startActivity(intent);
    }

    @Override
    public void onResume() {  // Refreshes screen when returning to this page, after eg. logging in or out
        super.onResume();
        new ConnectionCheck().conMgr(this);
        drawScreen();
    }

    /**
    * Draws the needed components to the screen.
    */
    public void drawScreen() {
        CameraFragment cf = new CameraFragment();
        AnswerVideoFragment avf = new AnswerVideoFragment();
        TaskVideoFragment tvf = new TaskVideoFragment();
        InfoTextFragment uif = new InfoTextFragment();
        uif.setTitle("Tehtävän kuvaus");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.task_video_fragment, tvf);
        transaction.add(R.id.camera_fragment, cf);
        transaction.add(R.id.answer_video_fragment, avf);
        transaction.add(R.id.info_button_fragment, uif);
    }
}
