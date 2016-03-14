package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

//public class TaskActivity extends FragmentActivity {
public class TaskActivity extends AppCompatActivity {

    VideoScreen vs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Draw components described in activity_main.xml on screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        //get message (task_id) from category activity:
        Intent intent = getIntent();
        String message = intent.getStringExtra(CategoryActivity.EXTRA_MESSAGE);

        CameraFragment cf = new CameraFragment();
        AnswerVideoFragment avf = new AnswerVideoFragment();
        TaskVideoFragment tvf = new TaskVideoFragment();
        InfoTextFragment uif = new InfoTextFragment();
        uif.setTitle("Tehtävän kuvaus tähän");
        vs = new VideoScreen();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.task_video_fragment, tvf);
        transaction.add(R.id.camera_fragment, cf);
        transaction.add(R.id.answer_video_fragment, avf);
        transaction.add(R.id.video_view_fragment, vs);
        transaction.add(R.id.info_button_fragment, uif);

        Bundle bundle = new Bundle();
        bundle.putString("task", message);
        avf.setArguments(bundle);
        tvf.setArguments(bundle);

        transaction.commit();
    }

    public void playback (String uri) {
        vs.playVideo(uri);
    }
}
