package project.part2.editimage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_menu, container, false);

        Button mButtonFilter = view.findViewById(R.id.button_filter);
        Button mButtonContrast = view.findViewById(R.id.button_contrast);
        Button mButtonConvolution = view.findViewById(R.id.button_convolution);
        Button mButtonInfo = view.findViewById(R.id.button_information);
        Button mButtonStickers = view.findViewById(R.id.button_stickers);
        Button mButtonRotate = view.findViewById(R.id.button_rotate);

        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(new FilterFragment());
            }
        });

        mButtonContrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(new ContrastFragment());
            }
        });

        mButtonConvolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(new ConvolutionFragment());
            }
        });

        mButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFragment(new GraphFragment());
            }
        });

        mButtonStickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFragment(new StickersFragment());
            }
        });

        mButtonRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFragment(new RotateFragment());
            }
        });

        return view;
    }
}
