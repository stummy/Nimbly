package com.example.nimbly.ui.location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nimbly.Constants;
import com.example.nimbly.GeocodeIntentService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nimbly.R;


import static com.example.nimbly.MainActivity.longitude;
import static com.example.nimbly.MainActivity.latitude;
import static com.example.nimbly.MainActivity.city;

public class LocationFragment extends Fragment {
    private AddressResultReceiver mResultReceiver;

    private MaterialButton showCoordinatesBtn;
    private TextInputEditText addressEdit;
    private TextView coordinatesText;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mResultReceiver = new AddressResultReceiver(null);
        View root = inflater.inflate(R.layout.fragment_location, container, false);

        coordinatesText = root.findViewById(R.id.coordinates_text);
        showCoordinatesBtn = root.findViewById(R.id.show_coordinates_btn);
        addressEdit = root.findViewById(R.id.address_edit);

        showCoordinatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntentService();
            }
        });
        return root;
    }

    protected void startIntentService() {
        Intent intent = new Intent(getActivity(), GeocodeIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        if (addressEdit.getText().length() == 0) {
            Toast.makeText(getActivity(), "Please enter a valid address.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, addressEdit.getText().toString());
        getActivity().startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                Toast.makeText(getActivity(), "Error retrieving data",
                        Toast.LENGTH_LONG).show();;
            }

            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                latitude = address.getLatitude();
                longitude = address.getLongitude();
                city = address.getLocality();

                final String info = "Latitude: " + latitude + "\n" +
                        "Longitude: " + longitude + "\n";

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        coordinatesText.setText(info);
                    }
                });
            }
            else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        coordinatesText.setText(getString(R.string.text_invalid_address));
                    }
                });
            }

        }
    }
}