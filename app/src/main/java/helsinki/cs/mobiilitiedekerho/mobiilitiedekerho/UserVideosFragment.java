package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class UserVideosFragment extends Fragment implements View.OnClickListener {

    private Dialog list = null;
    private View view;
    private ImageButton userVideosButton;
    private ArrayList<HashMap<String, String>> uservideos;
    
    private AsyncTask hp = null;

    public class FetchUserVideos implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            openUserVideoDialog(response);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_videos_button_fragment, null);
        userVideosButton =
                (ImageButton) view.findViewById(R.id.user_videos_button);
        userVideosButton.setOnClickListener(this);
        return view;
    }

    // When loginButton is pressed call method openLoginDialog
    @Override
    public void onClick(View v) {

        String url = StatusService.StaticStatusService.sc.DescribeSubUserAnswers(StatusService.StaticStatusService.currentSubUserID);
        hp = new HTTPSRequester(new FetchUserVideos()).execute(url);
    }

    public void openUserVideoDialog(String response) {
        if(StatusService.StaticStatusService.currentSubUserID == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Videolaatikko on tyhjä");
            alert.setMessage("Voidaksesi katsella omia vastauksiasi, sinun on kirjauduttava ja/tai luotava käyttäjätunnus");
            alert.setNegativeButton("Sulje", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }

        list = new Dialog(UserVideosFragment.this.getActivity());
        // Set GUI of login screen
        list.setContentView(R.layout.user_videos_fragment);
        list.setTitle("OMAT VIDEOSI");

        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            uservideos = StatusService.StaticStatusService.jc.getObjects();
            if (!uservideos.isEmpty()) {
                drawImages();
            }

            // On click of cancel button close the dialog
            Button closePopupButton = (Button) list.findViewById(R.id.cancel_button);
            closePopupButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.dismiss();
                }

            });

            // Force the dialog to the right size
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(list.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            list.show();
            list.getWindow().setAttributes(lp);
        }
    }

    //draws buttons with user video thumbnails
    private void drawImages() {

        RelativeLayout rl = (RelativeLayout) list.findViewById(R.id.uservideos);

        //ImageButton[] videobutton = new Button[uservideos.size()]; <-- use this with thumbnail images
        Button[] videobutton = new Button[uservideos.size()];

        for (int i = 0; i < uservideos.size(); i++) {
            try {
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //TODO: thumbnail images downloaded from S3
                //Bitmap bitmap = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "task_icon" + uservideos.get(i).get("id"));
                //videobutton[i] = new ImageButton(getContext());
                videobutton[i] = new Button(getContext());
                videobutton[i].setHeight(300);
                videobutton[i].setWidth(400);
                //videobutton[i].setImageBitmap(bitmap.createScaledBitmap(bitmap, 300, 300, false));
                videobutton[i].setLayoutParams(rlp);

                //videobutton[i].setId(Integer.parseInt(uservideos.get(i).get("id")));
                final String url = uservideos.get(i).get("uri");
                final String answerType = uservideos.get(i).get("answer_type");
                videobutton[i].setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             ((MainActivity) getActivity()).playback(url, answerType);
                        }});
                //taskbutton[i].setBackgroundColor(Color.TRANSPARENT); <-- use this with thumbnail images
                videobutton[i].setBackgroundColor(Color.BLACK);
                videobutton[i].setTextColor(Color.WHITE);
                videobutton[i].setText(uservideos.get(i).get("uri"));
                rlp.leftMargin = 500*(i%2)+100;
                rlp.topMargin = 400*((int)i/2)+100;
                rl.addView(videobutton[i], rlp);
                //rl.addView(videobutton[i], rlp);
            } catch (Exception e) {
                Log.e("Image error", e.toString());
            }
        }
    }
}
