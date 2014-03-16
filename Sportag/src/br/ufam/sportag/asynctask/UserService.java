
package br.ufam.sportag.asynctask;

import java.util.Date;
import java.util.HashMap;
import org.json.JSONException;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import br.ufam.sportag.model.User;
import br.ufam.sportag.model.Usuario;
import br.ufam.sportag.util.Util;

public abstract class UserService
{
	private Context context;
	private String token;
	
	public UserService(Context context, String token)
	{
		this.context = context;
		this.token = token;
	}
	
	public void onError(String firstName)
	{
		Log.e("Erro - Servidor Sportag", firstName);
	}
	
	public abstract void onSuccess(Usuario usuario);
	
	public void startService()
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("oauth_token", token);
		args.put("v", DateFormat.format("yyyyMMdd", new Date()));
		
		String str = Util.selfDataRequestUrl + Util.dictionaryToString(args);
		HttpWebRequest selfDataRequest = new HttpWebRequest(this.context, str)
		{
			public void onSuccess(String jsonResponseString)
			{
				Util localUtil = new Util();
				try
				{
					selfRegister(localUtil.getUser(jsonResponseString));
				} catch (JSONException localJSONException)
				{
					Log.d("ErroJSON", "Erro", localJSONException);
				}
			}
		};
		
		selfDataRequest.execute();
	}
	
	private void selfRegister(final User userObj)
	{
		HashMap<String, Object> args = new HashMap<String, Object>()
		{
			{
				put("type", "usuario");
				put("nome", userObj.firstName);
				put("fotoPrefix", userObj.photoPrefix);
				put("fotoSuffix", userObj.photoSuffix);
				put("id_foursquare", userObj.id);
			}
		};
		
		String str = Util.addUrl + Util.dictionaryToString(args);
		HttpWebRequest registerUserRequest = new HttpWebRequest(UserService.this.context, str)
		{
			public void onSuccess(String response)
			{
				Usuario usuario = Usuario.parseUser(userObj);
				UserService.this.onSuccess(usuario);
			}
		};
		
		registerUserRequest.execute();
	}
}