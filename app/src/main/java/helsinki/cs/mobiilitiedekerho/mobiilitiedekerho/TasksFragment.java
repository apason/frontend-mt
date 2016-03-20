package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
                //TODO: Which bucket name to use, hard-code it to S3Download?
                new S3Download(taskImgsDownloaded, names).execute(kuvien_urlt_s3ssa_mitä_ei_löydy_kännykästä); //TODO: päätä asia!
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


/*  LISTA STAATTISIA LUOTUJA KUVAKKEITA GUI:n TESTAUSTA VARTEN:
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.tasks);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageButton[] taskbutton = new ImageButton[2];
        for (int i = 0; i < 2; i++) {
            taskbutton[i] = new ImageButton(getContext());
            String image = "";
            if (i == 0) image = "task1";
            if (i == 1) image = "task2";
            // gets image resource from drawable folder:
            int resID = getResources().getIdentifier(image, "drawable", getActivity().getApplicationContext().getPackageName());
            taskbutton[i].setImageResource(resID);
            taskbutton[i].setLayoutParams(lp);
            taskbutton[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
            taskbutton[i].setOnClickListener(this);
            taskbutton[i].setBackgroundColor(Color.TRANSPARENT);
            //taskbutton[i].setTag(i); = TURHA RIVI?
            taskbutton[i].setId(i);
            // lopulliseen versioon ServerCommunicationista getID:llä tai vastaavalla metodilla: taskbutton[i].setId(tasks.getId())
            ll.addView(taskbutton[i], lp);
        }
*/

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
