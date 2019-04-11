package project.part2.editimage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
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

        Button mButtonAveraging3 = view.findViewById(R.id.button_convolution_averaging_3);
        Button mButtonAveraging5 = view.findViewById(R.id.button_convolution_averaging_5);
        Button mButtonGaus5 = view.findViewById(R.id.button_convolution_gauss);
        Button mButtonSobel = view.findViewById(R.id.button_convolution_sobel);
        Button mButtonBlurRs = view.findViewById(R.id.button_blurRs);

        mButtonAveraging3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convolution(bitmap, getMatrixAveraging(3));
                i.setImageBitmap(bitmap);
            }
        });

        mButtonAveraging5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convolution(bitmap, getMatrixAveraging(5));
                i.setImageBitmap(bitmap);
            }
        });

        mButtonGaus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convolution(bitmap,matrixGaussian5);
                i.setImageBitmap(bitmap);
            }
        });

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
        radiusDialog.setTitle("Set radius");

        final EditText radiusText = new EditText(MainActivity.getContext());

        radiusText.setInputType(InputType.TYPE_CLASS_NUMBER);
        radiusDialog.setView(radiusText);

        radiusDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int radius = Integer.parseInt(radiusText.getText().toString());
                if(radius > 26){
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
