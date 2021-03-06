package br.ufam.sportag.activity;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import br.ufam.sportag.R;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.asynctask.UserService;
import br.ufam.sportag.model.Usuario;

import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;

public class MainActivity extends Activity  {

	private String tokenUrl;
	private String code;
	private String token;
	private static final int REQUEST_CODE_FSQ_CONNECT = 200;
	private static final String CLIENT_ID = "2WDE5IGFUJ2SUTOOS0MFTAZHQQFRZ0WOUDT2FKXWEQIDCF5U";
	private static final String CLIENT_SECRET = "SFOGBEQSUQIOG212M1YJVCRIXP4CA0SJUA5CWEQV1LTOBW1E";
	private SharedPreferences strings;
	public static Context ApplicationContext;
	
	// Usuário logado no app
	// Sempre cheque se é nulo antes de usar
	private Usuario usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ApplicationContext = getApplicationContext();

		// TOKEN: Verificaçãoo nicial
		strings = getSharedPreferences("strings", MODE_PRIVATE);
		token = strings.getString("token", "null");
		Log.i("Token Inicial", token);

		// Se houver um Token, ele vai direto para FriendsActivity.
		if (token != "null") {
			callMapActivity();
		}
	}

	// Se não houver token, pode-se fazer a request de um token em AccessToken
	// Esse é o método chamado ao apertar o botão "Iniciar autenticação"
	public void foursquareAuth(View view) {
		Intent intent = FoursquareOAuth.getConnectIntent(this, CLIENT_ID);
		if (!FoursquareOAuth.isPlayStoreIntent(intent)) {
			startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_FSQ_CONNECT:
			AuthCodeResponse codeResponse = FoursquareOAuth
					.getAuthCodeFromResult(resultCode, data);
			code = codeResponse.getCode();

			Log.d("Token", String.format("Code: %s", code));

			tokenUrl = "https://foursquare.com/oauth2/access_token"
					+ "?client_id=" + CLIENT_ID + "&client_secret="
					+ CLIENT_SECRET + "&grant_type=authorization_code"
					+ "&code=" + code + "&v=" + DateFormat.format("yyyyMMdd", new Date());
			
			HttpWebRequest tokenRequest = new HttpWebRequest(this, tokenUrl)
			{
				@Override
				public void onSuccess(String tokenString)
				{
					JSONObject jsonObj;
					try
					{
						jsonObj = new JSONObject(tokenString);
						token = jsonObj.optString("access_token");	
						tokenReceived(token);
						
					} catch (JSONException jsonExcep)
					{
						Log.e("Erro", "JSON", jsonExcep);
					}
									
				}
			}; 
			
			tokenRequest.execute();

			break;
		}
	}

	public void tokenReceived(String token) {

		SharedPreferences.Editor editor = strings.edit();
		editor.putString("token", token);
		editor.commit();
		Log.i("Token Armazenado", token);

		UserService userRegister = new UserService(this, token)
		{
			public void onSuccess(Usuario usuario)
			{
				MainActivity.this.usuario = null;
				registerSuccess(usuario);
			}
		};
		
		userRegister.startService();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	public void registerSuccess(Usuario usuario) {
		SharedPreferences.Editor editor = strings.edit();
		editor.putInt("idUser", usuario.getId_foursquare());
		editor.putString("userFirstName", usuario.getNome());
		editor.commit();
		Log.i("First Name Armazenado", usuario.getNome());
		callMapActivity();
	}

	private void callMapActivity() {
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra("usuario", usuario);
		startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
