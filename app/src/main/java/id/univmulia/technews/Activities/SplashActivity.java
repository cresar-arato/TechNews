package id.univmulia.technews.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import id.univmulia.technews.R;

public class SplashActivity extends AppCompatActivity {

    //loading aplikasi 1500ms = 1,5 detik
    private int waktu_loading = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //loading saat masuk aplikasi
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent home = new Intent(SplashActivity.this, PostActivity.class);
                startActivity(home);
                finish();

            }
        }, waktu_loading);
    }
}