package mq.com.chuohapps.ui.history.sos.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.xbase.BaseAdapter;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.data.DateUtils;

public class SOSAdapter extends BaseAdapter<SOSAdapter.ItemViewHolder, SOSAdapter.ItemListener, Vehicle> {

    @Override
    protected SOSAdapter.ItemViewHolder getCustomItemViewHolder(ViewGroup parent) {
        return new SOSAdapter.ItemViewHolder(createView(parent, R.layout.item_sos));
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
        @BindView(R.id.txtGPS)
        TextView txtGPS;
        @BindView(R.id.txtCamera)
        TextView txtCamera;

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
            try {
                textView.setText(DateUtils.convertServerDateToUserTimeZone(vehicle.dateTime));
                txtCamera.setText("Front: " + vehicle.frontCam + " - Back" + vehicle.backCam);
            } catch (Exception e) {
                AppLogger.error(e.getMessage());
            }
            setTrafficLight(vehicle);
        }

        private void setTrafficLight(Vehicle vehicle) {
            if (vehicle.sos.equals("1"))
                imgSOS.setImageResource(R.drawable.bg_traffic_light);
            else imgSOS.setImageResource(R.drawable.bg_traffic_dark);
            imgGPS.setImageResource(R.drawable.bg_traffic_light);
            if (vehicle.engine.equals("1"))
                imgEngine.setImageResource(R.drawable.bg_traffic_light);
            else imgEngine.setImageResource(R.drawable.bg_traffic_dark);
            if (vehicle.trunk.equals("1"))
                imgTrunk.setImageResource(R.drawable.bg_traffic_light);
            else imgTrunk.setImageResource(R.drawable.bg_traffic_dark);
            if (vehicle.status.equals("1"))
                imgStatus.setImageResource(R.drawable.bg_traffic_light);
            else imgStatus.setImageResource(R.drawable.bg_traffic_dark);
            txtGPS.setText(vehicle.posStatus != null ? vehicle.posStatus : "--");
        }
    }
}

