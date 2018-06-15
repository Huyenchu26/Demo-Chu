package mq.com.chuohapps.lib.swiperefresh;

import android.view.View;
import android.view.ViewGroup;

public interface Refresher {
    void setup(View container, int layoutResource);

    void onBeginRefresh(Runnable doSomething);

    boolean isRefreshing();

    void showRefresh();

    void endRefresh();

    void release();

    ViewGroup getView();
}
