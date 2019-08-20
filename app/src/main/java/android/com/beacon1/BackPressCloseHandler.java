package android.com.beacon1;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }
    //0.    시간을 저장하는 변수(t) = 0;
    // 1.    뒤로가기 버튼 (처음)클릭시 시간을 저장하는 변수(t) + 2000(2초)가 현재 시간보다 작다.
    // 2-1. 알림창을 띄운다.('뒤로'버튼을 한번 더 누르시면 종료됩니다.)
    // 2-2. 시간을 저장하는 변수(t)에 현재 시간을 저장한다.
    // 3.    뒤로가기 버튼을 한번더 클릭
    // 4-1. 현재 시간이 변수t + 2000보다 작으면 앱 종료.
    // 4-2. 현재 시간이 변수t + 2000보다 크면 t에 현재시간을 저장하고 알림창을 띄운다.

        public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
