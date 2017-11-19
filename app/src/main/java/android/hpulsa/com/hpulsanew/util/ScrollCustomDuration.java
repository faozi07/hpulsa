package android.hpulsa.com.hpulsanew.util;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by regopantes_apps on 19/11/17.
 */

public class ScrollCustomDuration extends Scroller {

    private double mScrollFactor = 2;

    public ScrollCustomDuration(Context context) {
        super(context);
    }

    public ScrollCustomDuration(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ScrollCustomDuration(Context context,
                                  Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy,
                            int duration) {
        super.startScroll(startX, startY, dx, dy,
                (int) (duration * mScrollFactor));
    }

}

