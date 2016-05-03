package helsinki.cs.mobiilitiedekerho.mobiilitiedekerho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A Fragment for drawnig the homebutton and handling the onClick events for the homebutton that takes the user back to MainActivity.
 */
public class HomeButtonFragment extends Fragment {

    private View view;

    
    // Draw the contents of home_button_fragment.xml to screen and set onclicklistener for home_button
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_button_fragment, null);

        ImageButton homeButton =
                (ImageButton) view.findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity().getApplication(), MainActivity.class);
                Log.i("Mainiin", "mennään");
                startActivity(mainIntent);
            }
        });
        return view;
    }
}
