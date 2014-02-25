package br.ufam.sportag.activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import br.ufam.sportag.R;

public class EventActivity extends Activity {
	private String eventTitle;
	private GoogleMap map;
	private LatLng eventLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		eventTitle = getIntent().getExtras().getString("eventTitle");

		addEventDetails();
	}

	private void addEventDetails() {
		// TODO Criar Holder para otimizar findViewById
		TextView tvEventName = (TextView) findViewById(R.id.tv_eventName);
		TextView tvEventPrivacy = (TextView) findViewById(R.id.tv_eventPrivacy);
		TextView tvEventAddress = (TextView) findViewById(R.id.tv_eventAddress);
		TextView tvEventDate = (TextView) findViewById(R.id.tv_eventDate);

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map_event)).getMap();

		// TODO Receber Evento, LocalizacaoEvento e Usuario para setar dados
		// Seta os detalhes do evento
		tvEventName.setText(eventTitle);
		if (eventTitle.equals("Runners")) {
			tvEventPrivacy.setText("Público");
			tvEventAddress.setText("Praça da Ponta Negra");
			tvEventDate.setText("Marcado para 25.02.14 às 17h");
			eventLocation = new LatLng(-3.045086, -60.085949);
		} else if (eventTitle.equals("Sk8 dos Brow")) {
			tvEventPrivacy.setText("Privado");
			tvEventAddress.setText("Parque dos Bilhares");
			tvEventDate.setText("Marcado para 02.03.14 às 18h");
			eventLocation = new LatLng(-3.102331, -60.025342);
		} else if (eventTitle.equals("Amigo Coração")) {
			tvEventPrivacy.setText("Público");
			tvEventAddress.setText("Largo São Sebastião");
			tvEventDate.setText("Marcado para 23.02.14 às 16h");
			eventLocation = new LatLng(-3.130390, -60.023165);
		} else if (eventTitle.equals("Procurando Nemo")) {
			tvEventPrivacy.setText("Privado");
			tvEventAddress.setText("CSU");
			tvEventDate.setText("Marcado para 06.03.14 às 9h");
			eventLocation = new LatLng(-3.082845, -60.009904);
		} else if (eventTitle.equals("Pedala Galera")) {
			tvEventPrivacy.setText("Público");
			tvEventAddress.setText("Av. do Turismo");
			tvEventDate.setText("Marcado para 08.03.14 às 10h");
			eventLocation = new LatLng(-3.067638, -60.095109);
		}

		// Adiciona a localização do evento no mapa
		map.addMarker(new MarkerOptions().position(eventLocation).icon(
				BitmapDescriptorFactory.fromResource(R.drawable.icone_futebol)));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 14));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

	public void callEventDiscussionActivity(View view) {
		Intent intent = new Intent(this, EventDiscussionActivity.class);
		startActivity(intent);
	}
}
