package br.ufam.sportag.activity;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.adapter.AdapterListView;
import br.ufam.sportag.model.Usuario;

public class FriendsActivity extends Activity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		makeListFriends();
	}

	private void makeListFriends()
	{
		ArrayList<Usuario> listaAmigos = new ArrayList<Usuario>()
		{{
			add(new Usuario(){{ setNome("Amigo1"); }});
			add(new Usuario(){{ setNome("Amigo2"); }});
			add(new Usuario(){{ setNome("Amigo3"); }});	
		}};
		
		AdapterListView<Usuario> adapter = new AdapterListView<Usuario>(this, R.layout.item_list_friends, listaAmigos)
		{
			public void setItemContentLayout(View view, Usuario item)
			{
				((TextView) view.findViewById(R.id.friendUserNameListFriend)).setText(item.getNome());				
			}
		};
		
		ListView mainList = (ListView)findViewById(R.id.list_friends);
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Intent intent = new Intent(this, FriendChatActivity.class);
		startActivity(intent);
	}

}
