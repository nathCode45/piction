package app.ncore.piction;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import app.ncore.piction.R;

public class Gameplay extends GameplayBase implements PauseAlertFrag.PauseDialogListener, TimesUpDialog.TimesUpDialogListener {

    private boolean resetTimerOnSkip= true;
    private boolean timerPaused=false;
    private boolean currentAllPlay = false;






    private int turnCount = 1;



    private Button btnGuess,btnFail,btnGuessAllPlayTeam1;
    private LinearLayout llBtnReset;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            totalTimeLeft = savedInstanceState.getLong("total_time_left");
            turnCount = savedInstanceState.getInt("turn_count");
            timerPaused = savedInstanceState.getBoolean("timer_paused");
            gameTimer = createTimer(totalTimeLeft);
        }else{
            gameTimer = createTimer(DataHolder.getTimerLength());
        }

        timeBar = findViewById(R.id.timeBar);
        btnGuess = findViewById(R.id.btnGuess);
        btnGuessAllPlayTeam1 = findViewById(R.id.btnGuessAllPlayTeam1);
        llBtnReset = findViewById(R.id.llBtnReset);

        if(DataHolder.isKeepScore()&&getCurrentTeam().getSkips()>-1) { //make sure skips aren't infinite before setting button text
            btnSkip.setText(String.format(getString(R.string.btn_skip), getCurrentTeam().getSkips()));
        }else{
            btnSkip.setText(getString(R.string.btn_skip_no_team));
        }

        if(DataHolder.isUseTimer()) {
            timeBar.setMax(DataHolder.getTimerLength() / factor);
            if (!timerPaused) {
                gameTimer.start();
            }
        }else{
            btnFail = findViewById(R.id.btnFail);
            btnFail.setVisibility(View.VISIBLE);
            btnFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFailedIt();
                }
            });
        }
        mp =  MediaPlayer.create(getApplicationContext(),DataHolder.getTimeUpRing());

    }
    public void onProcessSkip(View view){
        if(DataHolder.isKeepScore()) {
            if (getCurrentTeam().getSkips() > -1) { //make sure skips aren't infinite before setting button text
                getCurrentTeam().useSkip();
                btnSkip.setText(String.format(getString(R.string.btn_skip), getCurrentTeam().getSkips()));
                super.onSkip();
                if (!(getCurrentTeam().getSkips() > 0)) {
                    btnSkip.setEnabled(false);
                }
            } else {
                super.onSkip();
            }
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("total_time_left",totalTimeLeft);
        outState.putInt("turn_count", turnCount);
        outState.putBoolean("timer_paused",timerPaused);
        outState.putString("word",word);


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.show_word;
    }
    public void onGuessedIt(View view) {
        if(!currentAllPlay){
            addCurrentTeamScore(1);
        }
        launchBetween(true, 2);
    }
    public void onTeam1GuessedIt(View view) {//this button can only be pressed during an all play round
        launchBetween(true,1);
    }
    public void onFailedIt(){
        launchBetween(false, 0);
        //don't need to call cancelTimer since there is not timer to begin with
    }

    @Override
    protected void flash(String word, boolean first) {
        super.flash(word, first);
        if(resetTimerOnSkip && gameTimer!=null){
            resetTimer();
        }
    }




    protected void launchBetween(boolean success, int allPlaySuccessTeam){
        int lastTeam = 2-(turnCount%2); //will =1 if team 1 went last and will=2 if team 2 went last
        if(!currentAllPlay) {
            nextTurn(success); // increments if it isn't alternate turns or it wasn't a success
        }else{ // it is an allplay
            if(!DataHolder.isAlternateTurns()) {
                if(allPlaySuccessTeam!=0) {
                    turnCount = allPlaySuccessTeam; //whichever team was succesful will continue to take turns
                }else{//when timer runs out
                    nextTurn(false);//alternate turns
                }
            }
            switch (allPlaySuccessTeam) {
                    case 1:
                        DataHolder.getTeam1().incrementScore();
                        lastTeam = 1;
                        break;
                    case 2:
                        DataHolder.getTeam2().incrementScore();
                        lastTeam = 2;
                        break;

            }

            //Gameplay for this round is over so the next round will no longer be currentAllPlay necessarily
            currentAllPlay=false;
        }
        cancelTimer();
        Intent intent = new Intent(this, BetweenTurn.class);
        intent.putExtra("success", success);
        intent.putExtra("word",word);
        intent.putExtra("turn_count", turnCount);
        intent.putExtra("last_team",lastTeam);
        startActivityForResult(intent,2);
    }
    public void cancelTimer(){

        if (gameTimer!=null){
            gameTimer.cancel();
            //gameTimer = null;
        }
    }
    public void resetTimer(){
        if(gameTimer!=null) {
            gameTimer.cancel();
            gameTimer = null;
            gameTimer = createTimer(DataHolder.getTimerLength());
            gameTimer.start();
        }
    }
    private void addCurrentTeamScore(int value){
            if(DataHolder.isKeepScore()) {
                getCurrentTeam().incrementScore();
            }


    }
    private Team getCurrentTeam(){
        if ((turnCount % 2)  == 1) {
            return DataHolder.getTeam1();
        }else{
            return DataHolder.getTeam2();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        animated = false;
        if(resultCode ==RESULT_OK){
            btnGuessAllPlayTeam1.setVisibility(View.GONE);
            btnGuess.setText(getResources().getString(R.string.btn_success));//If you have performance issues these two lines might be able to only have happen after an all play is finished
            if (getCurrentTeam().getSkips() > -1) {
                btnSkip.setText(String.format(getString(R.string.btn_skip), getCurrentTeam().getSkips()));
                if (getCurrentTeam().getSkips() > 0) {
                    btnSkip.setEnabled(true);
                }
            }




        }else if(resultCode==BetweenTurn.ALLPLAY_INDICATOR){
            currentAllPlay = true;
            //TODO set buttons and such
            String buttonLabel = getResources().getString(R.string.team_guessed_it);
            btnGuessAllPlayTeam1.setText(String.format(buttonLabel,DataHolder.getTeam1().getName()));
            btnGuess.setText(String.format(buttonLabel,DataHolder.getTeam2().getName()));
            btnGuessAllPlayTeam1.setVisibility(View.VISIBLE);

        }
        flash(getNextWord(),true);
        resetTimer(); //checking whether timer is used is built into the method don't worry
    }

    private void nextTurn(boolean success){

        if (DataHolder.isAlternateTurns() || !success) {
            turnCount += 1;
        }


    }
    private void pauseTimer(){
        if(gameTimer!=null) {
            gameTimer.cancel();
            timerPaused = true;
        }

    }

    public void onResetClick(View view) {
        resetTimer();
    }

    @Override
    public void onPauseButton(View view) {
        super.onPauseButton(view);
        if(DataHolder.isUseTimer()){pauseTimer();}
    }

    public void onPauseDialogResume() {
        //TODO handle the possible change in settings

        if(DataHolder.isUseTimer()){
            gameTimer = createTimer(totalTimeLeft);
            gameTimer.start();
            timerPaused=false;
        }
        if(DataHolder.isHideResetTimer()){
            llBtnReset.setVisibility(View.GONE);
        }else{
            llBtnReset.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onTimesUpResume() {
        super.onTimesUpResume();
        launchBetween(false,0);
    }

    @Override
    public void onBackPressed(){
        AlertDialog box = ExitGame();
        box.show();
    }
    private AlertDialog ExitGame(){
        return new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to end the game?")
                .setIcon(R.drawable.ic_cancel)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        launchMainActivity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

    }

    private void launchMainActivity(){ //only called in ExitGame()
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }






}
