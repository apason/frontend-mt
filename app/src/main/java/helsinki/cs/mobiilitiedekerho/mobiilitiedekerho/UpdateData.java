package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateData {

    private AsyncTask hp = null;
    private int pointer = 0;
    private int categoryId = 0;

    public class Update implements TaskCompleted {

        @Override
        public void taskCompleted(String response) {
            cases(response);
        }
    }

        public void cases (String response) {
            StatusService.StaticStatusService.jc.newJson(response);
            ArrayList<HashMap<String, String>> data = StatusService.StaticStatusService.jc.getObjects();
            String str_result = "";
            /*
            while (!str_result.contains("objects")) {
                try {
                    str_result = hp.get().toString();
                    Log.i("tulos", str_result);
                } catch (Exception e) {
                    Log.i("virhe", "virhe");
                }
            }
            */
            switch (pointer) {
                /*
                case 0: {
                    StatusService.StaticStatusService.categories = new int[data.size()];
                    for (int i = 0; i < data.size(); i++) {
                        StatusService.StaticStatusService.categories[i] = Integer.parseInt(data.get(i).get("id"));
                    }
                    break;
                }

                case 1: {
                    StatusService.StaticStatusService.task[categoryId] = new int [data.size()];
                    for (HashMap<String, String> task : data) {
                        StatusService.StaticStatusService.task[categoryId].add(Integer.parseInt(task.get("id")));
                        categoryId++;
                        Log.i("categoryId", Integer.toString(categoryId));
                    }
                    updateTasks();
                    break;
                }
                */
                //case 2: download missing images
            }
        }


    public void updateCategories() {
        pointer = 0;
        String url = StatusService.StaticStatusService.sc.DescribeCategories();
        Log.i("urrl", url);
        hp = new HTTPSRequester(new Update()).execute(url);
    }

    public void updateTasks() {
        pointer = 1;
        /*
        if (categoryId == StatusService.StaticStatusService.categories.length) return;
        int id = StatusService.StaticStatusService.categories[categoryId];
        String url = StatusService.StaticStatusService.sc.DescribeCategoryTasks(Integer.toString(id));
        Log.i("uniikki", url);
        hp = new HTTPSRequester(new Update()).execute(url);
        */
    }

}