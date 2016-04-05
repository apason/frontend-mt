package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Activity to create new sub-users.
 */
public class SubUserActivity extends AppCompatActivity {

    View view;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView iv;
    String subUserNick;

    /**
     * A listener that checks if saving sub-user worked out and notifies the user of the result.
     */
    public class SubListener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            Log.i("subia", response);
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                Toast.makeText(SubUserActivity.this, "Alikäyttäjän luonti onnistui.",
                        Toast.LENGTH_LONG).show();
                SubUserActivity.this.onBackPressed();
            }
            else {
                Toast.makeText(SubUserActivity.this, "Alikäyttäjän luonti ei onnistunut.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_user_activity);

        final EditText subuserNickField = (EditText) findViewById(R.id.nickname_field);

        Button cameraButton = (Button) findViewById(R.id.thumbnailbutton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the phones camera and start a new intent to take a picture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(SubUserActivity.this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button saveSubUserButton = (Button) findViewById(R.id.savesubuserbutton);
        saveSubUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subUserNick = subuserNickField.getText().toString();
                saveSubUser(subUserNick);
            }
        });

        iv = (ImageView) findViewById(R.id.thumbnailpreview);
    }

    public void saveSubUser(String subuser) {
        String url = StatusService.StaticStatusService.sc.CreateSubUser(subuser);
        new HTTPSRequester(new SubListener()).execute(url);
    }

    /**
     * If the image capture is successful show the image in the ImageView iv
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == SubUserActivity.this.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv.setImageBitmap(imageBitmap);
        }
    }
}
