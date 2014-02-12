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

public class UserRegisterAsync extends AsyncTask<Void, Void, Void> {
	private Context contexto;
	private String token;
	private String userRegisterUrl = "http://sportag.net76.net/signup.php";
	private String selfDataRequestUrl = "https://api.foursquare.com/v2/users/self";
	public RegisterResponse delegate = null;

	public UserRegisterAsync(Context contexto, String token) {
		this.contexto = contexto;
		this.token = token;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Util util = new Util();
		
		try {
			Log.d("User Register", "Request started!");

			final URL userRequestUrl = new URL (selfDataRequestUrl + "?oauth_token=" + token);
			final HttpURLConnection userRequestConnection = (HttpURLConnection) userRequestUrl
					.openConnection();
			userRequestConnection.setRequestMethod("GET");
			userRequestConnection.setDoInput(true);
			userRequestConnection.connect();
			String requestResponse = Util.streamToString(userRequestConnection
					.getInputStream());
			Log.d("Self Data Response", requestResponse);

			User self = util.getUser(requestResponse);
			
			final URL selfRegisterUrl = new URL(userRegisterUrl + "?nome=" + self.firstName
					+ "&foto=" + self.photoURL + "&id_foursquare=" + self.id);
			final HttpURLConnection selfRegisterConnection = (HttpURLConnection) selfRegisterUrl
					.openConnection();
			selfRegisterConnection.setRequestMethod("GET");
			selfRegisterConnection.setDoInput(true);
			selfRegisterConnection.connect();
			String registerResponse = Util.streamToString(selfRegisterConnection
					.getInputStream());
			Log.d("Register Response", registerResponse);
			
			JSONObject jsonObj = new JSONObject(registerResponse);
			String successResult = jsonObj.optString("success");
			Log.i("Usu‡rio registrado", successResult);
			
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
		super.onPostExecute(result);
		delegate.registerSuccess();
	}
}