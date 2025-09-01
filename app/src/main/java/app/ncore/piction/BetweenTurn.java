package app.ncore.piction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.ncore.piction.R;

public class BetweenTurn extends AppCompatActivity implements PauseAlertFrag.PauseDialogListener, AllPlayDialog.AllPlayReadyListener{
    private TextView tvTeam1, tvTeam2, tvTeam1Score, tvTeam2Score, tvFailWord, tvNextTurn;
    private Button btnNextTurn;
    private String word;
    private int turnCount, lastTeam;
    private ValueAnimator animator;
    private boolean animated, nextIsAllPlay, success;

    public static final int ALLPLAY_INDICATOR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        word = bundle.getString("word");
        turnCount = bundle.getInt("turn_count");
        success = bundle.getBoolean("success");
        lastTeam = bundle.getInt("last_team");
        if(success){
            setContentView(R.layout.between_turn_success);
        }else{
            setContentView(R.layout.between_turn_fail);
            tvFailWord = findViewById(R.id.tvFailWord);
            tvFailWord.setText(word);
        }
        word = bundle.getString("word");


        tvTeam1 = findViewById(R.id.tvTeam1);
        tvTeam2 = findViewById(R.id.tvTeam2);
        tvTeam1Score = findViewById(R.id.tvTeam1Score);
        tvTeam2Score = findViewById(R.id.tvTeam2Score);
        tvNextTurn = findViewById(R.id.tvNextTurn);

        btnNextTurn = findViewById(R.id.btnNextTurn);
        //Log.d("DEBUG","Team 1 Score: "+Integer.toString(team1Score)+"  Team 2 Score: "+team2Score );

        tvTeam1.setText(DataHolder.getTeam1().getName());
        tvTeam2.setText(DataHolder.getTeam2().getName());

        animator = new ValueAnimator();
        animated = false;


        String nextTeam;
        final TextView tvTurnScore;
        final int turnScore;
        if(nextIsAllPlay){
            nextIsAllPlay=false;
        }
        if(lastTeam==1){
            tvTurnScore = tvTeam1Score;
            tvTeam2Score.setText(Integer.toString(DataHolder.getTeam2().getScore()));
            turnScore = DataHolder.getTeam1().getScore();
        }else{
            tvTurnScore = tvTeam2Score;
            tvTeam1Score.setText(Integer.toString(DataHolder.getTeam1().getScore()));
            turnScore = DataHolder.getTeam2().getScore();
        }
        if (turnCount % 2 == 0) { //means team two is signalled to go
            nextTeam = DataHolder.getTeam2().getName();

        } else { //means team one is signalled to go
            nextTeam = DataHolder.getTeam1().getName();
        }
        if(success && DataHolder.isAnimations()) {
            tvTurnScore.setText("");
            animator.setObjectValues(0, turnScore);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    tvTurnScore.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator.setEvaluator(new TypeEvaluator<Integer>() {
                public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                    return Math.round(startValue + (endValue - startValue) * fraction);
                }
            });
            animator.setDuration(100 * turnScore);
        }else{
            tvTeam2Score.setText(Integer.toString(DataHolder.getTeam2().getScore()));
            tvTeam1Score.setText(Integer.toString(DataHolder.getTeam1().getScore()));
        }


        if(DataHolder.isAllPlay()&&DataHolder.randomAllPlay()){
            tvNextTurn.setText(R.string.all_play);
            nextIsAllPlay = true;
        }else{
            tvNextTurn.setText(nextTeam);
        }



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !animated){
            if(success && DataHolder.isAnimations()) {
                animator.start();
                animated = true;
            }
        }
    }

    public void onNextTurnBtn(View view) {
        if(!nextIsAllPlay) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }else{
            DialogFragment frag = new AllPlayDialog();
            frag.show(getSupportFragmentManager(),"allplay");
        }
    }


    public void onSettingsClick(View view) {
        DialogFragment dialog = new PauseAlertFrag();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),"PauseAlertFrag");
        //TODO add a method to handle any settings changes that may result
    }

    @Override
    public void onPauseDialogResume() {
        //TODO add a method to handle any settings changes that may result
    }

    @Override
    public void onAllPlayReady() {
        Intent returnIntent = new Intent();
        setResult(ALLPLAY_INDICATOR, returnIntent); //3 indicates allplay
        finish();

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