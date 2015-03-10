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
 * 删除小区
 */
public class RemoveCommunity extends Activity{
	/**
	 *  MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 *  用MapController完成地图控制 
	 */
	private MapController mMapController = null;
	/**
	 *  MKMapViewListener 用于处理地图事件回调
	 */
	MKMapViewListener mMapListener = null;
	
	String tag = "删除小区地图显示";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 使用地图sdk前需先初始化BMapManager.
         * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
         * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
         */
        BaiduMapApplication app = (BaiduMapApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.mBMapManager.init(new BaiduMapApplication.MyGeneralListener());
        }
        /**
          * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
          */
        setContentView(R.layout.activity_community_remove);
        /*
         * 获取小区信息
         */
        AVQuery<AVObject> query = new AVQuery<AVObject>("Community");
        query.whereEqualTo("isValid", true);
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
		            int size = arg0.size();
		            if(size > 0) {
		            	//保存小区信息
		            	Toast.makeText(RemoveCommunity.this, ""+arg0.size(), Toast.LENGTH_SHORT).show();
		            	Community.setList(arg0);
		            }
		            initCommunity();
		        } else {
		            Log.d("失败", "查询错误: " + e.getMessage());
		        }
			}
		});
        mMapView = (MapView)findViewById(R.id.bmapView);
        /**
         * 获取地图控制器
         */
        mMapController = mMapView.getController();
        /**
         *  设置地图是否响应点击事件  .
         */
        mMapController.enableClick(false);
        /**
         * 设置地图缩放级别
         */
        mMapController.setZoom(12);
       
        /**
         * 将地图移动至指定点
         * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index.html查询地理坐标
         * 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
         */
        GeoPoint p ;
        Intent  intent = getIntent();
        if ( intent.hasExtra("x") && intent.hasExtra("y") ){
        	//当用intent参数时，设置中心点为指定点
        	Bundle b = intent.getExtras();
        	p = new GeoPoint(b.getInt("y"), b.getInt("x"));
        }else{
        	p =new GeoPoint((int)(23.078304* 1E6),(int)(113.402161* 1E6));
        }
        
        mMapController.setCenter(p);
        
        /**
         * 显示小区车位信息
         */
//        initCommunity();
        
        /**
    	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
    	 */
        mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调
				 * 缩放，平移等操作完成后，此回调被触发
				 */
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件
				 * 显示底图poi名称并移动至该点
				 * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
				 * 
				 */
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
				 *  可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}
            /**
             * 在此处理地图载完成事件 
             */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(RemoveCommunity.this, 
						       "地图加载完成", 
						       Toast.LENGTH_SHORT).show();
				
			}
		};
		mMapView.regMapViewListener(BaiduMapApplication.getInstance().mBMapManager, mMapListener);
    }
    
    /**
     * 在地图上显示小区maker
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
        			"地址："+list.get(i).getAddress(), String.valueOf(i));
        	overlayItem.setMarker(drawable);
        	overlay.addItem(overlayItem);
    	}
    	mMapView.getOverlays().clear();
        mMapView.getOverlays().add(overlay);
        mMapView.refresh();
    }
}
