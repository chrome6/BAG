package android.com.beacon1;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.UUID;

public class MyApplication {

/*
       beaconManager.setRangingListener(new BeaconManager.RangingListener() {
        @Override
        public void onBeaconsDiscovered(Region region, List<Beacon > list) {
            Beacon nearestBeacon = null;
            if (!list.isEmpty()) {
                nearestBeacon = list.get(0);
            }
            tvId.setText(nearestBeacon.getRssi() + "");

            if( !isConnected && nearestBeacon.getRssi() > -59) {  //TxPower = -59 , rssi = -60(1.3m) -70(2.5m) -80(5m), -90(9m)
                isConnected = true;

            } else if( isConnected && nearestBeacon.getRssi() < -90 ) {
                //Toast.makeText(MainActivity.this, "연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
                isConnected = false;
            }


            if (nearestBeacon.getRssi() < -90) {
                tvId.setText("9m이상 벗어남");
                backgroundImg = (ImageView) findViewById(R.id.imageView1);
                backgroundImg.setBackgroundColor(Color.rgb(0, 0, 0));
            } else if (nearestBeacon.getRssi() < -60 && nearestBeacon.getRssi() >= -90) {
                tvId.setText("2.5m ~ 9m 사이");
                backgroundImg = (ImageView) findViewById(R.id.imageView1);
                backgroundImg.setBackgroundColor(Color.rgb(255, 0, 0));
            } else if (nearestBeacon.getRssi() < -10 && nearestBeacon.getRssi() >= -60) {
                tvId.setText("2.5m 이내");
                backgroundImg = (ImageView) findViewById(R.id.imageView1);
                backgroundImg.setBackgroundColor(Color.rgb(0, 255, 0));
            }

        }

    });

          gpsdistance.setText(distFrom() + "m");
    Button bt = (Button)findViewById(R.id.gps);
          bt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, Map.class);
            startActivity(i);
        }
    });
    region = new Region("ranged region",
                        UUID.fromString("74278bda-b644-4520-8f0c-720eaf059935"), 65504, 65505); // 본인이 연결할 Beacon의 ID와 Major / Minor Code

}
    @Override protected void onResume() {     //주석처리하니까 비콘 감지안됨
        super.onResume();
        // 블루투스 권한 및 활성화 코드
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }
    @Override
    protected void onPause() {
        // beaconManager.stopRanging(region);
        super.onPause();
    }



    private String distFrom() {  //거리 계산하는 부분 (lat1,lng1 야탑역 위경도)
        double earthRadius = 3958.75;
        double lat1 = 37.411228;
        double lng1 = 127.128745;
        double lat2 = GS.getLatitude();
        double lng2 = GS.getLongitude();

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance  = Math.round(earthRadius * c * 1609.34 * 100d) / 100d;
        String dist  = Double.toString(distance);
        return dist;
    }
*/
}
