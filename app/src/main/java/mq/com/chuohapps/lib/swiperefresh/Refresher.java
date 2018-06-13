package mq.com.chuohapps.lib.swiperefresh;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nguyen.dang.tho on 9/14/2017.
 */

public interface Refresher {
    void setup(View container, int layoutResource);

    void onBeginRefresh(Runnable doSomething);

    boolean isRefreshing();

    void showRefresh();

    void endRefresh();

    void release();

    ViewGroup getView();
}
