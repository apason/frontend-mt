package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.os.AsyncTask;
// import android.util.Log;

/**
 * A class for uploading videos to S3.
 */
protected class S3Upload extends AsyncTask<String, void, String> {

    private TaskCompleted act;
    private File selectedFile;
    
    /**
    * Constructor for S3Upload.
    * @param act a interface for being able to pass the response for the calling ativity.
    */
    public S3Upload(TaskCompleted act, File selectedFile){
        this.act = act;
        this.selectedFile = selectedFile;
    }
    
    
    protected void doInBackground(String... selectedFileName) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
            getContext(),
            "HIDDEN", // Identity Pool ID
            Regions.EU_WEST_1 // Region
        );
        // Create an S3 client
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.EU_WEST_1));

        TransferUtility transferUtility = new TransferUtility(s3, getContext());

        TransferObserver observer = transferUtility.upload(
            "p60v4ow30312-answers",     /* The bucket to upload to */
            selectedFileName,    /* The key for the uploaded object */
            selectedFile        /* The file where the data to upload exists */
        );
        
        return "success";
    }
    
    protected void onPostExecute(String result) {
        act.taskCompleted(result);
    }
    
}