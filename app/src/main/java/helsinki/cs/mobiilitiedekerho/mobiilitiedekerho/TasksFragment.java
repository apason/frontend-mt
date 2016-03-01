package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TasksFragment extends Fragment implements View.OnClickListener {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tasks_fragment, container, false);

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.tasks);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageButton[] taskbutton = new ImageButton[2];
        for (int i = 0; i < 2; i++) {
            taskbutton[i] = new ImageButton(getContext());
            String image = "";
            if (i == 0) image = "rain";
            if (i == 1) image = "snow";
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

        //THIS CODE WHEN SERVERCOMMUNICATION CAN BE USED:
/*
        sc = new ServerCommunication();

        // only one category in this sprint: category_id = String "0" ?
        // HashMap<String, String> task: Key = task_id &  Value = name ???
        HashMap<String, String> tasks = sc.DescribeCategory("0");
        ImageButton [] taskbutton = new ImageButton[tasks.size()];
        for (int i = 0; i < tasks.size()-1; i++) {
            taskbutton[i] = new ImageButton(this);
            taskbutton[i].setImageResource(R.drawable.rain);
            taskbutton[i].setLayoutParams(lp);
            taskbutton[i].setOnClickListener(this);
            taskbutton[i].setBackgroundColor(Color.TRANSPARENT);
            taskbutton[i].setId(Integer.parseInt(tasks.get(i)));
            ll.addView(taskbutton[i], lp);
        }
        */

        return view;
    }

    @Override
    public void onClick(View v) {
        String id = Integer.toString(v.getId());
        ((CategoryActivity) getActivity()).startTask(id);
    }
}
