package com.example.user.courseproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.CamcorderProfile;
import android.os.Bundle;
import android.widget.Toast;

public class SingleChoiceClass extends DialogFragment {
    String selection;
    public static Integer size;
    final CharSequence[] items = {"Full HD 1080p","HD 720p","SD 480p"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose resolution").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1){
                    case 0:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_HIGH;
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_480P;
                        break;
                    case 2:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_CIF;
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "You choose resolution: " + selection, Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}
