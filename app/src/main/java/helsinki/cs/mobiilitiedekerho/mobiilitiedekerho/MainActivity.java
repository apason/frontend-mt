package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Piirrä activity_main.xml:ssä määritellyt fragmentit laitteen näytölle
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
