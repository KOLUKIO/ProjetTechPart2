package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Objects;

import static project.part2.editimage.Rotate.*;

public class MirrorFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;

    public MirrorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mirror, container, false);

        i = Objects.requireNonNull(getActivity()).findViewById(R.id.imageView);
        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        Button mButtonMirrorH = view.findViewById(R.id.button_mirrorH);
        Button mButtonMirrorV = view.findViewById(R.id.button_mirrorV);

        mButtonMirrorH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
                Bitmap out;
                Matrix m = new Matrix();
                m.preScale(-1.0f, 1.0f);
                out = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                i.setImageBitmap(out);
            }
        });

        mButtonMirrorV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
                Bitmap out;
                Matrix m = new Matrix();
                m.preScale(-1.0f, -1.0f);
                out = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                i.setImageBitmap(out);
            }
        });

        return view;
    }

}
