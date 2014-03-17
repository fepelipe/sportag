
package br.ufam.sportag.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.ufam.sportag.R;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.dialog.EventInviteDialog;
import br.ufam.sportag.model.Evento;
import br.ufam.sportag.model.User;
import br.ufam.sportag.model.Usuario;
import br.ufam.sportag.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventActivity extends Activity
{
	// private String eventTitle;
	private GoogleMap map;
	private LatLng eventLocation;
	private Evento evento;
	private Usuario usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
		evento = (Evento) getIntent().getExtras().getSerializable("evento");
		
		Log.i("Evento Recebido", evento.getNome());
		
		initJoinDetail();
		addEventDetails();
	}
	
	private void initJoinDetail()
	{
		// Verifica se o usuário já está participando do evento, e modifica o
		// estado do switch
		final Switch switchJoin = (Switch) findViewById(R.id.switch_join);
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("type", "usuario_evento_confirmado");
		args.put("usuario_id", usuario.getId_foursquare());
		args.put("evento_id", evento.getId());
		String isAttendantUrl = Util.searchUrl + Util.dictionaryToString(args);
		
		HttpWebRequest isAttendantRequest = new HttpWebRequest(this, isAttendantUrl)
		{
			
			@Override
			public void onSuccess(String paramString)
			{
				// TODO Identificar se o usuário está participando ou não
				JSONArray arrayObj;
				try {
					arrayObj = new JSONArray(paramString);
					if (!arrayObj.toString().equals("[]")) {
						switchJoin.setChecked(true);	
					}
					setarSwitchListener(switchJoin);
				} catch (JSONException jsonExcep)
				{
					Log.e("Erro", "JSON", jsonExcep);
				};
			}
		};
		
		isAttendantRequest.execute();
	}
	
	private void setarSwitchListener(final Switch switchJoin)
	{
		// Após verificar o estado do switch, seta o listener para habilitar a
		// participação ou cancelamento de participação
		switchJoin.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					saveJoin(switchJoin);
				} else
				{
					cancelJoin(switchJoin);
				}
			}
		});
		
	}
	
	protected void saveJoin(Switch switchJoin)
	{
		// Sinaliza a participação do usuário no evento
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("type", "usuario_evento_confirmado");
		args.put("usuario_id", usuario.getId_foursquare());
		args.put("evento_id", evento.getId());
		String saveJoinUrl = Util.addUrl + Util.dictionaryToString(args);
		
		HttpWebRequest saveJoinRequest = new HttpWebRequest(this, saveJoinUrl)
		{
			
			@Override
			public void onSuccess(String paramString)
			{
				Toast.makeText(getApplicationContext(), "Participação confirmada!", Toast.LENGTH_SHORT).show();
			}
		};
		
		saveJoinRequest.execute();
	}
	
	protected void cancelJoin(Switch switchJoin)
	{
		// Cancela a participação do usuário no evento
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("type", "usuario_evento_confirmado");
		args.put("usuario_id", usuario.getId_foursquare());
		args.put("evento_id", evento.getId());
		String saveJoinUrl = Util.deleteUrl + Util.dictionaryToString(args);
		
		HttpWebRequest cancelJoinRequest = new HttpWebRequest(this, saveJoinUrl)
		{
			
			@Override
			public void onSuccess(String paramString)
			{
				Toast.makeText(getApplicationContext(), "Participação cancelada", Toast.LENGTH_SHORT).show();
			}
		};
		
		cancelJoinRequest.execute();
	}
	
	private void addEventDetails()
	{
		// Popula a tela de eventos com os dados recebidos na intent
		
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
		
		// Adiciona a localização do evento no mapa
		eventLocation = new LatLng(latit, longit);
		map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.icone_futebol)));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 14));
		
		// Adiciona imagens dos participantes no grid
		addAttendantsToGrid();
	}
	
	private void addAttendantsToGrid()
	{
		final ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
	
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("type", "usuario_evento_confirmado");
		args.put("evento_id", evento.getId());
		String attendantsUrl = Util.searchUrl + Util.dictionaryToString(args);
		
		HttpWebRequest attendantsRequest = new HttpWebRequest(this, attendantsUrl)
		{
			@Override
			public void onSuccess(String paramString)
			{
				JSONArray arrayObj;
				try
				{
					GridLayout grid = (GridLayout) findViewById(R.id.grid_attendants);
					arrayObj = new JSONArray(paramString);
					
					for (int i = 0; i < arrayObj.length(); i++)
					{
						try
						{
							JSONObject eventoJSONObj = arrayObj.getJSONObject(i);
							
							Usuario usuario = new Usuario();
							usuario.setId_foursquare(eventoJSONObj.getInt("usuario.id_foursquare"));
							usuario.setNome(eventoJSONObj.getString("usuario.nome"));
							usuario.setFotoPrefix(eventoJSONObj.getString("usuario.fotoPrefix"));
							usuario.setFotoSuffix(eventoJSONObj.getString("usuario.fotoSuffix"));
							usuario.setAvatarString64(eventoJSONObj.optString("usuario.avatar"));
							
							ImageView imageView = new ImageView(getApplicationContext());
							imageView.setLayoutParams(new LayoutParams(32, 32));
							imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
							imageView.setPadding(5, 5, 5, 5);
							imageView.setImageBitmap(usuario.getAvatar());
							grid.addView(imageView);
							
							listaUsuarios.add(usuario);
						}
						catch (JSONException e)
						{
							Log.e("Erro", "JSON", e);
						}
					}
				} catch (JSONException e)
				{
					Log.e("JSON", "Get frineds attendants to event", e);
				}
			}
		};
		
		attendantsRequest.execute();
	}
	
//	private void parsingUsuarios(final ArrayList<Usuario> listaUsuarios, String jsonString, final Runnable afterParsing)
//	{
//		try
//		{
//			final JSONArray arrayObj = new JSONArray(jsonString);
//
//			String urlRequest = usuario.getFotoPrefix() + "36x36" + usuario.getFotoSuffix();
//			HttpWebRequest webRequest = new HttpWebRequest(this, urlRequest, true)
//			{
//				@Override
//				public void onSuccess(Object objReceived)
//				{
//					for (int i = 0; i < arrayObj.length(); i++)
//					{
//						try
//						{
//							JSONObject eventoJSONObj = arrayObj.getJSONObject(i);
//							
//							Usuario usuario = new Usuario();
//							usuario.setId_foursquare(eventoJSONObj.getInt("usuario.id_foursquare"));
//							usuario.setNome(eventoJSONObj.getString("usuario.nome"));
//							usuario.setFotoPrefix(eventoJSONObj.getString("usuario.fotoPrefix"));
//							usuario.setFotoSuffix(eventoJSONObj.getString("usuario.fotoSuffix"));
//							
//							Bitmap avatar = (Bitmap) objReceived;
//							usuario.setAvatar(avatar);
//							listaUsuarios.add(usuario);
//						}
//						catch (JSONException e)
//						{
//							Log.e("Erro", "JSON", e);
//						}
//					}
//					
//					afterParsing.run();
//				}
//			};
//			webRequest.execute();
//		} 
//		catch (JSONException e)
//		{
//			Log.e("Erro", "JSON", e);
//		}
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}
	
	public void callEventDiscussionActivity(View view)
	{
		// Chama a tela de discussão desse evento
		
		Intent intent = new Intent(getApplicationContext(), EventDiscussionActivity.class);
		intent.putExtras(getIntent().getExtras());
		startActivity(intent);
	}
	
	private void obterListaAmigos()
	{
		SharedPreferences strings = getSharedPreferences("strings", MODE_PRIVATE);
		String token = strings.getString("token", "0");
		
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("oauth_token", token);
		args.put("v", DateFormat.format("yyyyMMdd", new Date()));
		
		String urlString = Util.selfDataRequestUrl + "/friends" + Util.dictionaryToString(args);
		HttpWebRequest friendsRequest = new HttpWebRequest(this, urlString)
		{
			@Override
			public void onSuccess(String stringReceived)
			{
				try
				{
					Util util = new Util();
					ArrayList<User> listaUsers = util.getFriends(stringReceived);
					getFriendsWhoHaveSportag(listaUsers);
				} catch (JSONException e)
				{
					Log.e("JSON", "Parsing friends from JSON", e);
				}
				
			}
			
			private void getFriendsWhoHaveSportag(ArrayList<User> listaUsers)
			{
				String listaUsersString = Util.usersToJsonArray(listaUsers);
				
				HashMap<String, Object> argsFriendsSportag = new HashMap<String, Object>();
				argsFriendsSportag.put("type", "usuario");
				argsFriendsSportag.put("ids", listaUsersString);
				
				String friendsSportagUrl = Util.searchUrl + Util.dictionaryToString(argsFriendsSportag);
				HttpWebRequest friendsSportag = new HttpWebRequest(EventActivity.this, friendsSportagUrl)
				{
					@Override
					public void onSuccess(String stringReceived)
					{
						JSONArray usersArray;
						try
						{
							usersArray = new JSONArray(stringReceived);
							final ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
							for (int i = 0; i < usersArray.length(); i++)
							{
								JSONObject userObj = usersArray.getJSONObject(i);
								
								Usuario usuario = new Usuario();
								usuario.setId_foursquare(userObj.getInt("usuario.id_foursquare"));
								usuario.setNome(userObj.getString("usuario.nome"));
								usuario.setFotoPrefix(userObj.getString("usuario.fotoPrefix"));
								usuario.setFotoSuffix(userObj.getString("usuario.fotoSuffix"));
								usuario.setAvatarString64(userObj.optString("usuario.avatar"));
								listaUsuarios.add(usuario);
							}
							
							EventInviteDialog inviteDialog = new EventInviteDialog(listaUsuarios)
							{
								public void onSelectedItem(int position) 
								{
									HashMap<String, Object> argsConvite = new HashMap<String, Object>();
									argsConvite.put("type", "usuario_evento_convite");
									argsConvite.put("evento_id", evento.getId());
									argsConvite.put("usuario_id", listaUsuarios.get(position).getId_foursquare());
									String urlConviteRequest = Util.addUrl + Util.dictionaryToString(argsConvite);
									HttpWebRequest conviteRequest = new HttpWebRequest(EventActivity.this, urlConviteRequest)
									{
										public void onSuccess(String stringReceived) 
										{
											runOnUiThread(new Runnable(){
												@Override
												public void run()
												{
													Toast.makeText(getApplicationContext(), "O usuário foi convidado com sucesso!", Toast.LENGTH_SHORT).show();
												}
											});
										};
									};
									conviteRequest.execute();
								};
							};
							inviteDialog.show(getFragmentManager(), "event_invite");
							
						} catch (JSONException e)
						{
							Log.e("JSON", "Erro ao parsear lista de amigos que estão no Sportag", e);
						}
					}
				};
				
				friendsSportag.execute();
			}
		};
		
		friendsRequest.execute();
	}
	
	public void callEventInvitationDialog(View view)
	{
		// Chama a dialog de amigos que podem ser convidados para o evento
		
		obterListaAmigos();
	}
}