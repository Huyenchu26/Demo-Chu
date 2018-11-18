package mq.com.chuohapps.ui.rfid.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.xbase.BaseAdapter;

public class RFIDAdapter extends BaseAdapter<RFIDAdapter.ItemViewHolder, RFIDAdapter.ItemListener, Vehicle> {

    @Override
    protected ItemViewHolder getCustomItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(createView(parent, R.layout.item_rfid_frgmt));
    }

    public interface ItemListener extends BaseAdapter.BaseItemListener {

    }

    class ItemViewHolder extends BaseAdapter.BaseItemViewHolder {

        public String code = "";
        public boolean needUpdate = false;
        public int position = -1;
        protected boolean isBindData = true;

        @BindView(R.id.txtHour)
        TextView textHour;
        @BindView(R.id.rfidYes)
        TextView textRFIDYes;
        @BindView(R.id.rfidNo)
        TextView textRFIDNo;


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

            final Vehicle vehicle = data.get(position);
            textHour.setText(String.valueOf(position + 1));
            textRFIDYes.setText("");
            textRFIDNo.setText("");
        }
    }
}