package jp.tank.deepoperation.GPSService;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GPSMainActivity extends Activity implements View.OnClickListener {
	private final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	TextView titleText;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// レイアウトの生成 (1)
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundColor(Color.rgb(255, 255, 255));
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);
		// テキストビューの生成 (2)
		LinearLayout textLayout = new LinearLayout(this);
		textLayout.setBackgroundColor(Color.rgb(0, 0, 0));
		textLayout.setOrientation(LinearLayout.VERTICAL);
		titleText = new TextView(this);
		titleText.setText("GPSサービス終了画面");
		titleText.setTextColor(Color.rgb(255,255,255));
		textLayout.addView(titleText);
		layout.addView(textLayout);
		// ボタンの生成
		layout.addView(makeButton("サービス終了","0"));
	}

	//ボタンの生成(1)
	private Button makeButton(String text, String tag) {
		Button button = new Button(this);
		button.setText(text);
		button.setTag(tag);
		button.setOnClickListener(this);	//ボタンクリックイベントの処理 (2)
		button.setLayoutParams(new LinearLayout.LayoutParams(WC,WC));
		return button;
	}

	@Override
	public void onClick(View view) {
		int tag = Integer.parseInt((String)view.getTag());
		if(tag == 0) {
			// サービスクラスを指定したインテントの作成
			Intent intent = new Intent(this, GPSServiceMain.class);
			// サービスの起動
			stopService(intent);
			finish();
		}
	}
}
