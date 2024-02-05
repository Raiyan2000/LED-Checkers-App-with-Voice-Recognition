package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class HomeScreen extends AppCompatActivity {

    Button letsPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        letsPlay = findViewById(R.id.GetStartedButton);


        ImageView lightBulbView = findViewById(R.id.imageView);
        ImageView checkersView = findViewById(R.id.imageView3);


        final MediaPlayer background = MediaPlayer.create(this, R.raw.background);
        final MediaPlayer letsPlayBtnSound = MediaPlayer.create(this, R.raw.letsplaybutton);
        background.start();

        Glide.with(this)
                        .load(R.drawable.animatedlightbulb)
                        .into(lightBulbView);

        Glide.with(this)
                .load(R.drawable.animatedcheckerspart)
                .into(checkersView);


        letsPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.stop();
                letsPlayBtnSound.start();
                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}