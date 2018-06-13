package mq.com.chuohapps.customview;

import android.os.Handler;
import android.view.View;

public abstract class OnClickListener implements View.OnClickListener {
    private int DELAYED_TIME = 190;
    private boolean canClick = true;

    public OnClickListener() {
    }
    public OnClickListener(int DELAYED_TIME) {
        this.DELAYED_TIME = DELAYED_TIME;
    }

    @Override
    public void onClick(final View v) {
        if (!canClick) return;
        canClick = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onDelayedClick(v);
                canClick = true;
            }
        }, DELAYED_TIME);
    }

    public abstract void onDelayedClick(View v);
}
