package com.example.capstone;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class googlemap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText searchEditText;
    private Button searchButton;
    private List<Marker> markerList = new ArrayList<>(); // 마커를 관리하기 위한 리스트

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

        // 마커를 원하는 위치에 추가
        addMarker(37.5665, 126.9780, "서울"); // 서울의 좌표
        addMarker(37.512949, 127.102380, "시그니엘");
        addMarker(37.536884, 127.009209, "한남더힐");
        addMarker(37.507664, 127.014553, "반포자이");
        addMarker(37.53417, 126.987547, "경리단길");

    }

    // 위치를 검색하고 마커를 클릭한 것처럼 동작하는 메소드
    private void searchLocation(String location) {
        // 모든 마커를 순회하면서 검색어가 제목에 포함되어 있는지 확인하고,
        // 포함되어 있다면 해당 마커의 위치로 지도를 이동합니다.
        for (Marker marker : markerList) {
            if (marker.getTitle().contains(location)) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                return; // 찾았으면 더 이상 반복할 필요 없음
            }
        }
    }

    // 마커를 추가하는 메서드
    private void addMarker(double latitude, double longitude, String title) {
        LatLng markerLocation = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new MarkerOptions().position(markerLocation).title(title));
        markerList.add(marker); // 리스트에 마커 추가
    }
}
