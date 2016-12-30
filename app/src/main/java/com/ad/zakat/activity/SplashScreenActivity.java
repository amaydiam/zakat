package com.ad.zakat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.zakat.BuildConfig;
import com.ad.zakat.R; import com.ad.zakat.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.version_app)
    TextView versionApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            startActivity(new Intent(SplashScreenActivity.this, DrawerActivity.class));
            finish();
            return;
        }
        versionApp.setText(BuildConfig.VERSION_NAME);

        //thread for splash screen running
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, DrawerActivity.class));
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
