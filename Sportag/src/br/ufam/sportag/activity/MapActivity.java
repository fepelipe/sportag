package br.ufam.sportag.activity;

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

		strings = getSharedPreferences("strings", MODE_PRIVATE);
		addSelfMarker();
	}

	private void addSelfMarker() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double userLongitude = location.getLongitude();
		double userLatitude = location.getLatitude();

		userLocation = new LatLng(userLatitude, userLongitude);

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map_fragment)).getMap();
		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,
				14));

		map.addMarker(new MarkerOptions()
				.title(strings.getString("userFirstName", "User"))
				.snippet("Você está aqui").position(userLocation));
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				Intent intent = new Intent(getApplicationContext(),
						ProfileActivity.class);
				startActivity(intent);
			}
		});
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
