package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Fragment to be used inside UserActivity to create new sub-users.
 */
public class SubUserFragment extends Fragment {

    View view;
    String subusernameTV;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView iv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.sub_user_fragment, null);

        final EditText subuserNick = (EditText) view.findViewById(R.id.nickname_field);

        Button cameraButton = (Button) view.findViewById(R.id.thumbnailbutton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the phones camera and start a new intent to take a picture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button saveSubUserButton = (Button) view.findViewById(R.id.savesubuserbutton);
        saveSubUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Save the selected image and sub user nickname to sql database
                subusernameTV = subuserNick.getText().toString();
            }
        });

        iv = (ImageView) view.findViewById(R.id.thumbnailpreview);


        return view;
    }

    /**
     * If the image capture is successful show the image in the ImageView iv
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv.setImageBitmap(imageBitmap);
        }
    }
}