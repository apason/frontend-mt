package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public class start implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            start(response);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String url = StatusService.StaticStatusService.sc.DescribeTask("1");

        hp = new HTTPSRequester(new start()).execute(url);
    }

    public void start(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
        }
}
