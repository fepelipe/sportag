package br.ufam.sportag.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import br.ufam.sportag.R;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setFakeLabels();
	}

	private void setFakeLabels()
	{
		SharedPreferences strings = getSharedPreferences("strings", MODE_PRIVATE);
		String firstName = strings.getString("userFirstName", "Unknow name");		
		((TextView)findViewById(R.id.tv_profile_name)).setText(firstName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
