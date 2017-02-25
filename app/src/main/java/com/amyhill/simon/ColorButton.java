package com.amyhill.simon;

import android.content.Context;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Amy on 2/20/2017.
 */

public class ColorButton extends Button {
    //Fields
    private int baseColor;
    private int flashColor;
    private int soundID;
    private int duration;
    private SoundPool soundPool;
    Context context;

    public ColorButton(Context context) {
        super(context);
        this.context = context;
    }

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    //Getters and Setters
    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }

    public int getFlashColor() {
        return flashColor;
    }

    public void setFlashColor(int flashColor) {
        this.flashColor = flashColor;
    }

    public int getSound() {
        return soundID;
    }

    public void setSound(int sound) {
        this.soundID = sound;
    }

    public void flashButton(int duration){
        this.duration = duration;
        new flashButtonTask().execute();
    }

    public void playSound(int duration){
        if(soundPool != null){
            soundPool.play(soundID,1.0f,1.0f,0,0,1.0f);
        }
    }

    class flashButtonTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            publishProgress();
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
               return null;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            setBackground(context.getDrawable(flashColor));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setBackground(context.getDrawable(baseColor));
        }
    }
}
