package com.example.trialapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

    private final int trialDelayInDays = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	checkRemainingTrialDays();

    }

    private void checkRemainingTrialDays() {

	final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
		Locale.getDefault());
	final long oneDay = 24 * 60 * 60 * 1000;

	long remainingDays = 0;

	SharedPreferences preferences = getPreferences(MODE_PRIVATE);
	String savedFirstRunDate = preferences.getString("FirstRunDate", null);
	if (savedFirstRunDate == null) {

	    SharedPreferences.Editor editor = preferences.edit();
	    Date now = new Date();
	    String dateString = formatter.format(now);
	    editor.putString("FirstRunDate", dateString);
	    editor.commit();
	} else {

	    Date firstRunDate = new Date();
	    try {
		firstRunDate = (Date) formatter.parse(savedFirstRunDate);
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	    Date now = new Date();
	    long diff = now.getTime() - firstRunDate.getTime();
	    remainingDays = (long) Math.ceil(trialDelayInDays - diff / oneDay);

	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		    this);

	    if (remainingDays < 0) {

		alertDialogBuilder.setTitle("Alert");
		alertDialogBuilder
			.setMessage("Trial session expired")
			.setCancelable(false)
			.setPositiveButton("Quit",
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					MainActivity.this.finish();
				    }
				});
	    } else {

		alertDialogBuilder.setTitle("Trial version");
		alertDialogBuilder
			.setMessage(
				Long.toString(remainingDays)
					+ " days remaining")
			.setCancelable(false)
			.setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					dialog.cancel();
				    }
				});
	    }

	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
    }

}
