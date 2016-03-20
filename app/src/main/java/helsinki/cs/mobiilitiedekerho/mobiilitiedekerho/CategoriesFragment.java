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

public class CategoriesFragment extends Fragment implements View.OnClickListener {

    public class categorieslistener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            categories(response);
        }
    }
    
    public class catImgsDownloaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            categories2(response);
        }
    }

    View view;
    AsyncTask hp = null;

    private void categories(String response) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.categories);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        StatusService.StaticStatusService.jc.newJson(response);
        ArrayList<HashMap<String, String>> categories = StatusService.StaticStatusService.jc.getObjects();
        
        if (!categories.isEmpty()) {
            ArrayList<String> names = new ArrayList<String>();
            String imageName;
            for (int i = 0; i < categories.size(); i++) {
                imageName = "category-icon" + categories.get(i).get("id");
                if(!StatusService.StaticStatusService.fh.checkIfImageExists(imageName)) {
                    names.add(imagename);
                }
                    
            }
            
            
            //Either all images are in memory or some must be downloaded from S3.
            if (!names.isEmpty()) {
                //NOTE: The code works only as simple if S3 has saved the the needed images in a single bucket with the same naming convency.
                new S3Download(catImgsDownloaded, names).execute();
            } 
            else {
                ImageButton[] categorybutton = new ImageButton[categories.size()];
                for (int i = 0; i < categories.size(); i++) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getDataDirectory() + "/" + "category-icon" + categories.get(i).get("id"));
                        
                        categorybutton[i] = new ImageButton(getContext());
                        categorybutton[i].setImageBitmap(bitmap);
                        categorybutton[i].setLayoutParams(lp);
                        categorybutton[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
                        categorybutton[i].setOnClickListener(this);
                        categorybutton[i].setBackgroundColor(Color.TRANSPARENT);
                        categorybutton[i].setId(Integer.parseInt(categories.get(i).get("id")));
                        
                        //categorybutton[i].setId(i+1);
                        ll.addView(categorybutton[i], lp);
                    } catch (Exception e) {
                        Log.i("kuvavirhe", "");
                    }
                }
            }
        }
        else {
            //TODO: Something showing that there are no categories??? Not gonna happen! (Except of because a problem.)
        }
    }
    
    
    private void categories2(String response) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.categories);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        StatusService.StaticStatusService.jc.newJson(response);
        ArrayList<HashMap<String, String>> categories = StatusService.StaticStatusService.jc.getObjects();
        
        ImageButton[] categorybutton = new ImageButton[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getDataDirectory() + "/" + "category-icon" + categories.get(i).get("id"));
                
                categorybutton[i] = new ImageButton(getContext());
                categorybutton[i].setImageBitmap(bitmap);
                categorybutton[i].setLayoutParams(lp);
                categorybutton[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
                categorybutton[i].setOnClickListener(this);
                categorybutton[i].setBackgroundColor(Color.TRANSPARENT);
                categorybutton[i].setId(Integer.parseInt(categories.get(i).get("id")));
                
                //categorybutton[i].setId(i+1);
                ll.addView(categorybutton[i], lp);
            } catch (Exception e) {
                Log.i("kuvavirhe", "");
            }
        }
            
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.categories_fragment, container, false);

        String url = StatusService.StaticStatusService.sc.DescribeCategories();
        hp = new HTTPSRequester(new categorieslistener()).execute(url);

        return view;
    }

    @Override
    public void onClick(View v) {
        String id = Integer.toString(v.getId());
        Log.i("kuvaid", id);
        ((CategoriesActivity) getActivity()).startCategory(id);
    }
}