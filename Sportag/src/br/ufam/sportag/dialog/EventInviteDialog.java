package br.ufam.sportag.dialog;

import java.util.ArrayList;
import java.util.Arrays;

import br.ufam.sportag.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;

public class EventInviteDialog extends DialogFragment {
	ArrayList<Integer> mSelectedItems;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Single choice
		String[] friendsArray = getResources().getStringArray(
				R.array.sports_array);
		Arrays.sort(friendsArray);

		mSelectedItems = new ArrayList();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Convidar amigos")
				.setNegativeButton("Cancelar", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setItems(friendsArray, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
		return builder.create();

		
		// Multi choice
		// String[] friendsArray = getResources().getStringArray(
		// R.array.sports_array);
		// Arrays.sort(friendsArray);
		//
		// mSelectedItems = new ArrayList();
		// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// builder.setTitle("Convidar amigos")
		// .setNegativeButton("Cancelar", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// })
		// .setPositiveButton("Convidar amigos", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// })
		// .setMultiChoiceItems(friendsArray, null,
		// new OnMultiChoiceClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which, boolean isChecked) {
		// if (isChecked) {
		// mSelectedItems.add(which);
		// } else if (mSelectedItems.contains(which)) {
		// mSelectedItems.remove(Integer
		// .valueOf(which));
		// }
		// }
		// });
		// return builder.create();
	}
}
