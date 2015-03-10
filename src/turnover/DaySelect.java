package turnover;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import login.Login;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zhcs.parkingspaceadmin.R;

import common.SampleListFragment;
import android.text.Html;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author huangjiaming
 * 填写需查询交易额的日期
 */
public class DaySelect extends SlidingFragmentActivity{
	//添加小区按钮
	private Button daySearch;
	private TextView year,month,day;
	private CanvasTransformer mTransformer;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turnover_dayselect);
		setTitle("交易额查询");
		
		initAnimation();
		
		initSlidingMenu();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//获得实例对象
		daySearch = (Button)findViewById(R.id.day_search);
		year = (TextView)findViewById(R.id.year);
		month = (EditText)findViewById(R.id.month);
		day = (EditText)findViewById(R.id.day);
		
		//查询
		daySearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isValid())
					return;
				else{
					final Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, Integer.parseInt(year.getText().toString()));
					cal.set(Calendar.MONTH, Integer.parseInt(month.getText().toString()) - 1);
					cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.getText().toString()));
					AVQuery<AVObject> query = new AVQuery<AVObject>("BookSpaceLog");
					query.findInBackground(new FindCallback<AVObject>() {
						@Override
						public void done(List<AVObject> arg0, AVException e) {
							if (e == null) {
								final int spacesize = arg0.size();
								Log.d("daySelectTotal", ""+spacesize);
								int sumOfCount = 0, moneyCount = 0;
								for(int i = 0; i < spacesize; i++){
									Date createTimeDate = arg0.get(i).getCreatedAt();
									Calendar temp = Calendar.getInstance();
									temp.setTime(createTimeDate);
									if(temp.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
											temp.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
													temp.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)){
										sumOfCount++;
										moneyCount += arg0.get(i).getInt("cost");
									}
								}
								Log.d("daySelectCurrDay", ""+sumOfCount);
								StringBuffer buf = new StringBuffer();    
								buf.append(cal.get(Calendar.YEAR)).append("-");  
								buf.append(addZero(cal.get(Calendar.MONTH) + 1, 2));  
								buf.append("-");  
								buf.append(addZero(cal.get(Calendar.DAY_OF_MONTH), 2)); 
								Intent intent = new Intent();
								intent.putExtra("day", buf.toString());
								intent.putExtra("money", moneyCount);
								intent.setClass(DaySelect.this, TurnoverDay.class);
								startActivity(intent);
							}
						}
					});
				}
			}
		});
	}
	
	
	private String addZero(int num, int len) {  
           StringBuffer s = new StringBuffer();  
           s.append(num);  
           while (s.length() < len) {      // 如果长度不足，则继续补0  
               s.insert(0, "0");           // 在第1个位置处补0  
           }  
           return s.toString();  
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
		if(year.getText().toString().trim().equals("")||Integer.parseInt(year.getText().toString()) > 2015){
			year.setError(Html.fromHtml("<font color=#808183>"
                    + "年份不符合要求"+ "</font>"));
			return false;
		}				
		else if(month.getText().toString().trim().equals("")||Integer.parseInt(month.getText().toString()) < 1 
				|| Integer.parseInt(month.getText().toString()) > 12){
			month.setError(Html.fromHtml("<font color=#808183>"
                    + "月份不符合要求"+ "</font>"));
			return false;
		}
		else if(day.getText().toString().trim().equals("")||Integer.parseInt(day.getText().toString()) < 1 
				|| Integer.parseInt(day.getText().toString()) > 31){
			day.setError(Html.fromHtml("<font color=#808183>"
                    + "日期不符合要求"+ "</font>"));
			return false;
		}
		else{
			try {
				Calendar cal = Calendar.getInstance();
				cal.setLenient(false);
				cal.set(Calendar.YEAR, Integer.parseInt(year.getText().toString()));
				cal.set(Calendar.MONTH, Integer.parseInt(month.getText().toString()) - 1);
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.getText().toString()));
			} catch (Exception e) {
				Toast.makeText(DaySelect.this, "无效的日期", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
}


