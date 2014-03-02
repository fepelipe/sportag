
package br.ufam.sportag.asynctask;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Paint.Join;
import android.util.Log;
import br.ufam.sportag.Util;
import br.ufam.sportag.model.User;
import br.ufam.sportag.model.Usuario;

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
		
		String str = Util.addUrl + Util.dictionaryToString(args);
		HttpWebRequest registerUserRequest = new HttpWebRequest(UserService.this.context, str)
		{
			public void onSuccess(String response)
			{
				Usuario usuario = new Usuario();
				usuario.setId_foursquare(userObj.getId());
				usuario.setNome(userObj.getFirstName());
				usuario.setFotoPrefix(userObj.getPhotoPrefix());
				usuario.setFotoSuffix(userObj.getPhotoSuffix());
				
				UserService.this.onSuccess(usuario);
			}
		};
		
		registerUserRequest.execute();
	}
}