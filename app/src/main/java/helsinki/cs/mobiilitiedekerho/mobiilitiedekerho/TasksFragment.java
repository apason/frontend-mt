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

/**
 *  A class responsible for taking care of the individual tasks inside a category.
 */
public class TasksFragment extends Fragment implements View.OnClickListener {


    private View view;
    private String categoryId;
    private ArrayList<HashMap<String, String>> tasks;
    private ArrayList<String> names; //Save images to be downloaded & saved for error checking.
    private ArrayList<String> urls; //Save urls to be used for downloading for error checking.
    private boolean triedAlready = false;
    
    private AsyncTask hp = null;

    
    /**
     * A listener that takes the response of the DescribeTasksByCategory-API-call and calls the tasks-method with the response.
     * Just that.
     */
    public class listener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            tasks(response);
        }
    }

    /**
     * A listener that takes the response of S3Upload and if succes it calls drawImages-method, otherwise checkErrors-method.
     * Just that.
     */
    public class taskImgsDownloaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            if (response.equals("success")) drawImages(); //All went right, proceed to draw images.
            else checkErrors(response);
        }
    }

    /**
     * A listener that checks if the images that could not be downlaoded last time ahd been downloaded right this time.
     * TODO: This method does nothing at the moment actually, makes some error handling.
     */
    public class restOfImgsDownloaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            if (response.equals("success")) drawImages(); //All went right this time, proceed to draw images.
            else if (response.equals("failure")) {
                //This is actually a communication error. TODO: Should try again?
            }
            else {
                //Could not get the rest (or all) of the images (that is the ones that couldn't be gotten before)
                // TODO: What to do? Notice the user or just skip these tasks?
                
                // if it desired to just skip the tasks in question, implement the code below.
                // String[] tg = response.split(":");
                // Remove from names the ones in tg.
                // (Finally in drawImages() do not read the ones not supposed to be read... :D)
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

    /**
     * Checks if the tasks' icons are already in the device and if not it downloads the ones that are missing.
     * If no missing it calls drawImages() if some missing it calls S3Download correctly.
     * @param response contains the response form the server to the API-call DescribeTasksByCategory.
     */
    private void tasks(String response) {
        Log.i("responssi", response);
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            tasks = StatusService.StaticStatusService.jc.getObjects();

            if (!tasks.isEmpty()) {
                names = new ArrayList<String>();
                urls = new ArrayList<String>(); // The urls to be retrieved.
                String imageName;
                for (int i = 0; i < tasks.size(); i++) {
                    //imageName = "task_icon_id_" + tasks.get(i).get("id") + ".png";
                    if(!StatusService.StaticStatusService.fh.checkIfImageExists("task_icon_id_" + tasks.get(i).get("id") + ".png")) {
                        names.add("task_icon_id_" + tasks.get(i).get("id") + ".png");
                        urls.add(tasks.get(i).get("icon_uri"));
                    }
                }

                //Either all images are in memory or some must be downloaded from S3.
                if (!names.isEmpty()) {
                    //NOTE: saves the images to memery only based in hard-coded text + ID.
                    new S3Download(new taskImgsDownloaded(), names, urls).execute();
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

    /**
     * Checks which erros has happened in S3Download.
     * If "failure" it tries again if not already; if names and url don't match in size then nothing for now.
     * At last it does try to get again the images that could not be got last time.
     * @param response the Response from S3Download.
     */
    private void checkErrors(String response) {
        // Communicating with S3 failed, try again.
        if (response.equals("failure")) {
            if (!triedAlready) {
                triedAlready = true;
                new S3Download(new taskImgsDownloaded(), names, urls).execute();
            }
            else {
                //TODO: Show that some graphics could not be downloaded from S3 to user in some way.
            }
        }
        else if (response.equals("'ImageNames' and 'urlss' don't match in size")) {
            // This really should not happen except for an error when calling. In that case fix that.
        }
        // Some images could not be downloaded & saved, try again them.
        else {
            ArrayList<String> toGetAgain = new ArrayList<String>();
            ArrayList<String> urlsAgain = new ArrayList<String>();
            
            String[] tg = response.split(":");
            for (int i = 0 ; i < tg.length ; i++) {
                toGetAgain.add(names.get(Integer.valueOf(tg[i])));
                urlsAgain.add(urls.get(Integer.valueOf(tg[i])));
            }

            new S3Download(new restOfImgsDownloaded(), toGetAgain, urlsAgain).execute();
        }
    }

    /**
    * Draws imagebuttons with task video thumbnails and sets to the upper-left corner the category-icon too.
    */
    private void drawImages() {
        LinearLayout category = (LinearLayout) view.findViewById(R.id.category_icon);
        try {
            Bitmap bm = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "category_icon_id_" + categoryId + ".png");
            ImageView categoryImage = new ImageView(getContext());
            categoryImage.setImageBitmap(Bitmap.createScaledBitmap(bm, 300, 300, false));
            categoryImage.setBackgroundColor(Color.TRANSPARENT);
            category.addView(categoryImage);
        }
        catch (Exception e) {
            Log.e("Image error", e.toString());
        }

        createCategoryButtons();
    }

    /**
    * Draws the task-icons to the screen.
    * TODO: In the future it must fit the icons to a grid and place them based in the coordinates (that would be of the grid's).
    */
    private void createCategoryButtons() {
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.tasks);

        ImageButton[] taskbutton = new ImageButton[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            try {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                long start = System.currentTimeMillis();
                long timer = 0;

                while (!StatusService.StaticStatusService.fh.checkIfImageExists("task_icon_id_" + tasks.get(i).get("id") + ".png") && timer < 5000) {
                    timer = System.currentTimeMillis()-start;
                }

                Bitmap bitmap = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "task_icon_id_" + tasks.get(i).get("id") + ".png");
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
    public void onClick(View v) {
        String id = Integer.toString(v.getId());
        ((CategoryActivity) getActivity()).startTask(id);
    }
    
}
