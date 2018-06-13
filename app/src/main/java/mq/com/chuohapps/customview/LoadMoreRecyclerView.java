package mq.com.chuohapps.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class LoadMoreRecyclerView extends RecyclerView {

    private int currentPage = 0;
    private int maxPage = 0;
    private boolean isLoading = false;
    private boolean isLoadMoreError = false;
    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager linearLayoutManager;
    private int totalItemCount = 0;
    private int lastVisibleItem = 0;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        setDefaultLayoutManager(context);
        handleLoadMoreListener();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setDefaultLayoutManager(context);
        handleLoadMoreListener();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDefaultLayoutManager(context);
        handleLoadMoreListener();
    }

    public void updateLoadMore(int currentPage, int maxPage) {
        if (currentPage != -1) {
            this.currentPage = currentPage;
        }
        this.maxPage = maxPage;
    }

    public void refreshLoadMore() {
        currentPage = 0;
        maxPage = -1;
    }

    public int nextPage() {
        return currentPage + 1;
    }

    public int currentPage() {
        return currentPage;
    }

    public void loadingOn() {
        isLoading = true;
    }

    public void loadingOff() {
        isLoading = false;
    }

    public void errorOn() {
        isLoadMoreError = true;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void setDefaultLayoutManager(Context context) {
        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        linearLayoutManager = (LinearLayoutManager) getLayoutManager();
    }

    private void handleLoadMoreListener() {
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) isLoadMoreError = false;
                if (onLoadMoreListener != null && dy >= 0) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (totalItemCount > 0 && lastVisibleItem >= (totalItemCount - 1)) {
                        if (isLoading) return;
                        if (currentPage >= maxPage) return;
                        if (isLoadMoreError) return;
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });

    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
