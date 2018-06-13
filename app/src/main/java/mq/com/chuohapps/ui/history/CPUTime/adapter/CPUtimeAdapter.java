package mq.com.chuohapps.ui.history.CPUTime.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.xbase.BaseAdapter;

public class CPUtimeAdapter extends BaseAdapter<CPUtimeAdapter.ItemViewHolder, CPUtimeAdapter.ItemListener, Vehicle> {

    @Override
    protected ItemViewHolder getCustomItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(createView(parent, R.layout.item_cpu_time));
    }

    public interface ItemListener extends BaseAdapter.BaseItemListener {

    }

    class ItemViewHolder extends BaseAdapter.BaseItemViewHolder {

        public String code = "";
        public boolean needUpdate = false;
        public int position = -1;
        protected boolean isBindData = true;

        @BindView(R.id.txt)
        TextView textView;

        @BindView(R.id.imgTrunk)
        ImageView imgTrunk;
        @BindView(R.id.imgGPS)
        ImageView imgGPS;
        @BindView(R.id.imgSOS)
        ImageView imgSOS;
        @BindView(R.id.imgStatus)
        ImageView imgStatus;
        @BindView(R.id.imgEngine)
        ImageView imgEngine;

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

            final Vehicle.Data vehicle = data.get(position).data;
            textView.setText(vehicle.getDateTime());
            setTrafficLight(vehicle);
        }

        private void setTrafficLight(Vehicle.Data vehicle) {
            if (vehicle.getSos().equals("1"))
                imgSOS.setImageResource(R.drawable.bg_traffic_light);
            else imgSOS.setImageResource(R.drawable.bg_traffic_dark);
            if (vehicle.getGps().equals("1"))
                imgGPS.setImageResource(R.drawable.bg_traffic_light);
            else imgGPS.setImageResource(R.drawable.bg_traffic_dark);
            if (vehicle.getEngine().equals("1"))
                imgEngine.setImageResource(R.drawable.bg_traffic_light);
            else imgEngine.setImageResource(R.drawable.bg_traffic_dark);
            if (vehicle.getTrunk().equals("1"))
                imgTrunk.setImageResource(R.drawable.bg_traffic_light);
            else imgTrunk.setImageResource(R.drawable.bg_traffic_dark);
            if (vehicle.getStatus().equals("1"))
                imgStatus.setImageResource(R.drawable.bg_traffic_light);
            else imgStatus.setImageResource(R.drawable.bg_traffic_dark);
        }
    }
}

