package ru.semper_viventem.chromeor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by semper-viventem on 14.10.16.
 */

public class Div {
    private Context context;
    private String action_url;
    private String origin_url;
    private String username_value;
    private String password_value;

    private LinearLayout thisContanner;

    public Div(Context context, String action_url, String origin_url, String username_value, String password_value) {
        this.context = context;
        this.action_url = action_url;
        this.origin_url = origin_url;
        this.username_value = username_value;
        this.password_value = password_value;

        buildContanner();
    }

    private void buildContanner() {
        this.thisContanner = new LinearLayout(context);
        this.thisContanner.setOrientation(LinearLayout.VERTICAL);
        this.thisContanner.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams lp = new AppBarLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        this.thisContanner.setLayoutParams(lp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.thisContanner.setElevation(3);
        }
        this.thisContanner.setPadding(10, 10, 10, 10);


        TextView action_utlTextView = new TextView(context);
        action_utlTextView.setText("acction_url : "+action_url);

        TextView origin_urlTextView = new TextView(context);
        origin_urlTextView.setText("origin_url    : "+origin_url);

        TextView username_valueTextView = new TextView(context);
        username_valueTextView.setText("username   : "+username_value);

        TextView password_valueTextView = new TextView(context);
        password_valueTextView.setText("password   : "+password_value);

        this.thisContanner.addView(action_utlTextView);
        this.thisContanner.addView(origin_urlTextView);
        this.thisContanner.addView(username_valueTextView);
        this.thisContanner.addView(password_valueTextView);

        ImageButton shareButton = new ImageButton(context);
        shareButton.setImageResource(R.mipmap.ic_share_black_24dp);
        LinearLayout.LayoutParams lp1 = new AppBarLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(10, 10, 10, 10);
        shareButton.setBackgroundColor(0x00000000);
        shareButton.setLayoutParams(lp1);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String textToSend = "action_url: " + action_url + "\norigin_url: " + origin_url + "\nusername: " + username_value + "\npassword: " + password_value;
                intent.putExtra(Intent.EXTRA_TEXT, textToSend);
                try
                {
                    context.startActivity(Intent.createChooser(intent, "Share"));
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    //Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.thisContanner.addView(shareButton);
        this.thisContanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderdialog = new AlertDialog.Builder(context);
                View view = View.inflate(context, R.layout.dialog_edit, null);

                final EditText actionEditText = (EditText)view.findViewById(R.id.acctionEditText);
                final EditText originEditText = (EditText)view.findViewById(R.id.originEditText);
                final EditText usernameEditText = (EditText)view.findViewById(R.id.usernameEditText);
                final EditText passwordEditText = (EditText)view.findViewById(R.id.passwordEditText);

                actionEditText.setText(action_url);
                originEditText.setText(origin_url);
                usernameEditText.setText(username_value);
                passwordEditText.setText(password_value);

                builderdialog.setView(view);
                builderdialog.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Ничего
                    }
                });
                AlertDialog dialog = builderdialog.create();
                dialog.show();
            }
        });
    }

    public LinearLayout getThisContanner() {
        return thisContanner;
    }


}
