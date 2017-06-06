package com.shadow.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtTimer;
    SeekBar seekTimer;
    MediaPlayer player;
    CountDownTimer timer;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimer = (TextView)findViewById(R.id.txtTime);
        seekTimer = (SeekBar)findViewById(R.id.seekTimer);
        seekTimer.setMax(600); // 600 seconds = 10 minutes
        seekTimer.setProgress(30); // start at 30 seconds
        seekTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        player = MediaPlayer.create(this, R.raw.airhorn);

    }

    public void controlTimer(View View)
    {
        btnStart = (Button)findViewById(R.id.btnStart);
        if(btnStart.getText().equals("Go!"))
        {
            btnStart.setText("Stop");
            timer = new CountDownTimer(seekTimer.getProgress() * 1000 + 100, 1000)
            {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int)(millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    seekTimer.setEnabled(true);
                    txtTimer.setText("00:00");
                    player.start();
                    btnStart.setText("Go!");
                }
            }.start();
            seekTimer.setEnabled(false);
        }
        else
        {
            timer.cancel();
            btnStart.setText("Go!");
            updateTimer(seekTimer.getProgress());
            seekTimer.setEnabled(true);
        }
    }

    private void updateTimer(int secondsLeft)
    {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60; // Or you could do progress - minutes * 60 (Rob way)
        txtTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
