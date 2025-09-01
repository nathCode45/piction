package app.ncore.piction;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import app.ncore.piction.R;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView feedback = findViewById(R.id.tvFeedback);
        feedback.setText(Html.fromHtml("<a href=\"mailto:pictionappdeveloper@gmail.com\">Contact the developer/Send feedback</a>"));
        feedback.setMovementMethod(LinkMovementMethod.getInstance());

        Toolbar toolbar = findViewById(R.id.tbNav);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }
}