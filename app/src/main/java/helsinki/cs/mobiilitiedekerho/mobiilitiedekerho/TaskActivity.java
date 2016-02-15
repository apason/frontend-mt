package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Draw components described in activity_main.xml on screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
    }
}
