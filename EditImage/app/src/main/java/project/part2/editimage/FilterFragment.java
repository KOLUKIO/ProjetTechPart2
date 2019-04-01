package project.part2.editimage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import static project.part2.editimage.Convolution.BlurRS;
import static project.part2.editimage.Filters.*;

public class FilterFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;
    // empty public constructor
    public FilterFragment() { }

    private Button mButtonGrey, mButtonRed, mButtonColorize, mButtonKeepRed, mButtonNegative, mButtonBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_filter, container, false);
       // View v  = inflater.inflate(R.layout.activity_main, container, false);
        mButtonGrey = view.findViewById(R.id.button_filter_grey);
        mButtonRed = view.findViewById(R.id.button_filter_red);
        mButtonColorize = view.findViewById(R.id.button_colorize);
        mButtonKeepRed = view.findViewById(R.id.button_keep_red);
        mButtonNegative = view.findViewById(R.id.button_filter_negative);
        mButtonBar = view.findViewById(R.id.button_filter_bar);

        i = getActivity().findViewById(R.id.imageView);

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

        mButtonBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert();
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
