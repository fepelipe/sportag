package br.ufam.sportag;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		addSelfMarker();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	private void addSelfMarker(){
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double userLongitude = location.getLongitude();
		double userLatitude = location.getLatitude();
		
		GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_fragment)).getMap();

        LatLng userLocation = new LatLng(userLatitude, userLongitude);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13));

        map.addMarker(new MarkerOptions()
                .title("Felipe")
                .snippet("Você está aqui")
                .position(userLocation));
	}
}
