package com.playground.viewsplayground.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.playground.viewsplayground.R;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.root_linear_layout);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Activity  ", Toast.LENGTH_SHORT).show();
            }
        });
        
        setUpTaost();


    }

    private void setUpTaost() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Toast toast = Toast.makeText(getApplicationContext(), "",
                Toast.LENGTH_SHORT);
        View toastView = inflater.inflate(R.layout.dialer_layout, null);
        toast.setView(toastView);
        toast.setGravity(Gravity.FILL, 300, 300);
        fireLongToast(toast);
        //launchDialer();


       
    }

    @Override
    protected void onResume() {
        super.onResume();

        showDialog();
    }

    private void showDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }

    // this link helped:
    // http://thinkandroid.wordpress.com/2010/02/19/indefinite-toast-hack/
    private void fireLongToast(final Toast toast) {

        Thread t = new Thread() {
            public void run() {
                int count = 0;
                int max_count = 10;
                try {
                    while (true && count < max_count) {
                        toast.show();
                        /*
						 * We check to see when we are going to give the screen
						 * back. Right before our toasts end we swap activities
						 * to remove any visual clues
						 */
                        if (count == max_count - 1) {
                            ComponentName toLaunch;
                            toLaunch = new ComponentName(
                                    "com.playground.viewsplayground",
                                    "com.playground.viewsplayground.ui");
                            Intent intent = new Intent();
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.setComponent(toLaunch);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(intent);
                        }

						/*
						 * this short sleep helps our toasts transition
						 * seamlessly
						 */
                        sleep(1850);
                        count++;
                    }
                } catch (Exception e) {
                }

            }
        };
        t.start();
    }

    private void launchDialer() {

        Thread t = new Thread() {
            public void run() {
				/*
				 * We sleep first in order for the toasts to consume the screen
				 * before the dialer activity launches
				 */
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // showing Google some love
                intent.setData(Uri.parse("tel:650-253-0000"));
                getApplication().startActivity(intent);
            }
        };
        t.start();
    }
}

