package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Objects;

import static project.part2.editimage.Filters.colorizeRS;

public class ColorizeFragment extends Fragment {

    // empty public constructor
    public ColorizeFragment() { }

    Bitmap bitmap;
    ImageView i;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_colorize, container, false);
        SeekBar mSeekBar = view.findViewById(R.id.colorizeSeekBar);

        i = Objects.requireNonNull(getActivity()).findViewById(R.id.imageView);
        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image

        i.setImageBitmap(bitmap);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colorizeRS(bitmap, (double) progress);
                //colorize(bitmap, hue);
                i.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        return view;

    }
}
