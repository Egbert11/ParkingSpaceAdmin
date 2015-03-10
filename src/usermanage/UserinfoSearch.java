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
 * 用户信息修改入口：根据用户手机号搜索用户信息
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
		setTitle("用户管理");
		
		initAnimation();
		initSlidingMenu();
		// 给左上角图标的左边加上一个返回的图标 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		 // 给左上角图标的左边加上一个返回的图标 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		phone = (EditText)findViewById(R.id.usermanage_phone);
		
	}
	
	/**
	 * 搜索用户历史订单列表
	 * @param v
	 */
	public void search(View v){
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		final String userPhone = phone.getText().toString();
		Matcher matcher = pattern.matcher(userPhone); 
		if (matcher.matches() == false) {
			phone.setError(Html.fromHtml("<font color=#808183>"
                    + "手机号无效 "+ "</font>"));
			return;
		} 
		AVQuery<AVObject> query = new AVQuery<AVObject>("OwnerInfo");
		query.whereEqualTo("phone", userPhone);
		final Message msg = new Message();
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
					// 车位主
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
									// 车主
									if(arg0.size() > 0){
										objId = arg0.get(0).getObjectId();
										msg.what = 0x01;
									}
									// 无效号码
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
			if(msg.what == 0x00){   //车位主
				Log.e(TAG, "owner");
				Intent intent=new Intent();
				intent.putExtra("type", "owner");
				intent.putExtra("objId", objId);
				intent.setClass(UserinfoSearch.this,UserinfoModify.class);
				startActivity(intent);
			}else if(msg.what == 0x01){ //车主
				Log.e(TAG, "driver");
				Intent intent=new Intent();
				intent.putExtra("type", "driver");
				intent.putExtra("objId", objId);
				intent.setClass(UserinfoSearch.this,UserinfoModify.class);
				startActivity(intent);
			}else{   //无效号码
				Intent intent=new Intent();
				intent.setClass(UserinfoSearch.this,NotFound.class);
				startActivity(intent);
			}
		}
	};
	
	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu(){
		// 设置主界面视图
		//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SampleListFragment()).commit();
				
		// 设置滑动菜单视图
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new SampleListFragment()).commit();

		// 设置滑动菜单的属性值
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
	 * 初始化动画效果
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



