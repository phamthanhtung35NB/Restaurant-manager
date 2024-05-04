//package com.example.restaurantmanager;
//
//import static java.security.AccessController.getContext;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import android.app.ProgressDialog;
//import android.graphics.Rect;
//import android.location.GpsStatus;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import model.MyLocation;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.osmdroid.api.IMapController;
//import org.osmdroid.config.Configuration;
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;
//import org.osmdroid.views.overlay.Marker;
//import org.osmdroid.views.overlay.Polyline;
//import org.osmdroid.views.overlay.compass.CompassOverlay;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.net.URLEncoder;
//import java.util.List;
//
//public class MapActivity extends AppCompatActivity {
//    // Khai báo các biến cần thiết
//    private LocationManager locationManager; // Quản lý vị trí
//    private LocationListener locationListener; // Lắng nghe thay đổi vị trí
//    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1; // Mã yêu cầu quyền
//    private MapView map = null; // Bản đồ
//    private GeoPoint restaurantLocation; // Vị trí nhà hàng
//    IMapController mapController; // Điều khiển bản đồ
//    private List<MyLocation> locations; // Danh sách các vị trí
//    private Location currentLocation; // Vị trí hiện tại
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_map);
//        Context ctx = getApplicationContext();
//
//        // Khởi tạo các thành phần giao diện
//        Button buttonLocation = findViewById(R.id.button_location);
//        EditText editTextLocation = findViewById(R.id.edittext_location);
//        Button buttonSearch = findViewById(R.id.button_search);
//        Button buttonDirections = findViewById(R.id.button_directions);
//
//        // Cấu hình bản đồ
//        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
//        map = (MapView) findViewById(R.id.map);
//        map.setTileSource(TileSourceFactory.MAPNIK);
//        map.setBuiltInZoomControls(true);
//        map.setMultiTouchControls(true);
//        mapController = map.getController();
//        mapController.setZoom(9.5);
//
//        // Đặt điểm bắt đầu cho bản đồ
//        GeoPoint startPoint = new GeoPoint(21.028511, 105.804817);
//        mapController.setCenter(startPoint);
//
//        // Thêm hiển thị la bàn vào bản đồ
//        CompassOverlay compassOverlay = new CompassOverlay(this, map);
//        compassOverlay.enableCompass();
//        map.getOverlays().add(compassOverlay);
//
//        // Yêu cầu quyền nếu cần thiết
//        requestPermissionsIfNecessary(new String[]{
//                // if you need to show the current location, uncomment the line below
//                // Manifest.permission.ACCESS_FINE_LOCATION,
//                // WRITE_EXTERNAL_STORAGE is required in order to show the map
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        });
//
//        // Cấu hình cho việc hiển thị đầy đủ màn hình
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // Khởi tạo quản lý vị trí và lắng nghe thay đổi vị trí
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                currentLocation = location; // Cập nhật vị trí hiện tại
//                GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//                mapController.setCenter(startPoint); // Cập nhật vị trí trung tâm của bản đồ
//            }
//
//            // Override other methods as needed
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//            @Override
//            public void onProviderEnabled(String provider) {}
//
//            @Override
//            public void onProviderDisabled(String provider) {}
//        };
//        // Kiểm tra và yêu cầu quyền truy cập vị trí
//        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        } else {
//            // Request location permission if not granted
//            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//
//        // Khởi tạo danh sách các vị trí và thêm marker vào bản đồ
//        locations = new ArrayList<>();
//        locations.add(new MyLocation(new GeoPoint(21.028511, 105.804817), "Hanoi"));
//        locations.add(new MyLocation(new GeoPoint(21.0382323, 105.7826399), "uet"));
//        for (MyLocation location : locations) {
//            Marker marker = new Marker(map);
//            marker.setPosition(location.getGeoPoint());
//            marker.setTitle(location.getName());
//            marker.setOnMarkerClickListener((marker1, mapView) -> {
//                restaurantLocation = marker1.getPosition(); // Cập nhật vị trí nhà hàng
//                Toast.makeText(MapActivity.this, location.getName(), Toast.LENGTH_SHORT).show(); // Hiển thị tên vị trí
//                return true;
//            });
//            map.getOverlays().add(marker); // Thêm marker vào bản đồ
//        }
//
//        // Đặt sự kiện cho các nút
//        buttonLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                locationListener = new LocationListener() {
//                    @Override
//                    public void onLocationChanged(Location location) {
//                        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//                        mapController.setCenter(startPoint); // Cập nhật vị trí trung tâm của bản đồ
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
//                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//                } else {
//                    // Request location permission if not granted
//                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
//                }
//            }
//        });
//        buttonSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String location = editTextLocation.getText().toString();
////                searchLocation(location);
//            }
//        });
//
//        // Đặt sự kiện cho nút chỉ đường
//        buttonDirections.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (restaurantLocation != null) {
//                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + restaurantLocation.getLatitude() + "," + restaurantLocation.getLongitude() + "&mode=d");//xe máy
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    startActivity(mapIntent);
//                } else {
//                    Toast.makeText(MapActivity.this, "No location selected yet.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    /**
//     * Hàm tìm kiếm vị trí
//     * @param encoded vị trí cần tìm
//     *
//     * @return List<GeoPoint> danh sách các vị trí
//     */
//    private List<GeoPoint> decodePolyline(String encoded) {
//        List<GeoPoint> poly = new ArrayList<>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        // Duyệt qua từng ký tự trong chuỗi đã mã hóa
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                // Chuyển đổi ký tự thành số nguyên và thêm vào kết quả
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            // Tính toán độ vĩ độ
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                // Tương tự như trên, tính toán độ kinh độ
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            // Tạo một điểm GeoPoint từ độ vĩ và độ kinh độ đã tính toán
//            GeoPoint p = new GeoPoint(((double) lat / 1E5), ((double) lng / 1E5));
//            poly.add(p); // Thêm điểm vào danh sách
//        }
//
//        return poly; // Trả về danh sách các điểm
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Cập nhật cấu hình osmdroid khi tiếp tục
//        map.onResume(); // Cần thiết cho la bàn, các lớp vị trí của tôi, v6.0.0 trở lên
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        // Lưu cấu hình osmdroid khi tạm dừng
//        map.onPause();  // Cần thiết cho la bàn, các lớp vị trí của tôi, v6.0.0 trở lên
//    }
//
//    /**
//     * Hàm xử lý kết quả yêu cầu quyền
//     * @param permissions The requested permissions. Never null.
//     * @param grantResults The grant results for the corresponding permissions
//     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
//     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
//     *
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Gọi phương thức super ở đây
//        ArrayList<String> permissionsToRequest = new ArrayList<>();
//        for (int i = 0; i < grantResults.length; i++) {
//            // Nếu quyền bị từ chối, thêm vào danh sách yêu cầu quyền
//            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                permissionsToRequest.add(permissions[i]);
//            }
//        }
//        if (!permissionsToRequest.isEmpty()) {
//            // Nếu ứng dụng cần quyền vị trí và lưu trữ để hoạt động đúng cách
//            Toast.makeText(this, "This app needs location and storage permissions to work properly.", Toast.LENGTH_LONG).show();
//            ActivityCompat.requestPermissions(
//                    this,
//                    permissionsToRequest.toArray(new String[0]),
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }
//
//    /**
//     * Hàm yêu cầu quyền nếu cần thiết
//     * @param permissions danh sách quyền cần yêu cầu
//     */
//    private void requestPermissionsIfNecessary(String[] permissions) {
//        ArrayList<String> permissionsToRequest = new ArrayList<>();
//        for (String permission : permissions) {
//            // Nếu quyền chưa được cấp, thêm vào danh sách yêu cầu quyền
//            if (ContextCompat.checkSelfPermission(this, permission)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsToRequest.add(permission);
//            }
//        }
//        if (permissionsToRequest.size() > 0) {
//            // Nếu quyền vị trí chưa được cấp, hiển thị thông báo cho người dùng
//            if (permissionsToRequest.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Toast.makeText(this, "This app needs the location permission to show your current location.", Toast.LENGTH_LONG).show();
//            }
//            ActivityCompat.requestPermissions(
//                    this,
//                    permissionsToRequest.toArray(new String[0]),
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }
//}
