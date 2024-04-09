package ms.cs.countries_quiz;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {

    public interface OnSwipeListener {
        void onSwipeLeft();
        void onSwipeRight();
    }
    public static final String DEBUG_TAG = "OnSwipeTouchListener";
    private final GestureDetector gestureDetector;
    private OnSwipeListener listener;

    public OnSwipeTouchListener(Context context, OnSwipeListener listener) {
        Log.d(DEBUG_TAG, "OnSwipeTouchListener");
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(DEBUG_TAG, "onTouch");
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            Log.d("SwipeListener", "onFling: diffX=" + diffX + ", velocityX=" + velocityX);
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    listener.onSwipeRight();
                } else {
                    Log.d(DEBUG_TAG, "onSwipeLeft");
                    listener.onSwipeLeft();
                }
                return true;
            }
            return false;
        }
    }
}