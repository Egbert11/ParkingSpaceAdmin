package turnover;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.zhcs.parkingspaceadmin.R;

/**
 * 
 * @author huangjiaming
 * 每日交易额显示
 */
public class TurnoverDay extends Activity{
	private TextView moneyText = null, moneyShow = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_turnover_show_day);
		
		moneyText = (TextView)findViewById(R.id.turnover_day_text);
		moneyShow = (TextView)findViewById(R.id.turnover_day);
		
		moneyText.setText(getIntent().getExtras().getString("day")+"当天交易额：");
		moneyShow.setText(getIntent().getExtras().getInt("money")+"元");
	}
}
