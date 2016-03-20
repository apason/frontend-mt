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

    View view;
    AsyncTask hp = null;

    private void tasks(String response) {
        //Tässä jo, välttämättä ei laitteessa. -> else osaan.
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.tasks);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //

        StatusService.StaticStatusService.jc.newJson(response);
        ArrayList<HashMap<String, String>> tasks = StatusService.StaticStatusService.jc.getObjects();

        if (!tasks.isEmpty()) {
            ArrayList<String> names = new ArrayList<String>();
            String imageName;
            for (int i = 0; i < tasks.size(); i++) {
                imageName = "task" + tasks.get(i).get("id");
                if(!StatusService.StaticStatusService.fh.checkIfImageExists(imageName)) {
                    names.add(imagename);
                }
                    
            }
            
            
            //Either all images are in memory or some must be downloaded from S3.
            if (!names.isEmpty()) {
                //NOTE: The code works only as simple if S3 has saved the the needed images in a single bucket with the same naming convency.
                new S3Download(taskImgsDownloaded, names).execute();
            } 
            else {
                ImageButton[] taskbutton = new ImageButton[tasks.size()];
                for (int i = 0; i < tasks.size(); i++) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getDataDirectory() + "/" + "task" + tasks.get(i).get("id"));
                        
                        taskbutton[i] = new ImageButton(getContext());
                        taskbutton[i].setImageBitmap(bitmap);
                        taskbutton[i].setLayoutParams(lp);
                        taskbutton[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
                        taskbutton[i].setOnClickListener(this);
                        taskbutton[i].setBackgroundColor(Color.TRANSPARENT);
                        taskbutton[i].setId(Integer.parseInt(tasks.get(i).get("id")));
                        
                        //taskbutton[i].setId(i+1);
                        ll.addView(taskbutton[i], lp);
                    } catch (Exception e) {
                        Log.i("kuvavirhe", "");
                    }
                }
            }
        }
        else {
            //TODO: Something showing that no tasks belongs to the category.
        }
    }
    
    private void tasks2(String response) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.tasks);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        StatusService.StaticStatusService.jc.newJson(response);
        ArrayList<HashMap<String, String>> tasks = StatusService.StaticStatusService.jc.getObjects();
        
        ImageButton[] taskbutton = new ImageButton[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getDataDirectory() + "/" + "task" + tasks.get(i).get("id"));
                
                taskbutton[i] = new ImageButton(getContext());
                taskbutton[i].setImageBitmap(bitmap);
                taskbutton[i].setLayoutParams(lp);
                taskbutton[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
                taskbutton[i].setOnClickListener(this);
                taskbutton[i].setBackgroundColor(Color.TRANSPARENT);
                taskbutton[i].setId(Integer.parseInt(tasks.get(i).get("id")));
                
                //taskbutton[i].setId(i+1);
                ll.addView(taskbutton[i], lp);
            } catch (Exception e) {
                Log.i("kuvavirhe", "");
            }
        }
            
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tasks_fragment, container, false);

        String url = StatusService.StaticStatusService.sc.DescribeCategoryTasks("1");
        hp = new HTTPSRequester(new listener()).execute(url);

        return view;
    }

    @Override
    public void onClick(View v) {
        String id = Integer.toString(v.getId());
        ((CategoryActivity) getActivity()).startTask(id);
    }
    
}
