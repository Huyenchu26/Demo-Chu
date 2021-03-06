package mq.com.chuohapps.ui.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import mq.com.chuohapps.utils.data.DataFormatUtils;
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

    public void updateItemDone(RecyclerView recyclerView, String imei, String numberCar) {
        if (imei != null) {
            for (Vehicle vehicle : data) {
                if (vehicle.imei.equalsIgnoreCase(imei)) {
                    vehicle.isUpdate = true;
                    vehicle.numberCar = numberCar;
                    notifyDataSetChanged();
                    break;
                }
            }
        }
        updateViewHolder(recyclerView, imei);
    }

    public Vehicle getFirstItem() {
        if (data != null)
            return data.get(0);
        else return new Vehicle();
    }

    @Override
    protected ItemViewHolder getCustomItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(createView(parent, R.layout.item_vehicle_child));
    }

    public interface ItemListener extends BaseAdapter.BaseItemListener {
        void onImageLocationClick(String imei, String longi, String lati);

        void onOpenDialogRfid(List<String> rfid, String imei);

        void onItemListener(Vehicle vehicle);

        void onItemLongClick(String imei, String numberCar);
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
        @BindView(R.id.txtDateTimeBegin)
        TextView txtDateTimeBegin;
        @BindView(R.id.txtFullLine)
        TextView txtFullLine;
        @BindView(R.id.linearName)
        RelativeLayout linearName;

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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (itemListener != null) {
                        Vehicle vehicle = data.get(getAdapterPosition());
                        itemListener.onItemLongClick(vehicle.imei, vehicle.numberCar);
                    }
                    return false;
                }
            });
        }

        protected void bindData(int position) {
            super.bindData(position);
            this.position = position;
            needUpdate = false;
            isBindData = true;

            final Vehicle vehicle = data.get(position);
            final Vehicle vehicleData = vehicle;

            if (vehicleData.imei == null) data.remove(position);
            linearName.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null)
                        itemListener.onItemListener(vehicleData);
                }
            });
            if (vehicleData.numberCar == null)
                txtImei.setText(DataFormatUtils.getString(vehicleData.imei));
            else
                txtImei.setText(DataFormatUtils.getString(vehicleData.imei) + " - " + vehicleData.numberCar);
            txtDatetime.setText(DateUtils.convertServerDateToUserTimeZone(vehicleData.dateTime));
            imgLocation.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null)
                        itemListener.onImageLocationClick(DataFormatUtils.getString(vehicleData.imei),
                                vehicleData.longitude, vehicleData.latitude);
                }
            });

//            if (DataFormatUtils.getString(vehicleData.status).equals("1"))
//                imgRunning.setImageResource(R.mipmap.icon_running);
//            else
//                imgRunning.setImageResource(R.mipmap.icon_stop);
            imgRunning.setImageResource(R.mipmap.ic_rfid);
            imgRunning.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null && DataFormatUtils.getString(vehicleData.rfidList) != null)
                        itemListener.onOpenDialogRfid(
                                GetRFID.getRFID(DataFormatUtils.getString(vehicleData.rfidList)),
                                vehicle.imei);
                }
            });
            try {
                txtFirmWare.setText(" Firmware: " + DataFormatUtils.getString(vehicleData.firmware));
                txtCPUtime.setText(" CPU time: " + DataFormatUtils.getString(vehicleData.cpuTime));
                txtLocation.setText(" Location: " + DataFormatUtils.getString(vehicleData.latitude)
                        + " - " + DataFormatUtils.getString(vehicleData.longitude));
                txtCam.setText(" Camera image: " + DataFormatUtils.getString(vehicleData.frontCam)
                        + " - " + DataFormatUtils.getString(vehicleData.backCam));
                txtSize.setText(Long.valueOf(DataFormatUtils.getString(vehicleData.size)) / 1024 + " KB");
                txtDateTimeBegin.setText(DataFormatUtils.getString(vehicleData.firstTime) + "");
                txtFullLine.setText(DataFormatUtils.getString(vehicleData.lineAll));
            } catch (Exception e) {
            }

            if (data.size() == 1) txtFullLine.setVisibility(View.VISIBLE);
            else txtFullLine.setVisibility(View.GONE);
            setTrafficLight(vehicleData);
            setAnimation(itemView, position);
            if (vehicleData.isUpdate)
                txtImei.setTextColor(itemView.getResources().getColor(R.color.colorStatusError));
            else
                txtImei.setTextColor(itemView.getResources().getColor(R.color.colorAccentPressedLight));
        }

        private void setTrafficLight(Vehicle vehicle) {
            if (DataFormatUtils.getString(vehicle.sos).equals("1"))
                imgSOS.setImageResource(R.drawable.bg_traffic_light);
            else imgSOS.setImageResource(R.drawable.bg_traffic_dark);
            imgGPS.setImageResource(R.drawable.bg_traffic_light);
            if (DataFormatUtils.getString(vehicle.engine).equals("1"))
                imgEngine.setImageResource(R.drawable.bg_traffic_light);
            else imgEngine.setImageResource(R.drawable.bg_traffic_dark);
            if (DataFormatUtils.getString(vehicle.trunk).equals("1"))
                imgTrunk.setImageResource(R.drawable.bg_traffic_light);
            else imgTrunk.setImageResource(R.drawable.bg_traffic_dark);
            if (DataFormatUtils.getString(vehicle.status).equals("1"))
                imgStatus.setImageResource(R.drawable.bg_traffic_light);
            else imgStatus.setImageResource(R.drawable.bg_traffic_dark);
            textGPS.setText(DataFormatUtils.getString(vehicle.posStatus) != null ?
                    DataFormatUtils.getString(vehicle.posStatus) : "--");
        }
    }

}
