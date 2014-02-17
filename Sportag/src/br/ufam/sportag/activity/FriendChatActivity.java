package br.ufam.sportag.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import br.ufam.sportag.R;

public class FriendChatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_chat);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_chat, menu);
		return true;
	}

}
