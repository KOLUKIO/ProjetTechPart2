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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Objects;

public class StickersFragment extends Fragment {

    Bitmap bitmap;
    ImageView i, sticker;

//    RelativeLayout buttonLayout;
    RelativeLayout.LayoutParams layoutParams;
    RelativeLayout relativeLayout;


    public StickersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stickers, container, false);

        i = Objects.requireNonNull(getActivity()).findViewById(R.id.imageView);
        sticker = Objects.requireNonNull(getActivity()).findViewById(R.id.sticker);

        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        Button mButtonStar = view.findViewById(R.id.button_star);

        mButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBubble(view);
            }
        });

        return view;
    }

    public void createBubble(View view){

     //   final ImageView sticker = new ImageView(getContext());
       // sticker.setImageResource(R.drawable.ic_star_24dp);
       // final EditText sticker = new EditText(getContext());

        relativeLayout = view.findViewById(R.id.root);

        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

      //  int id = View.generateViewId();
     //   sticker.setId(id);
        sticker.setImageResource(R.drawable.ic_star_24dp);
      //  relativeLayout.addView(sticker);
       // relativeLayout.addView(sticker, layoutParams);


        // button to modify bubble
        final Button buttonDelete = new Button(getContext());

        // delete button
        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        buttonDelete.setBackgroundResource(R.drawable.ic_delete_24dp);
        buttonDelete.setPadding(10,5,10,5);
     //   buttonLayout.addView(buttonDelete, layoutParams);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker.setVisibility(View.GONE);
                buttonDelete.setVisibility(View.GONE);
            }
        });

        buttonDelete.setVisibility(View.GONE);

        //sticker.setBackgroundResource(R.drawable.ic_star_24dp);
    }

}
