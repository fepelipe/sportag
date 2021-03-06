
package br.ufam.sportag.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import br.ufam.sportag.R;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.dialog.EventFilterDialog;
import br.ufam.sportag.dialog.UserFilterDialog;
import br.ufam.sportag.model.Esporte;
import br.ufam.sportag.model.Evento;
import br.ufam.sportag.model.LocalizacaoEvento;
import br.ufam.sportag.model.Usuario;
import br.ufam.sportag.util.GeoCoordinate;
import br.ufam.sportag.util.Util;

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
	final private transient Map<String, Evento> titulosEventos = new HashMap<String, Evento>();
	
	private Usuario usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		strings = getSharedPreferences("strings", MODE_PRIVATE);
		
		buildMap();
		
		usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
		if (usuario == null)
		{
			obterDados();
		} else
		{
			executeAfterUserObtained();
		}
	}
	
	private void executeAfterUserObtained()
	{
		addSelfMarker();
	}
	
	private void buildMap()
	{
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
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
					
					final JSONObject usuarioObj = arrayObj.getJSONObject(0);
					
					usuario = new Usuario();
					usuario.setId_foursquare(usuarioObj.getInt("usuario.id_foursquare"));
					usuario.setNome(usuarioObj.getString("usuario.nome"));
					usuario.setFotoPrefix(usuarioObj.getString("usuario.fotoPrefix"));
					usuario.setFotoSuffix(usuarioObj.getString("usuario.fotoSuffix"));
					usuario.setAvatarString64(usuarioObj.optString("usuario.avatar"));
					
					executeAfterUserObtained();
				} catch (JSONException e)
				{
					Log.e("JSON", "Parsing while getting current user.", e);
					Log.e("JSON", jsonString);	
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
						
						final Esporte esporte = new Esporte();
						esporte.setId(eventoJSONObj.getInt("esporte.id"));
						esporte.setNome(eventoJSONObj.getString("esporte.nome"));
						
						final LocalizacaoEvento localizacao = new LocalizacaoEvento();
						localizacao.setId(eventoJSONObj.getInt("localizacao_evento.id"));
						localizacao.setNomeLocal(eventoJSONObj.getString("localizacao_evento.nomeLocal"));
						localizacao.setLatitude(eventoJSONObj.getDouble("localizacao_evento.latitude"));
						localizacao.setLongitude(eventoJSONObj.getDouble("localizacao_evento.longitude"));
						localizacao.setEndereco(eventoJSONObj.getString("localizacao_evento.endereco"));
						
						final Usuario usuario = new Usuario();
						usuario.setId_foursquare(eventoJSONObj.getInt("usuario.id_foursquare"));
						usuario.setNome(eventoJSONObj.getString("usuario.nome"));
						usuario.setFotoPrefix(eventoJSONObj.getString("usuario.fotoPrefix"));
						usuario.setFotoSuffix(eventoJSONObj.getString("usuario.fotoSuffix"));
						
						Evento evento = new Evento();
						
						String dataHora = eventoJSONObj.optString("evento.dataHora");
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US);
						final Date dataHoraObj = dateFormat.parse(dataHora);
						
						evento.setId(eventoJSONObj.getInt("evento.id"));
						evento.setNome(eventoJSONObj.getString("evento.nome"));
						evento.setVisivel(eventoJSONObj.getInt("evento.visivel") == 1);
						evento.setEsporte(esporte);
						evento.setDataHora(dataHoraObj);
						evento.setLocalizacaoEvento(localizacao);
						evento.setCriador(usuario);
						
						allEventos.add(evento);
					}
					afterParsing();
				} catch (JSONException e)
				{
					Log.e("Erro", "Parsing Eventos from JSON", e);
				} catch (ParseException e)
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
//		titulosEventos = new WeakHashMap<String, Evento>();
		
		titulosEventos.clear();
		
		for (int i = 0; i < allEventos.size(); i++)
		{
			final Evento evento = allEventos.get(i);
			final LocalizacaoEvento localizacao = evento.getLocalizacaoEvento();
			LatLng latLng = new LatLng(localizacao.getLatitude(), localizacao.getLongitude());
			MarkerOptions opt = new MarkerOptions().position(latLng);
			opt.title(evento.getNome());
			opt.snippet("Evento");
			opt.icon(BitmapDescriptorFactory.fromResource(R.drawable.icone_futebol));
			Marker eventoMarker = map.addMarker(opt);
			
			titulosEventos.put(eventoMarker.getId(), evento);
		}
		
		addListenerToMarkers();
		
	}
	
	private void addListenerToMarkers()
	{
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		{
			@Override
			public void onInfoWindowClick(Marker marker)
			{
				if (marker.getSnippet() != null)
				{
					if (marker.getSnippet().equals("Usuário"))
					{
						Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
						intent.putExtra("usuario", usuario);
						startActivity(intent);
					} else
					{
						Map<String, Evento> internalHashMap = titulosEventos;
						Evento eventoClique = internalHashMap.get(marker.getId());
						Log.i("Evento Clicado", eventoClique.getNome());
						
						Intent intent = new Intent(getApplicationContext(), EventActivity.class);
						intent.putExtra("usuario", usuario);
						intent.putExtra("evento", eventoClique);
						startActivity(intent);
					}
				}
			}	
		});
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
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
				map.addMarker(new MarkerOptions().title(usuario.getNome()).snippet("Usuário").position(userLocation));
				
				obterListaEventos();
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
				callEventManagementActivity();
				return true;
			case R.id.action_friends :
				callFriendsActivity();
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void callEventManagementActivity()
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
