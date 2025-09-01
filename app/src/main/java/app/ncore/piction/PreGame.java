package app.ncore.piction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import app.ncore.piction.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class PreGame extends AppCompatActivity {

    private ArrayList<OptionSelect> listOptions = new ArrayList<OptionSelect>();

    private static final int defaultTimerLength = 60;
    private static final int defaultSkipLimit = 3;

    private RadioButton rbInfiniteSkip, rbLimitedSkip;
    private SwitchMaterial swtKeepScore, swtAllPlay, swtTimer, swtAlternateTurns;
    private EditText etTeam1, etTeam2, etTimerLength, etSkipLimit;
    private TextView tvSeconds, tvSkips, tvSkipLimit, tvAlternateTurns, tvAlternateTurnsDescription;
    private RadioGroup rgWordSkip;
    private View divSkipLimit;
    private CheckBox cbEasy, cbMedium, cbHard;

    private LinearLayout llWordListOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);
        AppRater.app_launched(this);

        DataHolder.resetAllGameSettings();

        rbInfiniteSkip = findViewById(R.id.rbInfiniteSkip);
        rbLimitedSkip = findViewById(R.id.rbLimitedSkip);
        llWordListOptions = findViewById(R.id.llWordListOptions);
        swtKeepScore = findViewById(R.id.swtKeepScore);
        swtAllPlay = findViewById(R.id.swtAllPlay);
        swtTimer = findViewById(R.id.swtIncludeTimer);
        etTeam1 = findViewById(R.id.etTeam1);
        etTeam2 = findViewById(R.id.etTeam2);
        tvSeconds = findViewById(R.id.tvSeconds);
        tvSkips = findViewById(R.id.tvSkips);
        etTimerLength = findViewById(R.id.etTimerLength);
        rgWordSkip = findViewById(R.id.radioGroup2);
        etSkipLimit = findViewById(R.id.etSkipLimit);
        tvSkipLimit = findViewById(R.id.tvSkipLimit);
        divSkipLimit = findViewById(R.id.divSkipLimit);

        tvAlternateTurns = findViewById(R.id.tvAlternateTurns);
        tvAlternateTurnsDescription = findViewById(R.id.tvExplainAlternate);
        swtAlternateTurns = findViewById(R.id.swtAlternateTurns);

        cbEasy = findViewById(R.id.cbEasy);
        cbMedium = findViewById(R.id.cbMedium);
        cbHard = findViewById(R.id.cbHard);

        etSkipLimit.setText(String.format(Locale.getDefault(),"%d",defaultSkipLimit));
        etTimerLength.setText(String.format(Locale.getDefault(),"%d",defaultTimerLength));

        //setSupportActionBar((Toolbar)findViewById(R.id.tbNav));


        swtAlternateTurns.toggle();

        swtKeepScore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(swtKeepScore.isChecked()) {
                    etTeam1.setVisibility(View.VISIBLE);
                    etTeam2.setVisibility(View.VISIBLE);
                    rbInfiniteSkip.toggle();
                    rgWordSkip.setVisibility(View.VISIBLE);
                    tvSkipLimit.setVisibility(View.VISIBLE);
                    tvAlternateTurns.setVisibility(View.VISIBLE);
                    tvAlternateTurnsDescription.setVisibility(View.VISIBLE);
                    swtAlternateTurns.setVisibility(View.VISIBLE);
                }else{
                    etTeam1.setVisibility(View.GONE);
                    etTeam2.setVisibility(View.GONE);
                    rgWordSkip.setVisibility(View.GONE);
                    tvSkipLimit.setVisibility(View.GONE);
                    tvSkips.setVisibility(View.GONE);
                    etSkipLimit.setVisibility(View.GONE);
                    tvAlternateTurns.setVisibility(View.GONE);
                    tvAlternateTurnsDescription.setVisibility(View.GONE);
                    swtAlternateTurns.setVisibility(View.GONE);
                }
        }});

        swtTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(swtTimer.isChecked()){
                    etTimerLength.setVisibility(View.VISIBLE);

                    tvSeconds.setVisibility(View.VISIBLE);
                }else{
                    etTimerLength.setVisibility(View.GONE);
                    tvSeconds.setVisibility(View.GONE);
                }

            }
        });
        rgWordSkip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rbLimitedSkip.isChecked()){
                    etSkipLimit.setVisibility(View.VISIBLE);
                    tvSkips.setVisibility(View.VISIBLE);

                }else{
                    etSkipLimit.setVisibility(View.GONE);
                    tvSkips.setVisibility(View.GONE);
                }
            }
        });


        Category objects = new Category(
                R.array.objects_easy,
                R.array.objects_medium,
                R.array.objects_hard,
                "Objects",
                R.drawable.ic_object,
                "Lifeless things that can be seen or touched");
        Category persons = new Category(
                R.array.persons_easy,
                R.array.persons_medium,
                R.array.persons_hard,
                "Persons",
                R.drawable.ic_people,
                "All types of different people"
        );

        Category actions = new Category(
                R.array.actions_easy,
                R.array.actions_medium,
                R.array.actions_hard,
                "Actions",
                R.drawable.ic_action,
                "Verbs"
        );

        Category cAbstract = new Category(
                R.array.abstract_easy,
                R.array.abstract_medium,
                R.array.abstract_hard,
                "Abstract",
                R.drawable.ic_abstract,
                "Miscellaneous from the other categories, or conceptual"
        );

        Category celebrities = new Category(
                R.array.celebrities_easy,
                R.array.celebrities_medium,
                R.array.celebrities_hard,
                "Celebrities",
                R.drawable.ic_celebrities,
                "Famous people from the present and the past"
        );
        Category places = new Category(
                R.array.place_easy,
                R.array.place_medium,
                R.array.place_hard,
                "Places",
                R.drawable.ic_places,
                "Locations, both specific and general"
        );
        Category animals = new Category(
                R.array.animals_easy,
                R.array.animals_medium,
                R.array.animals_hard,
                "Animals",
                R.drawable.ic_animals,
                "All types of different living creatures"
        );

        DataHolder.setPersonsCategory(persons);
        DataHolder.setObjectsCategory(objects);
        listOptions.add(new OptionSelect("Persons",persons.getIconID(),persons));
        listOptions.add(new OptionSelect("Objects",objects.getIconID(),objects));
        listOptions.add(new OptionSelect(actions.getCategoryName(), actions.getIconID(), actions));
        listOptions.add(new OptionSelect(cAbstract.getCategoryName(), cAbstract.getIconID(), cAbstract));
        listOptions.add(new OptionSelect(celebrities.getCategoryName(),celebrities.getIconID(), celebrities));
        listOptions.add(new OptionSelect(places.getCategoryName(),places.getIconID(),places));
        listOptions.add(new OptionSelect(animals.getCategoryName(),animals.getIconID(), animals));
        ListOptionAdapter adapter = new ListOptionAdapter(this, listOptions);

        final int adapterCount = adapter.getCount();
        for(int i=0;i<adapterCount;i++){
            View item = adapter.getView(i,null,null);
            llWordListOptions.addView(item);
        }



    }
    public void onNext(View view){
        ArrayList<Category> categoriesInUse = new ArrayList<Category>();
        boolean wordListNotVoid = false;
        boolean difficultryNotNull = false;
        for(OptionSelect option:listOptions){
            if(option.isSelected()){
                categoriesInUse.add(option.getCategory());
                wordListNotVoid=true;
                int resID;
                ArrayList<String> words = new ArrayList<String>();
                if(cbEasy.isChecked()){
                    words.addAll(Arrays.asList(getResources().getStringArray(option.getCategory().getEasyWordsRes())));
                    difficultryNotNull = true;
                }
                if(cbMedium.isChecked()){
                    words.addAll(Arrays.asList(getResources().getStringArray(option.getCategory().getMediumWordsRes())));
                    difficultryNotNull = true;
                }
                if(cbHard.isChecked()){
                    words.addAll(Arrays.asList(getResources().getStringArray(option.getCategory().getHardWordsRes())));
                    difficultryNotNull = true;
                }
                option.getCategory().setWords(words);
                option.getCategory().shuffle();
            }
        }
        DataHolder.setCategoriesInUse(categoriesInUse);
        DataHolder.setKeepScore(swtKeepScore.isChecked());
        if(swtKeepScore.isChecked()){
            if(!etTeam1.getText().toString().equals("")){
                DataHolder.setTeam1(new Team(etTeam1.getText().toString()));
            }else{
                DataHolder.setTeam1(new Team("Team 1"));
            }
            if(!etTeam2.getText().toString().equals("")){
                DataHolder.setTeam2(new Team(etTeam2.getText().toString()));
            }else{
                DataHolder.setTeam2(new Team("Team 2"));
            }

        }
        DataHolder.setAlternateTurns(swtAlternateTurns.isChecked());
        DataHolder.setAllPlay(swtAllPlay.isChecked());
        DataHolder.setUseTimer(swtTimer.isChecked());
        if(swtTimer.isChecked()){
            int time = Integer.parseInt(etTimerLength.getText().toString());
            if(time<=0){
                DataHolder.setTimerLength(defaultTimerLength);
            }else{
                DataHolder.setTimerLength(time);
            }

        }
        if(rbLimitedSkip.isChecked() && swtKeepScore.isChecked()){
            int skips = Integer.parseInt(etSkipLimit.getText().toString());
            if(skips<=0){
                DataHolder.setSkipLimit(defaultSkipLimit);
            }else {
                DataHolder.setSkipLimit(skips);
            }
        }else if(rgWordSkip.getVisibility()==View.VISIBLE){
            DataHolder.setSkipLimit(-1);
        }



        //check to make sure a word list is selected
        if(wordListNotVoid && difficultryNotNull ) {
            //launch next activity
            if(DataHolder.isKeepScore()) {
                Intent intent = new Intent(this, FirstTurn.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(this, WordGenerator.class);
                startActivity(intent);
                finish();
            }
        }else{

                Toast.makeText(this, "Please select at least one word category and at least one difficulty level", Toast.LENGTH_LONG).show();

        }

    }
}