package mq.com.chuohapps.utils.views;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class AnimationUtils {
    private AnimationUtils() {
    }

    public static void showAnswer(View v, Animation.AnimationListener animationListener) {
        AnimationSet rootSet = new AnimationSet(true);
        ScaleAnimation scaleAnimationExpand = new ScaleAnimation(
                0.5f, 1f,
                0.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimationExpand.setDuration(300);

        AlphaAnimation alphaAnimationExpand = new AlphaAnimation(0, 1);
        alphaAnimationExpand.setDuration(150);
        ScaleAnimation scaleAnimationCollapse = new ScaleAnimation(
                1f, 0.5f,
                1f, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimationCollapse.setDuration(300);
        scaleAnimationCollapse.setStartOffset(500);
        AlphaAnimation alphaAnimationCollapse = new AlphaAnimation(1, 0);
        alphaAnimationCollapse.setDuration(150);
        alphaAnimationCollapse.setStartOffset(650);
        rootSet.addAnimation(alphaAnimationExpand);
        rootSet.addAnimation(scaleAnimationExpand);
        rootSet.addAnimation(scaleAnimationCollapse);
        rootSet.addAnimation(alphaAnimationCollapse);
        rootSet.setFillBefore(true);
        rootSet.setFillAfter(true);
        rootSet.setAnimationListener(animationListener);
        v.startAnimation(rootSet);

    }
}
