package com.amyhill.simon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nonam on 3/6/2017.
 */

public class GameRunner {
    public enum GameType implements Serializable {NORMAL, COLOR, POSITION, EXTREME}
    private final static int DURATION = 250;
    private final static int EXTREME_DURATION = 50;

    private int score;
    private int count;
    private SimonGame simonGame;
    private GameType gameType;
    private Highscore highscores;
    private ColorButton[] buttons;
    private PatternPlayer patternPlayer;


    public GameRunner(ColorButton[]buttons, GameType gameType) {
        this.buttons = buttons;
        this.gameType = gameType;
        highscores = new Highscore(buttons[0].getContext());
        highscores.readFile();
        patternPlayer = new PatternPlayer();
    }

    public void start(){
        simonGame = new SimonGame(SimonGame.Mode.PREVIOUS);
        simonGame.generatePattern();
        score = 0;
        count = 0;

        runAsyncPlayer();
    }

    public void stop() {
        stopAsyncPlayer();
    }

    public void play(ColorButton button){
        count++;
        if(gameType == GameType.EXTREME){
            button.pokeButton(EXTREME_DURATION);
        } else{
            button.pokeButton(DURATION);
        }

        boolean didSucceed = false;

        if(gameType == GameType.COLOR){
            didSucceed = simonGame.checkPattern(button.getIDColor());
        }
        else{
            for(int i = 0; i < buttons.length; i++){
                if(button.getId() == buttons[i].getId()){
                    didSucceed = simonGame.checkPattern(i);
                }
            }
        }

        if(gameType == GameType.COLOR || gameType == GameType.POSITION){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    shuffleButtons();
                }
            }, DURATION);

        }

        if(didSucceed){
            if(count == simonGame.getPatternLength()) {
                buttons[5].pokeButton(DURATION);
                simonGame.generatePattern();
                score++;
                runAsyncPlayer();
                count = 0;
            }
        } else {
            for (ColorButton item:
                 buttons) {
                item.playSound();
            }
            buttons[4].pokeButton(DURATION);
            toggleButtons(false);
            highscores.checkHighScore(score);
            loseDialog();
        }

    }

    private void runAsyncPlayer() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                patternPlayer = new PatternPlayer();
                patternPlayer.execute();
            }
        }, DURATION * 2);
    }

    private void stopAsyncPlayer(){
        patternPlayer.cancel(true);
        highscores.writeFile();
    }

    private void toggleButtons(boolean isEnabled){
        for(int i = 0; i < buttons.length - 2; i++){
            buttons[i].setEnabled(isEnabled);
        }
    }

    private void loseDialog(){
        Context context = buttons[0].getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                start();
            }
        });
//        builder.setMessage("Top Highscores\n1st: " +  highscores.getHighscore(0) + " "
//                + highscores.getName(0)+ " " + highscores.getGameType(0) + "\n2nd: "
//                + highscores.getHighscore(1)+ " " + highscores.getName(1) + " " + highscores.getGameType(1)
//                + "\n3rd: " + highscores.getHighscore(2) + " " + highscores.getName(2)+ " "
//                + highscores.getGameType(2));

        builder.setMessage("Top Highscores\n1st: " +  highscores.getHighscore(0) + " "
                + highscores.getName(0)+ " " +  "\n2nd: "
                + highscores.getHighscore(1)+ " " + highscores.getName(1) + " "
                + "\n3rd: " + highscores.getHighscore(2) + " " + highscores.getName(2)+ " ");

        builder.setTitle("You Lost");

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void shuffleButtons(){
        ArrayList<ColorButton.Color> numbers = new ArrayList<>();
        ArrayList<ColorButton.Color> shuffledNumbers = new ArrayList<>();
        Random random = new Random();

        numbers.add(ColorButton.Color.BLUE);
        numbers.add(ColorButton.Color.GREEN);
        numbers.add(ColorButton.Color.YELLOW);
        numbers.add(ColorButton.Color.RED);

        for(int i = 0; i < 4; i++){
            int number = random.nextInt(4-i);
            shuffledNumbers.add(numbers.get(number));
            numbers.remove(number);
        }

        for(int i = 0; i < 4; i++) {
            buttons[i].setColor(shuffledNumbers.get(i));
        }


    }

    class PatternPlayer extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            for(int i = 0; i < simonGame.getPatternLength(); i++){
                if(gameType == GameType.EXTREME){
                    if (sleep(EXTREME_DURATION )) return null;
                    publishProgress(simonGame.getPatternItem(simonGame.getPatternLength() - i - 1));
                    if (sleep(100 )) return null;

                }
                else{
                    if (sleep(DURATION )) return null;
                    publishProgress(simonGame.getPatternItem(i));
                    if (sleep(100)) return null;
                }
            }

            return null;
        }

        private boolean sleep(int duration) {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            toggleButtons(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            toggleButtons(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (gameType == GameType.EXTREME) {
                buttons[values[0]].pokeButton(EXTREME_DURATION);
            } else if(gameType == GameType.COLOR){
                for (ColorButton button:
                     buttons) {
                    if(button.getIDColor() ==  values[0]){
                        button.pokeButton(DURATION);
                    }
                }
            } else {
                buttons[values[0]].pokeButton(DURATION);
            }



        }
    }
}
