package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashLogin extends AppCompatActivity {

    private TextView name, loading;

    Animation topAnim, BottomAnim;

    private ImageView logo;

    private TextView topView1, topView2, topView3;
    private TextView bottomView1, bottomView2, bottomView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_login);


        topAnim = AnimationUtils.loadAnimation(this, R.anim.myanim);
        BottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottomanimation);

        topAnim.setDuration(3000);
        BottomAnim.setDuration(3000);

        logo = findViewById(R.id.imageViewCheckers);
        topView1 = findViewById(R.id.textViewtopGreen);
        topView2 = findViewById(R.id.textViewtopRed);
        topView3 = findViewById(R.id.textViewTopWhite);

        bottomView1 = findViewById(R.id.textViewbottomGreen);
        bottomView2 = findViewById(R.id.textViewbottomRed);
        bottomView3 = findViewById(R.id.textViewbottomWhite);

        logo.setAnimation(topAnim);
        topView1.setAnimation(topAnim);
        topView2.setAnimation(topAnim);
        topView3.setAnimation(topAnim);

        bottomView1.setAnimation(BottomAnim);
        bottomView2.setAnimation(BottomAnim);
        bottomView3.setAnimation(BottomAnim);

        //final boolean startNewMain = getIntent().getBooleanExtra("START_NEW_MAIN",true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(startNewMain)
//                {
                    Intent intent = new Intent(SplashLogin.this, HomeScreen.class);
                    //intent.putExtra("START_NEW_MAIN",false);
                    startActivity(intent);
                    finish();
                //}
            }
        }, 5000);
    }
}