package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksFragment extends Fragment implements View.OnClickListener {


    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            tasks2(response);
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

    private void tasks(String response) {
        //Tässä jo?, välttämättä ei laitteessa. -> else osaan.
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
                imageName = "task-icon" + tasks.get(i).get("id");
                if(!StatusService.StaticStatusService.fh.checkIfImageExists(imageName)) {
                    names.add(imageName);
                }
                    
            }
            
            
            //Either all images are in memory or some must be downloaded from S3.
            if (!names.isEmpty()) {
                //NOTE: The code works only as simple if S3 has saved the the needed images in a single bucket with the same naming convency.
                new S3Download(new taskImgsDownloaded(), names).execute();
            } 
            else {
                ImageButton[] taskbutton = new ImageButton[tasks.size()];
                for (int i = 0; i < tasks.size(); i++) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getDataDirectory() + "/" + "task-icon" + tasks.get(i).get("id"));
                        
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

        LinearLayout category = (LinearLayout) view.findViewById(R.id.category_icon);
        category.setOrientation(LinearLayout.VERTICAL);
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView categoryButton = new ImageView(getContext());
        String categoryIcon = "category_icon" + getArguments().getString("category");
        int categoryImageId = getResources().getIdentifier(categoryIcon, "drawable", getActivity().getApplicationContext().getPackageName());
        categoryButton.setImageResource(categoryImageId);
        category.addView(categoryButton);

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.tasks);
        ll.setOrientation(LinearLayout.VERTICAL);
        //LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        StatusService.StaticStatusService.jc.newJson(response);
        ArrayList<HashMap<String, String>> tasks = StatusService.StaticStatusService.jc.getObjects();

        TableLayout tl = (TableLayout) view.findViewById(R.id.columns);
        tl.setOrientation(TableLayout.VERTICAL);
        TableLayout.LayoutParams tlp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow tr1 = new TableRow(getContext());
        TableRow tr2 = new TableRow(getContext());
        tr1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tr2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tl.addView(tr1);
        tl.addView(tr2);

        ImageButton[] taskbutton = new ImageButton[tasks.size()];
        //for (int i = 0; i < tasks.size(); i++) {
        for (int i = 0; i < 10; i++) {
            try {
                //Bitmap bitmap = BitmapFactory.decodeFile(Environment.getDataDirectory() + "/" + "task-icon" + tasks.get(i).get("id"));
                String image; // = "task_icon" + tasks.get(i).get("id");
                if (i % 2 == 1) image = "task_icon1";
                else image = "task_icon2";
                int imageID = getResources().getIdentifier(image, "drawable", getActivity().getApplicationContext().getPackageName());
                taskbutton[i] = new ImageButton(getContext());
                taskbutton[i].setImageResource(imageID);
                taskbutton[i].setLayoutParams(tlp);
                taskbutton[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
                taskbutton[i].setOnClickListener(this);
                taskbutton[i].setBackgroundColor(Color.TRANSPARENT);
                taskbutton[i].setId(Integer.parseInt(tasks.get(i).get("id")));
                Log.i("onnistui", String.valueOf(i));
                if (i % 2 == 1) tl.addView(taskbutton[i], tlp);
                else tl.addView(taskbutton[i], tlp);
            } catch (Exception e) {
                Log.i("kuvavirhe", "");
            }
        }
            
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tasks_fragment, container, false);

        String url = StatusService.StaticStatusService.sc.DescribeCategoryTasks("1");
        Log.i("urli", url);
        hp = new HTTPSRequester(new listener()).execute(url);

        return view;
    }

    @Override
    public void onClick(View v) {
        String id = Integer.toString(v.getId());
        ((CategoryActivity) getActivity()).startTask(id);
    }
    
}
