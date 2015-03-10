package usermanage;

import infobean.UserinfoBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.zhcs.parkingspaceadmin.R;

/**
 * 
 * @author huangjiaming
 * �޸��û���Ϣ
 */
public class UserinfoModify extends Activity{

	private ProgressDialog proDialog;
	private final static String TAG = "UserinfoModify";
	private TextView phone,name,password;
	private RadioButton unlock, lock;
	private UserinfoBean userinfo = new UserinfoBean();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_usermanage_modify);
		setTitle("�û���Ϣ�޸�");
		
		phone = (TextView)findViewById(R.id.user_phone);
		name = (TextView)findViewById(R.id.user_name);
		password = (TextView)findViewById(R.id.user_password);
		unlock = (RadioButton)findViewById(R.id.unlock);
		lock = (RadioButton)findViewById(R.id.lock);
		
		proDialog = new ProgressDialog(UserinfoModify.this);
		proDialog.setMessage("���ڻ�ȡ���ݣ����Ժ�...");
		proDialog.show();
		
		//��ȡ��ʷ����
		if(getIntent().getExtras().getString("type").equals("owner")){
			String objId = getIntent().getExtras().getString("objId");
			getUserInfo("OwnerInfo", objId);
		}else{
			String objId = getIntent().getExtras().getString("objId");
			getUserInfo("DriverInfo", objId);
		}
	}
	
//	final Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			if(msg.what == 0x00){
//				proDialog.dismiss();
//				Log.e("Modify", "�ɹ���ȡ����");
//				mListView = (ListView)findViewById(R.id.listView1);
//				//�������б��ճ�λ�Ž�������
//				Comparator<SpaceHistoryBean> comparator = new Comparator<SpaceHistoryBean>(){
//					   public int compare(SpaceHistoryBean s1, SpaceHistoryBean s2) {
//						   return s1.getDate().compareTo(s2.getDate());
//					   }
//				};
//				Collections.sort(list,comparator);
//				mListView.setAdapter(new SpaceListAdapter());
//				mListView.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						Intent intent = new Intent(UserinfoModify.this, SubscribeInformation.class);
//						Bundle bun = new Bundle();
//						bun.putInt("index", position);
//						intent.putExtras(bun);
//						startActivity(intent);
//					}
//				});
//			}else{
//				proDialog.dismiss();
//			}
//		}
//	};
	
	/**
	 * ��ȡ�û���Ϣ
	 */
	public void getUserInfo(final String tablename, final String objId) {
		AVQuery<AVObject> query = new AVQuery<AVObject>(tablename);
		query.whereEqualTo("objectId", objId);
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
					Log.e(TAG, "hah"+arg0.size());
					AVObject userAvObject = arg0.get(0);
					userinfo.setObjId(userAvObject.getObjectId());
					userinfo.setPhone(userAvObject.getString("phone"));
					userinfo.setName(userAvObject.getString("name"));
					userinfo.setPassword(userAvObject.getString("password"));
					userinfo.setValid(userAvObject.getBoolean("isValid"));
					phone.setText(userinfo.getPhone().toString());
					name.setText(userinfo.getName().toString());
					password.setText(userinfo.getPassword().toString());
					if(userinfo.isValid())
						unlock.setChecked(true);
					else
						lock.setChecked(true);
					proDialog.dismiss();
				}
			}
		});
	}
	
	/**
	 * �ϴ��޸Ĺ����û���Ϣ
	 * @param v
	 */
	public void modify(View v){
		if(isValid() == false)
			return;
		final String tablename;
		if(getIntent().getExtras().getString("type") == "owner")
			tablename = "OwnerInfo";
		else
			tablename = "DriverInfo";	
		final String objId = getIntent().getExtras().getString("objId");
		userinfo.setPhone(phone.getText().toString());
		userinfo.setName(name.getText().toString());
		userinfo.setPassword(password.getText().toString());
		if(unlock.isChecked())
			userinfo.setValid(true);
		else
			userinfo.setValid(false);
		AVQuery<AVObject> query = new AVQuery<AVObject>(tablename);
		query.whereEqualTo("phone", userinfo.getPhone());
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
					if(arg0.size() > 0 && !arg0.get(0).getObjectId().equals(userinfo.getObjId())){
						Toast.makeText(UserinfoModify.this, "���ֻ����Ѿ����ڣ������", Toast.LENGTH_SHORT).show();
						return;
					}
					AVQuery<AVObject> query = new AVQuery<AVObject>(tablename);
					query.whereEqualTo("objectId", userinfo.getObjId());
					query.findInBackground(new FindCallback<AVObject>() {
						@Override
						public void done(List<AVObject> arg0, AVException e) {
							if (e == null) {
								AVObject modify = arg0.get(0);
								modify.put("phone", userinfo.getPhone());
								modify.put("name", userinfo.getName());
								modify.put("password", userinfo.getPassword());
								modify.put("isValid", userinfo.isValid());
								modify.saveInBackground(new SaveCallback() {
									@Override
									public void done(AVException arg0) {
										if (arg0 == null) {
											Toast.makeText(UserinfoModify.this,"�޸��û���Ϣ�ɹ�", Toast.LENGTH_SHORT).show();
								        } else {
								        	Toast.makeText(UserinfoModify.this,"�޸ĳ�λʧ��", Toast.LENGTH_SHORT).show();
								        	Log.e("UserinfoModify", arg0.getMessage());
								        }	
									}
								});
							}
						}
					});
				}
			}
		});
		
	}
	
	/**
	 * �ж������Ƿ���Ч
	 */
	private boolean isValid(){
		if(!isCellphone(phone.getText().toString())){
			phone.setError(Html.fromHtml("<font color=#808183>"
                    + "�ֻ�����Ч "+ "</font>"));
			return false;
		}
		else if(!isUser(name.getText().toString())){
			name.setError(Html.fromHtml("<font color=#808183>"
                    + "����������Ҫ��"+ "</font>"));
			return false;
		}				
		else if(!isPassword(password.getText().toString())){
			password.setError(Html.fromHtml("<font color=#808183>"
                    + "���벻����Ҫ��"+ "</font>"));
			return false;
		}
		else if((unlock.isChecked()&&lock.isChecked())||(!unlock.isChecked()&&!lock.isChecked())){
			Toast.makeText(UserinfoModify.this, "�û�״̬����ֻ��ѡ��һ��", Toast.LENGTH_SHORT).show();
			return false;
		}else{
			return true;
		}
	}
	
	private boolean isCellphone(String str){
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str); 
		if (matcher.matches())
		    return true;
	    else 
		    return false;
	 }
	  
	private boolean isUser(String str){
		if(str.trim().equals(""))
			return false;
		 else
			return true;
	 }
	  
	private boolean isPassword(String str){
		if(str.trim().equals("")||str.trim().length()<6||str.trim().length()>15)
			return false;
    	else
			return true;
	  }
}




