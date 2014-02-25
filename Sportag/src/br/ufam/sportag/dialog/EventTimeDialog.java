package br.ufam.sportag.dialog;

import android.app.TimePickerDialog;
import android.content.Context;

public class EventTimeDialog extends TimePickerDialog {

	public EventTimeDialog(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
	}

}
