package android.com.beacon1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    Button lbt ;
    SocketConnect client;
    EditText piID;
    private BackPressCloseHandler backPressCloseHandler;  //취소버튼 핸들러

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backPressCloseHandler = new BackPressCloseHandler(this);

        Intent intent = new Intent(this, SplashActivity.class);  //로딩화면
        startActivity(intent);

        lbt = (Button) findViewById(R.id.loginButton);
        piID = (EditText) findViewById(R.id.piid);

        new Thread(new Runnable() {
            @Override
            public void run() {

                lbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String inputPi = piID.getText().toString();

                        if(inputPi.isEmpty())
                        {
                            piID.setText("");
                            piID.setText(null);
                        }
                        else
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String msg;
                                    try {
                                        Log.d("###","thread connect socket");
                                        SocketConnect connect = new SocketConnect();
                                        msg = connect.sendAndReceive("Piid");
                                      Log.d("###","thread connect socket!!!");
                                        if(msg.equals("binding")) {
                                            Log.d("####","send start");
                                            if (connect.sendAndReceive("bind").equals("ReqID")) {
//
                                                String str = connect.sendAndReceive(inputPi);
                                                if (str.equals("IDbind")) {

                                                    connect.socketClose();
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    intent.putExtra("piName", inputPi);
                                                    startActivity(new Intent(Login.this, MainActivity.class));
                                                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);  //화면전환효과
                                                } else if (str.equals("IDbindfail")) {
                                                    connect.socketClose();
                                                    piID.setText("");
                                                    piID.setText(null);
                                                }
                                            }
                                        }

                                    } catch (Exception e) {
                                        System.out.println("connect exception");
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                });
            }
        }).start();

    }

    @Override
    public void onBackPressed() { //취소버튼 눌렀을때
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
