
package br.ufam.sportag.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.adapter.AdapterListView;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.model.User;
import br.ufam.sportag.model.Usuario;
import br.ufam.sportag.util.Util;

public class FriendsActivity extends Activity implements OnItemClickListener
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		obterListaAmigos();
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
				HttpWebRequest friendsSportag = new HttpWebRequest(FriendsActivity.this, friendsSportagUrl)
				{
					@Override
					public void onSuccess(String stringReceived)
					{
						JSONArray usersArray;
						try
						{
							usersArray = new JSONArray(stringReceived);
							ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
							for (int i = 0; i < usersArray.length(); i++)
							{
								JSONObject userObj = usersArray.getJSONObject(i);
								
								Usuario usuario = new Usuario();
								usuario.setId_foursquare(userObj.getInt("usuario.id_foursquare"));
								usuario.setNome(userObj.getString("usuario.nome"));
								usuario.setFotoPrefix(userObj.getString("usuario.fotoPrefix"));
								usuario.setFotoSuffix(userObj.getString("usuario.fotoSuffix"));
								
								if(!userObj.getString("usuario.avatar").equals(""))
								{
									byte[] decodedString = Base64.decode(userObj.getString("usuario.avatar"), Base64.DEFAULT);
									Bitmap avatar = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
									usuario.setAvatar(avatar);
								}								
								
								listaUsuarios.add(usuario);
							}
							
							makeListFriends(listaUsuarios);
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
	
	private void makeListFriends(ArrayList<Usuario> listaAmigos)
	{
		AdapterListView<Usuario> adapter = new AdapterListView<Usuario>(this, R.layout.item_list_friends, listaAmigos)
		{
			public void setItemContentLayout(View view, Usuario item)
			{
				if(item.getAvatar() != null)
					((ImageView) view.findViewById(R.id.friendPhotoListFriend)).setImageBitmap(item.getAvatar());
				
				((TextView) view.findViewById(R.id.friendUserNameListFriend)).setText(item.getNome());
			}
		};
		
		ListView mainList = (ListView) findViewById(R.id.list_friends);
		mainList.setAdapter(adapter);
		mainList.setOnItemClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends, menu);
		return true;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Intent intent = new Intent(this, FriendChatActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home :
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
