package project.part2.editimage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    private Button mButtonFilter, mButtonContrast, mButtonBlur, mButtonConvolution, mButtonInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_menu, container, false);
        mButtonFilter = (Button) view.findViewById(R.id.button_filter);
        mButtonContrast = (Button) view.findViewById(R.id.button_contrast);
        mButtonBlur = (Button) view.findViewById(R.id.button_blur);
        mButtonConvolution = (Button) view.findViewById(R.id.button_convolution);
        mButtonInfo = (Button) view.findViewById(R.id.button_information);

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

        mButtonBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setViewPager(3);
            }
        });

        mButtonConvolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(5);
            }
        });

        mButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setViewPager(7);
            }
        });

        return view;
    }
}
