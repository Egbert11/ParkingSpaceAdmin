package turnover;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.zhcs.parkingspaceadmin.R;

/**
 * 
 * @author huangjiaming
 * 总交易额显示
 */
public class TurnoverSum extends Activity{
	private TextView moneyShow = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_turnover_show_sum);
		
		moneyShow = (TextView)findViewById(R.id.turnover_sum);
		
		getTurnoverSum();
	}
	
	/**
	 * 获取总交易额
	 */
	private void getTurnoverSum(){
		AVQuery<AVObject> query = new AVQuery<AVObject>("BookSpaceLog");
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
					final int spacesize = arg0.size();
					int moneyCount = 0;
					for(int i = 0; i < spacesize; i++){
						moneyCount += arg0.get(i).getInt("cost");
					}
					moneyShow.setText(moneyCount+"元");
				}
			}
		});
	}
	
}