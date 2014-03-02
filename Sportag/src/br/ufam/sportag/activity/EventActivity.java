
package br.ufam.sportag.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.model.Evento;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventActivity extends Activity
{
//	private String eventTitle;
	private GoogleMap map;
	private LatLng eventLocation;
	Evento evento;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		evento = (Evento) getIntent().getExtras().getSerializable("evento");
		
		addEventDetails();
	}
	
	private void addEventDetails()
	{
		// TODO Criar Holder para otimizar findViewById
		TextView tvEventName = (TextView) findViewById(R.id.tv_eventName);
		TextView tvEventPrivacy = (TextView) findViewById(R.id.tv_eventPrivacy);
		TextView tvEventAddress = (TextView) findViewById(R.id.tv_eventAddress);
		TextView tvEventDate = (TextView) findViewById(R.id.tv_eventDate);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_event)).getMap();
		
		String dataHora = (String) DateFormat.format("yyyy-MM-dd kk:mm:ss", evento.getDataHora());
		int visibilidade = evento.isVisivel() ? 1 : 0;
		String nomeEvento = evento.getNome(), nomeLocal = evento.getLocalizacaoEvento().getNomeLocal();
		double latit = evento.getLocalizacaoEvento().getLatitude();
		double longit = evento.getLocalizacaoEvento().getLongitude();	
		
		tvEventName.setText(nomeEvento);
		tvEventPrivacy.setText(visibilidade == 1 ? "Público" : "Privado");
		tvEventAddress.setText(nomeLocal);
		tvEventDate.setText(dataHora);

		eventLocation = new LatLng(latit, longit);
		
		// Adiciona a localização do evento no mapa
		map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.icone_futebol)));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 14));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}
	
	public void callEventDiscussionActivity(View view)
	{
		Intent intent = new Intent(getApplicationContext(), EventDiscussionActivity.class);
		intent.putExtras(getIntent().getExtras());
		startActivity(intent);
	}
}
