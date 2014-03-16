package br.ufam.sportag.dialog;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public abstract class EventDateDialog extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	private Calendar c;
	public int year;
	public int month;
	public int day;

	public EventDateDialog() {
		c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		String dateText = "Marcado para: " + String.valueOf(this.day) + "/"
				+ String.valueOf(this.month + 1) + "/" + String.valueOf(this.year);
		onSuccess(dateText);
	}

	public void onSuccess(String dateText) {
	}
}