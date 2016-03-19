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
    public static Integer size = CamcorderProfile.QUALITY_TIME_LAPSE_1080P;
    final CharSequence[] items = {"4К 2160p","Full HD 1080p","HD 720p","SD 480p","QVGA 240p"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose resolution").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1){
                    case 0:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_2160P;
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_1080P;
                        break;
                    case 2:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_480P;
                        break;
                    case 3:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_720P;
                        break;
                    case 4:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_QVGA;
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
