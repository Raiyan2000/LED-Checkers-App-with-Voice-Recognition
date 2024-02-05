package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    TextView playerWin;
    Button startNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        final MediaPlayer gameover = android.media.MediaPlayer.create(this, R.raw.gameoversound);

        gameover.start();


        startNew = findViewById(R.id.startNewBtn);

        startNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOver.this, SplashLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}