package mq.com.chuohapps.ui.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import mq.com.chuohapps.utils.data.DateUtils;

/**
 * Created by Admin on 29/3/2018.
 */

public class VehicleAdapter extends BaseAdapter<VehicleAdapter.ItemViewHolder, VehicleAdapter.ItemListener, Vehicle> {

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            AppLogger.error("set animation");
            Animation animation = new AlphaAnimation(R.anim.splash_in, R.anim.splash_out);
            if (position < 4)
                animation.setStartOffset(position * 200);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    protected ItemViewHolder getCustomItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(createView(parent, R.layout.item_vehicle_child));
    }

    public interface ItemListener extends BaseAdapter.BaseItemListener {
        void onImageLocationClick(String imei, String longi, String lati);

        void onOpenDialogRfid(List<String> rfid);

        void onItemListener(Vehicle vehicle);
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
        @BindView(R.id.txtFirmWare)
        TextView txtFirmWare;
        @BindView(R.id.txtCPUtime)
        TextView txtCPUtime;
        @BindView(R.id.txtLocation)
        TextView txtLocation;
        @BindView(R.id.txtCam)
        TextView txtCam;
        @BindView(R.id.imgRunning)
        ImageView imgRunning;
        @BindView(R.id.txtSize)
        TextView txtSize;

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
        TextView textGPS;

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
            final Vehicle vehicleData = vehicle;
//            itemView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onDelayedClick(View v) {
//                    itemListener.onItemListener(vehicleData);
//                }
//            });
            txtImei.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null)
                        itemListener.onItemListener(vehicleData);
                }
            });
            txtDatetime.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null)
                        itemListener.onItemListener(vehicleData);
                }
            });
            txtImei.setText(vehicleData.imei);
            txtDatetime.setText(DateUtils.convertServerDateToUserTimeZone(vehicleData.dateTime));
            imgLocation.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null)
                        itemListener.onImageLocationClick(vehicleData.imei,
                                vehicleData.longitude,
                                vehicleData.latitude);
                }
            });

            if (vehicleData.status.equals("1"))
                imgRunning.setImageResource(R.mipmap.icon_running);
            else
                imgRunning.setImageResource(R.mipmap.icon_stop);
            imgRunning.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null)
                        if (vehicleData.rfidList == null) return;
                        else
                            itemListener.onOpenDialogRfid(GetRFID.getRFID(vehicleData.rfidList));
                }
            });
            try {
                txtFirmWare.setText(" Firmware: " + vehicleData.firmware);
                txtCPUtime.setText(" CPU time: " + vehicleData.cpuTime);
                txtLocation.setText(" Location: " + vehicleData.latitude + " - " + vehicleData.longitude);
                txtCam.setText(" Camera image: " + vehicleData.frontCam + " - " + vehicleData.backCam);
                txtSize.setText(vehicleData.size + " KB");
            } catch (Exception e) {
            }

            setTrafficLight(vehicleData);
            setAnimation(itemView, position);
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
            textGPS.setText(vehicle.posStatus != null ? vehicle.posStatus : "--");
        }
    }

}
