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

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class cameraControl extends AppCompatActivity {

    private static final int VIDEO_CAPTURE = 101;
    private Uri fileUri;
    private databaseControl dbControl = new databaseControl();
    private File mediaFile;
    private String mediaFileName;
    private String bucketName = "mobiilitiedekerho-testi";

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
            mediaFile = new File(mediaStorageDirectory.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
            mediaFileName = mediaFile.getName();
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
                        TransferUtility transutil = dbControl.getTransferUtility();
                TransferObserver observer = transutil.upload(
                        bucketName,     /* The bucket to upload to */
                        mediaFileName,    /* The key for the uploaded object */
                        mediaFile        /* The file where the data to upload exists */
                );
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










