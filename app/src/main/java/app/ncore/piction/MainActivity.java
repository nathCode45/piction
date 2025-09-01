package app.ncore.piction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import app.ncore.piction.R;

public class MainActivity extends AppCompatActivity {


    private androidx.appcompat.widget.Toolbar tbNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tbNav = findViewById(R.id.tbNav);
        setSupportActionBar(tbNav);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        DataHolder.setAnimations(sharedPreferences.getBoolean(getString(R.string.saved_animation_setting),true));
        Uri defaultRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(defaultRing==null){ //this accounts for if the user has not default alarm tone
            defaultRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if(defaultRing == null){
                defaultRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        DataHolder.setTimeUpRing(Uri.parse(sharedPreferences.getString(getString(R.string.saved_ringtone_setting), defaultRing.toString())));//sets ringer to sharedPref file or default if they have never used the app before








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_info:
                //launch an activity
                startActivity(new Intent(this, Info.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this,Settings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onStartBtn(View view) {
        Intent preGameIntent = new Intent(this, PreGame.class);
        startActivity(preGameIntent);

    }
}