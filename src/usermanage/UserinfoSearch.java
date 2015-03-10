package usermanage;

import history.NotFound;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.style.SubscriptSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zhcs.parkingspaceadmin.R;

import common.SampleListFragment;

/**
 * 
 * @author huangjiaming
 * �û���Ϣ�޸���ڣ������û��ֻ��������û���Ϣ
 */
public class UserinfoSearch extends SlidingFragmentActivity{
	private CanvasTransformer mTransformer;
	private final static String TAG = "UserinfoSearch";
	private EditText phone;
	private String objId;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usermanage_search);
		setTitle("�û�����");
		
		initAnimation();
		initSlidingMenu();
		// �����Ͻ�ͼ�����߼���һ�����ص�ͼ�� 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		 // �����Ͻ�ͼ�����߼���һ�����ص�ͼ�� 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		phone = (EditText)findViewById(R.id.usermanage_phone);
		
	}
	
	/**
	 * �����û���ʷ�����б�
	 * @param v
	 */
	public void search(View v){
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		final String userPhone = phone.getText().toString();
		Matcher matcher = pattern.matcher(userPhone); 
		if (matcher.matches() == false) {
			phone.setError(Html.fromHtml("<font color=#808183>"
                    + "�ֻ�����Ч "+ "</font>"));
			return;
		} 
		AVQuery<AVObject> query = new AVQuery<AVObject>("OwnerInfo");
		query.whereEqualTo("phone", userPhone);
		final Message msg = new Message();
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
					// ��λ��
					if(arg0.size() > 0){
						objId = arg0.get(0).getObjectId();
						msg.what = 0x00;
						handler.sendMessage(msg);
					}
					else{
						AVQuery<AVObject> query = new AVQuery<AVObject>("DriverInfo");
						query.whereEqualTo("phone", userPhone);
						query.findInBackground(new FindCallback<AVObject>() {
							@Override
							public void done(List<AVObject> arg0, AVException e) {
								if (e == null) {
									// ����
									if(arg0.size() > 0){
										objId = arg0.get(0).getObjectId();
										msg.what = 0x01;
									}
									// ��Ч����
									else{
										msg.what = 0x02;
									}
									handler.sendMessage(msg);
								}
							}
						});
					}
				}
			}
		});
	}
	
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0x00){   //��λ��
				Log.e(TAG, "owner");
				Intent intent=new Intent();
				intent.putExtra("type", "owner");
				intent.putExtra("objId", objId);
				intent.setClass(UserinfoSearch.this,UserinfoModify.class);
				startActivity(intent);
			}else if(msg.what == 0x01){ //����
				Log.e(TAG, "driver");
				Intent intent=new Intent();
				intent.putExtra("type", "driver");
				intent.putExtra("objId", objId);
				intent.setClass(UserinfoSearch.this,UserinfoModify.class);
				startActivity(intent);
			}else{   //��Ч����
				Intent intent=new Intent();
				intent.setClass(UserinfoSearch.this,NotFound.class);
				startActivity(intent);
			}
		}
	};
	
	/**
	 * ��ʼ�������˵�
	 */
	private void initSlidingMenu(){
		// ������������ͼ
		//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SampleListFragment()).commit();
				
		// ���û����˵���ͼ
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new SampleListFragment()).commit();

		// ���û����˵�������ֵ
		SlidingMenu sm = getSlidingMenu();		
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.layout.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindWidth(400);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(mTransformer);
		
		setSlidingActionBarEnabled(true);
	}

	/**
	 * ��ʼ������Ч��
	 */
	private void initAnimation(){
		mTransformer = new CanvasTransformer(){
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);				
			}
			
		};
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}



