package br.ufam.sportag.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import br.ufam.sportag.AccessTokenAsync;
import br.ufam.sportag.R;
import br.ufam.sportag.RegisterResponse;
import br.ufam.sportag.UserRegisterAsync;
import br.ufam.sportag.R.layout;
import br.ufam.sportag.R.menu;

import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;

public class MainActivity extends Activity implements RegisterResponse {

	private String tokenUrl;
	private String code;
	private String token;
	private static final int REQUEST_CODE_FSQ_CONNECT = 200;
	private static final String CLIENT_ID = "2WDE5IGFUJ2SUTOOS0MFTAZHQQFRZ0WOUDT2FKXWEQIDCF5U";
	private static final String CLIENT_SECRET = "SFOGBEQSUQIOG212M1YJVCRIXP4CA0SJUA5CWEQV1LTOBW1E";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TOKEN: Verificação inicial
		SharedPreferences strings = getSharedPreferences("strings",
				MODE_PRIVATE);
		token = strings.getString("token", "null");
		Log.i("Token Inicial", token);

		// Se houver um Token, ele vai direto para FriendsActivity.
		if (token != "null") {
			Intent intent = new Intent(this, MapActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
					+ CLIENT_SECRET + "&grant_type=authorization_code";
			AccessTokenAsync accessToken = new AccessTokenAsync(this, code,
					tokenUrl);
			accessToken.delegate = this;
			accessToken.execute();
			break;
		}
	}

	@Override
	public void tokenReceived(String token) {
		ProgressDialog dialog = ProgressDialog.show(this, "", 
                "Carregando...", true);
		
		SharedPreferences strings = getSharedPreferences("strings",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = strings.edit();
		editor.putString("token", token);
		editor.commit();
		Log.i("Token Armazenado", token);

		UserRegisterAsync userRegister = new UserRegisterAsync(this, token);
		userRegister.delegate = this;
		userRegister.execute();
	}
	
	@Override
	public void registerSuccess() {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}
}
