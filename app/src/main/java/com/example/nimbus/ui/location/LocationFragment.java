package com.example.nimbus.ui.location;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nimbus.Constants;
import com.example.nimbus.GeocodeIntentService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nimbus.R;


import static com.example.nimbus.MainActivity.longitude;
import static com.example.nimbus.MainActivity.latitude;
import static com.example.nimbus.MainActivity.city;

/**
 * Fragment to display a location (coordinates) of an address input.
 *
 * @author/driver Zahra Atzuri
 */
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

    /**
     * Starts the GeocodeIntentService and sends the address.
     */
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

    /**
     * Receiver to accept address from GeocodeIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        /* Constructor for Address Receiver */
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives an address from a bundle of data results.
         * Then sets appropriate TextView based on validity of data.
         * @param resultCode Success or failed code as an int.
         * @param resultData Data containing address as a bundle.
         */
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