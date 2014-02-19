package br.ufam.sportag.activity;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import br.ufam.sportag.R;
import br.ufam.sportag.dialog.EventFilterDialog;
import br.ufam.sportag.dialog.UserFilterDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
	private SharedPreferences strings;
	private GoogleMap map;
	private LatLng userLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map_fragment)).getMap();

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				if (marker.getSnippet().equals("Usuário")) {
					Intent intent = new Intent(getApplicationContext(),
							ProfileActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getApplicationContext(),
							EventActivity.class);
					startActivity(intent);
				}
			}
		});
		
		addSelfMarker();
		addEventsMarker();
	}

	private void addEventsMarker() {
		map.addMarker(new MarkerOptions().position(
				new LatLng(-3.045086, -60.085949)).title("Runners"));
		map.addMarker(new MarkerOptions().position(
				new LatLng(-3.102331, -60.025342)).title("Sk8 dos Brow"));
		map.addMarker(new MarkerOptions().position(
				new LatLng(-3.130390, -60.023165)).title("Amigo Coração"));
		map.addMarker(new MarkerOptions().position(
				new LatLng(-3.082845, -60.009904)).title("Procurando Nemo"));
		map.addMarker(new MarkerOptions().position(
				new LatLng(-3.067638, -60.095109)).title("Pedala Galera"));
	}

	private void addSelfMarker() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double userLongitude = location.getLongitude();
		double userLatitude = location.getLatitude();

		userLocation = new LatLng(userLatitude, userLongitude);

		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));

		map.addMarker(new MarkerOptions()
				.title(strings.getString("userFirstName", "User"))
				.snippet("Usuário").position(userLocation));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_events:
			callEventActivity();
			return true;
		case R.id.action_friends:
			callFriendsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void callEventActivity() {
		Intent intent = new Intent(this, EventActivity.class);
		startActivity(intent);
	}

	private void callFriendsActivity() {
		Intent intent = new Intent(this, FriendsActivity.class);
		startActivity(intent);
	}

	public void callEventFilter(View view) {
		EventFilterDialog filterDialog = new EventFilterDialog();
		filterDialog.show(getFragmentManager(), "event_filter");
	}

	public void callUserFilter(View view) {
		UserFilterDialog filterDialog = new UserFilterDialog();
		filterDialog.show(getFragmentManager(), "event_filter");
	}
}
