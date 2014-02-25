package br.ufam.sportag.dialog;

import android.app.DatePickerDialog;
import android.content.Context;

public class EventDateDialog extends DatePickerDialog {

	public EventDateDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		// TODO Auto-generated constructor stub
	}

}
