
package br.ufam.sportag.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import br.ufam.sportag.util.Util;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class HttpWebRequest extends AsyncTask<Void, Void, Object>
{
	Context contexto;
	ProgressDialog progressDialog;
	private String urlString;
	private String loadingMessage;
	private boolean isBitmap;
//	private HttpURLConnection localHttpURLConnection;
	
	public HttpWebRequest(Context paramContext, String urlString, boolean isBitmap)
	{
		this.loadingMessage = "Carregando...";
		this.contexto = paramContext;
		this.urlString = urlString;
		this.isBitmap = isBitmap;
	}
	
	public HttpWebRequest(Context paramContext, String urlString)
	{
		this.loadingMessage = "Carregando...";
		this.contexto = paramContext;
		this.urlString = urlString;
		this.isBitmap = false;
	}
	
	public HttpWebRequest(Context paramContext, String urlString,
			String loadingMessage)
	{
		this.loadingMessage = loadingMessage;
		this.contexto = paramContext;
		this.urlString = urlString;
		this.isBitmap = false;
	}
	
	protected Object doInBackground(Void... paramVarArgs)
	{
		try
		{
			Log.d("Request", "[GET] " + urlString);
			
			HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(this.urlString).openConnection();
			localHttpURLConnection.setRequestMethod("GET");
			localHttpURLConnection.setDoInput(true);
			localHttpURLConnection.connect();
			
			if(!isBitmap)
				return Util.streamToString(localHttpURLConnection.getInputStream());
			else
				return BitmapFactory.decodeStream(localHttpURLConnection.getInputStream());
			
		} catch (MalformedURLException localMalformedURLException)
		{
			Log.d("ErroMalFormed", "Erro", localMalformedURLException);
		} catch (IOException localIOException)
		{
			Log.d("ErroIO", "Erro", localIOException);
		}
		
		return null;
	}
	
	public void onError()
	{
	}
	
	protected void onPostExecute(Object paramString)
	{
		super.onPostExecute(paramString);
		try
		{
			this.progressDialog.dismiss();
		} catch (Exception e)
		{
		}
		if (paramString != null)
		{
			if(paramString.getClass() == String.class)
				onSuccess( (String) paramString );
			else
				onSuccess( (Object) paramString );
			
			return;
		}
		onError();
	}
	
	protected void onPreExecute()
	{
		super.onPreExecute();
		this.progressDialog = ProgressDialog.show(this.contexto, "", loadingMessage);
		this.progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			public void onCancel(DialogInterface dialog)
			{
				// if(localHttpURLConnection != null)
				// localHttpURLConnection.disconnect();
				//
				cancel(true);
			}
		});
	}
	
	public void onSuccess(String stringReceived){}
	public void onSuccess(Object objReceived){}
}
