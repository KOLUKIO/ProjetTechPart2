package project.part2.editimage;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import static project.part2.editimage.Functions.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContrastFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;

    // empty public constructor
    public ContrastFragment() {    }

    private ImageButton mButtonArrowBack;
    private Button mButtonColorize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contrast, container, false);

        mButtonArrowBack = (ImageButton) view.findViewById(R.id.button_arrow_back);
        mButtonColorize = (Button) view.findViewById(R.id.button_contrast);

        i = (ImageView) getActivity().findViewById(R.id.imageView);
        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image

        i.setImageBitmap(bitmap);

        mButtonArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(0);
            }
        });

        mButtonColorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contrast(bitmap, histogramEqualization(bitmap));
                i.setImageBitmap(bitmap);
            }
        });

        return view;
    }
}
