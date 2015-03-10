package history;

import com.zhcs.parkingspaceadmin.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author huangjiaming
 * 手机号无效页面
 */
public class NotFound extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_404);
		setTitle("订单查询");
	}
}
