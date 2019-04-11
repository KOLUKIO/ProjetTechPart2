package project.part2.editimage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static project.part2.editimage.Filters.*;

public class FilterFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;

    // empty public constructor
    public FilterFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_filter, container, false);

        Button mButtonGrey = view.findViewById(R.id.button_filter_grey);
        Button mButtonRed = view.findViewById(R.id.button_filter_red);
        Button mButtonColorize = view.findViewById(R.id.button_colorize);
        Button mButtonKeepRed = view.findViewById(R.id.button_keep_red);
        Button mButtonNegative = view.findViewById(R.id.button_filter_negative);
        Button mButtonBar = view.findViewById(R.id.button_filter_bar);
        Button mButtonCrayon = view.findViewById(R.id.button_filter_crayon);
        Button mButtonMedian = view.findViewById(R.id.button_filter_median);

        i = getActivity().findViewById(R.id.imageView);

        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        mButtonGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toGrey(bitmap);
                toGreyRS(bitmap);
                i.setImageBitmap(bitmap); // force refresh imageView
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

        mButtonBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert();
            }
        });

        mButtonCrayon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencilSketch(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        mButtonMedian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medianFilter(bitmap);
                i.setImageBitmap(bitmap);
            }
        });

        return view;
    }

    public void Alert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Set number of bar");

        final EditText radiusText = new EditText(MainActivity.getContext());

        radiusText.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog.setView(radiusText);


        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int nbBar = Integer.parseInt(radiusText.getText().toString());
                int widthBar = 26;
                if((nbBar+1)*widthBar > bitmap.getWidth()){
                    Toast.makeText(getContext(), "Bar number too great", Toast.LENGTH_SHORT).show();
                }else {
                    bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
                    addBarHorizontal(bitmap, nbBar, widthBar);
                    i.setImageBitmap(bitmap);
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
