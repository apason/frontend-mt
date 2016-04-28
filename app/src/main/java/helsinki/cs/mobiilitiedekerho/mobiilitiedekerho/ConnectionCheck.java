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
    * Checks if network connection aviable and notifies the user if there is no network available.
    */
    public void conMgr() {
        if (!isNetworkAvailable(StatusService.StaticStatusService.context)) {
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
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting(); //TODO: Is Connecting does not mean that the connection establisation will work out!
        Log.i("yhteys", Boolean.toString(isConnected));
        return isConnected;
    }
}
