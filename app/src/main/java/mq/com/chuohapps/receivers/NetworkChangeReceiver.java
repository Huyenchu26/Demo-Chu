package mq.com.chuohapps.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import mq.com.chuohapps.utils.functions.NetworkUtils;


public class NetworkChangeReceiver extends BroadcastReceiver {
    private Listener listener;

    public NetworkChangeReceiver() {
    }

    public NetworkChangeReceiver(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context != null && NetworkUtils.isNetworkConnected(context.getApplicationContext())) {
            if (listener != null)
                listener.onConnected();
        }
    }

    public interface Listener {
        void onConnected();
    }
}
