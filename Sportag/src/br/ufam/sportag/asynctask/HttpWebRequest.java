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

	public HttpWebRequest(Context paramContext, String paramString) {
		this.contexto = paramContext;
		this.urlString = paramString;
	}

	protected String doInBackground(Void... paramVarArgs) {
		try {
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
		this.progressDialog.dismiss();
		if (paramString != null) {
			onSuccess(paramString);
			return;
		}
		onError();
	}

	protected void onPreExecute() {
		super.onPreExecute();
		this.progressDialog = ProgressDialog.show(this.contexto, "",
				"Carregando...");
		this.progressDialog.setCancelable(false);
	}

	public abstract void onSuccess(String paramString);
}
