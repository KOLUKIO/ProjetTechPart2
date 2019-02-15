package project.part2.editimage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContrastFragment extends Fragment {

    // empty public constructor
    public ContrastFragment() {    }

    private Button mButtonArrowBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contrast, container, false);

        mButtonArrowBack = (Button) view.findViewById(R.id.button_arrow_back);

        mButtonArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }

}
