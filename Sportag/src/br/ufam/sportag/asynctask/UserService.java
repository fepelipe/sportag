
package br.ufam.sportag.asynctask;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Paint.Join;
import android.util.Log;
import br.ufam.sportag.Util;
import br.ufam.sportag.model.User;

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
	
	public abstract void onSuccess(String paramString);
	
	public void startService()
	{
		String str = Util.selfDataRequestUrl + "?oauth_token=" + this.token + "&v=20140212";
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
		
		String str = Util.userRegisterUrl + Util.dictionaryToString(args);
		HttpWebRequest registerUserRequest = new HttpWebRequest(UserService.this.context, str)
		{
			public void onSuccess(String response)
			{
				JSONObject jsonObj;
				try
				{
					jsonObj = new JSONObject(response);
					if (jsonObj.optBoolean("success"))
					{
						UserService.this.onSuccess(userObj.firstName);
					} else
					{
						UserService.this.onError(jsonObj.optString("message"));
					}
				} catch (JSONException jsonExcep)
				{
					Log.e("Error", "Json", jsonExcep);
				}
				
			}
		};
		
		registerUserRequest.execute();
	}
}