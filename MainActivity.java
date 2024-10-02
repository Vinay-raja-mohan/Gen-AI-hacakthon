package com.example.sxcur;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sXcur";
    private static final int REQUEST_ENABLE_BT = 1;
    private LocationManager locationManager;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize location and Bluetooth services
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Start DBA and PBA features
        startDynamicBehaviouralAuthentication();
        startProximityBasedAuthentication();
    }

    // Dynamic Behavioural Authentication (DBA)
    private void startDynamicBehaviouralAuthentication() {
        Log.d(TAG, "Starting DBA...");

        // Simulating behavioural detection by monitoring typing patterns (as an example)
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Example logic: Detect abnormal typing speed
            float typingSpeed = detectTypingSpeed();
            if (typingSpeed > 50.0f) {
                // Trigger security prompt
                Log.d(TAG, "Abnormal typing speed detected. Prompting for extra security...");
                promptSecurityQuestion();
            }
        }, 5000);  // Check every 5 seconds
    }

    private float detectTypingSpeed() {
        // Placeholder function for detecting typing speed
        // In a real scenario, this would monitor user interaction with the app
        return 55.0f;  // Example abnormal speed
    }

    private void promptSecurityQuestion() {
        // Logic to prompt user for extra verification (e.g., security question)
        Toast.makeText(this, "Please answer the security question", Toast.LENGTH_SHORT).show();
    }

    // Proximity Based Authentication (PBA)
    private void startProximityBasedAuthentication() {
        Log.d(TAG, "Starting PBA...");

        // Check if Bluetooth is enabled
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            requestBluetoothEnable();
        }

        // Check if location is available (for GPS proximity)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            checkLocationProximity();
        }
    }

    private void requestBluetoothEnable() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            // Prompt user to enable Bluetooth
            Log.d(TAG, "Requesting Bluetooth to be enabled...");
            // Add code to prompt user to enable Bluetooth
        }
    }

    private void checkLocationProximity() {
        // Placeholder logic for checking if user is in a secure location using GPS
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.d(TAG, "User location: " + location.getLatitude() + ", " + location.getLongitude());

            // Example: Allow access only if the user is within 100 meters of a specific location
            double safeLat = 37.4219983;
            double safeLong = -122.084;
            float[] results = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), safeLat, safeLong, results);
            if (results[0] < 100) {
                Log.d(TAG, "User is within a secure proximity.");
            } else {
                Log.d(TAG, "User is outside the secure proximity.");
                Toast.makeText(this, "Access Denied: You are not in the secure area.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
