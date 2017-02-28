package com.amyhill.simon;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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
    private int id;
    private int radius;
    private boolean makesSound;
    private SoundPool soundPool;
    Context context;


    public ColorButton(Context context) {
        super(context);
        initialConstructorSetup(context);
    }

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialConstructorSetup(context);
    }

    //Getters and Setters
    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = ContextCompat.getColor(context, baseColor);
    }

    public int getFlashColor() {
        return flashColor;
    }

    public void setFlashColor(int flashColor) {
        this.flashColor = ContextCompat.getColor(context, flashColor);
    }

    public void setSoundPool(SoundPool sp){this.soundPool = sp; makesSound = true;}

    public int getSound() {
        return soundID;
    }

    public void setSound(int sound) {
        this.soundID = sound;
    }

    public void setId(int id){this.id = id;}

    public int getID(){return id;}

    //Public methods
    //A method for setting up all the details not set in the contructors.
    public void setUpButton(int baseColor, int flashColor, OnClickListener listener, int radius){
        this.baseColor = ContextCompat.getColor(context, baseColor);
        this.flashColor = ContextCompat.getColor(context, flashColor);
        this.radius = radius;
        this.setOnClickListener(listener);
        generateBackground(false);
    }

    //Starts the async task that flashes and makes the button sound.
    public void pokeButton(int duration){
        if(flashColor != 0 && baseColor != 0) {
            this.duration = duration;

            generateBackground(true);
            if(makesSound) {
                soundPool.play(getSound(), .5f, 1.0f, 0, 0, 1.0f);
            }

            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateBackground(false);
                }
            }, duration);
        }
    }

    //Private helper methods
    private void generateBackground(boolean isFlashColor) {
        GradientDrawable background = new GradientDrawable();
        background.setStroke(10, ContextCompat.getColor(context, R.color.colorPrimaryDark));
        background.setCornerRadius(radius);
        if (isFlashColor) {
            background.setColor(flashColor);
        } else {
            background.setColor(baseColor);
        }
        setBackground(background);
    }

    //Helper for constructor
    private void initialConstructorSetup(Context context) {
        this.context = context;
        this.baseColor = 0;
        this.flashColor = 0;
        makesSound = false;
    }
    
    //Task that flashes and makes noise for button.
    class pokeButtonTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
               return null;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            generateBackground(true);
            if(makesSound) {
                soundPool.play(getSound(), .5f, 1.0f, 0, 0, 1.0f);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            generateBackground(false);
        }
    }
}
