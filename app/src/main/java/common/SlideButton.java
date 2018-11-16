package common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Ashish.Kumar on 23-05-2018.
 */

public class SlideButton extends android.support.v7.widget.AppCompatSeekBar {

    private Drawable thumb;
    private SlideButtonListener listener;

    public SlideButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        this.thumb = thumb;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (thumb.getBounds().contains((int) event.getX(), (int) event.getY())) {
                super.onTouchEvent(event);
            } else
                return false;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getProgress()> 90) {
                handleSlide();
                setProgress(getProgress());
            }else {
                setProgress(0);
            }
        } else
            super.onTouchEvent(event);

        return true;
    }

    private void handleSlide() {
        listener.handleSlide();
    }

    public void setSlideButtonListener(SlideButtonListener listener) {
        this.listener = listener;
    }


    public interface SlideButtonListener {
        public void handleSlide();
    }
}