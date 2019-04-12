package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Objects;

public class StickersFragment extends Fragment {

    Bitmap bitmap;
    ImageView i;
    public static ImageView sticker = null;
    float posX;
    float posY;
    private int _xDelta;
    private int _yDelta;
    float x0 = 0, x1 = 0, y0 = 0, y1 = 0;
    double d;

    RelativeLayout.LayoutParams layoutParams;

    public StickersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stickers, container, false);

        i = Objects.requireNonNull(getActivity()).findViewById(R.id.imageView);
        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        Button mButtonStar = view.findViewById(R.id.button_star);
        Button mButtonHeart = view.findViewById(R.id.button_heart);
        Button mButtonSun = view.findViewById(R.id.button_sun);

        mButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker = addSticker(R.drawable.ic_star_24dp);
                sticker.setOnTouchListener(onTouchListener);
                posX = v.getX();
                posY = v.getY();
            }
        });

        mButtonHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker = addSticker(R.drawable.ic_heart_24dp);
                sticker.setOnTouchListener(onTouchListener);
                posX = v.getX();
                posY = v.getY();
            }
        });

        mButtonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker = addSticker(R.drawable.ic_sun_24dp);
                sticker.setOnTouchListener(onTouchListener);
                posX = v.getX();
                posY = v.getY();
            }
        });

        return view;
    }

    public ImageView addSticker(int resId){
        final ImageView sticker = new ImageView(getContext());
        sticker.setImageResource(resId);

        final RelativeLayout relativeLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.imageViewRoot);
        FrameLayout buttonLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.buttonStickersRoot);

        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT+150,
                RelativeLayout.LayoutParams.WRAP_CONTENT+150);

        int id = View.generateViewId();
        sticker.setId(id);
        relativeLayout.addView(sticker, layoutParams);

        // button to modify bubble
        final ImageButton buttonDelete = new ImageButton(getContext());

        // delete button
        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
       // layoutParams.setMargins(0, 30, 30, 0);

        buttonDelete.setBackgroundResource(R.drawable.ic_delete_24dp);
        buttonDelete.setPadding(50, 50, 50, 50);
        buttonLayout.addView(buttonDelete, layoutParams);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker.setVisibility(View.GONE);
                buttonDelete.setVisibility(View.GONE);
            }
        });

        return sticker;
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    x0 = event.getX();
                    y0 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    v.requestFocus();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    x0 = event.getX(0);
                    y0 = event.getY(0);
                    x1 = event.getX(1);
                    y1 = event.getY(1);
                    d = MainActivity.dist(x0, x1, y0, y1);
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    v.setLayoutParams(layoutParams);
                    break;
            }
            return true;
        }
    };

}
