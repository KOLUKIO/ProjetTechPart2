package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import static project.part2.editimage.Rotate.*;

public class RotateFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;

    public RotateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rotate, container, false);

        i = getActivity().findViewById(R.id.imageView);

        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        Button mButtonRotateL = view.findViewById(R.id.button_rotate_left);
        Button mButtonRotateR = view.findViewById(R.id.button_rotate_right);

        mButtonRotateL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // i.setRotation(i.getRotation() - 90);
                bitmap = rotateLeft(bitmap);
                i.setImageBitmap(bitmap);

            }
        });

        mButtonRotateR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // i.setRotation(i.getRotation() + 90);
                bitmap = rotateRight(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        return view;
    }

}
