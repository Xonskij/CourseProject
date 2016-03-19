package com.example.user.courseproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.CamcorderProfile;
import android.os.Bundle;
import android.widget.Toast;

public class SelectFpsClass extends DialogFragment {
    String selection;
    public static Integer rate = 10;
    final CharSequence[] items = {"1 second","5 second","10 second","15second","30 second"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Frame Interval").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1) {
                    case 0:
                        selection = (String) items[arg1];
                        rate = 1;
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        rate = 5;
                        break;
                    case 2:
                        selection = (String) items[arg1];
                        rate = 10;
                        break;
                    case 3:
                        selection = (String) items[arg1];
                        rate = 15;
                        break;
                    case 4:
                        selection = (String) items[arg1];
                        rate = 30;
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "You choose FPS = " + selection, Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}
