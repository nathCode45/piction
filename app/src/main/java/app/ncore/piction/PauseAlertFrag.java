package app.ncore.piction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import app.ncore.piction.R;


public class PauseAlertFrag extends DialogFragment {

    private CheckBox cbHideReset;
    private TextView tvReset, tvAllPlayDialog;
    private SwitchMaterial swtAllPlayDialog;
    public interface PauseDialogListener{
        public void onPauseDialogResume();
    }

    PauseDialogListener listener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (PauseDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(getActivity().toString()+ " must implement PauseDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.pause_alert,null);
        cbHideReset = layout.findViewById(R.id.cbHideReset);
        swtAllPlayDialog = layout.findViewById(R.id.swtAllPlayDialog);
        tvReset = layout.findViewById(R.id.tvReset);
        tvAllPlayDialog = layout.findViewById(R.id.tvAllPlayDialog);
        if(!DataHolder.isUseTimer()){
            tvReset.setVisibility(View.GONE);
            cbHideReset.setVisibility(View.GONE);
        }
        if(!DataHolder.isKeepScore()){
            swtAllPlayDialog.setVisibility(View.GONE);
            tvAllPlayDialog.setVisibility(View.GONE);
        }
        if(DataHolder.isAllPlay()){
            swtAllPlayDialog.toggle();
        }
        if(DataHolder.isHideResetTimer()){
            cbHideReset.toggle();
        }

        builder.setTitle(R.string.dialog_pause_message).setIcon(R.drawable.ic_pause);
        builder.setView(layout);
        builder.setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataHolder.setHideResetTimer(cbHideReset.isChecked());
                DataHolder.setAllPlay(swtAllPlayDialog.isChecked());
                listener.onPauseDialogResume();


            }
        });

        builder.setNegativeButton(R.string.quit_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataHolder.resetAllGameSettings();
                startActivity(new Intent(getActivity(),MainActivity.class));

            }
        });
        return builder.create();


    }

}