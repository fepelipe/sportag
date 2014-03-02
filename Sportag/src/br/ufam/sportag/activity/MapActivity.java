
package br.ufam.sportag.activity;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import br.ufam.sportag.GeoCoordinate;
import br.ufam.sportag.R;
import br.ufam.sportag.Util;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.dialog.EventFilterDialog;
import br.ufam.sportag.dialog.UserFilterDialog;
import br.ufam.sportag.model.Esporte;
import br.ufam.sportag.model.Evento;
import br.ufam.sportag.model.LocalizacaoEvento;
import br.ufam.sportag.model.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity
{
	private SharedPreferences strings;
	private GoogleMap map;
	private LatLng userLocation;
	
	private ArrayList<Evento> allEventos = new ArrayList<Evento>();
	
	Usuario usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		strings = getSharedPreferences("strings", MODE_PRIVATE);
		
		buildMap();
		
		usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
		if (usuario == null)
			obterDados();
		else
			executeAfterUserObtained();
	}
	
	private void executeAfterUserObtained()
	{
		obterListaEventos();
		addSelfMarker();
	}
	
	private void buildMap()
	{
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		{
			public void onInfoWindowClick(Marker marker)
			{
				if (marker.getSnippet() != null)
				{
					if (marker.getSnippet().equals("Usuário"))
					{
						Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
						startActivity(intent);
					} else
					{
						Intent intent = new Intent(getApplicationContext(), EventActivity.class);
						intent.putExtra("eventTitle", marker.getTitle());
						startActivity(intent);
					}
				}
			}
			
		});
	}
	
	private void obterDados()
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("type", "usuario");
		args.put("id_foursquare", strings.getInt("idUser", 0));
		
		// Requisição para obter dados do Usuário
		HttpWebRequest webRequest = new HttpWebRequest(this, Util.searchUrl + Util.dictionaryToString(args))
		{
			@Override
			public void onSuccess(String jsonString)
			{
				JSONArray arrayObj;
				try
				{
					arrayObj = new JSONArray(jsonString);
					
					final JSONObject eventoJSONObj = arrayObj.getJSONObject(0);
					
					usuario = new Usuario();
					usuario.setId_foursquare(eventoJSONObj.getInt("usuario.id_foursquare"));
					usuario.setNome(eventoJSONObj.getString("usuario.nome"));
					usuario.setFotoPrefix(eventoJSONObj.getString("usuario.fotoPrefix"));
					usuario.setFotoSuffix(eventoJSONObj.getString("usuario.fotoSuffix"));
					
					executeAfterUserObtained();
				} catch (JSONException e)
				{
					Log.e("JSON", "Parsing while getting current user.", e);
				}
				
			}
		};
		
		webRequest.execute();
	}
	
	private void obterListaEventos()
	{
		HashMap<String, Object> args = new HashMap<String, Object>()
		{
			{
				put("type", "evento");
			}
		};
		
		HttpWebRequest webRequest = new HttpWebRequest(this, Util.searchUrl + Util.dictionaryToString(args))
		{
			public void onSuccess(String jsonString)
			{
				try
				{
					JSONArray eventosJSONArray = new JSONArray(jsonString);
					for (int i = 0; i < eventosJSONArray.length(); i++)
					{
						final JSONObject eventoJSONObj = eventosJSONArray.getJSONObject(i);
						
						final Esporte esporte = new Esporte()
						{
							{
								setId(eventoJSONObj.getInt("esporte.id"));
								setNome(eventoJSONObj.getString("esporte.nome"));
							}
						};
						
						final LocalizacaoEvento localizacao = new LocalizacaoEvento()
						{
							{
								setId(eventoJSONObj.getInt("localizacao_evento.id"));
								setNomeLocal(eventoJSONObj.getString("localizacao_evento.nomeLocal"));
								setLatitude(eventoJSONObj.getDouble("localizacao_evento.latitude"));
								setLongitude(eventoJSONObj.getDouble("localizacao_evento.longitude"));
								setEndereco(eventoJSONObj.getString("localizacao_evento.endereço"));
							}
						};
						
						final Usuario usuario = new Usuario()
						{
							{
								setId_foursquare(eventoJSONObj.getInt("usuario.id_foursquare"));
								setNome(eventoJSONObj.getString("usuario.nome"));
								setFotoPrefix(eventoJSONObj.getString("usuario.fotoPrefix"));
								setFotoSuffix(eventoJSONObj.getString("usuario.fotoSuffix"));
							}
						};
						
						Evento evento = new Evento()
						{
							{
								setId(eventoJSONObj.getInt("evento.id"));
								setNome(eventoJSONObj.getString("evento.nome"));
								setVisivel(eventoJSONObj.getInt("evento.visivel") == 1);
								setEsporte(esporte);
								setLocalizacaoEvento(localizacao);
								setCriador(usuario);
							}
						};
						
						allEventos.add(evento);
						
						afterParsing();
					}
					
				} catch (JSONException e)
				{
					Log.e("Erro", "Parsing Eventos from JSON", e);
				}
			}
			
			private void afterParsing()
			{
				addEventsMarker();
			}
		};
		webRequest.execute();
	}
	
	private void addEventsMarker()
	{
		// Eventos recuperados do servidor
		for (int i = 0; i < allEventos.size(); i++)
		{
			final Evento evento = allEventos.get(i);
			final LocalizacaoEvento localizacao = evento.getLocalizacaoEvento();
			map.addMarker(new MarkerOptions().position(new LatLng(localizacao.getLatitude(), localizacao.getLongitude())).title(evento.getNome()).snippet("Evento").icon(BitmapDescriptorFactory.fromResource(R.drawable.icone_futebol)));
		}
		
		// Eventos estáticos
		// map.addMarker(new MarkerOptions()
		// .position(new LatLng(-3.045086, -60.085949))
		// .title("Runners")
		// .snippet("Evento")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.icone_futebol)));
		// map.addMarker(new MarkerOptions()
		// .position(new LatLng(-3.102331, -60.025342))
		// .title("Sk8 dos Brow")
		// .snippet("Evento")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.icone_futebol)));
		// map.addMarker(new MarkerOptions()
		// .position(new LatLng(-3.130390, -60.023165))
		// .title("Amigo Coração")
		// .snippet("Evento")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.icone_futebol)));
		// map.addMarker(new MarkerOptions()
		// .position(new LatLng(-3.082845, -60.009904))
		// .title("Procurando Nemo")
		// .snippet("Evento")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.icone_futebol)));
		// map.addMarker(new MarkerOptions()
		// .position(new LatLng(-3.067638, -60.095109))
		// .title("Pedala Galera")
		// .snippet("Evento")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.icone_futebol)));
	}
	
	private void addSelfMarker()
	{
		GeoCoordinate coordinates = new GeoCoordinate(this)
		{
			public void onGPSSuccess(Location location)
			{
				double userLongitude = location.getLongitude();
				double userLatitude = location.getLatitude();
				
				usuario.setLatitude(userLatitude);
				usuario.setLongitude(userLongitude);
				
				userLocation = new LatLng(usuario.getLatitude(), usuario.getLongitude());
				map.setMyLocationEnabled(true);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
				map.addMarker(new MarkerOptions().title(usuario.getNome()).snippet("Usuário").position(userLocation));
			}
			
			public void onFail(String title, String message)
			{
				// TODO Toast error
			}
		};
		
		coordinates.startService();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_events :
				callEventActivity();
				return true;
			case R.id.action_friends :
				callFriendsActivity();
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void callEventActivity()
	{
		Intent intent = new Intent(this, EventManagementActivity.class);
		intent.putExtras(getIntent().getExtras());
		intent.putExtra("usuario", usuario);
		startActivity(intent);
	}
	
	private void callFriendsActivity()
	{
		Intent intent = new Intent(this, FriendsActivity.class);
		startActivity(intent);
	}
	
	public void callEventFilter(View view)
	{
		EventFilterDialog filterDialog = new EventFilterDialog();
		filterDialog.show(getFragmentManager(), "event_filter");
	}
	
	public void callUserFilter(View view)
	{
		UserFilterDialog filterDialog = new UserFilterDialog();
		filterDialog.show(getFragmentManager(), "event_filter");
	}
}
