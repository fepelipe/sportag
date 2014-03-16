package br.ufam.sportag.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.model.Usuario;

public class ProfileActivity extends Activity {

	private Usuario usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		usuario = (Usuario) getIntent().getSerializableExtra("usuario");
		
		fillContent();
	}

	private void fillContent()
	{
		((TextView)findViewById(R.id.tv_profile_name)).setText(usuario.getNome());
		((ImageView) findViewById(R.id.img_profile_pic)).setImageBitmap(usuario.getAvatar());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
