package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);

        LoginFragment lf = new LoginFragment();
        CategoriesFragment cf = new CategoriesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.categories_fragment, cf);
        transaction.commit();
    }

    //Starts CategoryActivity with a particular task chosen
    public void startCategory(String id) {
        Log.i("kategoria", id);
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CATEGORY, id);
        startActivity(intent);
    }
}
