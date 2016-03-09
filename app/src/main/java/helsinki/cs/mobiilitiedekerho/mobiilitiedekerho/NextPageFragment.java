package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

//Fragment that creates an arrow button to screen, currently clicking changes view from front page to next page
public class NextPageFragment extends Fragment implements View.OnClickListener {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.next_button_fragment, null);

        ImageButton nextButton =
            (ImageButton) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).startCategories();
    }
}
