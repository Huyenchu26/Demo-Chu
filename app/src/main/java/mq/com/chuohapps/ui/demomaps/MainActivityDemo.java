package mq.com.chuohapps.ui.demomaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mq.com.chuohapps.R;

public class MainActivityDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView hello = findViewById(R.id.hello);
        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityDemo.this, MapsActivityDemo.class));
            }
        });
    }
}
