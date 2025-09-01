package app.ncore.piction;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.ncore.piction.R;

public class WordGenerator extends GameplayBase implements AllPlayDialog.AllPlayReadyListener, TimesUpDialog.TimesUpDialogListener {
    private TextView tvCategory;
    private ImageView ivCategoryIcon;
    private boolean wasAllPlay = false;
    private boolean timerRunning = false;
    private ImageView ivPausePlayIcon;
    private LinearLayout llBtnReset;
    private FrameLayout flBtnPlayPause;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSkip();
            }
        });
        tvCategory = findViewById(R.id.tvCategory);
        ivCategoryIcon = findViewById(R.id.ivCategoryIcon);
        if (DataHolder.isUseTimer()) {
            llBtnReset = findViewById(R.id.llBtnReset);
            flBtnPlayPause = findViewById(R.id.flBtnPlay);
            ivPausePlayIcon = findViewById(R.id.ivPausePlayIcon);
            timeBar = findViewById(R.id.timeBar);
            timeBar.setVisibility(View.VISIBLE);
            llBtnReset.setVisibility(View.VISIBLE);
            flBtnPlayPause.setVisibility(View.VISIBLE);
            if (DataHolder.isUseTimer()) {
                timeBar.setMax(DataHolder.getTimerLength() / factor);
                totalTimeLeft = DataHolder.getTimerLength();
                timeBar.setProgress(0);

            }


        }

    }

    @Override
    protected void onSkip() {
        super.onSkip();
        if (wasAllPlay) {
            ivCategoryIcon.setScaleX((float) 1);
            ivCategoryIcon.setScaleY((float) 1);
            wasAllPlay = false;
        }
        if (DataHolder.isAllPlay() && DataHolder.randomAllPlay()) {
            DialogFragment frag = new AllPlayDialog();
            frag.show(getSupportFragmentManager(), "allplay");
            tvCategory.setText(R.string.all_play);
            ivCategoryIcon.setImageResource(R.drawable.allplay_thickest);///TODO consider using all play symbol
            ivCategoryIcon.setScaleX((float) 1.2);
            ivCategoryIcon.setScaleY((float) 1.2);
            wasAllPlay = true;
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_word_generator;
    }


    public void onBackButton(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    @Override
    public void onBackPressed() {
        if(gameTimer!=null) {
            gameTimer.cancel();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onAllPlayReady() {

    }



    public void onPausePlay(View view) {
        if (!timerRunning) { //timer is paused
            gameTimer = createTimer(totalTimeLeft);
            gameTimer.start();
            timerRunning =true;
            ivPausePlayIcon.setImageResource(R.drawable.ic_pause);

        }else{
            gameTimer.cancel();
            gameTimer = null;
            timerRunning = false;
            ivPausePlayIcon.setImageResource(R.drawable.ic_play);
        }

    }


    public void onResetClick(View view) {
        if (gameTimer != null && timerRunning) { //this means the timer is currently running as opposed to already being paused
            //pause the timer
            gameTimer.cancel();
            gameTimer = null;
            timerRunning = false;
            ivPausePlayIcon.setImageResource(R.drawable.ic_play);
        }
        //reset the time and the timeBar
        totalTimeLeft=DataHolder.getTimerLength();
        timeBar.setProgress(0);



    }






}