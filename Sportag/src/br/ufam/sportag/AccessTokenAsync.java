package br.ufam.sportag;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AccessTokenAsync extends AsyncTask<Void, Void, Void> {
	private Context contexto;
	private String code;
	private String token;
	private String tokenUrl;
	public RegisterResponse delegate = null;

	public AccessTokenAsync(Context contexto, String code, String tokenUrl) {
		this.contexto = contexto;
		this.code = code;
		this.tokenUrl = tokenUrl;
	}

	public String getToken() {
		return token;
	}

	@Override
	protected Void doInBackground(Void... params) {

		try {
			Log.d("Token", "Request started!");

			final URL url = new URL(tokenUrl + "&code=" + code + "&v=20140212");
			final HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			String response = Util.streamToString(urlConnection
					.getInputStream());
			Log.d("Token Response", response);
			JSONObject jsonObj = new JSONObject(response);
			token = jsonObj.optString("access_token");
			Log.i("Token Recebido", token);
		} catch (MalformedURLException malForException) {
			Log.d("ErroMalFormed", "Erro", malForException);
		} catch (IOException ioException) {
			Log.d("ErroIO", "Erro", ioException);
		} catch (JSONException jsonException) {
			Log.d("ErroJSON", "Erro", jsonException);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.i("Token Recebido", token);
		super.onPostExecute(result);
		delegate.tokenReceived(token);
	}
}