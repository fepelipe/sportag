package br.ufam.sportag.activity;

import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.adapter.AdapterListView;
import br.ufam.sportag.model.Chat;
import br.ufam.sportag.model.Mensagem;
import br.ufam.sportag.model.Usuario;

public class FriendChatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_chat);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		makeListFriendChat();
	}

	private void makeListFriendChat()
	{
		final Usuario user1 = new Usuario() {{ setNome("Richard"); }};
		final Usuario user2 = new Usuario() {{ setNome("Felipe"); }};
		
		final ArrayList<Mensagem> listaMensagens = new ArrayList<Mensagem>(){{
			add(new Mensagem() {{ setConteudo("Olá!"); setUsuario(user1); setDataHora(new Date()); }});
			add(new Mensagem() {{ setConteudo("Olá! Tudo bem"); setUsuario(user2); setDataHora(new Date()); }});
		}};
		
		Chat chat = new Chat(){{
			setUsuarioDestinatario(user1);
			setUsuarioRemetente(user2);
			setListaMensagem(listaMensagens);
		}};
		
		AdapterListView<Mensagem> adapter = new AdapterListView<Mensagem>(this, R.layout.item_list_friendchat, chat.getListaMensagem())
		{
			public void setItemContentLayout(View view, Mensagem item)
			{
				((TextView)view.findViewById(R.id.nomeUsuarioChatMessage)).setText(item.getUsuario().getNome());
				((TextView)view.findViewById(R.id.horaEnvioChatMessage)).setText(DateFormat.format("kk:mm", item.getDataHora()).toString());
				((TextView)view.findViewById(R.id.textoMensagemChatMessage)).setText(item.getConteudo());
			}
		};
		
		ListView mainList = (ListView) findViewById(R.id.list_chatMessages);
		
		if(mainList != null)
			mainList.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_chat, menu);
		return true;
	}

}
