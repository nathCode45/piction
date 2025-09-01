package app.ncore.piction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import app.ncore.piction.R;

public class TimesUpDialog extends DialogFragment {
    private TimesUpDialogListener listener;

    public interface TimesUpDialogListener{
        public void onTimesUpResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Instantiate the NoticeDialogListener so we can send events to the host
        listener = (TimesUpDialogListener) context;

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.time_up).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onTimesUpResume();
            }
        });
        return builder.create();

    }
}
