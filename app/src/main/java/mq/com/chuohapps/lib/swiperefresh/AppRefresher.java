package mq.com.chuohapps.lib.swiperefresh;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import mq.com.chuohapps.R;

public class AppRefresher implements Refresher {
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void setup(View container, int layoutResource) {
        refreshLayout = (SwipeRefreshLayout) container.findViewById(layoutResource);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    public void onBeginRefresh(final Runnable doSomething) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                new Handler().postDelayed(doSomething, 0);
            }
        });
    }

    @Override
    public boolean isRefreshing() {
        return refreshLayout != null && refreshLayout.isRefreshing();
    }

    @Override
    public void endRefresh() {
        if (refreshLayout != null) {
            if (refreshLayout.isRefreshing())
                refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showRefresh() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void release() {
        endRefresh();
        refreshLayout = null;
    }

    @Override
    public ViewGroup getView() {
        return refreshLayout;
    }
}
