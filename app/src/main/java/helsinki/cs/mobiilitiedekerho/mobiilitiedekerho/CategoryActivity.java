package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
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

public class CategoryActivity extends AppCompatActivity {

    // task_id viestinä (MESSAGE) TaskActivity.javalle:
    public final static String EXTRA_MESSAGE = "helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.MESSAGE";
    LinearLayout ll;
    AsyncTask hp = null;
    boolean CommunicationBound = false; //false at the beggining



    public void start(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
    }

    //Creates dynamically imagebuttons for each task in a category
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String url = StatusService.StaticStatusService.sc.DescribeTask("1");

        hp = new HTTPSRequester(new start()).execute(url);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //All activities must have these variables

/*
        //On onStart() there must be added (after super calling):
        Intent intent = new Intent(this, ServerCommunication.class);
        bindService(intent, CommunicationConnection, Context.BIND_AUTO_CREATE); //CommunicationConnection told in this file
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clouds_layout);

        LoginFragment lf = new LoginFragment();
        TasksFragment tf = new TasksFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.tasks_fragment, tf);
        transaction.add(R.id.login_button_fragment, lf);
        transaction.commit();
    }

    //Starts TaskActivity with a particular task chosen
    public void startTask(String id) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id); // s = task_id
        startActivity(intent);
    }

/*
    //Starts TaskActivity and passes task_id as MESSAGE to TaskActivity class
    @Override
    public void onClick(View v) {
        String s = Integer.toString(v.getId());
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(EXTRA_MESSAGE, s); // s = task_id
        startActivity(intent);
    }

*/
    /*
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
    */
}

