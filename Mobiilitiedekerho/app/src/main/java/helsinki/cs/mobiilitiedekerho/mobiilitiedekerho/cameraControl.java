package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class cameraControl extends AppCompatActivity {

    private static final int VIDEO_CAPTURE = 101;
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videobrowser);

        Button recordButton =
                (Button) findViewById(R.id.recordButton);

        //POISTETTU KÄYTÖSTÄ EMULAATTORIN TAKIA!
        //Poista painike käytöstä, jos laitteessa ei ole kameraa
        //if (!hasCamera())
        //    recordButton.setEnabled(false);
    }

    //Tarkistaa onko laitteessa kameraa
    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }


    public void recordButtonOnClick(View view) {

        //Luo puhelimen muistiin uusi hakemisto 'Mobiilitiedekerho', johon appiin liittyvät videot
        //tallennetaan.
        File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Mobiilitiedekerho");

        //Jos hakemistoa ei pystytty luomaan, niin ilmoita siitä.
        if (!mediaStorageDirectory.exists()) {
            if (!mediaStorageDirectory.mkdirs()) {
                Log.e("Mobiilitiedekerho", "failed to create directory");
            }
        }

            //Luodaan kuvattavalle videolle uniikki nimi VID + timestamp + .mp4
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDirectory.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");

            //Luo uusi video-intent, jonka tallennustiedoiksi annetaan äsken luotu osoite ja nimi.
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            fileUri = Uri.fromFile(mediaFile);

            //Käynnistetään äsken lutu intent käyttäen laitteen omaa kamerasoftaa.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, VIDEO_CAPTURE);
        }


    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video has been saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}










