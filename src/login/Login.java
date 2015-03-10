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
 * ��¼ҳ��
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
		
		//ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		//����Ӧ�õ� Application ID �� Key
		AVOSCloud.initialize(this, 
				"zp4jwp0gcihnamp8rae57x3ee8gl8owu4kmjizul9xpea9zy", 
				"lc2c40rxcl2nowkb155wnl67nh4qrkv97wmbela3w1h8jfyd");
		//���ʵ������
		sp=this.getSharedPreferences("Userinfo", Context.MODE_PRIVATE);
		userName=(EditText)findViewById(R.id.Phone);
		password=(EditText)findViewById(R.id.password);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);  
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
		login=(Button)findViewById(R.id.log);
		
		//�жϼ�ס�����ѡ���״̬  
        if(sp.getBoolean("ISCHECK", false))  
        {  
          //Ĭ��Ϊ��ס����
          rem_pw.setChecked(true);
          userName.setText(sp.getString("USERNAME", ""));  
          password.setText(sp.getString("PASSWORD", ""));  
          //�ж��Զ���½��ѡ��״̬  
          if(sp.getBoolean("AUTO_ISCHECK", false))  
          {  
        	  	// ִ������У��
				if(validate())
				{
					loginToServer();
				}  
			}  
         }  
		
		
		//������ס�����ѡ��ť�¼�
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
		
		//�����Զ���¼��ѡ��ť�¼�
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
		
		//��½���������¼�
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0){
				// ִ������У��
				if(validate())
				{
					loginToServer();
				}
			}
		});
	}
	
	/*
	 * ��½��������
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
			             //��ס�û���������
			              Editor editor = sp.edit();  
			              editor.putString("USERNAME",username);  
			              editor.putString("PASSWORD",pass);  
			              editor.commit();  
			            }

			            Toast.makeText(Login.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(Login.this,ModeDisplay.class);
						startActivity(intent);
						finish();
		            }else {
		            	Toast.makeText(Login.this, "�û������������", Toast.LENGTH_SHORT).show();
		            }
		        } else {
		            Log.d("ʧ��", "��½ʧ��: " + e.getMessage());
		            Toast.makeText(Login.this, "��½ʧ��", Toast.LENGTH_SHORT).show();
		        }
		    }
		});
	
	}
	
	
	//���û�������ֻ��š��������У���Ƿ�Ϊ��
	private boolean validate()
	{
		String phone=userName.getText().toString().trim();//trim������ȥ�ո�ķ���
		String pass=password.getText().toString().trim();//trim������ȥ�ո�ķ���
		if(phone.equals("")||pass.equals(""))
		{
			new AlertDialog.Builder(Login.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("��¼ʧ��")
			.setMessage("�ֻ��Ż������벻��Ϊ�գ�\n������������룡")
			.create().show();
			return false;
		}
		return true;
	}

    @Override
	public void onBackPressed() {
	    //ʵ��Home��Ч������Ҫ���Ȩ��:<uses-permission android:name="android.permission.RESTART_PACKAGES" />
	    //super.onBackPressed();��仰һ��Ҫע��,��Ȼ��ȥ����Ĭ�ϵ�back����ʽ��
	    Intent i= new Intent(Intent.ACTION_MAIN);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.addCategory(Intent.CATEGORY_HOME);
	    startActivity(i); 
	}
    
    //�˳�Ӧ�ó����ʵ�֣���Ҫ���Ȩ��:<uses-permission android:name="android.permission.RESTART_PACKAGES" />
    public void exitProgrames(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                android.os.Process.killProcess(android.os.Process.myPid());
    }
}

