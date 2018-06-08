package jp.tank.deepoperation.GPSService;

import jp.tank.deepoperation.GPSService.GPSServiceMain;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GPSServiceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// サービスクラスを指定したインテントの作成
		Intent intent = new Intent(this, GPSServiceMain.class);
		// サービスの起動
		startService(intent);
		finish();
    }
}