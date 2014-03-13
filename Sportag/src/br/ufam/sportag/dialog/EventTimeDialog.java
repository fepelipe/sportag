package br.ufam.sportag.dialog;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public abstract class EventTimeDialog extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {
	private Calendar c;
	public int hour;
	public int minute;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		this.hour = hourOfDay;
		this.minute = minute;
		String timeText = "Hora: " + String.valueOf(this.hour) + ":"
				+ String.valueOf(this.minute);
		onSuccess(timeText);
	}

	public void onSuccess(String timeText) {
	}
}