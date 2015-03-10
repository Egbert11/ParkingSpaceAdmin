package communitymanage;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zhcs.parkingspaceadmin.R;
import common.SampleListFragment;

import android.text.Html;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author huangjiaming
 *	添加小区
 */
public class AddCommunity extends SlidingFragmentActivity{
	//添加小区按钮
	private Button addCommunity;
	private TextView communityName,latitude,longitude,username,password;
	private CanvasTransformer mTransformer;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_add);
		setTitle("增加小区");
		
		initAnimation();
		
		initSlidingMenu();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//获得实例对象
		addCommunity = (Button)findViewById(R.id.add);
		communityName = (TextView)findViewById(R.id.community_name);
		latitude = (EditText)findViewById(R.id.latitude);
		longitude = (EditText)findViewById(R.id.longitude);
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		
		//增加小区
		addCommunity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isValid())
					return;
				else{
					AVObject publish = new AVObject("Community");
					
					publish.put("address", communityName.getText().toString());
					publish.put("latitude", (int)(Float.parseFloat(latitude.getText().toString()) * 1000000));
					publish.put("longitude", (int)(Float.parseFloat(longitude.getText().toString()) * 1000000));
					publish.put("userName", username.getText().toString());
					publish.put("password", password.getText().toString());
					publish.saveInBackground(new SaveCallback() {
						@Override
						public void done(AVException arg0) {
							if (arg0 == null) {
								Toast.makeText(AddCommunity.this,"小区添加成功", Toast.LENGTH_SHORT).show();
					        } else {
					        	Toast.makeText(AddCommunity.this,"小区添加失败", Toast.LENGTH_SHORT).show();
						}
					}
		        });
				}
			}
		});
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
	
	
	//判断输入数据是否合法
	private boolean isValid(){
		if(communityName.getText().toString().trim().equals("")){
			communityName.setError(Html.fromHtml("<font color=#808183>"
                    + "小区名称不符合要求"+ "</font>"));
			return false;
		}				
		else if(latitude.getText().toString().trim().equals("")){
			latitude.setError(Html.fromHtml("<font color=#808183>"
                    + "纬度不符合要求"+ "</font>"));
			return false;
		}
		else if(longitude.getText().toString().trim().equals("")){
			longitude.setError(Html.fromHtml("<font color=#808183>"
                    + "经度不符合要求"+ "</font>"));
			return false;
		}
		else if(username.getText().toString().trim().equals("")){
			username.setError(Html.fromHtml("<font color=#808183>"
                    + "用户名不符合要求"+ "</font>"));
			return false;
		}
		else if(password.getText().toString().trim().equals("")){
			password.setError(Html.fromHtml("<font color=#808183>"
                    + "用户名不符合要求"+ "</font>"));
			return false;
		}
		else{
			return true;
		}
	}
}


