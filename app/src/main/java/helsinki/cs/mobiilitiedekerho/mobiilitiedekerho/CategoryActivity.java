package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    // task_id viestinä (MESSAGE) TaskActivity.javalle:
    public final static String EXTRA_MESSAGE = "helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.MESSAGE";
    LinearLayout ll;
    ServerCommunication commService;
    boolean CommunicationBound = false; //false at the beggining

    //Creates dynamically imagebuttons for each task in a category
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //All activities must have these variables


        //On onStart() there must be added (after super calling):
        Intent intent = new Intent(this, ServerCommunication.class);
        bindService(intent, CommunicationConnection, Context.BIND_AUTO_CREATE); //CommunicationConnection told in this file

        super.onCreate(savedInstanceState);
        setContentView(R.layout.clouds_layout);

        LoginFragment lf = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.login_button_fragment, lf);


        ll = (LinearLayout) findViewById(R.id.category);
        //ll.setOrientation(FrameLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        //HashMap<String, String> tasks = commService.DescribeCategory("1");
        //String koko = Integer.toString(tasks.size());
        //Log.i("koko", koko);
        //IMAGEBUTTONS ARE DRAWN LIKE THIS WHEN SERVER COMMUNICATION IS USED:
/*
        sc = new ServerCommunication();

        // only one category in this sprint: category_id = String "0" ?
        // HashMap<String, String> task: Key = task_id &  Value = name ???
        HashMap<String, String> tasks = sc.DescribeCategory("0");
        ImageButton [] taskbutton = new ImageButton[tasks.size()];
        for (int i = 0; i < tasks.size()-1; i++) {
            taskbutton[i] = new ImageButton(this);
            taskbutton[i].setImageResource(R.drawable.rain);
            taskbutton[i].setLayoutParams(lp);
            taskbutton[i].setOnClickListener(this);
            taskbutton[i].setBackgroundColor(Color.TRANSPARENT);
            taskbutton[i].setId(Integer.parseInt(tasks.get(i)));
            ll.addView(taskbutton[i], lp);
        }
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

        transaction.commit();

    }

    //Starts TaskActivity and passes task_id as MESSAGE to TaskActivity class
    @Override
    public void onClick(View v) {
        String s = Integer.toString(v.getId());
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(EXTRA_MESSAGE, s); // s = task_id
        startActivity(intent);
    }


   //** Defines callbacks for service binding, passed to bindService() *//*
    private ServiceConnection CommunicationConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to ServerCommunication, cast the IBinder and get ServerCommunication instance
            //LocalBinder binder = (LocalBinder) service;
            commService = ((ServerCommunication.LocalBinder)service).getService();
            CommunicationBound  = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            CommunicationBound  = false;
        }
    };
}

