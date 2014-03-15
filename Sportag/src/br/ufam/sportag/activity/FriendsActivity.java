package br.ufam.sportag.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.adapter.AdapterListView;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.model.User;
import br.ufam.sportag.model.Usuario;
import br.ufam.sportag.util.Util;

public class FriendsActivity extends Activity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		
		String urlString = Util.selfDataRequestUrl + "/friends" + Util.dictionaryToString(args );
		HttpWebRequest friendsRequest = new HttpWebRequest(this, urlString )
		{
			@Override
			public void onSuccess(String stringReceived)
			{
				try
				{
					Util util = new Util();
					ArrayList<User> listaUsers = util.getFriends(stringReceived);
					ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
					for(User userObj : listaUsers)
					{
						listaUsuarios.add(Usuario.parseUser(userObj));
					}
					
					makeListFriends(listaUsuarios);
					
				} catch (JSONException e)
				{
					Log.e("JSON", "Parsing friends from JSON", e);
				}
				
			}
		}; 
		
		friendsRequest.execute();
	}

	private void makeListFriends(ArrayList<Usuario> listaAmigos) 
	{

		AdapterListView<Usuario> adapter = new AdapterListView<Usuario>(this,
				R.layout.item_list_friends, listaAmigos) {
			public void setItemContentLayout(View view, Usuario item) {
				((TextView) view.findViewById(R.id.friendUserNameListFriend))
						.setText(item.getNome());
			}
		};

		ListView mainList = (ListView) findViewById(R.id.list_friends);
		mainList.setAdapter(adapter);
		mainList.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(this, FriendChatActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
