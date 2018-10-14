package mq.com.chuohapps.ui.xbase;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.OnClickListener;

/**
 * Created by nguyen.dang.tho on 2/21/2018.
 */

public abstract class BaseAdapter<IVH extends BaseAdapter.BaseItemViewHolder, IL extends BaseAdapter.BaseItemListener, DT> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING_MORE = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private static final int VIEW_TYPE_LIST_ERROR = 3;
    private static final int VIEW_TYPE_LIST_EMPTY = 4;
    private static final int VIEW_TYPE_LIST_LOAD_MORE_ERROR = 5;
    protected List<DT> data = new ArrayList<>();
    protected IL itemListener;
    private boolean showLoadingMore = false;
    private boolean showLoading = true;
    private boolean isError = false;

    /*Xử lý dữ liệu*/
    public void addData(List<DT> materials) {
        int startPosition = data.size();
        int endPosition = startPosition;
        if (materials != null && !materials.isEmpty()) {
            this.data.addAll(materials);
            endPosition = data.size();
        }
        loadingOff(false);
        if (endPosition != startPosition) notifyItemRangeChanged(startPosition, endPosition);
        else notifyDataSetChanged();

    }

    public void delItem(int index) {
        if (data != null)
            if (index < data.size())
                this.data.remove(index);
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
        loadingOff(false);
        showLoading = true;
        notifyDataSetChanged();
    }

    /*Xử lý trạng thái*/
    public void loadingOn(int currentPage) {

        if (currentPage == 0 && !showLoading) {
            isError = false;
            showLoading = true;
            notifyItemRangeChanged(0, 1);
        } else if (!showLoading) {
            showLoadingMore = true;
            if (isError) {
                isError = false;
                // notifyItemChanged(data.size() + 1);
                notifyItemRemoved(data.size());
                notifyItemRangeChanged(data.size(), data.size() + 1);
            } else
                notifyItemRangeChanged(data.size(), data.size() + 1);
        }

    }

    public void loadingOff(boolean isError) {
        showLoadingMore = false;
        showLoading = false;
        if (isError) errorOn();
    }

    private void errorOn() {
        isError = true;
        notifyItemRemoved(data.size());
        notifyItemRangeChanged(data.size(), data.size() + 1);
    }

    protected void updateViewHolder(RecyclerView recyclerView, String code) {
        if (recyclerView == null) return;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            BaseItemViewHolder itemViewHolder = ((IVH) recyclerView.getChildViewHolder(recyclerView.getChildAt(i)));
            if (itemViewHolder.code.equals(code))
                onBindViewHolder(itemViewHolder, itemViewHolder.position, null);
        }
    }

    public void refreshViewHolder(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            BaseItemViewHolder itemViewHolder = ((IVH) recyclerView.getChildViewHolder(recyclerView.getChildAt(i)));
            if (itemViewHolder.needUpdate)
                onBindViewHolder(recyclerView.getChildViewHolder(recyclerView.getChildAt(i)),
                        itemViewHolder.position,
                        null);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM)
            return getCustomItemViewHolder(parent);
        else if (viewType == VIEW_TYPE_LOADING_MORE)
            return new EmptyViewHolder(createView(parent, R.layout.x_item_loading_more_linear));
        else if (viewType == VIEW_TYPE_LOADING)
            return new EmptyViewHolder(createView(parent, R.layout.x_layout_list_loading));
        else if (viewType == VIEW_TYPE_LIST_ERROR)
            return new ErrorViewHolder(createView(parent, R.layout.x_layout_list_error));
        else if (viewType == VIEW_TYPE_LIST_LOAD_MORE_ERROR)
            return new ErrorViewHolder(createView(parent, R.layout.x_layout_list_load_more_error));
        else
            return new EmptyViewHolder(createView(parent, R.layout.x_layout_list_empty));
    }

    protected abstract IVH getCustomItemViewHolder(ViewGroup parent);

    protected View createView(ViewGroup parent, int layoutResource) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseItemViewHolder)
            ((IVH) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        if (showLoading || isError || data.size() == 0) return data.size() + 1;
        return showLoadingMore ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoading) return VIEW_TYPE_LOADING;
        if (isError) {
            if (data.size() == 0)
                return VIEW_TYPE_LIST_ERROR;
            else if (position == data.size()) return VIEW_TYPE_LIST_LOAD_MORE_ERROR;
        }
        if (data.size() == 0) return VIEW_TYPE_LIST_EMPTY;
        if (position == data.size()) return VIEW_TYPE_LOADING_MORE;
        return VIEW_TYPE_ITEM;
    }

    public void setItemListener(IL itemListener) {
        this.itemListener = itemListener;
    }

    public interface BaseItemListener {
        void onRetryClick();
    }

    public static abstract class BaseItemViewHolder extends RecyclerView.ViewHolder {

        public String code = "";
        public boolean needUpdate = false;
        public int position = -1;
        protected boolean isBindData = true;

        public BaseItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setupView();
        }

        protected abstract void setupView();

        protected void bindData(int position) {
            this.position = position;
            needUpdate = false;
            isBindData = true;
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ErrorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.buttonRetry)
        Button buttonRetry;

        ErrorViewHolder(View itemView) {
            super(itemView);
            setupView();
        }

        private void setupView() {
            ButterKnife.bind(this, itemView);
            buttonRetry.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null) itemListener.onRetryClick();
                }
            });
        }

    }
}
