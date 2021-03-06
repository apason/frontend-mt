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

    /**
     * Checks if a network connection is aviable and notifies the user about the program's use if there is no network available.
     */
    public void conMgr() {
        if (!isNetworkAvailable()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(StatusService.StaticStatusService.context);
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

    // A method for checking if there is a network connection available. Uses permission to access ACCESS_NETWORK_STATE
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)StatusService.StaticStatusService.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // Note: Is Connecting does not mean that the connection establisation will work out!
        // TODO: Do something appart that for what happens in the "succes/failure -branches" that is do somehting in case of connecting.
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        Log.i("yhteys", Boolean.toString(isConnected));
        return isConnected;
    }
}
