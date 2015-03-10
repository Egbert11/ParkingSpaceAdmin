package communitymanage;

import java.util.List;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

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
 * 小区管理入口：1.增加小区 2.删除小区
 */
public class ModeDisplay extends SlidingFragmentActivity{
	private CanvasTransformer mTransformer;
	private final static String TAG = "ModeDisplay";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//去除标题
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_community_manage);
		setTitle("小区管理");
		
		initAnimation();
		initSlidingMenu();
		// 给左上角图标的左边加上一个返回的图标 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		 // 给左上角图标的左边加上一个返回的图标 
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void addCommunity(View v){
		Intent intent = new Intent(ModeDisplay.this, AddCommunity.class);
		startActivity(intent);
	}
	
	public void removeCommunity(View v){
		Intent intent = new Intent(ModeDisplay.this, RemoveCommunity.class);
		startActivity(intent);
	}
	
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

