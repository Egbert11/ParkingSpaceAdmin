package common;

import turnover.TypeSelect;
import usermanage.UserinfoSearch;
import history.HistorySearch;

import com.zhcs.parkingspaceadmin.R;

import communitymanage.ModeDisplay;
import login.Login;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author huangjiaming
 *	功能描述：列表Fragment，用来显示滑动菜单打开后的内容
 */
public class SampleListFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		adapter.add(new SampleItem("小区管理", R.drawable.menu_ico5));
		adapter.add(new SampleItem("订单查询", R.drawable.menu_ico6));
		adapter.add(new SampleItem("交易额查询", R.drawable.menu_money));
		adapter.add(new SampleItem("用户管理", android.R.drawable.ic_menu_recent_history));
//		adapter.add(new SampleItem("服务条款", R.drawable.menu_ico1));
//		adapter.add(new SampleItem("软件信息", R.drawable.menu_ico2));
		adapter.add(new SampleItem("退出", R.drawable.menu_ico3));
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Class<?> act = null;
		switch (position) {
		case 0:
			act = ModeDisplay.class;
			break;
		case 1:
			act = HistorySearch.class;
			break;
		case 2:
			act = TypeSelect.class;
			break;
		case 3:
			act = UserinfoSearch.class;
			break;
//		case 4:
//			act = ShowBalance.class;
//			break;
//		case 5:
//			act = TermOfServices.class;
//			break;
//		case 6:
//			act = SoftwareInformation.class;
//			break;
		case 4:
			act = Login.class;
			break;
		}
		Intent intent = new Intent(getActivity(), act);
		startActivity(intent);
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}
}
