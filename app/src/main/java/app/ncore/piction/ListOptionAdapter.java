package app.ncore.piction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.ncore.piction.R;

import java.util.ArrayList;

public class ListOptionAdapter extends ArrayAdapter<OptionSelect> {
    private final Context context;
    private final ArrayList<OptionSelect> values;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_element,parent,false);
        TextView tvOption = rowView.findViewById(R.id.tvOption);
        ImageView icon = rowView.findViewById(R.id.ivOptionIcon);
        CheckBox checkBox = rowView.findViewById(R.id.cbWordList);
        final OptionSelect option = values.get(position);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) (view)).isChecked();
                option.select(checked);
            }
        });

        icon.setImageResource(option.getIconId());
        tvOption.setText(option.getText());
        return rowView;
    }

    public ListOptionAdapter(@NonNull Context context, ArrayList<OptionSelect> values){
        super(context, R.layout.list_element, values);
        this.context = context;
        this.values = values;

    }
}
