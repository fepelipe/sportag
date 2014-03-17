package br.ufam.sportag.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
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
import br.ufam.sportag.model.Esporte;
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
	private LocalizacaoEvento localizacaoEventoCriado = new LocalizacaoEvento();
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
				localizacaoEventoCriado = localizacaoEvento;
				((TextView) findViewById(R.id.tv_label_spinnerLocation))
						.setText(localizacaoEventoCriado.getNomeLocal());
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_creation, menu);
		return true;
	}

	public void callLocationDialog(View view) {
		locationDialog.show(getFragmentManager(), "locationMap");
	}

	public void callDateDialog(View view) {
		dateDialog.show(getFragmentManager(), "datePicker");
	}

	public void callTimeDialog(View view) {
		timeDialog.show(getFragmentManager(), "timePicker");
	}

	public void createEvent(View view) {
		HashMap<String, Object> argsLocation = new HashMap<String, Object>() {
			{
				put("type", "localizacao_evento");
				put("nomeLocal", localizacaoEventoCriado.getNomeLocal());
				put("latitude",
						String.valueOf(localizacaoEventoCriado.getLatitude()));
				put("longitude",
						String.valueOf(localizacaoEventoCriado.getLongitude()));
				put("endereco", "Sem endereço");
			}
		};

		String createLocationUrl = Util.addUrl
				+ Util.dictionaryToString(argsLocation);

		HttpWebRequest createLocationRequest = new HttpWebRequest(this,
				createLocationUrl) {

			@Override
			public void onSuccess(String locationString) {
				try {
					JSONArray locationJSONArray = new JSONArray(locationString);
					JSONObject jsonObj = locationJSONArray
							.getJSONObject(0);
					int locationId = jsonObj.optInt("localizacao_evento.id");
					Log.i("locationId", String.valueOf(locationId));
					callEventRequest(locationId);
				} catch (JSONException jsonExcep) {
					Log.e("Erro", "JSON", jsonExcep);
				}
			}
		};

		createLocationRequest.execute();
	}

	protected void callEventRequest(final int locationId) {
		// Construir timestamp no formato aceito pelo servidor
		final String datahora = dateDialog.year + "-" + dateDialog.month + "-"
				+ dateDialog.day + " " + timeDialog.hour + ":"
				+ timeDialog.minute + ":00";
		Log.i("Evento DataHora", datahora);

		// Construir URL de requisição para criação de evento no servidor
		HashMap<String, Object> args = new HashMap<String, Object>() {
			{
				put("type", "evento");
				put("nome", editEventName.getText().toString());
				put("visivel", String.valueOf(privacySpinner
						.getSelectedItemPosition()));
				put("dataHora", datahora);
				put("localizacao_id", locationId);
				put("esporte_id",
						String.valueOf(sportsSpinner.getSelectedItemPosition() + 1));
				put("criador_usuario_id", usuario.getId_foursquare());
			}
		};
		String createEventUrl = Util.addUrl + Util.dictionaryToString(args);

		// Requisição para criação de evento
		HttpWebRequest createEventRequest = new HttpWebRequest(this,
				createEventUrl) {

			@Override
			public void onSuccess(String successString) {
				try {
					JSONArray eventosJSONArray = new JSONArray(successString);

					final JSONObject eventoJSONObj = eventosJSONArray
							.getJSONObject(0);

					final Esporte esporte = new Esporte();
					esporte.setId(eventoJSONObj.getInt("esporte.id"));
					esporte.setNome(eventoJSONObj.getString("esporte.nome"));

					final LocalizacaoEvento localizacao = new LocalizacaoEvento();
					localizacao.setId(eventoJSONObj
							.getInt("localizacao_evento.id"));
					localizacao.setNomeLocal(eventoJSONObj
							.getString("localizacao_evento.nomeLocal"));
					localizacao.setLatitude(eventoJSONObj
							.getDouble("localizacao_evento.latitude"));
					localizacao.setLongitude(eventoJSONObj
							.getDouble("localizacao_evento.longitude"));
					localizacao.setEndereco(eventoJSONObj
							.getString("localizacao_evento.endereco"));

					final Usuario usuario = new Usuario();
					usuario.setId_foursquare(eventoJSONObj
							.getInt("usuario.id_foursquare"));
					usuario.setNome(eventoJSONObj.getString("usuario.nome"));
					usuario.setFotoPrefix(eventoJSONObj
							.getString("usuario.fotoPrefix"));
					usuario.setFotoSuffix(eventoJSONObj
							.getString("usuario.fotoSuffix"));

					Evento evento = new Evento();

					String dataHora = eventoJSONObj
							.optString("evento.dataHora");
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd kk:mm:ss", Locale.US);
					final Date dataHoraObj = dateFormat.parse(dataHora);

					evento.setId(eventoJSONObj.getInt("evento.id"));
					evento.setNome(eventoJSONObj.getString("evento.nome"));
					evento.setVisivel(eventoJSONObj.getInt("evento.visivel") == 1);
					evento.setEsporte(esporte);
					evento.setDataHora(dataHoraObj);
					evento.setLocalizacaoEvento(localizacao);
					evento.setCriador(usuario);

					eventoCriado = evento;
					
					callDetailsActivity();
				} catch (JSONException jsonExcep) {
					Log.e("Erro", "JSON", jsonExcep);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		createEventRequest.execute();

	}

	// Chama os detalhes do evento que acabou de ser criado
	private void callDetailsActivity() {
			Intent intent = new Intent(getApplicationContext(),
					EventActivity.class);
			intent.putExtras(getIntent().getExtras());
			intent.putExtra("evento", eventoCriado);
			startActivity(intent);
			this.finish();
	}
}
