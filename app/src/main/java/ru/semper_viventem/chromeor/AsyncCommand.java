package ru.semper_viventem.chromeor;

import android.content.Context;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by semper-viventem on 09.10.16.
 */

public class AsyncCommand extends AsyncTask<Void, Void, Void> {

    Context context;
    String command;

    public int exitCode = 255;

    AsyncCommand(Context context, String command)
    {
        this.context = context;
        this.command = command;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Process p;
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(command + " \n");

            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();

                exitCode = p.exitValue();

                if (p.exitValue() != 255) {
                    // TODO Code to run on success
                    //Toast.makeText(context, "root", Toast.LENGTH_SHORT).show();

                } else {
                    // TODO Code to run on unsuccessful
                }
            } catch (InterruptedException e) {
                // TODO Code to run in interrupted exception
            }
        } catch (IOException e) {
            // TODO Code to run in input/output exception
        }
        return null;
    }
}
