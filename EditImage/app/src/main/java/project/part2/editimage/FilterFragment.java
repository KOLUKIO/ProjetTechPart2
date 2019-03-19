package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import static project.part2.editimage.Filters.*;

public class FilterFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;
    // empty public constructor
    public FilterFragment() { }

    private Button mButtonGrey, mButtonRed, mButtonColorize, mButtonKeepRed, mButtonNegative;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_filter, container, false);
        View v  = inflater.inflate(R.layout.activity_main, container, false);
        mButtonGrey = (Button) view.findViewById(R.id.button_filter_grey);
        mButtonRed = (Button) view.findViewById(R.id.button_filter_red);
        mButtonColorize = (Button) view.findViewById(R.id.button_colorize);
        mButtonKeepRed = (Button) view.findViewById(R.id.button_keep_red);
        mButtonNegative = (Button) view.findViewById(R.id.button_filter_negative);

        i = (ImageView) getActivity().findViewById(R.id.imageView);

        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        mButtonGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toGrey(bitmap);
                toGreyRS(bitmap);
                i.setImageBitmap(bitmap); // Force refresh imageview
            }
        });

        mButtonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toRed(bitmap);
                toRedRS(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        mButtonColorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(new ColorizeFragment());
            }
        });

        mButtonKeepRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //keepRed(bitmap);
                keepRedRS(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        mButtonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toNegative(bitmap);
                toNegativeRS(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        return view;
    }
}
