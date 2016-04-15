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

private boolean triedAlready = false;
private ArrayList<String> names; //Save images to be downloaded & saved for error checking.

    public class categorieslistener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            categories(response);
        }
    }

    public class catImgsDownloaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            if (response.equals("success")) drawImages(); //All went right, proceed to draw images.
            else checkErrors(response);
        }
    }
    
    public class restOfImgsDownloaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
        	if (response.equals("success")) drawImages(); //All went right this time, proceed to draw images.
        	else if (response.equals("failure")) {
        		//This is actually a communication error. TODO: Should try again?
        	}
        	else {
        		//Could not get the rest (or all) of the images (that is the ones that couldn't be gotten before)
        		// TODO: What to do? Notice the user or just skip these categories?
        		
        		// if it desired to just skip the categories in question, implement the code below.
        		// String[] tg = response.split(":");
        		// Remove from names the ones in tg.
        		// (Finally in drawImages() do not read the ones not supposed to be read... :D)
        	}
        }
    }

    private View view;
    private AsyncTask hp = null;
    private ArrayList<HashMap<String, String>> categories;

    private void categories(String response) {
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            categories = StatusService.StaticStatusService.jc.getObjects();
            if (!categories.isEmpty()) {
                names = new ArrayList<String>();
                if (!StatusService.StaticStatusService.fh.checkIfImageExists("category_menu_bg.png")) names.add("category_menu_bg.png");

                for (int i = 0; i < categories.size(); i++) {
                    String imageName = "category_icon_id_" + categories.get(i).get("id") + ".png";
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
            //TODO: There is no tasks in the category, show some dialog.
        }
    }


    private void checkErrors(String response) {
        // Communicating with S3 failed, try again.
        if (response.equals("failure")) {
            if (!triedAlready) {
                triedAlready = true;
                new S3Download(new catImgsDownloaded(), names).execute();
            }
            else {
            	//TODO: Show that some graphics could not be downloaded from S3 to user in some way.
            }
        }
        // Some images could not be downloaded & saved, try again them.
        else {
            ArrayList<String> toGetAgain = new ArrayList<String>();
            
            String[] tg = response.split(":");
            for (int i = 0 ; i < tg.length ; i++) {
            	toGetAgain.add(tg[i]);
            }
            
            new S3Download(new catImgsDownloaded(), names).execute();
        }
    }

    private void drawImages() {
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.categories);
        
        // WHAT, busy wait?!!? TODO: Fix something this is no good.
        long start = System.currentTimeMillis();
        long timer = 0;
        while ((!StatusService.StaticStatusService.fh.checkIfImageExists("category_menu_bg.png")) && timer < 5000) {
            timer = System.currentTimeMillis()-start;
        }
        // WHATTTTTTEEE HECK!
        

        Bitmap background = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "category_menu_bg.png");
        Drawable d = new BitmapDrawable(getResources(), background);
        rl.setBackground(d);
        rl.getLayoutParams().height = 6000;
        rl.getLayoutParams().width = 9000;

        ImageButton[] categorybutton = new ImageButton[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            try {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                start = System.currentTimeMillis();
                timer = 0;
                while (!StatusService.StaticStatusService.fh.checkIfImageExists("category_icon_id_" + categories.get(i).get("id") + ".png") && timer < 5000) {
                    timer = System.currentTimeMillis() - start;
                }
                Bitmap bitmap = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "category_icon_id_" + categories.get(i).get("id") +".png");
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
        hp = new HTTPSRequester(new categorieslistener()).execute(url);
        return view;
    }

    @Override
    public void onClick(View v) {
        String id = Integer.toString(v.getId());
        ((CategoriesActivity) getActivity()).startCategory(id);
    }
/* Rip scrolling in fragments.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float curX, curY;
        float mx = event.getX();
        float my = event.getY();
        ScrollView vScroll = (ScrollView) view.findViewById(R.id.scrollVertical);
        HorizontalScrollView hScroll = (HorizontalScrollView) view.findViewById(R.id.scrollVertical);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
        }
        super.onTouchEvent(event);
    }
*/
}