package project.part2.editimage;

import android.annotation.SuppressLint;
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
    boolean zoom = false;
    double d;

    RelativeLayout buttonLayout;
    RelativeLayout.LayoutParams layoutParams;
    RelativeLayout relativeLayout;

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

        mButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker = addSticker();
                sticker.setOnTouchListener(new View.OnTouchListener() {
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
                            case MotionEvent.ACTION_POINTER_UP:
                                zoom = false;
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                x0 = event.getX(0);
                                y0 = event.getY(0);
                                x1 = event.getX(1);
                                y1 = event.getY(1);
                                zoom = true;
                                d = dist(x0, x1, y0, y1);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (zoom){
                                    try{
                                        if (dist(x0, event.getX(1), y0, event.getY(1)) > d) {
                                            v.setScaleX(v.getScaleX() + 0.025f);
                                            v.setScaleY(v.getScaleY() + 0.025f);
                                        }
                                        if (dist(x0, event.getX(1), y0, event.getY(1)) < d) {
                                            v.setScaleX(v.getScaleX() - 0.025f);
                                            v.setScaleY(v.getScaleY() - 0.025f);
                                        }
                                        if (v.getScaleX() > 1.2) {
                                            v.setScaleX(1.2f);
                                            v.setScaleY(1.2f);
                                        }
                                        if (v.getScaleX() < 0.5) {
                                            v.setScaleX(0.5f);
                                            v.setScaleY(0.5f);
                                        }
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                    layoutParams.leftMargin = X - _xDelta;
                                    layoutParams.topMargin = Y - _yDelta;
                                    layoutParams.rightMargin = -250;
                                    layoutParams.bottomMargin = -250;
                                    v.setLayoutParams(layoutParams);
                                }
                                break;
                        }
                        return true;
                    }
                });
                posX = v.getX();
                posY = v.getY();
            }
        });

        return view;
    }

    public double dist(float x1, float x2, float y1, float y2) {
        float a = x1-x2;
        float b = y1 - y2;
        return Math.sqrt(a*a + b*b);
    }

    public ImageView addSticker(){
        final ImageView sticker = new ImageView(getContext());
        sticker.setImageResource(R.drawable.ic_star_24dp);

        relativeLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.imageViewRoot);
        buttonLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.layout_main);

        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT+200,
                RelativeLayout.LayoutParams.WRAP_CONTENT+200);

        int id = View.generateViewId();
        sticker.setId(id);
        relativeLayout.addView(sticker, layoutParams);

        // button to modify bubble
        final ImageButton buttonDelete = new ImageButton(getContext());

        // delete button
        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.setMargins(0, 0, 50, 60);

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

}
