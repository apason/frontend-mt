package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksFragment extends Fragment implements View.OnClickListener {

    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            tasks(response);
        }
    }
    
    public class taskImgsDownloaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            tasks2(response);
        }
    }

    private View view;
    private AsyncTask hp = null;
    private ArrayList<HashMap<String, String>> tasks;
    private String categoryId;

    private void tasks(String response) {

        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            tasks = StatusService.StaticStatusService.jc.getObjects();

            if (!tasks.isEmpty()) {
                ArrayList<String> names = new ArrayList<String>();
                String imageName;
                for (int i = 0; i < tasks.size(); i++) {
                    imageName = "task_icon" + tasks.get(i).get("id");
                    if(!StatusService.StaticStatusService.fh.checkIfImageExists(imageName)) {
                        names.add(imageName);
                    }
                }
                
                //Either all images are in memory or some must be downloaded from S3.
                if (!names.isEmpty()) {
                    //NOTE: The code works only as simple if S3 has saved the the needed images in a single bucket with the same naming convention.
                    new S3Download(new taskImgsDownloaded(), names).execute();
                } 
                else {
                    drawImages();
                }
            }
            else {
                //TODO: Something showing that no tasks belongs to the category.
            }
        }
        //TODO: else?
    }
    
    private void tasks2(String response) {
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
                if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                    tasks = StatusService.StaticStatusService.jc.getObjects();

                    drawImages();
                }
        //TODO: else?
    }

    //draws imageebuttons with task video thumbnails
    private void drawImages() {
        LinearLayout category = (LinearLayout) view.findViewById(R.id.category_icon);
        Bitmap bm = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "category_icon" + categoryId);
        ImageView categoryImage = new ImageView(getContext());
        categoryImage.setImageBitmap(Bitmap.createScaledBitmap(bm, 300, 300, false));
        categoryImage.setBackgroundColor(Color.TRANSPARENT);
        category.addView(categoryImage);

        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.tasks);

        ImageButton[] taskbutton = new ImageButton[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            try {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                Bitmap bitmap = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "task_icon" + tasks.get(i).get("id"));
                taskbutton[i] = new ImageButton(getContext());
                taskbutton[i].setImageBitmap(bitmap.createScaledBitmap(bitmap, 300, 300, false));
                taskbutton[i].setLayoutParams(lp);
                taskbutton[i].setOnClickListener(this);
                taskbutton[i].setBackgroundColor(Color.TRANSPARENT);
                taskbutton[i].setId(Integer.parseInt(tasks.get(i).get("id")));
                lp.leftMargin = 400*(i%2)+500;
                lp.topMargin = 400*((int)i/2);
                rl.addView(taskbutton[i], lp);
            } catch (Exception e) {
                Log.e("Image error", e.toString());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tasks_fragment, container, false);

        categoryId = getArguments().getString("category");
        String url = StatusService.StaticStatusService.sc.DescribeCategoryTasks(categoryId);
        hp = new HTTPSRequester(new listener()).execute(url);

        return view;
    }

    @Override
    public void onClick(View v) {
        String id = Integer.toString(v.getId());
        ((CategoryActivity) getActivity()).startTask(id);
    }
    
}
