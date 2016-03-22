package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class CategoryActivity extends AppCompatActivity {

    // task_id viestinä (MESSAGE) TaskActivity.javalle:
    public final static String EXTRA_MESSAGE = "helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.MESSAGE";
    LinearLayout ll;

    public void start(String response) {
        StatusService.StaticStatusService.jc.newJson(response);
    }

    //Creates dynamically imagebuttons for each task in a category
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);

        TasksFragment tf = new TasksFragment();
        Intent intent = getIntent();
        String categoryId = intent.getStringExtra(CategoriesActivity.EXTRA_MESSAGE_CATEGORY);
        Bundle bundle = new Bundle();
        bundle.putString("category", categoryId);
        tf.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.tasks_fragment, tf);
        transaction.commit();
    }

    //Starts TaskActivity with a particular task chosen
    public void startTask(String id) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }

}