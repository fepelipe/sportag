package br.ufam.sportag.asynctask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufam.sportag.RegisterResponse;
import br.ufam.sportag.Util;
import br.ufam.sportag.model.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UserRegisterAsync extends AsyncTask<Void, Void, Void> {
	private Context contexto;
	private String token;
	private String userRegisterUrl = "http://sportag.net76.net/add.php";
	private String selfDataRequestUrl = "https://api.foursquare.com/v2/users/self";
	public RegisterResponse delegate = null;
	private User self;
	ProgressDialog progressDialog;

	public UserRegisterAsync(Context contexto, String token) {
		this.contexto = contexto;
		this.token = token;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(contexto, "", "Carregando...");
		progressDialog.setCancelable(false);
	}

	@Override
	protected Void doInBackground(Void... params) {
		Util util = new Util();

		try {
			Log.d("User Register", "Request started!");

			final URL userRequestUrl = new URL(selfDataRequestUrl
					+ "?oauth_token=" + token + "&v=20140212");
			final HttpURLConnection userRequestConnection = (HttpURLConnection) userRequestUrl
					.openConnection();
			userRequestConnection.setRequestMethod("GET");
			userRequestConnection.setDoInput(true);
			userRequestConnection.connect();
			String requestResponse = Util.streamToString(userRequestConnection
					.getInputStream());
			Log.d("Self Data Response", requestResponse);

			self = util.getUser(requestResponse);

			Log.i("Self", self.firstName + " " + self.photoPrefix + " "
					+ self.photoSuffix + " " + self.id);

			final URL selfRegisterUrl = new URL(userRegisterUrl + "?nome="
					+ self.firstName + "&foto=" + self.photoPrefix + "36x36" + self.photoSuffix
					+ "&id_foursquare=" + self.id);
			final HttpURLConnection selfRegisterConnection = (HttpURLConnection) selfRegisterUrl
					.openConnection();
			selfRegisterConnection.setRequestMethod("GET");
			selfRegisterConnection.setDoInput(true);
			selfRegisterConnection.connect();
			String registerResponse = Util
					.streamToString(selfRegisterConnection.getInputStream());
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
		progressDialog.dismiss();
		delegate.registerSuccess(self.firstName);
	}
}