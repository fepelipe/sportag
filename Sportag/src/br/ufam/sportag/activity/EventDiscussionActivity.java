package br.ufam.sportag.activity;

import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.adapter.AdapterListView;
import br.ufam.sportag.model.Comentario;
import br.ufam.sportag.model.Usuario;

public class EventDiscussionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_discussion);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mostrarListaComentarios();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_discussion, menu);
		return true;
	}
	
	public void mostrarListaComentarios()
	{
		final Usuario user1 = new Usuario() {{ setNome("Fulano 1"); }};
		final Usuario user2 = new Usuario() {{ setNome("Fulano 2"); }};

		ArrayList<Comentario> listaComentarios = new ArrayList<Comentario>()
		{{
			add(new Comentario() {{ setDateTime(new Date()); setConteudo("Vamo tentar na ponta negra?"); setUsuario(user1);  }});
			add(new Comentario() {{ setDateTime(new Date()); setConteudo("Não sei, ouvi dizer que ia chover."); setUsuario(user2);  }});
			add(new Comentario() {{ setDateTime(new Date()); setConteudo("Vai não, pô!"); setUsuario(user1);  }});
			add(new Comentario() {{ setDateTime(new Date()); setConteudo("Como você sabe?"); setUsuario(user2);  }});
			add(new Comentario() {{ setDateTime(new Date()); setConteudo("Ah, eu olhei na previsão do tempo!"); setUsuario(user1);  }});
			add(new Comentario() {{ setDateTime(new Date()); setConteudo("Beleza, chama o Fulano 3, ele joga bem!"); setUsuario(user2);  }});
			add(new Comentario() {{ setDateTime(new Date()); setConteudo("Pode deixar então!"); setUsuario(user1);  }});
			add(new Comentario() {{ setDateTime(new Date()); setConteudo("Fechou!"); setUsuario(user2);  }});
		}};
		
		ListAdapter adapter = new AdapterListView<Comentario>(this, R.layout.list_itemtemplate_comentario, listaComentarios)
		{
			public void setItemContentLayout(View view, Comentario item)
			{
				String nome = item.getUsuario().getNome();
				String dataHora = DateFormat.format("kk:mm", item.getDateTime()).toString();
				String conteudo = item.getConteudo();
				
				((TextView) view.findViewById(R.id.nomeUsuarioComentario)).setText(nome);
				((TextView) view.findViewById(R.id.horaEnvioComentario)).setText(dataHora);
				((TextView) view.findViewById(R.id.textoMensagemComentario)).setText(conteudo);
			}
		};
		
		ListView mainListView = (ListView) findViewById(R.id.list_eventMessages);
		mainListView.setAdapter(adapter);
		mainListView.setCacheColorHint(Color.TRANSPARENT);
	}
}
