package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Checks if there is an internet connection available.
 * Note:  isConnectedOrConnecting () is true if connection is being established, but hasn't already.
 */
public class ConnectionCheck {

    public void conMgr(Context context) {
        if (!isNetworkAvailable(context)) {
            Log.i("täällä", "ollaan");
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Tietoliikennevirhe");
            alert.setMessage("Laite ei ole yhteydessä internetiin. Suurinta osaa Mobiilitiedekerhon toiminnoista ei voi käyttää ilman toimivaa verkkoyhteyttä");
            alert.setNegativeButton("Sulje", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.i("yhteys", Boolean.toString(isConnected));
        return isConnected;
    }
}
