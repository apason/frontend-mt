package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateData {

    private AsyncTask hp = null;
    private int pointer = 0;
    private int categoryId = 0;

    public class Update implements TaskCompleted {

        @Override
        public void taskCompleted(String response) {
            StatusService.StaticStatusService.jc.newJson(response);
            ArrayList<HashMap<String, String>> data = StatusService.StaticStatusService.jc.getObjects();
            switch (pointer) {
                case 0:
                    StatusService.StaticStatusService.categories = new int[data.size()];
                    for (int i = 0; i < data.size(); i++) {
                        StatusService.StaticStatusService.categories[i] = Integer.parseInt(data.get(i).get("id"));
                    }
                case 1:
                    for (HashMap<String, String> task : data) {
                        StatusService.StaticStatusService.task[categoryId].add(Integer.parseInt(task.get("id")));
                        categoryId++;
                    }
                //case 2: download missing images
            }
            refresh();
        }
    }

    public void refresh() {
        switch (pointer) {
            case 0:
                hp = new HTTPSRequester(new Update()).execute(StatusService.StaticStatusService.sc.DescribeCategories());
            case 1:
                if (categoryId == StatusService.StaticStatusService.categories.length) pointer++;
                updateTasks();
            //case 2: download missing images
        }
    }

    public void updateTasks() {
        String url = StatusService.StaticStatusService.sc.DescribeCategoryTasks(Integer.toString(categoryId));
        hp = new HTTPSRequester(new Update()).execute(url);
    }

}