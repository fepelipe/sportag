package br.ufam.sportag.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import br.ufam.sportag.R;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.dialog.EventDateDialog;
import br.ufam.sportag.dialog.EventTimeDialog;
import br.ufam.sportag.model.Evento;
import br.ufam.sportag.util.Util;

public class EventCreationActivity extends Activity {
	private String success;
	private EditText editEventName;
	private Spinner sportsSpinner;
	private Spinner locationSpinner;
	private Spinner privacySpinner;
	private EventDateDialog dateDialog = new EventDateDialog();
	private EventTimeDialog timeDialog = new EventTimeDialog();
	private Evento eventoCriado = new Evento();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);

		editEventName = (EditText) findViewById(R.id.edt_eventName);
		sportsSpinner = (Spinner) findViewById(R.id.spinner_sport);
		locationSpinner = (Spinner) findViewById(R.id.spinner_location);
		privacySpinner = (Spinner) findViewById(R.id.spinner_privacy);

		ArrayAdapter<CharSequence> sportsAdapter = ArrayAdapter
				.createFromResource(this, R.array.sports_array,
						android.R.layout.simple_spinner_item);
		sportsAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sportsSpinner.setAdapter(sportsAdapter);

		ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter
				.createFromResource(this, R.array.fakeLocation_array,
						android.R.layout.simple_spinner_item);
		locationAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(locationAdapter);

		ArrayAdapter<CharSequence> privacyAdapter = ArrayAdapter
				.createFromResource(this, R.array.privacy_array,
						android.R.layout.simple_spinner_item);
		privacyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		privacySpinner.setAdapter(privacyAdapter);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_creation, menu);
		return true;
	}

	public void callDateDialog(View view) {
		dateDialog.show(getFragmentManager(), "datePicker");
	}

	public void callTimeDialog(View view) {
		timeDialog.show(getFragmentManager(), "timePicker");
	}

	public void createEvent(View view) {
		// Construir timestamp no formato aceito pelo servidor
		final String datahora = dateDialog.year + "-" + dateDialog.month + "-"
				+ dateDialog.day + " " + timeDialog.hour + ":"
				+ timeDialog.minute + ":00";
		Log.i("Evento DataHora", datahora);
		
		// // TODO Tratamento de exceções (campos vazios)
		// Toast.makeText(getApplicationContext(), "msg msg",
		// Toast.LENGTH_SHORT).show();
		
		// Construir URL de requisição para criação de evento no servidor
		HashMap<String, Object> args = new HashMap<String, Object>() {
			{
				put("type", "evento");
				put("nome", editEventName.getText().toString());
				put("visivel", "1");
				put("dataHora", datahora);
				put("localizacao_id", "1");
				put("esporte_id", "1");
				put("criador_usuario_id", "30004723");
			}
		};
		String createEventUrl = Util.addUrl + Util.dictionaryToString(args);
		
		// Requisição para criação de evento
		HttpWebRequest createEventRequest = new HttpWebRequest(this,
				createEventUrl) {

			@Override
			public void onSuccess(String successString) {
				JSONObject jsonObj;
				try {
					jsonObj = new JSONObject(successString);
					success = jsonObj.optString("success");
					Log.i("Success", successString);
					Log.i("Event Creation Success", success);
					callDetailsActivity();
				} catch (JSONException jsonExcep) {
					Log.e("Erro", "JSON", jsonExcep);
				}
			}
		};
		createEventRequest.execute();
	}

	// Chama os detalhes do evento que acabou de ser criado
	private void callDetailsActivity() {
		if (success == "true") {
			Intent intent = new Intent(getApplicationContext(),
					EventActivity.class);
			intent.putExtra("eventTitle", eventoCriado);
			startActivity(intent);
			this.finish();
		}
	}
}
