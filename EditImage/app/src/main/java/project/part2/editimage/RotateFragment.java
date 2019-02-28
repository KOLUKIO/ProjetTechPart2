package project.part2.editimage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class RotateFragment extends Fragment {

    ImageView i;
    private Button mButtonRotateL, mButtonRotateR;
    public RotateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rotate, container, false);

        i = (ImageView) getActivity().findViewById(R.id.imageView);
        mButtonRotateL = (Button) view.findViewById(R.id.button_rotate_left);
        mButtonRotateR = (Button) view.findViewById(R.id.button_rotate_right);

        mButtonRotateL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setRotation(i.getRotation() - 90);
            }
        });

        mButtonRotateR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setRotation(i.getRotation() + 90);
            }
        });
        return view;
    }

}
