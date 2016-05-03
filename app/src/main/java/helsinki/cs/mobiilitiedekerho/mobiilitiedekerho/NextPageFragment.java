package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 *  Fragment that draws an arrow button to the screen, currently clicking changes view from front page to next page.
 */
public class NextPageFragment extends Fragment implements View.OnClickListener {

    private View view;

    // Draw the contents of next_button_fragment.xml to screen and add onclicklistener to next_button
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.next_button_fragment, null);

        ImageButton nextButton =
            (ImageButton) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        return view;
    }

    // On click of next_button start Categories fragment.
    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).startCategories();
    }
}
