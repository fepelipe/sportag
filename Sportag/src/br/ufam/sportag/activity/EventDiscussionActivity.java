package br.ufam.sportag.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.Util;
import br.ufam.sportag.adapter.AdapterListView;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.model.Comentario;
import br.ufam.sportag.model.Evento;
import br.ufam.sportag.model.Usuario;

public class EventDiscussionActivity extends Activity {

	private Evento evento;
	private Usuario usuario;
	private ArrayList<Comentario> listaComentarios = new ArrayList<Comentario>();
	private AdapterListView<Comentario> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_discussion);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		obterDados();
		mostrarListaComentarios();
	}

	private void obterDados()
	{
		//O evento selecionado na lista de eventos
		evento = (Evento) getIntent().getExtras().getSerializable("evento");
		
		//O usuário que está logado no aplicativo
		usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_discussion, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_refreshEventDiscussion :
				receberComentarios();
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void receberComentarios()
	{
		listaComentarios.clear();
		listAdapter.notifyDataSetChanged();
		
		HashMap<String, Object> args = new HashMap<String, Object>()
		{{
			put("type", "comentario");
			put("evento_id", evento.getId());
		}};
		
		String requestUrl = Util.sportagServerUrl + "search.php" + Util.dictionaryToString(args);
		HttpWebRequest webRequest = new HttpWebRequest(this, requestUrl, "Carregando comentários...")
		{
			public void onSuccess(String dataReceived)
			{	
				try
				{
					JSONArray arrayComentarios = new JSONArray(dataReceived);
					for(int index=0; index<arrayComentarios.length(); index++)
					{
						final JSONObject eventoJSONObj = arrayComentarios.getJSONObject(index);
						String dataHora = eventoJSONObj.optString("comentario.dataHora");
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US);
						final Date dataHoraObj = dateFormat.parse(dataHora);
						
						final Usuario usuario = new Usuario();
						usuario.setId_foursquare(eventoJSONObj.getInt("usuario.id_foursquare"));
						usuario.setNome(eventoJSONObj.getString("usuario.nome"));
						usuario.setFotoPrefix(eventoJSONObj.getString("usuario.fotoPrefix"));
						usuario.setFotoSuffix(eventoJSONObj.getString("usuario.fotoSuffix"));
						
						Comentario comentario = new Comentario();
						comentario.setId(eventoJSONObj.optInt("comentario.id"));
						comentario.setDataHora(dataHoraObj);
						comentario.setConteudo(eventoJSONObj.optString("comentario.conteudo"));
						comentario.setUsuario(usuario);
						comentario.setEvento(evento);
						
						listaComentarios.add(comentario);
						listAdapter.notifyDataSetChanged();
					}
					
				} catch (JSONException e)
				{
					Log.e("Erro", "JSON", e);
				}catch (ParseException e)
				{
					Log.e("Erro", "Parsing date", e);
				}
			}
		};
		webRequest.execute();
	}
	
	public void postarComentario(final Comentario comentario)
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("type", "comentario");
		args.put("dataHora", DateFormat.format("yyyy-MM-dd kk:mm:ss", comentario.getDataHora()));
		args.put("conteudo", comentario.getConteudo());
		args.put("evento_id", comentario.getEvento().getId());
		args.put("usuario_id", comentario.getUsuario().getId_foursquare());
		
		String requestUrl = Util.sportagServerUrl + "add.php" + Util.dictionaryToString(args);
		HttpWebRequest webRequest = new HttpWebRequest(this, requestUrl, "Postando comentário...")
		{
			public void onSuccess(String dataReceived)
			{	
				EventDiscussionActivity.this.listaComentarios.add(comentario);
				listAdapter.notifyDataSetChanged();
			}
		};
		webRequest.execute();
	}
	
	public void btn_eventMessageSend_Click(View view)
	{
		EditText editTextMessage = (EditText) findViewById(R.id.edit_eventMessage);
		final String mensagemDigitada = editTextMessage.getText().toString();
		editTextMessage.setText("");
		Comentario comentario = new Comentario() {{ setConteudo(mensagemDigitada); setUsuario(usuario); setEvento(evento); setDataHora(new Date()); }};
		postarComentario(comentario);
	}
	
	public void mostrarListaComentarios()
	{			
		listAdapter = new AdapterListView<Comentario>(this, R.layout.item_list_eventcomment, listaComentarios)
		{
			public void setItemContentLayout(View view, Comentario item)
			{
				String nome = item.getUsuario().getNome();
				String dataHora = DateFormat.format("kk:mm", item.getDataHora()).toString();
				String conteudo = item.getConteudo();
				
				((TextView) view.findViewById(R.id.nomeUsuarioComentario)).setText(nome);
				((TextView) view.findViewById(R.id.horaEnvioComentario)).setText(dataHora);
				((TextView) view.findViewById(R.id.textoMensagemComentario)).setText(conteudo);
			}
		};
		
		ListView mainListView = (ListView) findViewById(R.id.list_eventMessages);
		mainListView.setAdapter(listAdapter);
		mainListView.setCacheColorHint(Color.TRANSPARENT);
		
		receberComentarios();
	}
}
