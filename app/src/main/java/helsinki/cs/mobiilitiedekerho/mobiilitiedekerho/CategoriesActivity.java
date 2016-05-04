package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;


/**
 *  A class responsible for adding the needed fragments to show the categories
 */
public class CategoriesActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE_CATEGORY = "helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.CATEGORY";
    LinearLayout ll;

    // Adds the fragments needed for this activity to FragmentTransaction
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  Checks if there is an internet connection available. Note:  isConnectedOrConnecting () is true if connection is being established, but hasn't already.
        StatusService.StaticStatusService.cc.conMgr();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);

        CategoriesFragment cf = new CategoriesFragment();
        HomeButtonFragment hbf = new HomeButtonFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.categories_fragment, cf);
        transaction.add(R.id.home_button_fragment, hbf);
        transaction.commit();
    }

    /**
    * Starts CategoryActivity with the particular task chosen.
    * @param id the task id.
    */
    public void startCategory(String id) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CATEGORY, id);
        startActivity(intent);
    }

    // If the user returns to this activity, then do the following
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
        CategoriesFragment cf = new CategoriesFragment();
        HomeButtonFragment hbf = new HomeButtonFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.categories_fragment, cf);
        transaction.add(R.id.home_button_fragment, hbf);
        transaction.commit();
    }
}
