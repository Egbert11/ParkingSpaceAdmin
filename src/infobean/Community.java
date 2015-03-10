package infobean;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVObject;
/**
 * 
 * @author huangjiaming
 * 记录小区列表以及业主发布车位时的小区序号
 */
public class Community {
	//小区列表
	public static ArrayList<CommunityInfoBean> list = new ArrayList<CommunityInfoBean>();

	public static ArrayList<CommunityInfoBean> getList() {
		return list;
	}

	public static void setList(ArrayList<CommunityInfoBean> list) {
		Community.list = list;
	}
	
	public static void setList(List<AVObject> arg0) {
		if(!Community.list.isEmpty())
			Community.list.clear();
		for(int i = 0; i < arg0.size(); i++) {
        	CommunityInfoBean bean = new CommunityInfoBean();
        	bean.setCommunityId(arg0.get(i).getObjectId());
        	bean.setAddress(arg0.get(i).getString("address"));
        	bean.setLatitude(arg0.get(i).getNumber("latitude"));
        	bean.setLongitude(arg0.get(i).getNumber("longitude"));
        	Community.list.add(bean);
        }
	}
}
