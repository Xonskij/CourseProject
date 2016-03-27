package com.example.user.courseproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class SelectDuration extends DialogFragment {
    String selection;
    public static Integer dur = 2000000000;
    final CharSequence[] items = {"3 second","5 second","10 second","15 second","20 second","30 second", "1 minute", "3 minute",
            "5 minute", "10 minute", "30 minute", "1 hour"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Duration Video").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1) {
                    case 0:
                        selection = (String) items[arg1];
                        dur = 3000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        dur = 5000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 2:
                        selection = (String) items[arg1];
                        dur = 10000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 3:
                        selection = (String) items[arg1];
                        dur = 15000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 4:
                        selection = (String) items[arg1];
                        dur = 20000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 5:
                        selection = (String) items[arg1];
                        dur = 30000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 6:
                        selection = (String) items[arg1];
                        dur = 60000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 7:
                        selection = (String) items[arg1];
                        dur = 180000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 8:
                        selection = (String) items[arg1];
                        dur = 300000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 9:
                        selection = (String) items[arg1];
                        dur = 600000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 10:
                        selection = (String) items[arg1];
                        dur = 1800000;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 11:
                        selection = (String) items[arg1];
                        dur = 3600000;
                        getActivity();
                        arg0.dismiss();
                        break;
                }
            }
        });

        return builder.create();
    }
}
