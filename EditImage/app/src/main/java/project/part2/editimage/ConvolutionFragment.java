package project.part2.editimage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Objects;

import static project.part2.editimage.Convolution.*;

public class ConvolutionFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;

    public ConvolutionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_convolution, container, false);

        i = Objects.requireNonNull(getActivity()).findViewById(R.id.imageView);
        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        Button mButtonSobel = view.findViewById(R.id.button_convolution_sobel);
        Button mButtonBlurRs = view.findViewById(R.id.button_blurRs);

        mButtonSobel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sobelFilter(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        mButtonBlurRs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert();
            }
        });

        return view;
    }

    public void Alert() {
        AlertDialog.Builder radiusDialog = new AlertDialog.Builder(getActivity());
        radiusDialog.setTitle("Set radius (25 max)");

        final EditText radiusText = new EditText(MainActivity.getContext());

        radiusText.setInputType(InputType.TYPE_CLASS_NUMBER);
        radiusDialog.setView(radiusText);

        radiusDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int radius = Integer.parseInt(radiusText.getText().toString());
                if(radius > 25){
                    Toast.makeText(getContext(), "Radius too great", Toast.LENGTH_SHORT).show();
                }else {
                    BlurRS(bitmap, radius);
                    i.setImageBitmap(bitmap);
                }
            }
        });

        radiusDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        radiusDialog.show();
    }

}
