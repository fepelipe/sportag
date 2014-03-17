package br.ufam.sportag.dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.ufam.sportag.R;
import br.ufam.sportag.asynctask.HttpWebRequest;
import br.ufam.sportag.model.LocalizacaoEvento;
import br.ufam.sportag.util.GeoCoordinate;
import br.ufam.sportag.util.Util;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class EventLocationDialog extends DialogFragment {
	private GoogleMap map;
	private ListView listLocation;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> locationStrings = new ArrayList<String>();
	private HttpWebRequest locationsRequest;
	private String token;
	private ArrayList<LocalizacaoEvento> locationEventArray = new ArrayList<LocalizacaoEvento>();
	private LatLng currentPosition;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_location, null);

		SharedPreferences strings = getActivity().getSharedPreferences(
				"strings", 0);
		token = strings.getString("token", "");

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.location_fragment)).getMap();
		listLocation = (ListView) view
				.findViewById(R.id.list_location_foursquare);

		listLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				LocalizacaoEvento locationSelection = locationEventArray.get(position);
				locationSelection.setLatitude(currentPosition.latitude);
				locationSelection.setLongitude(currentPosition.longitude);
				onLocationSelected(locationSelection);
			}
		});

		map.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker marker) {
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				currentPosition = marker.getPosition();
				getNearestLocations(currentPosition.latitude, currentPosition.longitude,
						token);
			}

			@Override
			public void onMarkerDrag(Marker marker) {
			}
		});

		addLocationMarker();

		builder.setView(view).setNegativeButton("Cancelar",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		return builder.create();
	}

	private void addLocationMarker() {
		GeoCoordinate coordinates = new GeoCoordinate(getActivity()) {
			public void onGPSSuccess(Location location) {
				double userLongitude = location.getLongitude();
				double userLatitude = location.getLatitude();

				LatLng userLocation = new LatLng(userLatitude, userLongitude);
				map.setMyLocationEnabled(true);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,
						12));
				map.addMarker(new MarkerOptions()
						.position(userLocation)
						.draggable(true)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icone_futebol)));

				getNearestLocations(userLatitude, userLongitude, token);
			}

			@Override
			public void onFail(String title, String message) {
				// TODO Auto-generated method stub

			}
		};

		coordinates.startService();
	}

	private void getNearestLocations(double latitude, double longitude,
			String token) {
		String latlngString = String.valueOf(latitude) + ","
				+ String.valueOf(longitude);

		HashMap<String, Object> args = new HashMap<String, Object>();

		args.put("ll", latlngString);
		args.put("oauth_token", token);
		args.put("v", DateFormat.format("yyyyMMdd", new Date()));

		String locationsUrl = Util.venuesRequestUrl
				+ Util.dictionaryToString(args);
		locationsRequest = new HttpWebRequest(getActivity(), locationsUrl) {
			@Override
			public void onSuccess(String stringReceived) {
				locationStrings.clear();

				try {
					JSONObject root = new JSONObject(stringReceived);
					JSONObject response = root.getJSONObject("response");
					JSONArray locationsJSONArray = response
							.getJSONArray("venues");

					for (int i = 0; i < locationsJSONArray.length(); i++) {
						final JSONObject locationObj = locationsJSONArray
								.getJSONObject(i);

						final LocalizacaoEvento localizacao = new LocalizacaoEvento();
						localizacao.setNomeLocal(locationObj.optString("name"));
						Log.i("Location Name", locationObj.optString("name"));

						JSONObject location = locationObj
								.getJSONObject("location");

						localizacao.setEndereco(locationObj
								.optString("address"));
						Log.i("Location Address",
								locationObj.optString("address"));

						locationEventArray.add(localizacao);
						locationStrings.add(locationObj.getString("name"));
					}

					if (listLocation.getAdapter() == null) {
						adapter = new ArrayAdapter<String>(getActivity(),
								android.R.layout.simple_list_item_1,
								locationStrings);
						listLocation.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		locationsRequest.execute();
	}

	public abstract void onLocationSelected(LocalizacaoEvento localizacaoEvento);
}
