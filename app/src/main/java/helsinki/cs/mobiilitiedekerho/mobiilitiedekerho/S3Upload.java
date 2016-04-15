package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.File;

/**
 * A class for uploading videos to S3.
 */
public class S3Upload extends AsyncTask<String, Void, String> {

    private TaskCompleted act;
    private File selectedFile;
    private Context context;
    
    private String outcome = "succes";
    
    /**
    * Constructor for S3Upload.
    * @param act a interface for being able to pass the response for the calling ativity.
    */
    public S3Upload(TaskCompleted act, Context context, File selectedFile){
        this.act = act;
        this.context = context;
        this.selectedFile = selectedFile;
    }
    
    
    protected String doInBackground(String... selectedFileName) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
            context,
            "LISÄÄ TÄHÄN IDENTITY POOL ID", // Identity Pool ID
                Regions.EU_CENTRAL_1 // Region
        );
        // Create an S3 client
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        
        
        //Sets the metadata to the file to be uploaded.
        ObjectMetadata metadata = new ObjectMetadata();
        
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        //TODO: There is no need for the extension part ".type" actually (except for Windows) so possibly there could be a viable file without its type written in the file's name.
        //Also the extension may or may not tell the truth about the file's type. (No need to take into account?)
        //In android it is possible to use "android.media.MediaMetadataRetriever" for reading the file's metadata and get the real type. TODO: Do this to the File selectedFile, Note: this is not very favored for some reason, why?
        String ext = selectedFileName[0].substring(selectedFileName[0].lastIndexOf(".")).toLowerCase(); //toLowerCase in the (odd) case of the extension being in UpperCase, else MimeType may not recognize it.
        String type = mime.getMimeTypeFromExtension(ext); //Gets the Mime type corresponding to the extension. E.G: mp4 -> video/mp4
        
        metadata.setContentType(type);
        metadata.setContentDisposition("inline");

        
        TransferUtility transferUtility = new TransferUtility(s3, context);

        TransferObserver observer = transferUtility.upload(
            StatusService.StaticStatusService.answerBucket,     /* The bucket to upload to */
            selectedFileName[0],                                /* The key for the uploaded object */
            selectedFile,                                       /* The file where the data to upload exists */
            metadata                                            /* The metadata (HTTP-header stuff) for the file to be uploaded */
        );

        observer.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.i("dev", state.name());
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.i("dev", bytesCurrent + "/" + bytesTotal + "-" + ((float) bytesCurrent / (float) bytesTotal) * 100);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.i("Amazon", "Error on amazon service ! " + ex);
                outcome = "failure";
            }
        });

        return outcome;
    }
    
    protected void onPostExecute(String result) {
        act.taskCompleted(result);
    }
    
}