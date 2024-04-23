package com.example.restaurantmanager;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    // 1. Get the current location of the user
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private GeoPoint searchedLocation;
    IMapController mapController;
    private List<MyLocation> locations;
    private Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(9.5);
        //lấy vị trí hiện tại của người dùng


        GeoPoint startPoint = new GeoPoint(21.028511, 105.804817);
        mapController.setCenter(startPoint);
        //add a compass overlay
        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        System.out.println("MapActivity.onCreate");
        requestPermissionsIfNecessary(new String[]{
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });




//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location; // Update currentLocation here
                GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                mapController.setCenter(startPoint);
            }

            // Override other methods as needed
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////
        locations = new ArrayList<>();
        locations.add(new MyLocation(new GeoPoint(21.028511, 105.804817), "Hanoi"));
        locations.add(new MyLocation(new GeoPoint(21.028511, 105.994817), "Hanoiiiiiiiii"));
        // Add markers to the map
        for (MyLocation location : locations) {
            Marker marker = new Marker(map);
            marker.setPosition(location.getGeoPoint());
            marker.setTitle(location.getName());
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                // Calculate the distance
                float[] results = new float[1];
                Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), marker1.getPosition().getLatitude(), marker1.getPosition().getLongitude(), results);
                float distanceInMeters = results[0];
                Toast.makeText(MapActivity.this, "Distance: " + distanceInMeters + " meters", Toast.LENGTH_SHORT).show();

                // Add a button to navigate
                Button navigateButton = new Button(MapActivity.this);
                navigateButton.setText("Navigate");
                navigateButton.setOnClickListener(v -> {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + marker1.getPosition().getLatitude() + "," + marker1.getPosition().getLongitude());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                });

                return true;
            });
            map.getOverlays().add(marker);
        }
        Button buttonLocation = findViewById(R.id.button_location);
        EditText editTextLocation = findViewById(R.id.edittext_location);
        Button buttonSearch = findViewById(R.id.button_search);
        Button buttonDirections = findViewById(R.id.button_directions);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                locationListener = new LocationListener() {
//                    @Override
//                    public void onLocationChanged(Location location) {
//                        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//                        mapController.setCenter(startPoint);
//                    }
//
//                    // Override other methods as needed
//                    @Override
//                    public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//                    @Override
//                    public void onProviderEnabled(String provider) {}
//
//                    @Override
//                    public void onProviderDisabled(String provider) {}
//                };
                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                } else {
                    // Request location permission if not granted
                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                }
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editTextLocation.getText().toString();
//                searchLocation(location);
            }
        });

        buttonDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedLocation != null) {
//                    getDirections(searchedLocation);
                } else {
                    Toast.makeText(MapActivity.this, "No location searched yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchLocation(String location) {
        // Implement your method to search for the location
        // For example, you can use a geocoding API to get the latitude and longitude of the location
        // Then, you can create a GeoPoint with these coordinates and set it as the center of the map
        // Don't forget to save the GeoPoint in the searchedLocation variable
        new Thread(() -> {
            try {
                String urlString = "https://nominatim.openstreetmap.org/search?format=json&q=" + URLEncoder.encode(location, "UTF-8");
                URL url = new URL(urlString);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    double lat = jsonObject.getDouble("lat");
                    double lon = jsonObject.getDouble("lon");

                    searchedLocation = new GeoPoint(lat, lon);
                    runOnUiThread(() -> mapController.setCenter(searchedLocation));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void getDirections(GeoPoint destination) {
        // Implement your method to get directions to the destination
        // You can use a routing API to get a route from the current location to the destination
        // Then, you can display this route on the map
        // Create a new thread to perform the network request
    new Thread(() -> {
        try {
            if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                runOnUiThread(() -> Toast.makeText(MapActivity.this, "Location permission is required", Toast.LENGTH_SHORT).show());
                return;
            }

            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation == null) {
                runOnUiThread(() -> Toast.makeText(MapActivity.this, "Current location not available", Toast.LENGTH_SHORT).show());
                return;
            }

            String urlString = "http://router.project-osrm.org/route/v1/driving/" + currentLocation.getLongitude() + "," + currentLocation.getLatitude() + ";" + destination.getLongitude() + "," + destination.getLatitude() + "?overview=full";
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray routes = jsonObject.getJSONArray("routes");
            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                String geometry = route.getString("geometry");

                // Decode the polyline and create a list of GeoPoints
                List<GeoPoint> geoPoints = decodePolyline(geometry);
                Polyline polyline = new Polyline();
                polyline.setPoints(geoPoints);

                runOnUiThread(() -> map.getOverlayManager().add(polyline));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }).start();
}

private List<GeoPoint> decodePolyline(String encoded) {
    List<GeoPoint> poly = new ArrayList<>();
    int index = 0, len = encoded.length();
    int lat = 0, lng = 0;

    while (index < len) {
        int b, shift = 0, result = 0;
        do {
            b = encoded.charAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);
        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        lat += dlat;

        shift = 0;
        result = 0;
        do {
            b = encoded.charAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);
        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        lng += dlng;

        GeoPoint p = new GeoPoint(((double) lat / 1E5), ((double) lng / 1E5));
        poly.add(p);
    }

    return poly;
}
    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }


    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call super method here
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionsToRequest.add(permissions[i]);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            Toast.makeText(this, "This app needs location and storage permissions to work properly.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            // If the location permission is not granted, show a message to the user
            if (permissionsToRequest.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "This app needs the location permission to show your current location.", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
class MyLocation {
    private GeoPoint location;
    private String name;

    public MyLocation(GeoPoint location, String name) {
        this.location = location;
        this.name = name;
    }

    public GeoPoint getGeoPoint() {
        return location;
    }

    public String getName() {
        return name;
    }
}