package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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
            if (response.equals("succes")) categories2(response);
            else if (response.equals("failure")) categories2(response)/*TODO: Try again?*/;
            else categories2(response);
        }/*TODO: Check which images couldn't be saved and try to do their loading again?*/;
    }

    private View view;
    private AsyncTask hp = null;
    private ArrayList<HashMap<String, String>> categories;

    private void categories(String response) {
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            categories = StatusService.StaticStatusService.jc.getObjects();
            if (!categories.isEmpty()) {
                ArrayList<String> names = new ArrayList<String>();
                if (!StatusService.StaticStatusService.fh.checkIfImageExists("category_menu_bg.png")) names.add("category_menu_bg.png");

                for (int i = 0; i < categories.size(); i++) {
                    String imageName = "category_icon" + categories.get(i).get("id");
                    if (!StatusService.StaticStatusService.fh.checkIfImageExists(imageName)) {
                        names.add(imageName);
                    }
                }

                //Either all images are in memory or some must be downloaded from S3.
                if (!names.isEmpty()) {
                    //NOTE: The code works only as simple if S3 has saved the the needed images in a single bucket with the same naming convency.
                    new S3Download(new catImgsDownloaded(), names).execute();
                } else {
                    drawImages();
                }
            }
            //TODO else?
        }
    }


    private void categories2(String response) {

        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            categories = StatusService.StaticStatusService.jc.getObjects();
            drawImages();
        }
        //TODO else?

    }

    private void drawImages() {
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.categories);
        Bitmap background = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "category_menu_bg.png");
        Drawable d = new BitmapDrawable(getResources(), background);
        rl.setBackground(d);
        rl.getLayoutParams().height = 6000;
        rl.getLayoutParams().width = 9000;

        ImageButton[] categorybutton = new ImageButton[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            try {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                Bitmap bitmap = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "category_icon" + categories.get(i).get("id"));
                categorybutton[i] = new ImageButton(getContext());
                categorybutton[i].setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
                categorybutton[i].setLayoutParams(lp);
                categorybutton[i].setOnClickListener(this);
                categorybutton[i].setBackgroundColor(Color.TRANSPARENT);
                categorybutton[i].setId(Integer.parseInt(categories.get(i).get("id")));
                lp.leftMargin = 300*i; //TODO: (categories.get(i).get("x")));
                lp.topMargin = 300*i; //TODO: (categories.get(i).get("y")));
                rl.addView(categorybutton[i], lp);
            } catch (Exception e) {
                Log.e("Image error", e.toString());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.categories_fragment, container, false);
        String url = StatusService.StaticStatusService.sc.DescribeCategories();
        Log.i("urli", url);
        hp = new HTTPSRequester(new categorieslistener()).execute(url);
        return view;
    }

    @Override
    public void onClick(View v) {
        String id = Integer.toString(v.getId());
        ((CategoriesActivity) getActivity()).startCategory(id);
    }

}