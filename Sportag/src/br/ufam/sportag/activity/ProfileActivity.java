package br.ufam.sportag.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.adapter.AdapterListView;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.model.Esporte;
import br.ufam.sportag.model.Usuario;
import br.ufam.sportag.util.Util;

public class ProfileActivity extends Activity {

	private Usuario usuario;
	private ArrayList<Esporte> listaProfileSports;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		usuario = (Usuario) getIntent().getSerializableExtra("usuario");

		fillContent();
	}

	private void fillContent() {
		((TextView) findViewById(R.id.tv_profile_name)).setText(usuario
				.getNome());
		((ImageView) findViewById(R.id.img_profile_pic)).setImageBitmap(usuario
				.getAvatar());

		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("type", "usuario_esporte_preferido");
		args.put("usuario_id", usuario.getId_foursquare());
		String profileSportsRequestUrl = Util.searchUrl
				+ Util.dictionaryToString(args);

		HttpWebRequest profileSportsRequest = new HttpWebRequest(this,
				profileSportsRequestUrl) {

			@Override
			public void onSuccess(String stringReceived) {
				Log.i("String received Sports", stringReceived);
				JSONArray sportsArray;
				try {
					sportsArray = new JSONArray(stringReceived);
					listaProfileSports = new ArrayList<Esporte>();
					for (int i = 0; i < sportsArray.length(); i++) {
						JSONObject sportObj = sportsArray.getJSONObject(i);

						Esporte esporte = new Esporte();
						esporte.setId(sportObj.getInt("esporte.id"));
						esporte.setNome(sportObj.getString("esporte.nome"));
						listaProfileSports.add(esporte);
					}
					makeListProfileSports(listaProfileSports);
				} catch (JSONException e) {
					Log.e("JSON",
							"Erro ao parsear lista de esportes preferidos no Sportag",
							e);
				}
			}
		};

	}

	private void makeListProfileSports(ArrayList<Esporte> listaProfileSports) {
		AdapterListView<Esporte> adapter = new AdapterListView<Esporte>(this,
				R.layout.item_list_sports, listaProfileSports) {
			public void setItemContentLayout(View view, Esporte item) {
				if (item != null) {
					((ImageView) view.findViewById(R.id.image_sport))
							.setImageResource(Util.sportsDrawables[item.getId()-1]);
					((TextView) view.findViewById(R.id.tv_profile_sport))
							.setText(item.getNome());
				}
			}
		};

		ListView mainList = (ListView) findViewById(R.id.list_profile_sports);
		mainList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
