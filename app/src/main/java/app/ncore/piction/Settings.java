package app.ncore.piction;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import app.ncore.piction.R;

public class Settings extends AppCompatActivity {

    public final int RINGTONEPICKER_REQ = 2;
    private TextView tvCurrentTone;
    private SwitchMaterial swtAnimations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolBar = findViewById(R.id.tbNav);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        swtAnimations = findViewById(R.id.swtAnimations);

        if(DataHolder.isAnimations()){
            swtAnimations.toggle();
        }

        tvCurrentTone = findViewById(R.id.tvCurrentTone);
        setRingtoneText();

    }

    public void startRingTonePicker(View view){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        startActivityForResult(intent,RINGTONEPICKER_REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RINGTONEPICKER_REQ && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            DataHolder.setTimeUpRing(uri);
            String ringtoneText = RingtoneManager.getRingtone(getApplicationContext(),
                    DataHolder.getTimeUpRing()).getTitle(getApplicationContext());
            if(ringtoneText.length()>19) {
                ringtoneText = ringtoneText.substring(0, 18) + "...";
                tvCurrentTone.setText(ringtoneText);
            }
            SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.saved_ringtone_setting),DataHolder.getTimeUpRing().toString());
            editor.apply();
            setRingtoneText();


        }
    }
    private void setRingtoneText(){
        String ringToneText = RingtoneManager.getRingtone(getApplicationContext(),
                DataHolder.getTimeUpRing()).getTitle(getApplicationContext());
        if(ringToneText.length()>19){
            ringToneText= ringToneText.substring(0,18)+"...";
        }
        tvCurrentTone.setText(ringToneText);
    }

    public void onProcessAnimationsSwitch(View view) {
        DataHolder.setAnimations(swtAnimations.isChecked());

        SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.saved_animation_setting),DataHolder.isAnimations());
        editor.apply();
    }
}