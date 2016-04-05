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
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraFragment extends Fragment implements View.OnClickListener {

    private Dialog upload;


    public class S3uploadFinished implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            String url = StatusService.StaticStatusService.sc.EndAnswerUpload(answerID, response);
            new HTTPSRequester(null).execute(url);
        }
    }
    
    public class GotUrlToUpload implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            Log.i("upataan", "uppaus on yeeeeah");
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                String answerUri = StatusService.StaticStatusService.jc.getProperty("answer_uri");
                answerID = StatusService.StaticStatusService.jc.getProperty("answer_id");
                upload(answerUri);
            }
        }
    }
    

    private View view;
    
    private static final int VIDEO_CAPTURE = 101;
    private static final int IMAGE_CAPTURE = 102;
    private static final int VIDEO_SAVED = 201;
    private static final int IMAGE_SAVED = 202;
    
    private Uri fileUri;
    private File selectedFile;
    private String selectedFileName;
    private String selectedFileType;
    private String answerID;

    private LoginFragment lf;
    private AsyncTask S3 = null;
    private AsyncTask hp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        upload = new Dialog(getActivity());

        upload.setContentView(R.layout.answer_upload_fragment);
        upload.setTitle("Lisää vastaus tehtävään");

        //Create button that after clicking leads the user to record a video using the device's camera
        Button newVideoButton =
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

                // Creates a file for saving the shot video VID + timestamp + .mp4 TODO: Check if possible to use WebM in device, NEVER expect that the recorded video is of a certain format.
                // TODO: the name should contain task.
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
        });

        // Creates a button that after clicking leads the user to upload a video from the device's gallery.
        Button existingVideoButton =
                (Button) upload.findViewById(R.id.existingVideoButton);
        existingVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
                pickMedia.setType("video/*");
                startActivityForResult(pickMedia, VIDEO_SAVED);
            }
        });

        //Create button that after clicking leads the user to take a picture using the device's camera.
        Button newImageButton =
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
                selectedFileName = selectedFile.getName();

                // Create a new Intent to shoot image and save the result to the file specified earlier
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(selectedFile);

                // Start the intent using the device's own camera software
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, IMAGE_CAPTURE);
            }
        });
        // Creates a button that opens the device's image gallery
        Button existingImageButton =
                (Button) upload.findViewById(R.id.existingImageButton);
        existingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
                pickMedia.setType("image/*");
                startActivityForResult(pickMedia, IMAGE_SAVED);
            }
        });

        // Creates a button that closes the dialog
        Button cancelButton =
                (Button) upload.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.dismiss();
            }
        });
    }

            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                // Result handler for videos from the gallery
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == VIDEO_SAVED) {
                    if (resultCode == TaskActivity.RESULT_OK) {

                        // Get the selected file's Uri, name and the file itself
                        Uri selectedVideoLocation = data.getData();

                        // Mikä ihme kovakoodattu tiesdosto tämä on?
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        selectedFileName = "VID_" + timeStamp + ".mp4";

                        selectedFile = new File(selectedVideoLocation.getLastPathSegment(), selectedFileName);

                        Log.i("filename", selectedFileName);
                        Log.i("location", selectedVideoLocation.toString());
                        Log.i("file", selectedFile.getName());

                        // Initialize the Amazon Cognito credentials provider
                        //if(selectedFile.exists()) {
                        Log.i("lataa", "lataa");
                        String url = StatusService.StaticStatusService.sc.StartAnswerUpload
                                ("taskId", StatusService.StaticStatusService.currentSubUserID, selectedFileName.substring(selectedFileName.lastIndexOf(".")).toLowerCase()); //TODO: Check if there is subuser in use! Get task.
                        hp = new HTTPSRequester(new GotUrlToUpload()).execute(url);
                        //}
                    }
                }
                // Result handler for videos from the camera
                else if (requestCode == VIDEO_CAPTURE) {
                    if (resultCode == TaskActivity.RESULT_OK) {
                        //Toast t = Toast.makeText(CameraFragment.this.getContext(), "Video has been saved to:\n" +
                        //        data.getData(), Toast.LENGTH_LONG);
                        //t.show();-->
                        // Initialize the Amazon Cognito credentials provider

                        if (selectedFile.exists()) {
                            String url = StatusService.StaticStatusService.sc.StartAnswerUpload
                                    ("taskId", StatusService.StaticStatusService.currentSubUserID, selectedFileName.substring(selectedFileName.lastIndexOf(".")).toLowerCase()); //TODO: Check if there is subuser in use! Get task.
                            hp = new HTTPSRequester(new GotUrlToUpload()).execute(url);
                        }

                    } else if (resultCode == TaskActivity.RESULT_CANCELED) {
                        Toast.makeText(CameraFragment.this.getActivity(), "Videon ottaminen peruttu.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CameraFragment.this.getActivity(), "Videon ottaminen epäonnistui.",
                                Toast.LENGTH_LONG).show();
                    }
                }
                // Result handler for iamges from the gallery
                else if (requestCode == IMAGE_SAVED) {
                    if (resultCode == TaskActivity.RESULT_OK) {

                        // Get the selected file's Uri, name and the file itself
                        Uri selectedVideoLocation = data.getData();

                        // Mikä ihme kovakoodattu tiesdosto tämä on? D: Tein samantyylinsen kun näemmäs tämä oikeasti ei toimi kuten pitää...
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        selectedFileName = "VID_" + timeStamp + ".JPEG";

                        selectedFile = new File(selectedVideoLocation.getLastPathSegment(), selectedFileName);

                        Log.i("filename", selectedFileName);
                        Log.i("location", selectedVideoLocation.toString());
                        Log.i("file", selectedFile.getName());

                        // Initialize the Amazon Cognito credentials provider
                        //if(selectedFile.exists()) {
                        Log.i("lataa", "lataa");
                        String url = StatusService.StaticStatusService.sc.StartAnswerUpload
                                ("taskId", StatusService.StaticStatusService.currentSubUserID, selectedFileName.substring(selectedFileName.lastIndexOf(".")).toLowerCase()); //TODO: Check if there is subuser in use! Get task.
                        hp = new HTTPSRequester(new GotUrlToUpload()).execute(url);
                        //}
                    }
                } else if (requestCode == IMAGE_CAPTURE) {
                    if (resultCode == TaskActivity.RESULT_OK) {
                        //Toast t = Toast.makeText(CameraFragment.this.getContext(), "Video has been saved to:\n" +
                        //        data.getData(), Toast.LENGTH_LONG);
                        //t.show();-->
                        // Initialize the Amazon Cognito credentials provider

                        if (selectedFile.exists()) {
                            String url = StatusService.StaticStatusService.sc.StartAnswerUpload
                                    ("taskId", StatusService.StaticStatusService.currentSubUserID, selectedFileName.substring(selectedFileName.lastIndexOf(".")).toLowerCase()); //TODO: Check if there is subuser in use! Get task.
                            hp = new HTTPSRequester(new GotUrlToUpload()).execute(url);
                        }

                    } else if (resultCode == TaskActivity.RESULT_CANCELED) {
                        Toast.makeText(CameraFragment.this.getActivity(), "Kuvan ottaminen peruttu.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CameraFragment.this.getActivity(), "Kuvan ottaminen epäonnistui.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            public void upload(String answerUri) {
                S3 = new S3Upload(new S3uploadFinished(), getContext(), selectedFile).execute(answerUri);
            }
        }
