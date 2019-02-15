package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import static project.part2.editimage.Functions.*;

public class FilterFragment extends Fragment {

    // empty public constructor
    public FilterFragment() { }

    private static final String TAG = "FragmentMenu";

    private Button mButtonArrowBack, mButtonGrey, mButtonRed, mButtonColorize, mButtonKeepRed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_filter, container, false);
        mButtonArrowBack = (Button) view.findViewById(R.id.button_arrow_back);
        mButtonGrey = (Button) view.findViewById(R.id.button_filter_grey);
        mButtonRed = (Button) view.findViewById(R.id.button_filter_red);
        mButtonColorize = (Button) view.findViewById(R.id.button_colorize);
        mButtonKeepRed = (Button) view.findViewById(R.id.button_keep_red);

        Log.d(TAG, "onCreateView: started.");

        ImageView i = (ImageView) getActivity().findViewById(R.id.imageView);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.image_test);
        i.setImageBitmap(bitmap);

        mButtonArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(0);
            }
        });

        mButtonGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // toGrey(bitmap);
            }
        });

        mButtonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mButtonColorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mButtonKeepRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }


}
