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
public class SingleChoiceClass extends DialogFragment {
    String selection;
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
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        break;
                    case 2:
                        selection = (String) items[arg1];
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
