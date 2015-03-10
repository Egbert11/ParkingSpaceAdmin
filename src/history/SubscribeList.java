package history;

import infobean.SpaceHistoryBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
 * ���������б�
 */
public class SubscribeList extends SlidingFragmentActivity{

	private ListView mListView;
	private CanvasTransformer mTransformer;
	private ProgressDialog proDialog;
	private int sum = 0; // ����ѯ��λ����ʷ����ʹ��
	private final static String TAG = "SubscribeList";
	private static ArrayList<SpaceHistoryBean> list = new ArrayList<SpaceHistoryBean>();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_history_list);
		setTitle("������ѯ");
		
		initAnimation();
		initSlidingMenu();
		// �����Ͻ�ͼ�����߼���һ�����ص�ͼ�� 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		 // �����Ͻ�ͼ�����߼���һ�����ص�ͼ�� 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		proDialog = new ProgressDialog(SubscribeList.this);
		proDialog.setMessage("���ڻ�ȡ���ݣ����Ժ�...");
		proDialog.show();
		
		//��ȡ��ʷ����
		if(getIntent().getExtras().getString("type").equals("owner")){
			String objId = getIntent().getExtras().getString("objId");
			getOwnerSpaceInfo(objId);
		}else{
			String objId = getIntent().getExtras().getString("objId");
			getDriverSpaceInfo(objId);
		}
	}
	
	
	private class SpaceListAdapter extends BaseAdapter{
		public SpaceListAdapter(){
			super();
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(SubscribeList.this, R.layout.activity_history_infoitem, null);
			TextView number = (TextView)convertView.findViewById(R.id.number);
			TextView starttime = (TextView)convertView.findViewById(R.id.start_time_item);
			TextView endtime = (TextView)convertView.findViewById(R.id.end_time_item);
			number.setText("��λ" + list.get(position).getNumber());
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String start = sdf.format(list.get(position).getStart());
			starttime.setText(start);
			String end = sdf.format(list.get(position).getEnd());
			endtime.setText(end);
			return convertView;
		}
		
	}
	
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0x00){
				proDialog.dismiss();
				Log.e("SubscribeList", "�ɹ���ȡ����");
				mListView = (ListView)findViewById(R.id.listView1);
				//�������б��ճ�λ�Ž�������
				Comparator<SpaceHistoryBean> comparator = new Comparator<SpaceHistoryBean>(){
					   public int compare(SpaceHistoryBean s1, SpaceHistoryBean s2) {
						   return s1.getDate().compareTo(s2.getDate());
					   }
				};
				Collections.sort(list,comparator);
				mListView.setAdapter(new SpaceListAdapter());
				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(SubscribeList.this, SubscribeInformation.class);
						Bundle bun = new Bundle();
						bun.putInt("index", position);
						intent.putExtras(bun);
						startActivity(intent);
					}
				});
			}else{
				proDialog.dismiss();
			}
		}
	};
	
	/**
	 * ��ȡ��λ����ʷ����
	 */
	public void getOwnerSpaceInfo(final String ownerid) {
		if(!list.isEmpty())
			list.clear();
		AVQuery<AVObject> query = new AVQuery<AVObject>("SpaceInfo");
		query.whereEqualTo("ownerid", ownerid);
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
					final int spacesize = arg0.size();
					for(int k = 0; k < spacesize; k++) {
						final int j = k;
						final String spaceid = arg0.get(k).getObjectId();
						AVQuery<AVObject> query = new AVQuery<AVObject>("BookSpaceLog");
						query.whereEqualTo("spaceid", spaceid);
						query.findInBackground(new FindCallback<AVObject>() {
							@Override
							public void done(List<AVObject> arg0, AVException e) {
								if (e == null) {
									final int size = arg0.size();
									Log.e(TAG, ""+size);
									sum += size;
									for(int i = 0; i < size; i++) {
										AVObject obj = arg0.get(i);
										final SpaceHistoryBean bean = new SpaceHistoryBean();
										//���ó�λ��
										bean.setNumber(obj.getNumber("number").intValue());
										//���óɽ��۸�
										bean.setCost(obj.getNumber("cost").intValue());
										bean.setFine(obj.getNumber("fine").intValue());
										//����ʱ��
										bean.setDate(obj.getCreatedAt());
										bean.setStart(obj.getDate("start"));
										bean.setEnd(obj.getDate("end"));
										final String communityid = obj.getString("communityid");
										final String driverid = obj.getString("driverid");
										AVQuery<AVObject> query01 = new AVQuery<AVObject>("Community");
										query01.whereEqualTo("objectId", communityid);
										query01.findInBackground(new FindCallback<AVObject>() {
											@Override
											public void done(List<AVObject> arg0, AVException e) {
											if (e == null) {
												AVObject obj = arg0.get(0);
												String address = obj.getString("address");
												bean.setCommunityname(address);
												AVQuery<AVObject> query03 = new AVQuery<AVObject>("OwnerInfo");
												query03.whereEqualTo("objectId", ownerid);
												query03.findInBackground(new FindCallback<AVObject>() {
												@Override
												public void done(List<AVObject> arg0, AVException e) {
													if (e == null) {
															AVObject owner = arg0.get(0);
															bean.setOwnerphone(owner.getString("phone"));
															//��ѯ�������ֻ���
															AVQuery<AVObject> dealCount = new AVQuery<AVObject>("DriverInfo");
															dealCount.whereEqualTo("objectId", driverid);
															dealCount.findInBackground(new FindCallback<AVObject>() {
																@Override
																public void done(List<AVObject> arg0, AVException e) {
																	if (e == null) {
																		AVObject driver = arg0.get(0);
																		bean.setDriverphone(driver.getString("phone"));
																		list.add(bean);
																		if(j == spacesize - 1 && list.size() == sum) {
																			Message msg = new Message();
																			msg.what = 0x00;
																			handler.sendMessage(msg);
																		}
																		
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
									
								}
							}
						});
						}
					}
			}
		});
	}
	
	/**
	 * ��ȡ������ʷ����
	 */
	public void getDriverSpaceInfo(final String driverid) {
		if(!list.isEmpty())
			list.clear();
		AVQuery<AVObject> query = new AVQuery<AVObject>("BookSpaceLog");
		query.whereEqualTo("driverid", driverid);
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
					final int size = arg0.size();
					Log.e(TAG, ""+size);
					for(int i = 0; i < size; i++) {
						final int j = i;
						AVObject obj = arg0.get(i);
						final SpaceHistoryBean bean = new SpaceHistoryBean();
						//���ó�λ��
						bean.setNumber(obj.getNumber("number").intValue());
						//���óɽ��۸�
						bean.setCost(obj.getNumber("cost").intValue());
						bean.setFine(obj.getNumber("fine").intValue());
						//����ʱ��
						bean.setDate(obj.getCreatedAt());
						bean.setStart(obj.getDate("start"));
						bean.setEnd(obj.getDate("end"));
						final String communityid = obj.getString("communityid");
						final String spaceid = obj.getString("spaceid");
						final String driverid = obj.getString("driverid");
						AVQuery<AVObject> query01 = new AVQuery<AVObject>("Community");
						query01.whereEqualTo("objectId", communityid);
						query01.findInBackground(new FindCallback<AVObject>() {
							@Override
							public void done(List<AVObject> arg0, AVException e) {
								if (e == null) {
									AVObject obj = arg0.get(0);
									String address = obj.getString("address");
									bean.setCommunityname(address);
									
									AVQuery<AVObject> query02 = new AVQuery<AVObject>("SpaceInfo");
									query02.whereEqualTo("objectId", spaceid);
									query02.findInBackground(new FindCallback<AVObject>() {
										@Override
										public void done(List<AVObject> arg0, AVException e) {
											if (e == null) {
													AVObject space = arg0.get(0);
													final String ownerid = space.getString("ownerid");
													AVQuery<AVObject> query03 = new AVQuery<AVObject>("OwnerInfo");
													query03.whereEqualTo("objectId", ownerid);
													query03.findInBackground(new FindCallback<AVObject>() {
													@Override
													public void done(List<AVObject> arg0, AVException e) {
														if (e == null) {
																AVObject owner = arg0.get(0);
																bean.setOwnerphone(owner.getString("phone"));
																//��ѯ�������ֻ���
																AVQuery<AVObject> dealCount = new AVQuery<AVObject>("DriverInfo");
																dealCount.whereEqualTo("objectId", driverid);
																dealCount.findInBackground(new FindCallback<AVObject>() {
																	@Override
																	public void done(List<AVObject> arg0, AVException e) {
																		if (e == null) {
																			AVObject driver = arg0.get(0);
																			bean.setDriverphone(driver.getString("phone"));
																			list.add(bean);
																			if(list.size() == size) {
																				Message msg = new Message();
																				msg.what = 0x00;
																				handler.sendMessage(msg);
																			}
																			
																		} else {
																			
																		}
																	}
																});
														} else {
															
														}
													}
												});
											}else{
												
											}
										}
									});
								}else {
									
								}
							}
						});
					}
					
				} else {
					
				}
			}
		});
		
	}
	
	
	public static ArrayList<SpaceHistoryBean> getList() {
		return list;
	}

	public static void setList(ArrayList<SpaceHistoryBean> list) {
		SubscribeList.list = list;
	}

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




