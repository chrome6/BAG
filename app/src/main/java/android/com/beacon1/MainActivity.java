package android.com.beacon1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
     private BeaconManager beaconManager;
     private Region region;
     private TextView tvId;
     private boolean isConnected = false;
     private  TextView gpsdistance;
     private GpsInfo GS;
     private  TextView backgroundImg ;
     private  TextView gpstext;
     private  TextView btext;
     private  ImageView img1;
     private  ImageView img2;
     private  ImageView img3;
     SocketConnect connect = null;
     String piid = null;
     Button bt;
     static int count = 0;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          Intent intent = getIntent();
          piid = intent.getStringExtra("piName");

          tvId = (TextView) findViewById(R.id.tvId);
          beaconManager = new BeaconManager(this);
          gpsdistance = (TextView) findViewById(R.id.gpsdistance);
          GS = new GpsInfo(this);
          backgroundImg = (TextView) findViewById(R.id.backgroundImg);
          bt = (Button)findViewById(R.id.gps);
          gpstext = (TextView) findViewById(R.id.textView4);

          img1 = (ImageView) findViewById(R.id.imageView3);
          img2 = (ImageView) findViewById(R.id.imageView4);
          img3 = (ImageView) findViewById(R.id.imageView5);

          gpstext.setVisibility(View.INVISIBLE);
          gpsdistance.setVisibility(View.INVISIBLE);

          new Thread(new Runnable() {
               @Override
               public void run() {
                    try {
                         region = new Region("ranged region",
                                 UUID.fromString("11111111-2222-3333-4444-555555555555"), 4660, 22136); // 본인이 연결할 Beacon의 ID와 Major / Minor Code를 알아야 한다.11

                         connect = new SocketConnect();

                         beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                              @Override
                              public void onServiceReady() {
                                   beaconManager.startRanging(region);
                              }
                         });

                         Log.d("####", "main inner thread start");
                         if (connect.sendAndReceive("Piid").equals("binding")) {
                              if (connect.sendAndReceive("bindclear").equals("ReqID")) {
                                   if (connect.sendAndReceive(piid).equals("next")) {

                                        // 소켓 연결 완료 상태
                                        runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {

                                                  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                                  StrictMode.setThreadPolicy(policy);



                                                  bt.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                            String str = null;

                                                            connect.sendData("reqgps");
                                                            str = connect.receiveData();
                                                            Log.d("####", str);
                                                            StringTokenizer st = new StringTokenizer(str, "&");

                                                            Intent i = new Intent(MainActivity.this, Map.class);
                                                            i.putExtra("pilat", st.nextToken());
                                                            i.putExtra("pilong", st.nextToken());
                                                            startActivity(new Intent(MainActivity.this, Map.class));
                                                            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left); //화면전환효과
                                                       }
                                                  });

                                                  beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                                                       @Override
                                                       public void onBeaconsDiscovered(Region region, List<Beacon > list) {
                                                            Beacon nearestBeacon = null;
                                                            if (!list.isEmpty()) {
                                                                 nearestBeacon = list.get(0);
                                                            }
                                                            //tvId.setText(nearestBeacon.getRssi() + " ");
                                                            try {
                                                            if( !isConnected && nearestBeacon.getRssi() > -59) {  //TxPower = -59 , rssi = -60(1.3m) -70(2.5m) -80(5m), -90(9m)
                                                                 isConnected = true;
                                                                 count = 0;  //exception 확인값
                                                            } else if( isConnected && nearestBeacon.getRssi() < -90 ) {
                                                                 //Toast.makeText(MainActivity.this, "연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
                                                                 isConnected = false;
                                                            }

                                                            if(isConnected) {
                                                                 gpstext.setVisibility(View.INVISIBLE);
                                                                 gpsdistance.setVisibility(View.INVISIBLE);
                                                                 img1.setVisibility(View.INVISIBLE);
                                                                 img2.setVisibility(View.INVISIBLE);
                                                                 img3.setVisibility(View.INVISIBLE);
                                                                 tvId.setTextSize(30);
                                                                 if (nearestBeacon.getRssi() < -80) {
                                                                      tvId.setText("8m~9m");
                                                                      img2.setVisibility(View.VISIBLE);
                                                                      backgroundImg.setBackgroundColor(Color.rgb(0, 0, 255));

                                                                      if (connect != null) {
                                                                           if (connect.sendAndReceive("resbeacon").equals("reqbeacon")) {
                                                                                String msg = connect.sendAndReceive("blu");
                                                                                Log.d("beaconListener if#", msg);
                                                                           }
                                                                      }
                                                                 } else if (nearestBeacon.getRssi() < -60 && nearestBeacon.getRssi() >= -80) {
                                                                      tvId.setText("2.5m ~ 8m");
                                                                      img2.setVisibility(View.VISIBLE);
                                                                      backgroundImg.setBackgroundColor(Color.rgb(255, 0, 0));
                                                                      if (connect != null) {
                                                                           if (connect.sendAndReceive("resbeacon").equals("reqbeacon")) {
                                                                                String msg = connect.sendAndReceive("red");
                                                                                Log.d("beaconListener if#", msg);
                                                                           }
                                                                      }
                                                                 } else if (nearestBeacon.getRssi() < -10 && nearestBeacon.getRssi() >= -60) {
                                                                      tvId.setText("2.5m 이내");
                                                                      img1.setVisibility(View.VISIBLE);
                                                                      backgroundImg.setBackgroundColor(Color.rgb(0, 255, 0));
                                                                      if (connect != null) {
                                                                           if (connect.sendAndReceive("resbeacon").equals("reqbeacon")) {
                                                                                String msg = connect.sendAndReceive("gre");
                                                                                Log.d("beaconListener if#", msg);
                                                                           }
                                                                      }
                                                                 }
                                                            }

                                                            else{
                                                                 img1.setVisibility(View.INVISIBLE);
                                                                 img2.setVisibility(View.INVISIBLE);
                                                                 img3.setVisibility(View.VISIBLE);
                                                                 tvId.setTextSize(20);
                                                                 tvId.setText("알수없습니다.");

                                                                 gpstext.setVisibility(View.VISIBLE);
                                                                 gpsdistance.setVisibility(View.VISIBLE);
                                                                 backgroundImg.setBackgroundColor(Color.rgb(0, 0, 0));
                                                                 String str = null;

                                                                 if (connect != null) {
                                                                      if (connect.sendAndReceive("resbeacon").equals("reqbeacon")) {
                                                                           String msg = connect.sendAndReceive("bla");
                                                                           Log.d("beaconListener if#", msg);
                                                                      }
                                                                      connect.sendData("reqgps");
                                                                      str = connect.receiveData();
                                                                      Log.d("####", str);
                                                                      StringTokenizer st = new StringTokenizer(str, "&");
                                                                      String lat = st.nextToken();
                                                                      String lon = st.nextToken();

                                                                      if(lat.equals("zero")||lon.equals("zero")) {
                                                                           gpsdistance.setTextSize(18);
                                                                           gpsdistance.setText("GPS위치를 알수 없습니다.");
                                                                      }else{
                                                                           gpsdistance.setTextSize(30);
                                                                           gpsdistance.setText(distFrom(Double.parseDouble(lat), Double.parseDouble(lon) )+ "m");
                                                                      }
                                                                 }



                                                            }
                                                            } catch (IllegalArgumentException e) {
                                                                 Toast.makeText(MainActivity.this, "연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
                                                            } catch (NullPointerException e) {
                                                                 if(count == 0) {
                                                                      Toast.makeText(MainActivity.this, "수신 가능한 beacon이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                                                      count ++; //toast 한번만 뜨게
                                                                 }
                                                            }
                                                       }

                                                  });
                                             }
                                        });


                                   }
                              }
                         }
                    } catch (Exception e) {
                    e.printStackTrace();

                    }
               }
          }).start();
     }
     @Override protected void onResume() {     //주석처리하니까 비콘 감지안됨
          super.onResume();
          // 블루투스 권한 및 활성화 코드
          SystemRequirementsChecker.checkWithDefaultDialogs(this);

     }
     protected void onPause() {
          // beaconManager.stopRanging(region);
          super.onPause();
     }
     private String distFrom(double lat1 ,double lng1) {  //거리 계산하는 부분 (lat1,lng1 야탑역 위경도)
          double earthRadius = 3958.75;
          //double lat1 = 37.411228;
          //double lng1 = 127.128745;
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

     @Override
     public void onBackPressed() {  // 취소버튼 눌렀을떄
          super.onBackPressed();
          overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);  //화면 전환효과
     }
}

