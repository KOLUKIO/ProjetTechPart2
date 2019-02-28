package project.part2.editimage;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class Gesture extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onDown(MotionEvent event) {
        Log.d("TAG","onDown: ");
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

        if (e2.getPointerCount() == 1){
            MainActivity.imageView.scrollBy((int)distanceX, (int)distanceY);
        }
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        Log.d("TAG", "onFling: ");
        return true;
    }
}
