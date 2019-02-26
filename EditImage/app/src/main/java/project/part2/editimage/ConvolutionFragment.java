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
    private Button mButtonSobel;
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
        mButtonSobel = (Button) view.findViewById(R.id.button_convolution_sobel);

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
