package mq.com.chuohapps.ui.xbase.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.R;
import mq.com.chuohapps.ui.xbase.container.ContainerActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ContainerActivity.class));
                overridePendingTransition(R.anim.splash_in, R.anim.splash_out);
                finish();
            }
        }, AppConfigs.SPLASH_SHOWING_DURATION);
    }
}
