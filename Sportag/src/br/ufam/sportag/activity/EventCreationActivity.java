package br.ufam.sportag.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import br.ufam.sportag.R;
import br.ufam.sportag.asynctask.HttpWebRequest;

public class EventCreationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);
		
		Spinner sportsSpinner = (Spinner) findViewById(R.id.spinner_sport);
		Spinner locationSpinner = (Spinner) findViewById(R.id.spinner_location);
		Spinner privacySpinner = (Spinner) findViewById(R.id.spinner_privacy);

		ArrayAdapter<CharSequence> sportsAdapter = ArrayAdapter.createFromResource(this,
		        R.array.sports_array, android.R.layout.simple_spinner_item);
		sportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sportsSpinner.setAdapter(sportsAdapter);
		
		ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,
		        R.array.fakeLocation_array, android.R.layout.simple_spinner_item);
		locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(locationAdapter);
		
		ArrayAdapter<CharSequence> privacyAdapter = ArrayAdapter.createFromResource(this,
		        R.array.privacy_array, android.R.layout.simple_spinner_item);
		privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		privacySpinner.setAdapter(privacyAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_creation, menu);
		return true;
	}

	public void createEvent(View view){
		//TODO Criar evento no servidor
		// nome, visivel, localizacao_id, esporte_id, criador_usuario_id
		
		//Chama os detalhes do evento
		Intent intent = new Intent(getApplicationContext(),
				EventActivity.class);
		intent.putExtra("eventTitle", "Runners");
		startActivity(intent);
		this.finish();
	}
}
