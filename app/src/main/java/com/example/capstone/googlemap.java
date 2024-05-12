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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class googlemap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText searchEditText;
    private Button searchButton;
    private Button backButton; // 뒤로가기 버튼 추가
    private List<LatLng> markerLocations = new ArrayList<>(); // 미리 찍어둔 좌표 리스트
    private Map<String, Float> zoomLevels = new HashMap<>(); // 검색 위치별 줌 레벨 매핑

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap);

        // 미리 찍어둔 좌표와 검색 위치별 줌 레벨 설정
        markerLocations.add(new LatLng(37.5665, 126.9780)); // 서울의 좌표
        markerLocations.add(new LatLng(37.512949, 127.102380)); // 시그니엘
        markerLocations.add(new LatLng(37.536884, 127.009209)); // 한남더힐
        markerLocations.add(new LatLng(37.507664, 127.014553)); // 반포자이
        markerLocations.add(new LatLng(37.53417, 126.987547)); // 경리단길
        markerLocations.add(new LatLng(36.317566, 127.367761)); // 배재대학교 정보과학관

        zoomLevels.put("시그니엘", 10f);
        zoomLevels.put("한남더힐", 10f);
        zoomLevels.put("반포자이", 15f);
        zoomLevels.put("경리단길", 15f);
        zoomLevels.put("배재대 정보과학관", 15f); // 정보과학관에 대한 줌 레벨 추가

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        backButton = findViewById(R.id.backButton); // 뒤로가기 버튼 연결

        // 검색 버튼 클릭 이벤트 처리
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = searchEditText.getText().toString();
                // 위치를 검색하고 이동하는 메서드 호출
                searchLocation(location);
            }
        });

        // 뒤로가기 버튼 클릭 이벤트 처리
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 액티비티를 종료하고 이전 액티비티로 이동
                finish();
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

        // 미리 찍어둔 좌표에 마커 추가
        for (int i = 0; i < markerLocations.size(); i++) {
            LatLng location = markerLocations.get(i);
            String title = getTitleFromPosition(i);
            mMap.addMarker(new MarkerOptions().position(location).title(title));
        }
    }

    // 위치를 검색하고 해당 위치로 지도를 이동하는 메소드
    private void searchLocation(String location) {
        // 검색한 위치로 지도를 이동합니다.
        // 이동 애니메이션은 지도만 이동시키고, 마커를 찍지 않습니다.
        LatLng searchedLocation = getLocationFromAddress(location);
        if (searchedLocation != null) {
            // 검색된 위치의 이름으로 줌 레벨을 가져옵니다.
            Float zoomLevel = zoomLevels.get(location);
            if (zoomLevel == null) {
                zoomLevel = 10f; // 기본 줌 레벨
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, zoomLevel));
        }
    }

    // 주소를 받아서 해당 주소의 위도와 경도를 반환하는 메소드
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;
    }

    // 마커 위치에 해당하는 이름 가져오기
    private String getTitleFromPosition(int position) {
        switch (position) {
            case 0:
                return "서울";
            case 1:
                return "시그니엘";
            case 2:
                return "한남더힐";
            case 3:
                return "반포자이";
            case 4:
                return "경리단길";
            case 5:
                return "정보과학관";
            default:
                return "";
        }
    }
}
