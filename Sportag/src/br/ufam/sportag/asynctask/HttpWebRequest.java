package br.ufam.sportag.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.ufam.sportag.Util;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class HttpWebRequest extends AsyncTask<Void, Void, String> {
	Context contexto;
	ProgressDialog progressDialog;
	private String urlString;
	private String loadingMessage;

	public HttpWebRequest(Context paramContext, String urlString) {
		this.loadingMessage = "Carregando...";
		this.contexto = paramContext;
		this.urlString = urlString;
	}

	public HttpWebRequest(Context paramContext, String urlString,
			String loadingMessage) {
		this.loadingMessage = loadingMessage;
		this.contexto = paramContext;
		this.urlString = urlString;
	}

	protected String doInBackground(Void... paramVarArgs) {
		try {

			Log.d("Request", "[GET] " + urlString);

			HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
					this.urlString).openConnection();
			localHttpURLConnection.setRequestMethod("GET");
			localHttpURLConnection.setDoInput(true);
			localHttpURLConnection.connect();
			String str = Util.streamToString(localHttpURLConnection
					.getInputStream());
			return str;
		} catch (MalformedURLException localMalformedURLException) {
			Log.d("ErroMalFormed", "Erro", localMalformedURLException);
		} catch (IOException localIOException) {
			Log.d("ErroIO", "Erro", localIOException);
		}
		return null;
	}

	public void onError() {
	}

	protected void onPostExecute(String paramString) {
		super.onPostExecute(paramString);
		try {
			this.progressDialog.dismiss();
		} catch (Exception e) {
		}
		if (paramString != null) {
			onSuccess(paramString);
			return;
		}
		onError();
	}

	protected void onPreExecute() {
		super.onPreExecute();
		this.progressDialog = ProgressDialog.show(this.contexto, "",
				loadingMessage);
		this.progressDialog.setCancelable(false);
	}

	public abstract void onSuccess(String paramString);
}
