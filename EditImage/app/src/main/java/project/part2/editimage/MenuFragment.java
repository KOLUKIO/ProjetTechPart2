package project.part2.editimage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "FragmentMenu";

    private Button mButtonFilter;
    private Button mButtonContrast;
    private Button mButtonConvolution;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_menu, container, false);
        mButtonFilter = (Button) view.findViewById(R.id.button_filter);
        mButtonContrast = (Button) view.findViewById(R.id.button_contrast);
        mButtonConvolution = (Button) view.findViewById(R.id.button_convolution);
        Log.d(TAG, "onCreateView: started.");

        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(1);
            }
        });

        mButtonContrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(2);
            }
        });

        mButtonConvolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ((MainActivity)getActivity()).setViewPager(2);
            }
        });

        return view;
    }
}
