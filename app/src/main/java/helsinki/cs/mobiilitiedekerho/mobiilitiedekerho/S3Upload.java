package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
// import android.util.Log;

/**
 * A class for uploading videos to S3.
 */
public class S3Upload extends AsyncTask<String, Void, String> {

    private TaskCompleted act;
    private File selectedFile;
    private Context context;
    
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
            "HIDDEN", // Identity Pool ID
            Regions.EU_WEST_1 // Region
        );
        // Create an S3 client
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.EU_WEST_1));

        TransferUtility transferUtility = new TransferUtility(s3, context);

        TransferObserver observer = transferUtility.upload(
            "p60v4ow30312-answers",     /* The bucket to upload to */
            selectedFileName[0],    /* The key for the uploaded object */
            selectedFile        /* The file where the data to upload exists */
        );
        
        return "success";
    }
    
    protected void onPostExecute(String result) {
        act.taskCompleted(result);
    }
    
}