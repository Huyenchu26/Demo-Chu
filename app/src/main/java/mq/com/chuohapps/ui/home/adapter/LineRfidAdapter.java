package mq.com.chuohapps.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mq.com.chuohapps.R;

public class LineRfidAdapter extends ArrayAdapter<String> {
    private List<String> listRfid = new ArrayList<>();

    public LineRfidAdapter(@NonNull Context context, List<String> listRfid) {
        super(context, 0);
        this.listRfid = listRfid;
    }


    public List<String> getData() {
        return this.listRfid;
    }

    public void addData(List<String> room) {
        if (room != null && !room.isEmpty()) {
            this.listRfid.addAll(room);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return listRfid.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String rfid = listRfid.get(position);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_rfid, parent,
                false);
        TextView lineRfid = (TextView) convertView.findViewById(R.id.lineRfid);
        lineRfid.setText(rfid);
        return convertView;
    }

}
