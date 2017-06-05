package com.shadow.basicphrases;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public void playPhrase(View view)
    {
        Button phrase = (Button)view;
        String soundToPlay = phrase.getResources().getResourceEntryName(phrase.getId()); // Gets the id of the element (the actual string we named it (Ex: howareyou)
        Log.i("Phrase", soundToPlay);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + soundToPlay); // Can use this...

        //int resID = getResources().getIdentifier(soundToPlay, "raw", getPackageName());        // Or this; Either way works
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, uri);

        // Sometimes you can get a weird "MediaPlayer Error (-19, 0) or something like that. This
        // is because you don't free up the resources after playing the sound files multiple times.
        // Calling the onCompletion method to release the resources should fix that error
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
