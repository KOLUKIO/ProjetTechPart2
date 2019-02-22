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
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        Log.i("TAG", "onScroll: ");
        String s = Integer.toString(e1.getPointerCount());

        //System.out.println("fingers : " + e1.getPointerCount());

        if (e2.getPointerCount() > 1)
        {
            if (distanceX > 0 || distanceY > 0)
            {
                MainActivity.imageView.setScaleX(MainActivity.imageView.getScaleX() + 0.1f);
                MainActivity.imageView.setScaleY(MainActivity.imageView.getScaleY() + 0.1f);
            }

            if (distanceX < 0 || distanceY < 0)
            {
                MainActivity.imageView.setScaleX(MainActivity.imageView.getScaleX() - 0.1f);
                MainActivity.imageView.setScaleY(MainActivity.imageView.getScaleY() - 0.1f);
            }

            if (MainActivity.imageView.getScaleX() < 0.5)
            {
                MainActivity.imageView.setScaleX(0.5f);
                MainActivity.imageView.setScaleY(0.5f);
            }
        }
        else{
            MainActivity.imageView.scrollBy((int)distanceX, (int)distanceY);
        }
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d("TAG", "onFling: ");

        return true;
    }
}
