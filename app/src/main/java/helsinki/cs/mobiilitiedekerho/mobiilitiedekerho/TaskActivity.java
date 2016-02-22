package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Draw components described in activity_main.xml on screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
        //get message (task_id) from category activity:
        Intent intent = getIntent();
        String message = intent.getStringExtra(CategoryActivity.EXTRA_MESSAGE);
        Log.i("viesti ", message);
        /*
        // yksi tapa välittää viesti fragmentille on Bundle:
        //http://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
        Bundle bundle = new Bundle();
        bundle.putString("taskidmessage", message);
        TaskVideoFragment fragobj = new TaskVideoFragment();
        fragobj.setArguments(bundle);
        */
    }
}
