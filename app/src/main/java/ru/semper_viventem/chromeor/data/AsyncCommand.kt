package ru.semper_viventem.chromeor.data

import android.content.Context
import android.os.AsyncTask

import java.io.DataOutputStream
import java.io.IOException

/**
 * @author Kulikov Konstantin
 * @since 09.10.16.
 */

open class AsyncCommand protected constructor(
        internal var context: Context,
        internal var command: String
    ) : AsyncTask<Void, Void, Void>() {

    var exitCode = 255

    override fun doInBackground(vararg params: Void): Void? {
        val p: Process
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su")

            // Attempt to write a file to a root-only
            val os = DataOutputStream(p.outputStream)
            os.writeBytes(command + " \n")

            // Close the terminal
            os.writeBytes("exit\n")
            os.flush()
            try {
                p.waitFor()

                exitCode = p.exitValue()

                if (p.exitValue() != 255) {
                    // TODO Code to run on success
                    //Toast.makeText(context, "root", Toast.LENGTH_SHORT).show();

                } else {
                    // TODO Code to run on unsuccessful
                }
            } catch (e: InterruptedException) {
                // TODO Code to run in interrupted exception
            }

        } catch (e: IOException) {
            // TODO Code to run in input/output exception
        }

        return null
    }
}
