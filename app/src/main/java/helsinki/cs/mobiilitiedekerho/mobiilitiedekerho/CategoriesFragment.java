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


/**
 *  A fragment-class responsible for showing all kind of stuff related to the 'category-view' and doing all backstage work for it.
 */
public class CategoriesFragment extends Fragment implements View.OnClickListener {

    private View view;

    private boolean triedAlready = false;
    private ArrayList<HashMap<String, String>> categories;
    private ArrayList<String> names; //Save images to be downloaded & saved for error checking.
    private ArrayList<String> urls; //Save urls to be used for downloading for error checking.

    private AsyncTask hp = null;


    /**
     * A listener that checks the given url to download the category menu's BG and call S3Download to get it.
     */
    public class CategoryMenuBGDownload implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
            Log.i("responssiBGload", response);
            if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
                ArrayList<String> name = new ArrayList<String>();
                name.add("category_menu_bg.png");
                ArrayList<String> url = new ArrayList<String>();
                url.add(StatusService.StaticStatusService.jc.getProperty("category_menu_bg_uri"));
                new S3Download(new CategoryMenuBGDownloaded(), name, url).execute();
            }
            else ; //TODO: PROOOBLEEEMMM
        }
    }

    /**
    * A listener that after a successful category menu's BG downloading
    * -> Makes a call to the server for getting the info of all categories.
    * TODO: Do something in case of error.
    */
    public class CategoryMenuBGDownloaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            Log.i("responssiBGloaded", response);
            if (response.equals("success")) {
                String url = StatusService.StaticStatusService.sc.DescribeCategories();
                hp = new HTTPSRequester(new categorieslistener()).execute(url);
            }
            else ; //TODO: Try again or something.
        }
    }

    /**
     * A listener that takes the response of the DescribeCategories-API-call and calls the categories-method with the response.
     * Just that.
     */
    public class categorieslistener implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            categories(response);
        }
    }

    /**
     * A listener that takes the response of S3Upload and if succes it calls drawImages-method, otherwise checkErrors-method.
     * Just that.
     */
    public class catImgsDownloaded implements TaskCompleted {
        @Override
        public void taskCompleted(String response) {
            Log.i("responssicatImgs", response);
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
                // TODO: What to do? Notice the user or just skip these categories?

                // if it desired to just skip the categories in question, implement the code below.
                // String[] tg = response.split(":");
                // Remove from names the ones in tg.
                // (Finally in drawImages() do not read the ones not supposed to be read... :D)
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.categories_fragment, container, false);
        this.menuBG();
        return view;
    }
/*
    @Override
    public void onResume() {  // Refreshes screen when returned to the front page, after eg. logging in or out
        super.onResume();
        drawImages();
    }
*/

    /**
     * Checks if the category_menu_bg is already in the device and if not it does GetCategoryMenuBG-API-call to get it.
     */
    private void menuBG() {
        if (StatusService.StaticStatusService.fh.checkIfImageExists("category_menu_bg.png")) {
            String url = StatusService.StaticStatusService.sc.DescribeCategories();
            hp = new HTTPSRequester(new categorieslistener()).execute(url);
        }
        else {
            String url = StatusService.StaticStatusService.sc.GetCategoryMenuBG();
            hp = new HTTPSRequester(new CategoryMenuBGDownload()).execute(url);
        }
    }


    /**
     * Checks if the categories' icons are already in the device and if not it downloads the ones that are missing.
     * If no missing it calls drawImages() if some missing it calls S3Download correctly.
     * @param response contains the response form the server to the API-call DescribeCategories.
     */
    private void categories(String response) {
        boolean parsingWorked = StatusService.StaticStatusService.jc.newJson(response);
        if (parsingWorked && StatusService.StaticStatusService.sc.checkStatus()) {
            categories = StatusService.StaticStatusService.jc.getObjects();
            if (!categories.isEmpty()) {
                names = new ArrayList<String>(); //Names of the images to be saved.
                urls = new ArrayList<String>(); // The urls to be retrieved.

                for (int i = 0; i < categories.size(); i++) {
                    //String imageName = "category_icon_id_" + categories.get(i).get("id") + ".png";
                    if (!StatusService.StaticStatusService.fh.checkIfImageExists("category_icon_id_"+ categories.get(i).get("id") + ".png")) {
                        Log.i("nimet", categories.get(i).get("icon_uri"));
                        names.add("category_icon_id_"+ categories.get(i).get("id") + ".png");
                        urls.add(categories.get(i).get("icon_uri"));
                    }
                }

                //Either all images are in memory or some must be downloaded from S3.
                if (!names.isEmpty()) {
                    //NOTE: saves the images to memory only based in hard-coded text + ID.
                    new S3Download(new catImgsDownloaded(), names, urls).execute();
                } else {
                    drawImages();
                }
            }
            //TODO: There is no tasks in the category, show some dialog.
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
                new S3Download(new catImgsDownloaded(), names, urls).execute();
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
    * Draws the needed components to the screen.
    * That is the background and category-icons.
    */
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

        createCategoryButtons(rl);
    }

    /**
    * Draws the category-icons to the screen.
    * TODO: In the future it must fit the icons to a grid and place them based in the coordinates (that would be of the grid's).
    */
    private void createCategoryButtons(RelativeLayout rl) {
        ImageButton[] categorybutton = new ImageButton[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            try {
                //TODO: check if category icon is enabled
                if (true) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    // WHAT, busy wait?!!? TODO: Fix something this is no good.
                    long start = System.currentTimeMillis();
                    long timer = 0;
                    while (!StatusService.StaticStatusService.fh.checkIfImageExists("category_icon_id_" + categories.get(i).get("id") + ".png") && timer < 5000) {
                        timer = System.currentTimeMillis() - start;
                    }
                    // WHATTTTTTEEE HECK!

                    Bitmap bitmap = BitmapFactory.decodeFile(StatusService.StaticStatusService.context.getFilesDir() + "/" + "category_icon_id_" + categories.get(i).get("id") + ".png");
                    categorybutton[i] = new ImageButton(getContext());
                    categorybutton[i].setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
                    categorybutton[i].setLayoutParams(lp);
                    categorybutton[i].setOnClickListener(this);
                    categorybutton[i].setBackgroundColor(Color.TRANSPARENT);
                    categorybutton[i].setId(Integer.parseInt(categories.get(i).get("id")));
                    lp.leftMargin = Integer.parseInt(categories.get(i).get("coordinate_x"));
                    lp.topMargin = Integer.parseInt(categories.get(i).get("coordinate_y"));
                    rl.addView(categorybutton[i], lp);
                }
            } catch (Exception e) {
                Log.e("Image error", e.toString());
            }
        }
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