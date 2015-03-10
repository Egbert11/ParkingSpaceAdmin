package communitymanage;

import infobean.Community;
import infobean.CommunityInfoBean;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zhcs.parkingspaceadmin.R;

import common.BaiduMapApplication;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author huangjiaming
 * ɾ��С��
 */
public class RemoveCommunity extends Activity{
	/**
	 *  MapView �ǵ�ͼ���ؼ�
	 */
	private MapView mMapView = null;
	/**
	 *  ��MapController��ɵ�ͼ���� 
	 */
	private MapController mMapController = null;
	/**
	 *  MKMapViewListener ���ڴ����ͼ�¼��ص�
	 */
	MKMapViewListener mMapListener = null;
	
	String tag = "ɾ��С����ͼ��ʾ";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * ʹ�õ�ͼsdkǰ���ȳ�ʼ��BMapManager.
         * BMapManager��ȫ�ֵģ���Ϊ���MapView���ã�����Ҫ��ͼģ�鴴��ǰ������
         * ���ڵ�ͼ��ͼģ�����ٺ����٣�ֻҪ���е�ͼģ����ʹ�ã�BMapManager�Ͳ�Ӧ������
         */
        BaiduMapApplication app = (BaiduMapApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
             */
            app.mBMapManager.init(new BaiduMapApplication.MyGeneralListener());
        }
        /**
          * ����MapView��setContentView()�г�ʼ��,��������Ҫ��BMapManager��ʼ��֮��
          */
        setContentView(R.layout.activity_community_remove);
        /*
         * ��ȡС����Ϣ
         */
        AVQuery<AVObject> query = new AVQuery<AVObject>("Community");
        query.whereEqualTo("isValid", true);
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
		            int size = arg0.size();
		            if(size > 0) {
		            	//����С����Ϣ
		            	Toast.makeText(RemoveCommunity.this, ""+arg0.size(), Toast.LENGTH_SHORT).show();
		            	Community.setList(arg0);
		            }
		            initCommunity();
		        } else {
		            Log.d("ʧ��", "��ѯ����: " + e.getMessage());
		        }
			}
		});
        mMapView = (MapView)findViewById(R.id.bmapView);
        /**
         * ��ȡ��ͼ������
         */
        mMapController = mMapView.getController();
        /**
         *  ���õ�ͼ�Ƿ���Ӧ����¼�  .
         */
        mMapController.enableClick(false);
        /**
         * ���õ�ͼ���ż���
         */
        mMapController.setZoom(12);
       
        /**
         * ����ͼ�ƶ���ָ����
         * ʹ�ðٶȾ�γ�����꣬����ͨ��http://api.map.baidu.com/lbsapi/getpoint/index.html��ѯ��������
         * �����Ҫ�ڰٶȵ�ͼ����ʾʹ����������ϵͳ��λ�ã��뷢�ʼ���mapapi@baidu.com��������ת���ӿ�
         */
        GeoPoint p ;
        Intent  intent = getIntent();
        if ( intent.hasExtra("x") && intent.hasExtra("y") ){
        	//����intent����ʱ���������ĵ�Ϊָ����
        	Bundle b = intent.getExtras();
        	p = new GeoPoint(b.getInt("y"), b.getInt("x"));
        }else{
        	p =new GeoPoint((int)(23.078304* 1E6),(int)(113.402161* 1E6));
        }
        
        mMapController.setCenter(p);
        
        /**
         * ��ʾС����λ��Ϣ
         */
//        initCommunity();
        
        /**
    	 *  MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
    	 */
        mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * �ڴ˴����ͼ�ƶ���ɻص�
				 * ���ţ�ƽ�ƵȲ�����ɺ󣬴˻ص�������
				 */
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * �ڴ˴����ͼpoi����¼�
				 * ��ʾ��ͼpoi���Ʋ��ƶ����õ�
				 * ���ù��� mMapController.enableClick(true); ʱ���˻ص����ܱ�����
				 * 
				 */
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 *  �����ù� mMapView.getCurrentMap()�󣬴˻ص��ᱻ����
				 *  ���ڴ˱����ͼ���洢�豸
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 *  ��ͼ��ɴ������Ĳ�������: animationTo()���󣬴˻ص�������
				 */
			}
            /**
             * �ڴ˴����ͼ������¼� 
             */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(RemoveCommunity.this, 
						       "��ͼ�������", 
						       Toast.LENGTH_SHORT).show();
				
			}
		};
		mMapView.regMapViewListener(BaiduMapApplication.getInstance().mBMapManager, mMapListener);
    }
    
    /**
     * �ڵ�ͼ����ʾС��maker
     */
    private void initCommunity() {
    	Log.v(tag, ""+Community.getList().size());
    	if(Community.getList().isEmpty()) {
    		mMapView.getOverlays().clear();
            mMapView.refresh();
    		return;
    	}
    	ArrayList<CommunityInfoBean> list = Community.getList();
		Drawable drawable = getResources().getDrawable(R.drawable.icon_maker);
    	CommunityOverlay overlay = new CommunityOverlay(RemoveCommunity.this, drawable, mMapView);
    	for(int i = 0; i < list.size(); i++){
        	OverlayItem overlayItem = new OverlayItem(new GeoPoint(list.get(i).getLatitude().intValue(), list.get(i).getLongitude().intValue()), 
        			"��ַ��"+list.get(i).getAddress(), String.valueOf(i));
        	overlayItem.setMarker(drawable);
        	overlay.addItem(overlayItem);
    	}
    	mMapView.getOverlays().clear();
        mMapView.getOverlays().add(overlay);
        mMapView.refresh();
    }
}
