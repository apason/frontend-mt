package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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


    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            //TODO
        }
    }

    View view;
    private static final int VIDEO_CAPTURE = 101;
    private Uri fileUri;
    private File selectedFile;
    private String selectedFileName;
    
    AsyncTask hp = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.camera_fragment, container, false);


        // Add onClickListener to the record button
        Button recordButton =
                (Button) view.findViewById(R.id.recordButton);
        recordButton.setOnClickListener(this);

        // If user hasn't logged in disable camera functionality.
        if(!StatusService.loggedIn()) {
            recordButton.setEnabled(false);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

                // Create a dialog that allows the user to choose which method they want to use to create the answer video
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Miten haluat lisätä vastauksen?")

                        //Create button that enables to user to record a video using the device's camera
                        .setNegativeButton("Kuvaa uusi vastaus", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Create a new folder on the device, where videos related to this app are stored
                                File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), "Mobiilitiedekerho");

                                // If the new folder could not be created, then notify
                                if (!mediaStorageDirectory.exists()) {
                                    if (!mediaStorageDirectory.mkdirs()) {
                                        Log.e("Mobiilitiedekerho", "failed to create directory");
                                    }
                                }

                                // Create a file for saving the shot video VID + timestamp + .mp4
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                selectedFile = new File(mediaStorageDirectory.getPath() + File.separator +
                                        "VID_" + timeStamp + ".mp4");
                                selectedFileName = selectedFile.getName();
                                // Create a new Intent to shoot video and save the result to the file specified earlier
                                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                fileUri = Uri.fromFile(selectedFile);

                                // Start the intent using the device's own camera software
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                startActivityForResult(intent, VIDEO_CAPTURE);
                            }
                        })
                        // Create button that enables the user to use a file from the device's gallery
                        .setNeutralButton("Valitse olemassaoleva", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
                                pickMedia.setType("video/*");
                                startActivityForResult(pickMedia, 12345);
                            }
                        })
                                // Create button that enables the user to use a file from the device's gallery
                        .setPositiveButton("Peruuta", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
            }


    public void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        // Result handler for videos from the gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12345) {
            if (resultCode == TaskActivity.RESULT_OK) {

                // Get the selected file's Uri, name and the file itself
                Uri selectedVideoLocation = data.getData();
                File selectedFile = new File(selectedVideoLocation.getPath());
                String selectedFileName = selectedVideoLocation.toString();

                // Initialize the Amazon Cognito credentials provider
                if(selectedFile.exists()) {
                    hp = new S3Upload(new listener(), getContext(), selectedFile).execute(selectedFileName);
                }
            }
        }
        // Result handler for videos from the camera
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == TaskActivity.RESULT_OK) {
                //Toast t = Toast.makeText(CameraFragment.this.getContext(), "Video has been saved to:\n" +
                //        data.getData(), Toast.LENGTH_LONG);
                //t.show();-->
                // Initialize the Amazon Cognito credentials provider

                if(selectedFile.exists()) {
                    hp = new S3Upload(new listener(), getContext(), selectedFile).execute(selectedFileName);
                }

            } else if (resultCode == TaskActivity.RESULT_CANCELED) {
                Toast.makeText(CameraFragment.this.getActivity(), "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CameraFragment.this.getActivity(), "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
