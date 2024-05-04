package com.example.restaurantmanager.Client;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantmanager.R;
import com.squareup.picasso.Picasso;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Restaurant;


public class MapFragment extends Fragment {
    private LocationManager locationManager; // Quản lý vị trí
    private LocationListener locationListener; // Lắng nghe thay đổi vị trí
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1; // Mã yêu cầu quyền
    private MapView map = null; // Bản đồ
    private GeoPoint restaurantLocation; // Vị trí nhà hàng
    IMapController mapController; // Điều khiển bản đồ
    private Location currentLocation; // Vị trí hiện tại
    Marker currentLocationMarker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        init(view);
        addEvents(view);
        return view;
    }
    void init(View view) {
        // Khởi tạo các thành phần giao diện
        Button buttonLocation = view.findViewById(R.id.button_location);
        Button buttonDirections = view.findViewById(R.id.button_directions);

        // Cấu hình bản đồ
        Configuration.getInstance().load(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(17.5);
        currentLocationMarker = new Marker(map);

        // Đặt điểm bắt đầu cho bản đồ
//        GeoPoint startPoint = new GeoPoint(21.028511, 105.804817);
//        mapController.setCenter(startPoint);

        // Thêm hiển thị la bàn vào bản đồ
        CompassOverlay compassOverlay = new CompassOverlay(getActivity(), map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        // Yêu cầu quyền nếu cần thiết
        requestPermissionsIfNecessary(new String[]{
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        // Cấu hình cho việc hiển thị đầy đủ màn hình
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo quản lý vị trí và lắng nghe thay đổi vị trí
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location; // Cập nhật vị trí hiện tại
                // Thiết lập vị trí cho Marker này
                currentLocationMarker.setPosition(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));

                // Thiết lập tiêu đề cho Marker này
                currentLocationMarker.setTitle("Me Location");

//                        // Thiết lập icon cho Marker này
                Drawable currentLocationIcon = getResources().getDrawable(android.R.drawable.radiobutton_on_background);
                currentLocationMarker.setIcon(currentLocationIcon);

                // Thêm Marker này vào bản đồ
                map.getOverlays().add(currentLocationMarker);
            }

            // Override other methods as needed
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };


        // Kiểm tra và yêu cầu quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }

        // Khởi tạo danh sách các vị trí và thêm marker vào bản đồ

        for (Restaurant restaurant : HomeClientFragment.restaurantList) {
            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(restaurant.getLatitude(), restaurant.getLongitude()));
            marker.setTitle(restaurant.getUsername());
//            Drawable currentLocationIcon = getResources().getDrawable(R.drawable.account);
//            marker.setIcon(currentLocationIcon);
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    showDialogRestaurant(restaurant.getUsername(), restaurant.getIdTableMax(), restaurant.getIdMax(), restaurant.getDescription(),
                            restaurant.getAddress(), restaurant.getImage(), restaurant.getPhone(), restaurant.getLatitude(), restaurant.getLongitude());
                    return true;
                }
            });
            map.getOverlays().add(marker);
        }

        // Đặt sự kiện cho các nút
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "get", Toast.LENGTH_SHORT).show();


                Log.d("LocationUpdates", "Location changed: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
                Toast.makeText(getActivity(), "Location: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                if (currentLocation != null) {
                    mapController.setZoom(17.5);
                    GeoPoint startPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mapController.setCenter(startPoint); // Cập nhật vị trí trung tâm của bản đồ

                }

            }
        });


        if (currentLocation != null) {
            GeoPoint startPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            mapController.setCenter(startPoint); // Cập nhật vị trí trung tâm của bản đồ
        }
    }
    void addEvents(View view) {
    }
    private void showDialogRestaurant(String userName_, int idTableMax_, int idMenuMax_, String description_, String address_, String image_, String phone_, double lat_, double lng_){
        // Tạo một Dialog
        Dialog dialog = new Dialog(getActivity());
        // Yêu cầu không hiển thị tiêu đề cho Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt layout cho Dialog từ file XML
        dialog.setContentView(R.layout.dialog_center_show_restaurant);

        // Tìm các thành phần trong layout của Dialog
        CircleImageView logo = dialog.findViewById(R.id.logo);
        TextView idMax = dialog.findViewById(R.id.idMax);
        TextView idTableMax = dialog.findViewById(R.id.idTableMax);
        TextView address = dialog.findViewById(R.id.address);
        TextView phone = dialog.findViewById(R.id.phone);
        TextView description = dialog.findViewById(R.id.description);
        TextView username = dialog.findViewById(R.id.username);
        Button btnDirection = dialog.findViewById(R.id.btnDirection);
        Button btnCall = dialog.findViewById(R.id.btnCall);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        //set data
        if (image_!=null&&image_.length()>0){
            Picasso.get().load(image_).into(logo);
        }else {
            logo.setImageResource(R.drawable.account);
        }
        //set data
        // chuyển idMax_ và idTableMax_ sang String
        idMax.setText(String.valueOf(idMenuMax_));
        idTableMax.setText(String.valueOf(idTableMax_));
        address.setText(address_);
        phone.setText(phone_);
        description.setText(description_);
        username.setText(userName_);
        //button
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mở google map
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat_ + "," + lng_+ "&mode=d");//xe máy
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                getActivity().startActivity(mapIntent);

            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mở cuộc gọi điện
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(android.net.Uri.parse("tel:"+phone_));
                getActivity().startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        // Hiển thị Dialog
        dialog.show();
        // Đặt kích thước cho Dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // Đặt màu nền cho Dialog là trong suốt
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Đặt hiệu ứng cho Dialog
        dialog.getWindow().getAttributes().windowAnimations = R.anim.slide_in_notification;
        // Đặt vị trí cho Dialog ở phía dưới
        dialog.getWindow().setGravity(Gravity.TOP);
    }
//    void openGoogleMap() {
////        // Mở ứng dụng Google Maps với vị trí nhà hàng
////        String uri = "http://maps.google.com/maps?saddr=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&daddr=" + restaurantLocation.getLatitude() + "," + restaurantLocation.getLongitude();
////        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
////        intent.setPackage("com.google.android.apps.maps");
////        startActivity(intent);
//        if (restaurantLocation != null) {
//            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + restaurantLocation.getLatitude() + "," + restaurantLocation.getLongitude() + "&mode=d");//xe máy
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//            mapIntent.setPackage("com.google.android.apps.maps");
//            startActivity(mapIntent);
//        } else {
//            Toast.makeText(getActivity(), "No location selected yet.", Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * Hàm tìm kiếm vị trí
     * @param encoded vị trí cần tìm
     *
     * @return List<GeoPoint> danh sách các vị trí
     */
    private List<GeoPoint> decodePolyline(String encoded) {
        List<GeoPoint> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        // Duyệt qua từng ký tự trong chuỗi đã mã hóa
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                // Chuyển đổi ký tự thành số nguyên và thêm vào kết quả
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            // Tính toán độ vĩ độ
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                // Tương tự như trên, tính toán độ kinh độ
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            // Tạo một điểm GeoPoint từ độ vĩ và độ kinh độ đã tính toán
            GeoPoint p = new GeoPoint(((double) lat / 1E5), ((double) lng / 1E5));
            poly.add(p); // Thêm điểm vào danh sách
        }

        return poly; // Trả về danh sách các điểm
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật cấu hình osmdroid khi tiếp tục
        map.onResume(); // Cần thiết cho la bàn, các lớp vị trí của tôi, v6.0.0 trở lên
    }

    @Override
    public void onPause() {
        super.onPause();
        // Lưu cấu hình osmdroid khi tạm dừng
        map.onPause();  // Cần thiết cho la bàn, các lớp vị trí của tôi, v6.0.0 trở lên
    }

    /**
     * Hàm xử lý kết quả yêu cầu quyền
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Gọi phương thức super ở đây
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            // Nếu quyền bị từ chối, thêm vào danh sách yêu cầu quyền
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionsToRequest.add(permissions[i]);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            // Nếu ứng dụng cần quyền vị trí và lưu trữ để hoạt động đúng cách
//            Toast.makeText(getActivity(), "This app needs location and storage permissions to work properly.", Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), "This app needs location and storage permissions to work properly.", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Hàm yêu cầu quyền nếu cần thiết
     * @param permissions danh sách quyền cần yêu cầu
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            // Nếu quyền chưa được cấp, thêm vào danh sách yêu cầu quyền
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            // Nếu quyền vị trí chưa được cấp, hiển thị thông báo cho người dùng
            if (permissionsToRequest.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Toast.makeText(getActivity(), "This app needs the location permission to show your current location.", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "This app needs the location permission to show your current location.", Toast.LENGTH_LONG).show();

            }
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}