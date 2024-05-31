package com.example.capstone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class googlemap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;

    private EditText searchEditText;
    private Button searchButton;
    private Button backButton;
    private List<LatLng> markerLocations = new ArrayList<>();
    private Map<String, Float> zoomLevels = new HashMap<>();
    private Map<String, LatLng> predefinedLocations = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (mMap != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        if (currentLocationMarker != null) {
                            currentLocationMarker.setPosition(currentLatLng);
                        } else {
                            BitmapDescriptor blueDot = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                            currentLocationMarker = mMap.addMarker(new MarkerOptions()
                                    .position(currentLatLng)
                                    .icon(blueDot)
                                    .title("Current Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                        }
                    }
                }
            }
        };

        // 미리 찍어둔 좌표와 검색 위치별 줌 레벨 설정
        markerLocations.add(new LatLng(37.541662, 127.058166)); // (popup_info - A Cloud Traveler : 구름 위를 걷는 기분)
        markerLocations.add(new LatLng(37.555116, 126.922558)); // (popup_info1 - It's Your Day: 이번 광고, 생일 카페 주인공은 바로 너!)
        markerLocations.add(new LatLng(37.556670, 126.936601)); // (popup_info2 - 담곰이 카페)
        markerLocations.add(new LatLng(37.556431, 126.923832)); // (popup_info3 - 담곰이 팝업스토어 <봄날의 담곰이>)
        markerLocations.add(new LatLng(37.543378, 127.051716)); // (popup_info4 - 로에베 퍼퓸 팝업스토어)
        markerLocations.add(new LatLng(37.525888, 126.928449)); // (popup_info5 - IKEA 팝업스토어 더현대 서울)
        markerLocations.add(new LatLng(37.541241, 127.058824)); // (popup_info6 - 엄브로 100주년 <MR.UM's CLEANERS>)
        markerLocations.add(new LatLng(37.512459, 127.102545)); // (popup_info7 - 블레스문 팝업스토어)
        markerLocations.add(new LatLng(37.579405, 126.978686)); // (popup_info8 - 요물, 우리를 홀린 고양이)
        markerLocations.add(new LatLng(37.556966, 126.978106)); // (popup_info9 - 달리기 : 새는 날고 물고기는 헤엄치고 인간은 달린다)


        // 줌 레벨 추가
        zoomLevels.put("구름 위를 걷는 기분", 15f); // (popup_info - A Cloud Traveler : 구름 위를 걷는 기분)
        zoomLevels.put("생일카페", 15f); // (popup_info1 - It's Your Day: 이번 광고, 생일 카페 주인공은 바로 너!)
        zoomLevels.put("담곰이 카페", 15f); // (popup_info2 - 담곰이 카페)
        zoomLevels.put("담곰이 팝업스토어", 15f); // (popup_info3 - 담곰이 팝업스토어 <봄날의 담곰이>)
        zoomLevels.put("로에베 퍼퓸", 15f); // (popup_info4 - 로에베 퍼퓸 팝업스토어)
        zoomLevels.put("이케아", 15f); // (popup_info5 - IKEA 팝업스토어 더현대 서울)
        zoomLevels.put("엄브로", 15f); // (popup_info6 - 엄브로 100주년 <MR.UM's CLEANERS>)
        zoomLevels.put("블레스문", 15f); // (popup_info7 - 블레스문 팝업스토어)
        zoomLevels.put("요물", 15f); // (popup_info8 - 요물, 우리를 홀린 고양이)
        zoomLevels.put("달리기", 15f); // (popup_info9 - 달리기 : 새는 날고 물고기는 헤엄치고 인간은 달린다)


        // 검색창 검색할때 검색어 지정 #############################################################################################################
        predefinedLocations.put("구름 위를 걷는 기분", new LatLng(37.541662, 127.058166)); // (popup_info - A Cloud Traveler : 구름 위를 걷는 기분)
        predefinedLocations.put("생일카페", new LatLng(37.555116, 126.922558)); // (popup_info1 - It's Your Day: 이번 광고, 생일 카페 주인공은 바로 너!)
        predefinedLocations.put("담곰이 카페", new LatLng(37.556670, 126.936601)); // (popup_info2 - 담곰이 카페)
        predefinedLocations.put("담곰이 팝업스토어", new LatLng(37.556431, 126.923832)); // (popup_info3 - 담곰이 팝업스토어 <봄날의 담곰이>)
        predefinedLocations.put("로에베 퍼퓸", new LatLng(37.543378, 127.051716)); // (popup_info4 - 로에베 퍼퓸 팝업스토어)
        predefinedLocations.put("이케아", new LatLng(37.525888, 126.928449)); // (popup_info5 - IKEA 팝업스토어 더현대 서울)
        predefinedLocations.put("엄브로", new LatLng(37.541241, 127.058824)); // (popup_info6 - 엄브로 100주년 <MR.UM's CLEANERS>)
        predefinedLocations.put("블레스문", new LatLng(37.512459, 127.102545)); // (popup_info7 - 블레스문 팝업스토어)
        predefinedLocations.put("요물", new LatLng(37.579405, 126.978686)); // (popup_info8 - 요물, 우리를 홀린 고양이)
        predefinedLocations.put("달리기", new LatLng(37.556966, 126.978106)); // (popup_info9 - 달리기 : 새는 날고 물고기는 헤엄치고 인간은 달린다)


        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = searchEditText.getText().toString();
                searchLocation(location);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getCurrentLocation();

        for (int i = 0; i < markerLocations.size(); i++) {
            LatLng location = markerLocations.get(i);
            String title = getTitleFromPosition(i);
            mMap.addMarker(new MarkerOptions().position(location).title(title));
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String markerTitle = marker.getTitle();
                Intent intent = new Intent(googlemap.this, info.class);

                if ("A Cloud Traveler : 구름 위를 걷는 기분".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info");
                    intent.putExtra("imageFileName", "store1.png");
                } else if ("It's Your Day: 이번 광고, 생일 카페 주인공은 바로 너!".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info1");
                    intent.putExtra("imageFileName", "store2.png");
                } else if ("담곰이 카페".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info2");
                    intent.putExtra("imageFileName", "store3.png");
                } else if ("담곰이 팝업스토어 <봄날의 담곰이>".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info3");
                    intent.putExtra("imageFileName", "store4.png");
                } else if ("로에베 퍼퓸 팝업스토어".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info4");
                    intent.putExtra("imageFileName", "store5.png");
                } else if ("IKEA 팝업스토어 더현대 서울".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info5");
                    intent.putExtra("imageFileName", "store6.png");
                }else if ("엄브로 100주년 <MR.UM's CLEANERS>".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info6");
                    intent.putExtra("imageFileName", "store7.png");
                }else if ("블레스문 팝업스토어".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info7");
                    intent.putExtra("imageFileName", "store8.png");
                }else if ("요물, 우리를 홀린 고양이".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info8");
                    intent.putExtra("imageFileName", "store9.png");
                }else if ("달리기 : 새는 날고 물고기는 헤엄치고 인간은 달린다".equals(markerTitle)) {
                    intent.putExtra("tableName", "popup_info9");
                    intent.putExtra("imageFileName", "store10.png");
                }
                // 필요한 만큼 else if 블록을 추가하여 다른 마커 제목에 맞는 테이블 이름을 설정할 수 있음

                startActivity(intent);

            }
        });
    }

    private void searchLocation(String location) {
        if (predefinedLocations.containsKey(location)) {
            LatLng predefinedLocation = predefinedLocations.get(location);
            Float zoomLevel = zoomLevels.get(location);
            if (zoomLevel == null) {
                zoomLevel = 10f;
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(predefinedLocation, zoomLevel));
        } else {
            LatLng searchedLocation = getLocationFromAddress(location);
            if (searchedLocation != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 10f));
            }
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    // 마커 위치에 해당하는 이름 가져오기
    private String getTitleFromPosition(int position) {
        switch (position) {
            case 0:
                return "A Cloud Traveler : 구름 위를 걷는 기분";
            case 1:
                return "It's Your Day: 이번 광고, 생일 카페 주인공은 바로 너!";
            case 2:
                return "담곰이 카페";
            case 3:
                return "담곰이 팝업스토어 <봄날의 담곰이>";
            case 4:
                return "로에베 퍼퓸 팝업스토어";
            case 5:
                return "IKEA 팝업스토어 더현대 서울";
            case 6:
                return "엄브로 100주년 <MR.UM's CLEANERS>";
            case 7:
                return "블레스문 팝업스토어";
            case 8:
                return "요물, 우리를 홀린 고양이";
            case 9:
                return "달리기 : 새는 날고 물고기는 헤엄치고 인간은 달린다";
            default:
                return "";

        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                BitmapDescriptor blueDot = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                currentLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(currentLatLng)
                        .icon(blueDot)
                        .title("현재 위치"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
            }
        });

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // 위치 업데이트 간격 (1초)
        locationRequest.setFastestInterval(500); // 가장 빠른 위치 업데이트 간격 (0.5초)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // 높은 정확도 우선

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
