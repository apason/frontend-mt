package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    // task_id viestinä (MESSAGE) TaskActivity.javalle:
    public final static String EXTRA_MESSAGE = "helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.MESSAGE";
    //public final static String EXTRA_MESSAGE = "MESSAGE";
    LinearLayout ll;

    //Creates dynamically imagebuttons for each task in a category
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clouds_layout);
        ll = (LinearLayout) findViewById(R.id.category);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        /*
        LISTA TASKIEN NIMISTÄ TÄLLAISELLA METODILLA BACKENDISTÄ - SC = ServiceCommunication-olio
        ArrayList<String> tasks = sc.getTasks("clouds");
        */

        ImageButton [] taskbutton = new ImageButton[2];
        for (int i = 0; i < 2; i++) {
            taskbutton[i] = new ImageButton(this);
            if (i == 0) taskbutton[i].setImageResource(R.drawable.rain);
            if (i == 1) taskbutton[i].setImageResource(R.drawable.snow);
            taskbutton[i].setLayoutParams(lp);
            taskbutton[i].setOnClickListener(this);
            taskbutton[i].setBackgroundColor(Color.TRANSPARENT);
            //taskbutton[i].setTag(i); = TURHA RIVI?
            taskbutton[i].setId(i);
            // lopulliseen versioon ServerCommunicationista getID:llä tai vastaavalla metodilla: taskbutton[i].setId(tasks.getId())
            ll.addView(taskbutton[i], lp);
            }

    }

    //Starts TaskActivity and passes task_id as MESSAGE to TaskActivity class
    @Override
    public void onClick(View v) {
        int number = v.getId();
        String s = Integer.toString(number);
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(EXTRA_MESSAGE, s); // s = task_id
        startActivity(intent);
    }

}
