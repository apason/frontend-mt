package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


/**
* An activity responsible of managing the category-view and the task-buttons.
*/
public class CategoryActivity extends AppCompatActivity {

    // task_id viestinä (MESSAGE) TaskActivity.javalle:
    public final static String EXTRA_MESSAGE = "helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.MESSAGE";

    public void start(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
    }

    //Creates dynamically imagebuttons for each task in a category
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_layout);

        StatusService.StaticStatusService.cc.conMgr();

        TasksFragment tf = new TasksFragment();
        HomeButtonFragment hbf = new HomeButtonFragment();
        Intent intent = getIntent();
        String categoryId = intent.getStringExtra(CategoriesActivity.EXTRA_MESSAGE_CATEGORY);
        Bundle bundle = new Bundle();
        bundle.putString("category", categoryId);
        tf.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.tasks_fragment, tf);
        transaction.add(R.id.home_button_fragment, hbf);
        transaction.commit();
    }

    //Starts TaskActivity with a particular task chosen
    public void startTask(String id) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }

    @Override
    public void onResume() {  // Refreshes screen when returning to this page, after eg. logging in or out
        super.onResume();
        StatusService.StaticStatusService.cc.conMgr();
        drawScreen();
    }

    /**
    * Draws the needed components to the screen.
    */
    public void drawScreen() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        String categoryId = intent.getStringExtra(CategoriesActivity.EXTRA_MESSAGE_CATEGORY);
        bundle.putString("category", categoryId);
        TasksFragment tf = new TasksFragment();
        tf.setArguments(bundle);
        HomeButtonFragment hbf = new HomeButtonFragment();
        transaction.add(R.id.tasks_fragment, tf);
        transaction.add(R.id.home_button_fragment, hbf);
        transaction.commit();
    }

}