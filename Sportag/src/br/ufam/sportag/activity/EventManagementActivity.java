package br.ufam.sportag.activity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import br.ufam.sportag.R;
import br.ufam.sportag.adapter.AdapterListView;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.model.Esporte;
import br.ufam.sportag.model.Evento;
import br.ufam.sportag.model.LocalizacaoEvento;
import br.ufam.sportag.model.Usuario;
import br.ufam.sportag.util.Util;

public class EventManagementActivity extends FragmentActivity implements
		ActionBar.TabListener {
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	static Usuario usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_management);

		usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");


		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_management, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_event:
			callCreateEventActivity();
			return true;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void callCreateEventActivity() {
		Intent intent = new Intent(this, EventCreationActivity.class);
		startActivity(intent);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new SectionFragment();
			Bundle args = new Bundle();
			args.putInt(SectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class SectionFragment extends Fragment implements
			Serializable {
		public static final String ARG_SECTION_NUMBER = "section_number";

		public SectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.i("ARG_SECTION_NUMBER",
					String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER)));
			View rootView = inflater.inflate(R.layout.fragment_event_list,
					container, false);
			ListView listEvent = (ListView) rootView
					.findViewById(R.id.list_event_management);

			ArrayList<Evento> listaEventos = new ArrayList<Evento>();

			AdapterListView<Evento> adapterListEvent = new AdapterListView<Evento>(
					getActivity(), R.layout.item_list_events, listaEventos) {
				public void setItemContentLayout(View view, Evento item) {
					((TextView) view.findViewById(R.id.tv_eventTitle))
							.setText(item.getNome());
					((TextView) view.findViewById(R.id.tv_eventTime))
							.setText(null);
					((TextView) view.findViewById(R.id.tv_eventSportLocal))
							.setText(item.getLocalizacaoEvento().getNomeLocal());
					((TextView) view.findViewById(R.id.tv_eventAttendants))
							.setText(null);
				}
			};

			// TODO Salvar 3 Arrays e mudar o Adapter com esses Arrays;
			
			// Criar listaEventos diferente para cada seção
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
			case 0:
				// Eventos confirmados
				obterEventosConfirmados(listaEventos, adapterListEvent);
				break;
			case 1:
				// Eventos criados
				obterEventosCriados(listaEventos, adapterListEvent);
				break;
			case 2:
				// Convites
				obterConvites(listaEventos, adapterListEvent);
				break;
			default:
				break;
			}
			
			listEvent.setAdapter(adapterListEvent);
			listEvent.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) {
					Intent intent = new Intent(getActivity()
							.getApplicationContext(), EventActivity.class);
					intent.putExtras(getActivity().getIntent().getExtras());
					intent.putExtra("evento",
							(Evento) adapter.getItemAtPosition(position));
					startActivity(intent);
				}
			});
			return rootView;
		}

		private void obterEventosCriados(final ArrayList<Evento> listaEventos,
				final AdapterListView<Evento> adapterListEvent) {
			// Exibe os eventos criados pelo usuário
			
			listaEventos.clear();

			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("type", "evento");
			args.put("criador_usuario_id", usuario.getId_foursquare());

			String requestUrl = Util.sportagServerUrl + "search.php"
					+ Util.dictionaryToString(args);
			HttpWebRequest eventosCriadosRequest = new HttpWebRequest(getActivity(),
					requestUrl) {
				public void onSuccess(String jsonString) {
					parsingEventos(listaEventos, adapterListEvent, jsonString);
				}
			};

			eventosCriadosRequest.execute();
		}

		private void obterEventosConfirmados(final ArrayList<Evento> listaEventos,
				final AdapterListView<Evento> adapterListEvent) {
			// Exibe os eventos que o usuário confirmou participação

			listaEventos.clear();

			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("type", "usuario_evento_confirmado");
			args.put("usuario_id", usuario.getId_foursquare());

			String requestUrl = Util.sportagServerUrl + "search.php"
					+ Util.dictionaryToString(args);
			HttpWebRequest eventosCriadosRequest = new HttpWebRequest(getActivity(),
					requestUrl) {
				public void onSuccess(String jsonString) {
					parsingEventos(listaEventos, adapterListEvent, jsonString);
				}
			};

			eventosCriadosRequest.execute();
		}

		private void obterConvites(final ArrayList<Evento> listaEventos,
				final AdapterListView<Evento> adapterListEvent) {
			// Exibe os eventos que o usuário foi convidado
			//TODO Url de requisição
			listaEventos.clear();

			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("type", "usuario_evento_convite");
			args.put("usuario_id", usuario.getId_foursquare());

			String requestUrl = Util.sportagServerUrl + "search.php"
					+ Util.dictionaryToString(args);
			HttpWebRequest eventosCriadosRequest = new HttpWebRequest(getActivity(),
					requestUrl) {
				public void onSuccess(String jsonString) {
					parsingEventos(listaEventos, adapterListEvent, jsonString);
				}
			};

			eventosCriadosRequest.execute();
		}
		
		private void parsingEventos(final ArrayList<Evento> listaEventos,
				final AdapterListView<Evento> adapterListEvent, String jsonString){
			JSONArray arrayObj;
			try {
				arrayObj = new JSONArray(jsonString);
				for (int i = 0; i < arrayObj.length(); i++) {
					final JSONObject eventoJSONObj = arrayObj
							.getJSONObject(i);

					String dataHora = eventoJSONObj
							.optString("evento.dataHora");
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd kk:mm:ss", Locale.US);
					final Date dataHoraObj = dateFormat.parse(dataHora);

					final Esporte esporte = new Esporte();
					esporte.setId(eventoJSONObj.getInt("esporte.id"));
					esporte.setNome(eventoJSONObj
							.getString("esporte.nome"));

					final LocalizacaoEvento localizacao = new LocalizacaoEvento();
					localizacao.setId(eventoJSONObj
							.getInt("localizacao_evento.id"));
					localizacao.setNomeLocal(eventoJSONObj
							.getString("localizacao_evento.nomeLocal"));
					localizacao.setLatitude(eventoJSONObj
							.getDouble("localizacao_evento.latitude"));
					localizacao.setLongitude(eventoJSONObj
							.getDouble("localizacao_evento.longitude"));
					localizacao.setEndereco(eventoJSONObj
							.getString("localizacao_evento.endereço"));

					Usuario usuario = new Usuario();
					usuario.setId_foursquare(eventoJSONObj
							.getInt("usuario.id_foursquare"));
					usuario.setNome(eventoJSONObj
							.getString("usuario.nome"));
					usuario.setFotoPrefix(eventoJSONObj
							.getString("usuario.fotoPrefix"));
					usuario.setFotoSuffix(eventoJSONObj
							.getString("usuario.fotoSuffix"));

					Evento evento = new Evento();
					evento.setId(eventoJSONObj.getInt("evento.id"));
					evento.setNome(eventoJSONObj
							.getString("evento.nome"));
					evento.setVisivel(eventoJSONObj
							.getInt("evento.visivel") == 1);
					evento.setEsporte(esporte);
					evento.setLocalizacaoEvento(localizacao);
					evento.setDataHora(dataHoraObj);
					evento.setCriador(usuario);

					listaEventos.add(evento);
				}
				adapterListEvent.notifyDataSetChanged();
			} catch (JSONException e) {
				Log.e("Erro", "JSON", e);
			} catch (ParseException e) {
				Log.e("Erro", "Date Parsing", e);
			}
		}
	}

}
