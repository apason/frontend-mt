package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

/**
import android.app.Activity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


public class DatabaseControl {

    // Initialize the Amazon Cognito credentials provider
    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "eu-west-1:12f5ef41-092f-4106-a9f5-51297d208cb1", // Identity Pool ID
                Regions.EU_WEST_1 // Region
        );
        return credentialsProvider;
    }

    public TransferUtility getTransferUtility() {
        // Create an S3 client
        AmazonS3 s3 = new AmazonS3Client(getCredentialsProvider());

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.EU_WEST_1));

        TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());

        return transferUtility;
    }
} */




