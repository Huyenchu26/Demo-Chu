package mq.com.chuohapps.ui.history.Trunk.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.ui.xbase.BaseAdapter;
import mq.com.chuohapps.utils.HistoryUtil;

public class TrunkAdapter extends BaseAdapter<TrunkAdapter.ItemViewHolder, TrunkAdapter.ItemListener,
        HistoryUtil.ItemTrunk> {

    @Override
    protected ItemViewHolder getCustomItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(createView(parent, R.layout.item_trunk));
    }

    public interface ItemListener extends BaseAdapter.BaseItemListener {
    }

    class ItemViewHolder extends BaseAdapter.BaseItemViewHolder {

        public String code = "";
        public boolean needUpdate = false;
        public int position = -1;
        protected boolean isBindData = true;

        @BindView(R.id.trunkNumber)
        TextView trunkNumber;
        @BindView(R.id.trunkNumberPicture1)
        TextView numberPictureFront;
        @BindView(R.id.trunkNumberPicture2)
        TextView numberPictureBehind;
        @BindView(R.id.trunkTimeLine)
        TextView trunkTimeLine;
        @BindView(R.id.trunkTime)
        TextView trunkTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupView() {

        }

        protected void bindData(int position) {
            super.bindData(position);
            needUpdate = false;
            isBindData = true;

            final HistoryUtil.ItemTrunk itemTrunk = data.get(position);

            trunkNumber.setText(Integer.toString(position + 1));
            trunkTimeLine.setText(itemTrunk.getTimeLine());
            numberPictureFront.setText(Integer.toString(itemTrunk.getFrontCam()));
            numberPictureBehind.setText(Integer.toString(itemTrunk.getBackCam()));
            trunkTime.setText(Double.toString(itemTrunk.getTime()));
        }
    }
}
