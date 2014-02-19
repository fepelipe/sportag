package br.ufam.sportag.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import br.ufam.sportag.R;

public class EventCreationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_creation, menu);
		return true;
	}

	public void createEvent(View view){
		//TODO Criar evento no servidor
		
		//Chama os detalhes do evento
		Intent intent = new Intent(getApplicationContext(),
				EventActivity.class);
		intent.putExtra("eventTitle", "Runners");
		startActivity(intent);
	}
}
