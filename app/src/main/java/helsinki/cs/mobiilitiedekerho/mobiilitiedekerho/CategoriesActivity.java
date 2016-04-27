package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

public class CategoriesActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE_CATEGORY = "helsinki.cs.mobiilitiedekerho.mobiilitiedekerho.CATEGORY";
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Cheks if there is an internet connection available. Note:  isConnectedOrConnecting () is true if connection is being established, but hasn't already.
        new ConnectionCheck().conMgr(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);

        CategoriesFragment cf = new CategoriesFragment();
        HomeButtonFragment hbf = new HomeButtonFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.categories_fragment, cf);
        transaction.add(R.id.home_button_fragment, hbf);
        transaction.commit();
    }

    //Starts CategoryActivity with the particular task chosen
    public void startCategory(String id) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CATEGORY, id);
        startActivity(intent);
    }

    @Override
    public void onResume() {  // Refreshes screen when returning to this page, after eg. logging in or out
        super.onResume();
        new ConnectionCheck().conMgr(this);
        drawScreen();
    }

    public void drawScreen() {
        CategoriesFragment cf = new CategoriesFragment();
        HomeButtonFragment hbf = new HomeButtonFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.categories_fragment, cf);
        transaction.add(R.id.home_button_fragment, hbf);
        transaction.commit();
    }
}
