package com.amyhill.simon;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by Amy on 2/20/2017.
 */

public class ColorButton extends Button {
    public enum Color {BLUE, GREEN, YELLOW, RED}

    //Fields
    private Color color;
    private int soundID;
    private int radius;
    private SoundPool soundPool;
    Context context;

    private int [] baseColorIDs = {R.color.colorBlue, R.color.colorGreen, R.color.colorYellow, R.color.colorRed};
    private int [] flashColorIDs = {R.color.colorBlueFlash, R.color.colorGreenFlash, R.color.colorYellowFlash, R.color.colorRedFLash};


    public ColorButton(Context context) {
        super(context);
        this.context = context;
    }

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    //Getters and Setters
    public void setSoundPool(SoundPool soundPool){this.soundPool = soundPool;}
    public void setSoundID(int soundID){this.soundID = soundID;}
    public void setColor(Color color){
        this.color = color;
        generateBackground(false);
    }

    public int getIDColor(){return color.ordinal();}

    //Public methods
    //A method for setting up all the details not set in the contructors.
    public void setUpButton(Color color, OnClickListener listener, int radius){
        this.color = color;
        this.radius = radius;
        this.setOnClickListener(listener);
        generateBackground(false);
    }

    //Starts the async task that flashes and makes the button sound.
    public void pokeButton(int duration) {
        generateBackground(true);
        playSound();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateBackground(false);
            }
        }, duration);

    }

    //this is for a special button that defaults to red and flashes another color
    public void redTheButton(){
        for(int i = 0; i < baseColorIDs.length; i++){
            baseColorIDs[i] =  R.color.colorRed;
        }
        generateBackground(false);
    }


    public void playSound(){
        if (soundPool != null) {
            soundPool.play(soundID, .5f, 1.0f, 0, 0, 1.0f);
        }
    }


    //Private helper methods
    private void generateBackground(boolean isFlashColor) {
        GradientDrawable background = new GradientDrawable();
        background.setStroke(10, ContextCompat.getColor(context, R.color.colorPrimaryDark));
        background.setCornerRadius(radius);
        if (isFlashColor) {
            background.setColor(ContextCompat.getColor(context, flashColorIDs[this.color.ordinal()]));
        } else {
            background.setColor(ContextCompat.getColor(context, baseColorIDs[this.color.ordinal()]));
        }
        setBackground(background);
    }

}
