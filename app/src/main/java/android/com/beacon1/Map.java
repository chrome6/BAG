package android.com.beacon1;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

public class Map extends NMapActivity {


    private NMapView mMapView;  // 지도 화면 View
    private ViewGroup mapLayout;
    private NMapController MC; //지도 상태 컨트롤 객체
    private NMapResourceProvider MRP;  //지도 뷰어 리소스 공급자 객체 생성
    private NMapOverlayManager OM; //오버레이 관리 객체
    private NMapLocationManager LM;  //단말기의 현재 위치 탐색 기능 사용 클래스
    private NMapMyLocationOverlay LO;  //지도 위에 현재 위치를 표시하는 오버레이 클래스
    private NMapCompassManager CM;  //단말기의 나침반 기능 사용 클래스

    protected  double maplat;
    protected  double maplong;
    private  boolean nullKey = false;

    private GpsInfo GS  ;
    private final String CLIENT_ID = "cOt9ih3JT_fzMA_gFAFL";  // 애플리케이션 클라이언트 아이디 값


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        //String name = intent.getStringExtra("name");
        if(intent.getStringExtra("pilat").equals("zero") || intent.getStringExtra("pilong").equals("zero")) {
            nullKey = false;
        }
        else{
            maplat = Double.parseDouble(intent.getStringExtra("pilat"));
            maplong = Double.parseDouble(intent.getStringExtra("pilong"));
            nullKey = true;
        }
        init();

        MRP = new NMapViewerResourceProvider(this);
        OM = new NMapOverlayManager(this, mMapView, MRP);
        LM = new NMapLocationManager(this);

    }

    public void init(){
        mapLayout = findViewById(R.id.mapLayout);

        GS = new GpsInfo(this);

        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        mMapView.setOnMapStateChangeListener(changeListener);
        mMapView.setOnMapViewTouchEventListener(mapListener);
        LM  = new NMapLocationManager(this);
        mapLayout.addView(mMapView);

        MC = mMapView.getMapController();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setMarker();
            }
        }, 500);



    }
/////////////////////////////////////////// Map Status Chaned callback
    private NMapView.OnMapStateChangeListener changeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
           // Log.e(TAG, "OnMapStateChangeListener onMapInitHandler : ");
        }

        @Override
        public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {
           // Log.e(TAG, "OnMapStateChangeListener onMapCenterChange : " + nGeoPoint.getLatitude() + " ㅡ  " + nGeoPoint.getLongitude());
        }

        @Override
        public void onMapCenterChangeFine(NMapView nMapView) {
        //    Log.e(TAG, "OnMapStateChangeListener onMapCenterChangeFine : ");
        }

        @Override
        public void onZoomLevelChange(NMapView nMapView, int i) {
          //  Log.e(TAG, "OnMapStateChangeListener onZoomLevelChange : " + i);
        }

        @Override
        public void onAnimationStateChange(NMapView nMapView, int i, int i1) {
            //Log.e(TAG, "OnMapStateChangeListener onAnimationStateChange : ");
        }
    };

    private NMapView.OnMapViewTouchEventListener mapListener = new NMapView.OnMapViewTouchEventListener() {
        @Override
        public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {
           // Log.e(TAG, "OnMapViewTouchEventListener onLongPress : ");
        }

        @Override
        public void onLongPressCanceled(NMapView nMapView) {
          //  Log.e(TAG, "OnMapViewTouchEventListener onLongPressCanceled : ");
        }

        @Override
        public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {
            //Log.e(TAG, "OnMapViewTouchEventListener onTouchDown : ");
        }

        @Override
        public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {
           // Log.e(TAG, "OnMapViewTouchEventListener onTouchUp : ");
        }

        @Override
        public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {
           // Log.e(TAG, "OnMapViewTouchEventListener onScroll : ");
        }

        @Override
        public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {
            //Log.e(TAG, "OnMapViewTouchEventListener onSingleTapUp : ");
        }
    };

    private NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

        }


        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            if (nMapPOIitem != null) {
               // Log.e(TAG, "onFocusChanged: " + nMapPOIitem.toString());
            } else {
               // Log.e(TAG, "onFocusChanged: ");
            }
        }
    };




///////////////////////////////////


    private void setMarker(){

        int markerId = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, MRP);
        poiData.beginPOIdata(2);

        poiData.addPOIitem(GS.getLongitude(), GS.getLatitude(), "내 위치", markerId, 0);  //현재위치
       // Toast.makeText(Map.this, lon + " " + lat, Toast.LENGTH_LONG).show();

        String lostr = Double.toString(GS.getLongitude());
        String lastr = Double.toString(GS.getLatitude());
        Log.d("####lostr", lostr);
        Log.d("####lastr", lastr);

        if(nullKey) {
            poiData.addPOIitem(maplong, maplat, "아이 위치", markerId, 0);
            poiData.endPOIdata();

        }

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = OM.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);  //좌표 클릭시 말풍선 리스트


        NMapPathData pathData = new NMapPathData(2);
        pathData.initPathData();   //경로 데이터 추가 시작
        //경로 데이터의 보간점 좌표 추가 - 좌표, 선 Type 설정(0으로 할시 이전 값 그대로 사용)
        pathData.addPathPoint(GS.getLongitude(), GS.getLatitude(), NMapPathLineStyle.TYPE_SOLID);
        pathData.addPathPoint(maplong, maplat, 0);
        pathData.endPathData();   //경로 데이터 추가 종료

        NMapPathDataOverlay pdo = OM.createPathDataOverlay(pathData);

    }

    @Override
    public void onBackPressed() {  // 취소버튼 눌렀을떄
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);  //화면 전환효과
    }

}
