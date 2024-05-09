package com.example.capstone;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class googlemap extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private EditText searchEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        // 검색 버튼 클릭 이벤트 처리
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = searchEditText.getText().toString();
                // 위치를 검색하고 이동하는 메서드 호출
                searchLocation(location);
            }
        });

        // SupportMapFragment 가져오기 및 맵 비동기적으로 로드
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // 지도가 준비되면 실행될 코드
        // 초기 지도 설정
        LatLng defaultLocation = new LatLng(37.5665, 126.9780); // 서울의 좌표로 초기화
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10)); // 서울을 중심으로 초기 줌 레벨 설정
    }

    // 위치를 검색하고 지도로 이동하는 메서드
    private void searchLocation(String location) {
        // Geocoder API를 사용하여 위치의 좌표 가져오기
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                moveCameraToLocation(latLng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 가져온 좌표로 지도 이동
    private void moveCameraToLocation(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // 이동한 위치로 지도 이동 및 줌 레벨 조정
    }
}
