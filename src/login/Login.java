package login;

import java.util.*;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.zhcs.parkingspaceadmin.R;
import communitymanage.ModeDisplay;

/**
 * 
 * @author Administrator
 * 登录页面
 */
public class Login extends Activity{
	private EditText userName;
	private EditText password;
	private CheckBox rem_pw, auto_login; 
	private Button login;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		//设置应用的 Application ID 和 Key
		AVOSCloud.initialize(this, 
				"zp4jwp0gcihnamp8rae57x3ee8gl8owu4kmjizul9xpea9zy", 
				"lc2c40rxcl2nowkb155wnl67nh4qrkv97wmbela3w1h8jfyd");
		//获得实例对象
		sp=this.getSharedPreferences("Userinfo", Context.MODE_PRIVATE);
		userName=(EditText)findViewById(R.id.Phone);
		password=(EditText)findViewById(R.id.password);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);  
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
		login=(Button)findViewById(R.id.log);
		
		//判断记住密码多选框的状态  
        if(sp.getBoolean("ISCHECK", false))  
        {  
          //默认为记住密码
          rem_pw.setChecked(true);
          userName.setText(sp.getString("USERNAME", ""));  
          password.setText(sp.getString("PASSWORD", ""));  
          //判断自动登陆多选框状态  
          if(sp.getBoolean("AUTO_ISCHECK", false))  
          {  
        	  	// 执行输入校验
				if(validate())
				{
					loginToServer();
				}  
			}  
         }  
		
		
		//监听记住密码多选框按钮事件
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					sp.edit().putBoolean("ISCHECK", true).commit();
				}
				else{
					sp.edit().putBoolean("ISCHECK", false).commit();
				}
			}
		});
		
		//监听自动登录多选框按钮事件
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
				}
				else{
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});
		
		//登陆按键触发事件
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0){
				// 执行输入校验
				if(validate())
				{
					loginToServer();
				}
			}
		});
	}
	
	/*
	 * 登陆到服务器
	 */
	private void loginToServer(){
		final String username=userName.getText().toString();
		final String pass=password.getText().toString();
		AVQuery<AVObject> query = new AVQuery<AVObject>("AdminInfo");
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
		    public void done(List<AVObject> avObjects, AVException e) {
		        if (e == null) {
		            AVObject obj = avObjects.get(0);
		            if(pass.equals(obj.getString("password"))) {
		            	if(rem_pw.isChecked())  
			            {  
			             //记住用户名、密码
			              Editor editor = sp.edit();  
			              editor.putString("USERNAME",username);  
			              editor.putString("PASSWORD",pass);  
			              editor.commit();  
			            }

			            Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(Login.this,ModeDisplay.class);
						startActivity(intent);
						finish();
		            }else {
		            	Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
		            }
		        } else {
		            Log.d("失败", "登陆失败: " + e.getMessage());
		            Toast.makeText(Login.this, "登陆失败", Toast.LENGTH_SHORT).show();
		        }
		    }
		});
	
	}
	
	
	//对用户输入的手机号、密码进行校验是否为空
	private boolean validate()
	{
		String phone=userName.getText().toString().trim();//trim是两边去空格的方法
		String pass=password.getText().toString().trim();//trim是两边去空格的方法
		if(phone.equals("")||pass.equals(""))
		{
			new AlertDialog.Builder(Login.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("登录失败")
			.setMessage("手机号或者密码不能为空，\n请检查后重新输入！")
			.create().show();
			return false;
		}
		return true;
	}

    @Override
	public void onBackPressed() {
	    //实现Home键效果，需要添加权限:<uses-permission android:name="android.permission.RESTART_PACKAGES" />
	    //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
	    Intent i= new Intent(Intent.ACTION_MAIN);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.addCategory(Intent.CATEGORY_HOME);
	    startActivity(i); 
	}
    
    //退出应用程序的实现，需要添加权限:<uses-permission android:name="android.permission.RESTART_PACKAGES" />
    public void exitProgrames(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                android.os.Process.killProcess(android.os.Process.myPid());
    }
}

