package com.example.user.courseproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Павел on 19.03.2016.
 */
public class SelectFpsInVideo extends DialogFragment{
    String selection;
    public static Integer fps = 24;
    final CharSequence[] items = {"30 frame","24 frame","15 frame","10 frame","5 frame"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("FPS in Video").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1) {
                    case 0:
                        selection = (String) items[arg1];
                        fps = 30;
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        fps = 24;
                        break;
                    case 2:
                        selection = (String) items[arg1];
                        fps = 15;
                        break;
                    case 3:
                        selection = (String) items[arg1];
                        fps = 10;
                        break;
                    case 4:
                        selection = (String) items[arg1];
                        fps = 5;
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
