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
    private int colorID;
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
    public int getColorID() {
        return colorID;
    }

    public void setColorID(int baseColorID) {
        this.colorID = baseColorID;
     setBaseColor(ContextCompat.getColor(context, colorID));
    }

    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
        generateBackground(false);
    }

    public int getFlashColor() {
        return flashColor;
    }

    public void setFlashColorID(int flashColorID) {
        this.flashColor = ContextCompat.getColor(context, flashColorID);
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
    public void setUpButton(int baseColorID, int flashColorID, OnClickListener listener, int radius){
        this.colorID = baseColorID;
        this.baseColor = ContextCompat.getColor(context, baseColorID);
        this.flashColor = ContextCompat.getColor(context, flashColorID);
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

}
