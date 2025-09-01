package app.ncore.piction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import app.ncore.piction.R;

public class AllPlayDialog extends DialogFragment {
    private AllPlayReadyListener listener;
    public interface AllPlayReadyListener{
        void onAllPlayReady();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Instantiate the NoticeDialogListener so we can send events to the host
        listener = (AllPlayReadyListener) context;

    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.allplay_warning);
        builder.setTitle(R.string.allplay_warning_title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onAllPlayReady();
            }
        });

        return builder.create();
    }
}
