package ru.semper_viventem.chromeor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by semper-viventem on 21.10.16.
 */

public class Dialog {

    public static void showDialog(Context context, String title, String text) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
