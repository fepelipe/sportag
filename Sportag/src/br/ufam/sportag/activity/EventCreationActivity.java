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
import br.ufam.sportag.Util;
import br.ufam.sportag.asynctask.HttpWebRequest;

public class EventCreationActivity extends Activity {
	private String success;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);

		Spinner sportsSpinner = (Spinner) findViewById(R.id.spinner_sport);
		Spinner locationSpinner = (Spinner) findViewById(R.id.spinner_location);
		Spinner privacySpinner = (Spinner) findViewById(R.id.spinner_privacy);

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

	public void createEvent(View view) {
		// TODO Criar evento no servidor
		// nome, visivel, localizacao_id, esporte_id, criador_usuario_id
		final EditText editEventName = (EditText) findViewById(R.id.edt_eventName);
		final Spinner spinnerSport = (Spinner) findViewById(R.id.spinner_sport);
		final Spinner spinnerLocation = (Spinner) findViewById(R.id.spinner_location);
		final Spinner spinnerPrivacy = (Spinner) findViewById(R.id.spinner_privacy);
		
		HashMap<String, Object> args = new HashMap<String, Object>() {
			{
				put("type", "evento");
				put("nome", editEventName.getText().toString());
				put("visivel", "1");
				put("dataHora", "2014-02-25 02:24:00");
				put("localizacao_id", "1");
				put("esporte_id", "1");
				put("criador_usuario_id", "30004723");
			}
		};

		String createEventUrl = Util.addUrl + Util.dictionaryToString(args);
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
				} catch (JSONException jsonExcep) {
					Log.e("Erro", "JSON", jsonExcep);
				}
			}
		};
		createEventRequest.execute();
		callDetailsActivity();
	}

	private void callDetailsActivity() {
		if (success == "true") {
			// Chama os detalhes do evento
			Intent intent = new Intent(getApplicationContext(),
					EventActivity.class);
			intent.putExtra("eventTitle", "Runners");
			startActivity(intent);
			this.finish();
		}
	}
}
