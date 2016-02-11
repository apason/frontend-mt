package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraFragment extends Fragment implements View.OnClickListener {

    View view;
    private static final int VIDEO_CAPTURE = 101;
    private Uri fileUri;
    private File mediaFile;
    private String mediaFileName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.camera_fragment, container, false);
        // Alusta nauhoitusnappi
        Button recordButton =
                (Button) view.findViewById(R.id.recordButton);
        recordButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

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


    public void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == MainActivity.RESULT_OK) {
                Toast.makeText(CameraFragment.this.getActivity(), "Video has been saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                // TransferUtility transutil = dbControl.getTransferUtility();
                //  TransferObserver observer = transutil.upload(
                //          bucketName,     /* The bucket to upload to */
                //          mediaFileName,    /* The key for the uploaded object */
                //          mediaFile        /* The file where the data to upload exists */
                //  );
            } else if (resultCode == MainActivity.RESULT_CANCELED) {
                Toast.makeText(CameraFragment.this.getActivity(), "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CameraFragment.this.getActivity(), "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }



}
