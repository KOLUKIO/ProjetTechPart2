package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import static project.part2.editimage.Functions.*;

public class FilterFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;
    // empty public constructor
    public FilterFragment() { }

    private static final String TAG = "FragmentMenu";

    private Button mButtonGrey, mButtonRed, mButtonColorize, mButtonKeepRed;
    ImageButton mButtonArrowBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_filter, container, false);
        mButtonArrowBack = (ImageButton) view.findViewById(R.id.button_arrow_back);
        mButtonGrey = (Button) view.findViewById(R.id.button_filter_grey);
        mButtonRed = (Button) view.findViewById(R.id.button_filter_red);
        mButtonColorize = (Button) view.findViewById(R.id.button_colorize);
        mButtonKeepRed = (Button) view.findViewById(R.id.button_keep_red);

        Log.d(TAG, "onCreateView: started.");

        i = (ImageView) getActivity().findViewById(R.id.imageView);
        //bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.image_test);
        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image

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
                toGrey(bitmap);
                i.setImageBitmap(bitmap); // Force refresh imageview
            }
        });

        mButtonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRed(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        mButtonColorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorize(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        mButtonKeepRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keepRed(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        return view;
    }


}
