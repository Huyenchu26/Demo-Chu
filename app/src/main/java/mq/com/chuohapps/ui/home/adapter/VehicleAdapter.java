package mq.com.chuohapps.ui.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.xbase.BaseAdapter;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.GetRFID;

/**
 * Created by Admin on 29/3/2018.
 */

public class VehicleAdapter extends BaseAdapter<VehicleAdapter.ItemViewHolder, VehicleAdapter.ItemListener, Vehicle> {

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            AppLogger.error("set animation");
            Animation animation = new AlphaAnimation(R.anim.splash_in, R.anim.splash_out);
            if(position < 4)
                animation.setStartOffset(position * 200);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    protected ItemViewHolder getCustomItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(createView(parent, R.layout.item_vehicle_child));
    }

    public interface ItemListener extends BaseAdapter.BaseItemListener{
        void onImageLocationClick(String imei, String longi, String lati);

        void onOpenDialogRfid(List<String> rfid);

        void onItemListener(Vehicle.Data vehicle);
    }

    class ItemViewHolder extends BaseAdapter.BaseItemViewHolder {
        @Override
        protected void setupView() {

        }

        public String code = "";
        public boolean needUpdate = false;
        public int position = -1;
        protected boolean isBindData = true;

        @BindView(R.id.txtImei)
        TextView txtImei;
        @BindView(R.id.txtDateTime)
        TextView txtDatetime;
        @BindView(R.id.imgLocation)
        ImageView imgLocation;
        @BindView(R.id.positionStatus)
        TextView positionStatus;
        @BindView(R.id.txtFirmWare)
        TextView txtFirmWare;
        @BindView(R.id.txtCPUtime)
        TextView txtCPUtime;
        @BindView(R.id.imgRunning)
        ImageView imgRunning;

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
            ButterKnife.bind(this, itemView);
        }



        protected void bindData(int position) {
            super.bindData(position);
            this.position = position;
            needUpdate = false;
            isBindData = true;

            final Vehicle vehicle = data.get(position);
            final Vehicle.Data vehicleData = vehicle.data;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    itemListener.onItemListener(vehicleData);
                }
            });
            txtImei.setText(vehicleData.getImei());
            txtDatetime.setText(vehicleData.getDateTime());
            positionStatus.setText(" Trạng thái định vị: " + vehicleData.getPosStatus());
            imgLocation.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null)
                        itemListener.onImageLocationClick(vehicleData.getImei(),
                                vehicleData.getLongitude(),
                                vehicleData.getLatitude());
                }
            });

            if (vehicleData.getStatus().equals("1"))
                imgRunning.setImageResource(R.mipmap.icon_running);
            else
                imgRunning.setImageResource(R.mipmap.icon_stop);
            imgRunning.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null)
                        itemListener.onOpenDialogRfid(GetRFID.getRFID(vehicleData.getRfidList()));
                }
            });
            txtFirmWare.setText(" Firmware: " + vehicleData.getFirmware());
            txtCPUtime.setText(" CPU time: " + vehicleData.getCpuTime());

            setTrafficLight(vehicleData);
            setAnimation(itemView, position);
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
