package app.ncore.piction;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import app.ncore.piction.R;

import java.io.IOException;
import java.util.Random;


public abstract class GameplayBase extends AppCompatActivity implements PauseAlertFrag.PauseDialogListener {

    protected boolean animated;
    protected ValueAnimator animator;

    private Random rand = new Random();

    protected String word;

    protected CountDownTimer gameTimer;



    protected Button btnSkip;
    protected TextView wordText, tvCategory;
    protected ImageView ivCategoryIcon;

    protected MediaPlayer mp;
    private Context mContext;
    protected Vibrator vib;
    protected long totalTimeLeft;

    protected boolean activityIsStopped;
    protected boolean timerIsFinished;


    protected ProgressBar timeBar;
    protected final int factor = 10; //factor to divide time measure longs by
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(DataHolder.isKeepScore() && !DataHolder.isUseTimer() ) {
            setContentView(R.layout.show_word_no_timer);
        }else{
            setContentView(getLayoutResourceId());
        }
        if(savedInstanceState!=null){
            word = savedInstanceState.getString("word");
        }


        mContext = this;

        btnSkip = findViewById(R.id.btnSkip);
        wordText = findViewById(R.id.tvWord);
        tvCategory = findViewById(R.id.tvCategory);
        ivCategoryIcon = findViewById(R.id.ivCategoryIcon);


        animated =false;
        animator = new ValueAnimator();
        vib = ((Vibrator) getSystemService(VIBRATOR_SERVICE));

        flash(getNextWord(),true);
    }
    protected String getNextWord(){
        int categories = DataHolder.getCategoriesInUse().size();
        Category category = DataHolder.getCategoriesInUse().get(rand.nextInt(categories));
        tvCategory.setText(category.getCategoryName());
        ivCategoryIcon.setImageResource(category.getIconID());
        return category.yield();
    }




    protected abstract int getLayoutResourceId();

    public void onPauseButton(View view) {
        DialogFragment dialog = new PauseAlertFrag();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),"PauseAlertFrag");
    }


    protected void flash(final String word, boolean first) {//first is used so that the animation isn't started before onWindowFocusChanged()
        setWord(word);
        if(DataHolder.isAnimations()) {
            wordText.setText("");
            animator.setObjectValues("", word);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedFraction() == 1) {
                        wordText.setText(word);
                    } else {
//                        wordText.setText(animation.getAnimatedValue() + "_");
                        wordText.setText(getString(R.string.animate_underscore,animation.getAnimatedValue()));
                    }
                }
            });
            animator.setEvaluator(new TypeEvaluator<CharSequence>() {
                public CharSequence evaluate(float fraction,
                                             CharSequence startValue, CharSequence endValue) {
                    if (startValue.length() < endValue.length())
                        return endValue.subSequence(0,
                                (int) (endValue.length() * fraction));
                    else
                        return startValue.subSequence(0, startValue.length() - (int) ((startValue.length() - endValue.length()) * fraction));
                }
            });
            animator.setDuration(100 * word.length());
            ValueAnimator.setFrameDelay(200);
            if (!first) {
                animator.start();
            }
        }else{
            wordText.setText(word);
        }

    }
    protected void setWord(String word){
        this.word= word;

    }
    protected void onSkip() {
        flash(getNextWord(),false);
    }

    public CountDownTimer createTimer(final long timerLength){
        if(DataHolder.isUseTimer()) {
            timerIsFinished = false;
            return new CountDownTimer(timerLength, 5) { // arg 1: milliseconds in the future arg 2: count down interval
                int totalTime = (int) DataHolder.getTimerLength() / factor;
                int lastSecLeft;
                @Override
                public void onTick(long l) {
                    int timeLeft = (int) l / factor;
                    int secLeft = (int) l/1000;

                    totalTimeLeft = l;
                    timeBar.setProgress(totalTime - timeLeft);

                    if (secLeft!=lastSecLeft && secLeft<3) {//vibrate for the last 3 seconds
                        if (Build.VERSION.SDK_INT >= 26) {
                            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
                        }
                        lastSecLeft = secLeft;
                    }

                }

                @Override
                public void onFinish() {
                    timerIsFinished = true;
                    if (Build.VERSION.SDK_INT >= 26) {
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(250);
                    }
                        long[] pattern = {0, 500, 500};
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(pattern, 0);


                    mp = new MediaPlayer();
                    try {
                        mp.setDataSource(mContext, DataHolder.getTimeUpRing());
                        mp.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build());
                        mp.prepare();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                    if(!activityIsStopped) {
                        DialogFragment timesUp = new TimesUpDialog();
                        timesUp.setCancelable(false);
                        timesUp.show(getSupportFragmentManager(), "Time's up");
                    }

                }
            };
        }else{return null;}

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !animated && DataHolder.isAnimations()){
            animator.start();
            animated = true;

        }
    }

   @Override
    public void onPauseDialogResume() { }

    @Override
    protected void onStop() {
        super.onStop();
        if(mp!=null) {
            try {
                if(mp.isPlaying()) {
                    mp.pause();
                }
                vib.cancel();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        activityIsStopped = true;
    }



    public void onTimesUpResume() {
        if(mp!=null) {
            try {
                if (mp.isPlaying()) {
                    mp.stop();
                }
                mp.reset();
                mp.release();
            }catch (IllegalStateException is){
                is.printStackTrace();
            }

        }
        if(vib!=null) {
            vib.cancel();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        activityIsStopped = false;
        if(timerIsFinished){
            DialogFragment timesUp = new TimesUpDialog();
            timesUp.setCancelable(false);
            timesUp.show(getSupportFragmentManager(), "Time's up");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gameTimer!=null) {
            gameTimer.cancel();
            gameTimer = null;
        }
        boolean isPlaying;
        try{
           isPlaying =mp.isPlaying();
        }catch (Exception e){
            isPlaying = false;
        }
        if(mp!=null&&isPlaying) {
            mp.stop();
        }
    }





}
