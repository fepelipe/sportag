package br.ufam.sportag.dialog;

import java.util.Arrays;

import br.ufam.sportag.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class UserFilterDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String[] sportsArray = getResources().getStringArray(R.array.sports_array);
		Arrays.sort(sportsArray);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Filtre Usuários por esporte")
				.setNegativeButton("Limpar Filtro", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setItems(R.array.sports_array,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
		return builder.create();
	}
}
