package br.ufam.sportag.activity;

import br.ufam.sportag.R;
import br.ufam.sportag.R.layout;
import br.ufam.sportag.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EventDiscussionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_discussion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_discussion, menu);
		return true;
	}

}
