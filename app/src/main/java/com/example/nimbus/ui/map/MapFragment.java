package com.example.nimbus.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.nimbus.R;

import static com.example.nimbus.MainActivity.longitude;
import static com.example.nimbus.MainActivity.latitude;
import static com.example.nimbus.MainActivity.city;

/**
 * Fragment to display a marker on a map.
 * Code based on the Android Developer Guide:
 *      https://developers.google.com/maps/documentation/android-sdk/start
 *
 * @author/driver Kayla Tran
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;
    private UiSettings mUiSettings;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mUiSettings = mGoogleMap.getUiSettings();

        LatLng pin = new LatLng(latitude, longitude);
        mGoogleMap.addMarker(new MarkerOptions().position(pin).title(city));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pin, 15));
        mUiSettings.setZoomControlsEnabled(true);

    }
}