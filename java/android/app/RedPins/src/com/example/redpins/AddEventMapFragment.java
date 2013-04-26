package com.example.redpins;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddEventMapFragment extends Fragment implements OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener, OnInfoWindowClickListener {
	
	private GoogleMap mMap;
	private View mapView;
	private SupportMapFragment mapFrag;
	private Marker currentMarker;
	public MapPicker parent;
	private Geocoder geocoder;
	private String title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		setUpMapIfNeeded();
		super.onResume();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_event_map_fragment, container, false);
		geocoder = new Geocoder(getActivity());
		setUpMapIfNeeded();
		title = getArguments().getString("title");
		if (title.length() == 0) {
			title = "New event";
		}
		return view;
	}
	
	private void setUpMapIfNeeded() {
        if (mMap == null) {
        	mapFrag = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.add_event_map));
            mMap = mapFrag.getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }
	
	private void setUpMap() {
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraChangeListener(this);
        //TODO: Set the latlng to be the current location
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(37.87557959345215, -122.25866295397283) , 14.0f) );
    }
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		
		System.out.println("DESTROY VIEW");
		SupportMapFragment f = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.add_event_map);
		if (f != null) {
			getFragmentManager().beginTransaction().remove(f).commit();
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
		System.out.println("ON DESTROY");
		getActivity().getSupportFragmentManager().beginTransaction().hide(this).remove(this).commit();
		
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		System.out.println("long pressed, point=" + point);
		if (currentMarker != null) {
			currentMarker.remove();
		}
		
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				Log.i("onMapLongClick", address.toString());
				int count = 0;
				String location = "";
				while (address.getAddressLine(count) != null) {
					location = location + "\n" + address.getAddressLine(count);
					count++;
				}
				Log.i("onMapLongClick", location);
				parent.setAddress(location);
				parent.setLatitudeLongitude(point.latitude, point.longitude);
				currentMarker = mMap.addMarker(new MarkerOptions().position(point).title(title).snippet(location));
			} else {
				((MainActivity) getActivity()).makeToast("Invalid Location Chosen", Toast.LENGTH_LONG);
			}
		} catch (IOException e) {
			((MainActivity) getActivity()).makeToast("Invalid Location Chosen", Toast.LENGTH_LONG);
		}
		
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		System.out.println("pressed, point=" + point);
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
}