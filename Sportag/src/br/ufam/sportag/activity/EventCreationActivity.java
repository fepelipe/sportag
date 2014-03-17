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
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.dialog.EventDateDialog;
import br.ufam.sportag.dialog.EventLocationDialog;
import br.ufam.sportag.dialog.EventTimeDialog;
import br.ufam.sportag.model.Evento;
import br.ufam.sportag.model.LocalizacaoEvento;
import br.ufam.sportag.model.Usuario;
import br.ufam.sportag.util.Util;

public class EventCreationActivity extends Activity {
	private String success;
	private EditText editEventName;
	private TextView tvDate;
	private TextView tvTime;
	private Spinner sportsSpinner;
	private Spinner privacySpinner;
	private EventDateDialog dateDialog;
	private EventTimeDialog timeDialog;
	private EventLocationDialog locationDialog;
	private Evento eventoCriado = new Evento();
	static Usuario usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);

		usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");

		editEventName = (EditText) findViewById(R.id.edt_eventName);
		sportsSpinner = (Spinner) findViewById(R.id.spinner_sport);
		privacySpinner = (Spinner) findViewById(R.id.spinner_privacy);

		ArrayAdapter<CharSequence> sportsAdapter = ArrayAdapter
				.createFromResource(this, R.array.sports_array,
						android.R.layout.simple_spinner_item);
		sportsAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sportsSpinner.setAdapter(sportsAdapter);

		ArrayAdapter<CharSequence> privacyAdapter = ArrayAdapter
				.createFromResource(this, R.array.privacy_array,
						android.R.layout.simple_spinner_item);
		privacyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		privacySpinner.setAdapter(privacyAdapter);

		tvDate = (TextView) findViewById(R.id.tv_label_eventDate);
		tvTime = (TextView) findViewById(R.id.tv_label_eventTime);

		dateDialog = new EventDateDialog() {
			@Override
			public void onSuccess(String dateText) {
				Log.i("Date", dateText);
				tvDate.setText(dateText);
			}
		};
		timeDialog = new EventTimeDialog() {
			@Override
			public void onSuccess(String timeText) {
				Log.i("Time", timeText);
				tvTime.setText(timeText);
			}
		};
		locationDialog = new EventLocationDialog() {
			@Override
			public void onLocationSelected(LocalizacaoEvento localizacaoEvento) {
				
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_creation, menu);
		return true;
	}
	
	public void callLocationDialog(View view){
		locationDialog.show(getFragmentManager(), "locationMap");
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
				put("visivel", String.valueOf(privacySpinner
						.getSelectedItemPosition()));
				put("dataHora", datahora);
				put("localizacao_id", "1");
				put("esporte_id",
						String.valueOf(sportsSpinner.getSelectedItemPosition()));
				put("criador_usuario_id", usuario.getId_foursquare());
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
