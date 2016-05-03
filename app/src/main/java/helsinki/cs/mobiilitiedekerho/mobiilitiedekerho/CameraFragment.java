
package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A class responsible for activating the camera for image or video capture.
 * This class also sends the resulting video/image answer further to be uploaded.
 */
public class CameraFragment extends Fragment implements View.OnClickListener {

    private Dialog upload;
    private Button newVideoButton;
    private Button existingVideoButton;
    private Button newImageButton;
    private Button existingImageButton;
    private ProgressBar progressBar;

    private View view;

    private String taskId;

    private static final int VIDEO_CAPTURE = 101;
    private static final int IMAGE_CAPTURE = 102;
    private static final int VIDEO_SAVED = 201;
    private static final int IMAGE_SAVED = 202;

    private Uri fileUri;
    private File selectedFile;
    private String selectedFileName;
    private String answerID;
    private int progress_status;

    private AsyncTask S3 = null;
    private AsyncTask hp = null;

    /**
     * A listener that closes the upload dialog when the upload task is finished.
     */
    public class CloseDialog implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            upload.dismiss();
        }
    }

    /**
     * A listener that checks whether the upload finished successfully.
     */
    public class S3uploadFinished implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            String url = StatusService.StaticStatusService.sc.EndAnswerUpload(answerID, response);
            Log.i("loppu", url);
            progress_status += 50;
            progressBar.setProgress(progress_status);
            new HTTPSRequester(new CloseDialog()).execute(url);
        }
    }

    /**
     * A listener that checks whether the URL for uploading was received.
     */
    public class GotUrlToUpload implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            Log.i("upataan", "uppaus on yeeeeah");
            Log.i("uppausresponssi", response);
            progress_status += 50;
            progressBar.setProgress(progress_status);
            // If the response was success then do the following
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                String answerUri = StatusService.StaticStatusService.jc.getProperty("answer_uri");
                Log.i("vastausuri", answerUri);
                answerID = StatusService.StaticStatusService.jc.getProperty("answer_id");
                upload(answerUri);
            }
        }
    }

    // A method used for drawing the objects needed for this fragment on screen
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //taskId = this.getArguments().toString();
        taskId = getArguments().getString("task");
        Log.i("taski2", taskId);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.camera_fragment, container, false);


        // Add onClickListener to the record button
        Button recordButton =
                (Button) view.findViewById(R.id.recordButton);
        recordButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        // If user hasn't logged in disable uploading functionality.
        if (!StatusService.loggedIn()) {
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginDialog.class);
            startActivity(intent);
        } else {
            // A dialog that shows the user multiple choices for adding an answer
            upload = new Dialog(getActivity());
            upload.setContentView(R.layout.answer_upload_fragment);
            upload.setTitle("Lisää vastaus tehtävään");

            // Create the buttons.
            createNewVideoButton();
            createExistingVideoButton();
            createNewImageButton();
            createExistingImageButton();
            createCancelButton();

            // Sets the buttons to the dialog.
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(upload.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            upload.show();
            upload.getWindow().setAttributes(lp);
        }
    }


	//Create a button that after clicking leads the user to record a video using the device's camera.
    private void createNewVideoButton() {
        newVideoButton =
                (Button) upload.findViewById(R.id.newVideoButton);
        newVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The directory where taken videos (and images) related to this app are stored.
                File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Mobiilitiedekerho");

                // Creates new folder if necessary. If the new folder could not be created, then notify ???
                if (!mediaStorageDirectory.exists()) {
                    if (!mediaStorageDirectory.mkdirs()) {
                        Log.e("Mobiilitiedekerho", "failed to create directory");
                    }
                }

                // Creates a file for saving the shot video VID + timestamp + .mp4
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                // The file that was selected by the user
                selectedFile = new File(mediaStorageDirectory.getPath() + File.separator +
                        "VID_" + timeStamp + ".mp4");
                try {
                    selectedFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedFileName = selectedFile.getName();

                // Create a new Intent to shoot video and save the result to the file specified earlier
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = Uri.fromFile(selectedFile);

                // Start the intent using the device's own camera software
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, VIDEO_CAPTURE);
            }
        });
    }
    
    // Creates a button that after clicking leads the user to upload a video from the device's gallery.
    private void createExistingVideoButton() {
        existingVideoButton =
                (Button) upload.findViewById(R.id.existingVideoButton);
        existingVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
                pickMedia.setType("video/*");
                startActivityForResult(pickMedia, VIDEO_SAVED);
            }
        });
	}
    
    //Create button that after clicking leads the user to take a picture using the device's camera.
	private void createNewImageButton() {
        newImageButton =
                (Button) upload.findViewById(R.id.newImageButton);
        newImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The directory where taken videos (and images) related to this app are stored.
                File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Mobiilitiedekerho");

                // Creates new folder if necessary. If the new folder could not be created, then notify ???
                if (!mediaStorageDirectory.exists()) {
                    if (!mediaStorageDirectory.mkdirs()) {
                        Log.e("Mobiilitiedekerho", "failed to create directory");
                    }
                }

                // Creates a file for saving the shot image VID + timestamp + .JPEG
                // TODO: the name should contain task.
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                selectedFile = new File(mediaStorageDirectory.getPath() + File.separator +
                        "IID_" + timeStamp + ".JPEG");
                try {
                    selectedFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedFileName = selectedFile.getName();

                // Create a new Intent to shoot image and save the result to the file specified earlier
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(selectedFile);

                // Start the intent using the device's own camera software
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, IMAGE_CAPTURE);
            }
        });
	}
    
	// Creates a button that opens the device's image gallery to upload a image as an asnwer.
	private void createExistingImageButton() {
        existingImageButton =
                (Button) upload.findViewById(R.id.existingImageButton);
        existingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
                pickMedia.setType("image/*");
                startActivityForResult(pickMedia, IMAGE_SAVED);
            }
        });
	}
	
	// Creates a button that closes the dialog
	private void createCancelButton() {
        Button cancelButton =
                (Button) upload.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.dismiss();
            }
        });
	}

    // Activity handler for checking if the answer recording was successful
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result handler for videos from the gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TaskActivity.RESULT_OK) {
            if (selectedFile.exists()) {
                createUrl();
            }
            // If the recording was cancelled by the user, then notify
        } else if ((requestCode == VIDEO_CAPTURE || requestCode == IMAGE_CAPTURE) && resultCode == TaskActivity.RESULT_CANCELED) {
            Toast.makeText(CameraFragment.this.getActivity(), "Kuvaaminen peruttu.",
                    Toast.LENGTH_LONG).show();
        } else {
            // If the recording was cancelled for some other reason, then notify
            Toast.makeText(CameraFragment.this.getActivity(), "Kuvaaminen epäonnistui.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * A method for creating a correct URL to make 'StartAnswerUpload' and makes the response to be passed to GotUrlToUpload-method.
     * Also it notify the user if no SubUser is in use at the moment.
     */
    private void createUrl() {
        if (StatusService.StaticStatusService.currentSubUserID != null) {
            String Ftype;
            try {
                Ftype = selectedFileName.substring(selectedFileName.lastIndexOf(".")).toLowerCase().substring(1);
            } catch (Exception e) {
                Ftype = "fail";
            }


            newVideoButton.setEnabled(false);
            existingVideoButton.setEnabled(false);
            newImageButton.setEnabled(false);
            existingImageButton.setEnabled(false);
            progressBar = (ProgressBar) upload.findViewById(R.id.Progressbar);
            progressBar.setProgress(0);

            String url = StatusService.StaticStatusService.sc.StartAnswerUpload
                    (taskId, StatusService.StaticStatusService.currentSubUserID, Ftype);
            Log.i("StartAnswerUpload", url);
            hp = new HTTPSRequester(new GotUrlToUpload()).execute(url);
        }
        else {
            Toast.makeText(CameraFragment.this.getActivity(), "Sinun on valittava tai luotava käyttäjä lähettääksesi vastauksen",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * A method for starting a new S3Upload that uploads the recorded answer to Amazon S3
     * @param answerUri the signed url to use for uploading the answer.
     */
    public void upload(String answerUri) {
        Log.i("answerUri", answerUri);
        S3 = new S3Upload(new S3uploadFinished(), selectedFile).execute(answerUri);
    }
}