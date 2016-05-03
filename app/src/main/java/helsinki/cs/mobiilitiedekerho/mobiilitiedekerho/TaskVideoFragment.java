package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;


/**
* Deals with the taskVideo or something.
*/
public class TaskVideoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private AsyncTask hp;
    private String taskURL;

    /**
    * Listener that pass VideoStreaming initialization to task-method.
    */
    public class Listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            task(response);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.task_video_fragment, container, false);

        String id = getArguments().getString("task");
        LinearLayout category = (LinearLayout) view.findViewById(R.id.taskbutton);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity= Gravity.CENTER;
        Log.i("leveys", Integer.toString(category.getLayoutParams().height));
        Log.i("leveys", Integer.toString(category.getLayoutParams().width));

        try {
            Bitmap bm = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "task_icon_id_" + id + ".png");
            ImageView categoryImage = new ImageView(getContext());


            final float scale = getContext().getResources().getDisplayMetrics().density;
            categoryImage.setImageBitmap(Bitmap.createScaledBitmap(bm, (int) (500 * scale + 0.5f), (int) (500 * scale + 0.5f), false));
            categoryImage.setBackgroundColor(Color.TRANSPARENT);
            categoryImage.setLayoutParams(lp);
            category.setBackgroundColor(Color.BLACK);
            category.addView(categoryImage);
            category.setOnClickListener(this);
        }
        catch (Exception e) {
            Log.e("Image error", e.toString());
        }
        return view;
    }

    @Override
    public void onClick(View v){

        //task_id from TaskActivity.java:
        String id = getArguments().getString("task");

        // String taskVideo = ServiceCommunication.DescribeTask(id);
        String url = StatusService.StaticStatusService.sc.DescribeTask(id);
        hp = new HTTPSRequester(new Listener()).execute(url);
    }

    /**
    * Makes VideoScreen be called to stream the desired video.
    * @param response the response of the API-call DescribeTask.
    */
    private void task(String response) {
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            ArrayList<HashMap<String, String>> task = StatusService.StaticStatusService.jc.getObjects();
            taskURL = task.get(0).get("uri");
            Log.i("taskURL", taskURL);
            ((TaskActivity) getActivity()).playback(taskURL, "video");
        }
        //TODO: else?
    }
}
