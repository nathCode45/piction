package app.ncore.piction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.ncore.piction.R;

public class FirstTurn extends AppCompatActivity {
    private Button btnFirstTurn;
    private TextView tvTeamFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_turn);

        btnFirstTurn = findViewById(R.id.btnStartFirstTurn);
        tvTeamFirst = findViewById(R.id.tvTeamFirst);
        tvTeamFirst.setText(DataHolder.getTeam1().getName());


    }

    public void onStartFirst(View view) {
        startActivity(new Intent(this, Gameplay.class));
        finish();
    }
}