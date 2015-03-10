package communitymanage;

import infobean.Community;
import infobean.CommunityInfoBean;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;

/**
 * 
 * @author huangjiaming
 * 小区位置标注
 */
public class CommunityOverlay extends ItemizedOverlay<OverlayItem> {
    private static Activity activity;
    private static ArrayList<CommunityInfoBean> list;
    private static OverlayItem item;

    public CommunityOverlay(Activity activity, Drawable drawable, MapView mapView) {
        super(drawable, mapView);
        CommunityOverlay.activity = activity;
    }

    @Override
	protected boolean onTap(int i) {
        item = this.getItem(i);
        list = Community.getList();
        final String communityId = list.get(i).getCommunityId();
        new AlertDialog.Builder(activity).setTitle("删除小区").setMessage("当前小区：" + 
        list.get(i).getAddress() + ",确定删除吗？")
        		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        		        removeCommunity(communityId);
        		        Intent intent = new Intent(activity, ModeDisplay.class);
						activity.startActivity(intent);
						activity.finish();
        			};
        				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
        					
        					@Override
        					public void onClick(DialogInterface dialog, int which) {
        						dialog.dismiss();
        					}
        				}).show();
        return true;
    }
    
    /**
     * 删除小区，将小区的isValid状态设置为false
     */
    private void removeCommunity(String communityId) {
    	AVQuery<AVObject> query = new AVQuery<AVObject>("Community");
		query.whereEqualTo("objectId", communityId);
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
		    public void done(List<AVObject> avObjects, AVException e) {
		        if (e == null) {
		            AVObject mod = avObjects.get(0);
		            mod.put("isValid", false);
					mod.saveInBackground(new SaveCallback() {
						@Override
						public void done(AVException arg0) {
							if (arg0 == null) {
								Toast.makeText(activity,"小区删除成功", Toast.LENGTH_SHORT).show();
					        } else {
					        	Toast.makeText(activity,"小区删除失败", Toast.LENGTH_SHORT).show();
						}
					}
		        });
		        }
			}
		});
    }
}
