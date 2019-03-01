package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import static project.part2.editimage.Functions.*;

public class ConvolutionFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;
    private Button mButtonAveraging3, mButtonAveraging5, mButtonGaus5, mButtonSobel;

    public ConvolutionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_convolution, container, false);

        i = (ImageView) getActivity().findViewById(R.id.imageView);
        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        mButtonAveraging3 = view.findViewById(R.id.button_convolution_averaging_3);
        mButtonAveraging5 = view.findViewById(R.id.button_convolution_averaging_5);
        mButtonGaus5 = (Button) view.findViewById(R.id.button_convolution_gauss);
        mButtonSobel = (Button) view.findViewById(R.id.button_convolution_sobel);

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

        return view;
    }

}
