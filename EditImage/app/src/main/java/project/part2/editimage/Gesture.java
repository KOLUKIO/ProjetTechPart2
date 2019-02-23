package project.part2.editimage;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class Gesture extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d("TAG","onDown: ");

        // don't return false here or else none of the other
        // gestures will work
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.i("TAG", "onSingleTapConfirmed: ");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i("TAG", "onLongPress: ");
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.i("TAG", "onDoubleTap: ");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("TAG", "onScroll: ");

        if (e2.getPointerCount() > 1) {  // two or more fingers
            // allows to enlarge the image
            if (distanceX > 0 || distanceY > 0) {
                MainActivity.imageView.setScaleX(MainActivity.imageView.getScaleX() + 0.1f);
                MainActivity.imageView.setScaleY(MainActivity.imageView.getScaleY() + 0.1f);
            }
            // allows to shrink the image
            if (distanceX < 0 || distanceY < 0) {
                MainActivity.imageView.setScaleX(MainActivity.imageView.getScaleX() - 0.1f);
                MainActivity.imageView.setScaleY(MainActivity.imageView.getScaleY() - 0.1f);
            }
            // allows not to shrink the image more than 0.3 times its size
            if (MainActivity.imageView.getScaleX() < 0.3) {
                MainActivity.imageView.setScaleX(0.3f);
                MainActivity.imageView.setScaleY(0.3f);
            }
            // allows not to enlarge the image more than 2.5 times its size
            if (MainActivity.imageView.getScaleX() > 2.5) {
                MainActivity.imageView.setScaleX(2.5f);
                MainActivity.imageView.setScaleY(2.5f);
            }
        }
        else{                                            // one fingers, allow to move the image
            if(MainActivity.imageView.getScaleX() > 1){  // if the size of the image is larger than the original size of the image
                MainActivity.imageView.scrollBy((int)distanceX, (int)distanceY);
            }
        }
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        Log.d("TAG", "onFling: ");

        return true;
    }
}
