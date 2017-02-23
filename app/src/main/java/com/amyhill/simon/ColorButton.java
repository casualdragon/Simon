package com.amyhill.simon;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.widget.ImageView;

/**
 * Created by Amy on 2/20/2017.
 */

public class ColorButton {
    //Fields
    private ImageView baseColor;
    private ImageView flashColor;
    private String sound;

    ColorButton(ImageView baseColor, ImageView flashColor, String sound){
        this.baseColor = baseColor;
        this.flashColor = flashColor;
        this.sound = sound;
    }

    //Getters and Setters
    public ImageView getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(ImageView baseColor) {
        this.baseColor = baseColor;
    }

    public ImageView getFlashColor() {
        return flashColor;
    }

    public void setFlashColor(ImageView flashColor) {
        this.flashColor = flashColor;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    //Methods
    public void playSound(SoundPool soundPool){

    }

}
