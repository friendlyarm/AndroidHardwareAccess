package com.friendlyarm.GPIODemo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemProperties;
import android.widget.AdapterView.OnItemClickListener;

import com.friendlyarm.AndroidSDK.GPIOEnum;
import com.friendlyarm.AndroidSDK.HardwareControler;

public class GPIODemoMainActivity extends Activity {
	
	private static final String TAG = "GPIODemo";
	private Timer timer = new Timer();
	private int step = 0; 
	private Map<String, Integer> demoGPIOPins = new HashMap<String, Integer>();  
	
	static int STEP_INIT_GPIO_DIRECTION = 1;
	static int STEP_SETALLPIN_TO_HIGH = 2;
	static int STEP_INIT_VIEW = 3;
	
	@Override
		public void onDestroy() {
			timer.cancel();
			super.onDestroy();
		}


	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					timer.cancel();
					// Generate list View from ArrayList
					displayListView();
					break;
			}
			super.handleMessage(msg);
		}
	};

	private TimerTask init_task = new TimerTask() {
		public void run() {
			Log.d(TAG, "init_task " + step);
			if (step == STEP_INIT_GPIO_DIRECTION) {
				for (Integer value: demoGPIOPins.values()) {
					if (HardwareControler.setGPIODirection(value, GPIOEnum.OUT) == 0) {
					} else {
						Log.v("TimerTask", String.format("setGPIODirection(%d) failed", value));
					}
				}

				step ++;
			} else if (step == STEP_SETALLPIN_TO_HIGH) {
				for (Integer value: demoGPIOPins.values()) {
					if (HardwareControler.setGPIOValue(value, GPIOEnum.HIGH) == 0) {
					} else {
						Log.v("TimerTask", String.format("setGPIOValue(%d) failed", value));
					}
				}

				step ++;
			} else if (step == STEP_INIT_VIEW) {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}

		}
	}; 
	//////////////////////////////////////

	MyCustomAdapter dataAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gpiodemo_main);
		setTitle("GPIO Demo");

		int boardType = HardwareControler.getBoardType();
		if (boardType == HardwareControler.Smart4418SDK) {
			demoGPIOPins.put("GPIOC9", 73);
			demoGPIOPins.put("GPIOC10", 74);
			demoGPIOPins.put("GPIOC11", 75);
			demoGPIOPins.put("GPIOC12", 76);
		} else if (boardType == HardwareControler.NanoPC_T2 || boardType == HardwareControler.NanoPC_T3) {
			demoGPIOPins.put("Pin17", 68);
			demoGPIOPins.put("Pin18", 71);
			demoGPIOPins.put("Pin19", 72);
			demoGPIOPins.put("Pin20", 88);
			demoGPIOPins.put("Pin21", 92);
			demoGPIOPins.put("Pin22", 58);
		} else if (SystemProperties.get("ro.build.version.release").contains("4.1.2")) {
			demoGPIOPins.put("LED 1", 281);
			demoGPIOPins.put("LED 2", 282);
			demoGPIOPins.put("LED 3", 283);
			demoGPIOPins.put("LED 4", 284);
		} else {
			demoGPIOPins.put("LED 1", 79);
			demoGPIOPins.put("LED 2", 80);
			demoGPIOPins.put("LED 3", 81);
			demoGPIOPins.put("LED 4", 82);	
		}
		
		// export all pins
		for (Integer value: demoGPIOPins.values()) {
			if (HardwareControler.exportGPIOPin(value) == 0) {
			} else {
				Toast.makeText(this, String.format("exportGPIOPin(%d) failed!", value),
						Toast.LENGTH_SHORT).show();
			}
		}

		step = STEP_INIT_GPIO_DIRECTION;
		timer.schedule(init_task, 100, 100);
	}

	private void displayListView() {
		Log.d(TAG, "displayListView");

		ArrayList<GPIOPin> pinList = new ArrayList<GPIOPin>();
		
		for (Map.Entry<String, Integer> entry : demoGPIOPins.entrySet()) {  
			GPIOPin pin = new GPIOPin(entry.getValue(), entry.getKey(), false);
			pinList.add(pin);
		}  

		dataAdapter = new MyCustomAdapter(this, R.layout.checkbox_listview_item,
				pinList);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GPIOPin pin = (GPIOPin) parent.getItemAtPosition(position);
				if (HardwareControler.setGPIOValue(pin.code,
					pin.isSelected()?GPIOEnum.LOW:GPIOEnum.HIGH) == 0) {
				} else {
					Log.v(TAG, String.format("setGPIOValue(%d) failed", pin.code));
				}
			}
		});

	}

	private class MyCustomAdapter extends ArrayAdapter<GPIOPin> {
		private ArrayList<GPIOPin> pinList;
		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<GPIOPin> pinList) {
			super(context, textViewResourceId, pinList);
			this.pinList = new ArrayList<GPIOPin>();
			this.pinList.addAll(pinList);
		}

		private class ViewHolder {
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v(TAG, String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.checkbox_listview_item, null);

				holder = new ViewHolder();
				holder.name = (CheckBox) convertView
						.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						GPIOPin pin = (GPIOPin) cb.getTag();
						pin.setSelected(cb.isChecked());
						if (HardwareControler.setGPIOValue(pin.code,
								pin.isSelected() ? GPIOEnum.LOW:GPIOEnum.HIGH) == 0) {
						} else {
							Log.v(TAG, String.format("setGPIOValue(%d) failed", pin.code));
						}
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			GPIOPin pin = pinList.get(position);
			holder.name.setText(pin.getName());
			holder.name.setChecked(pin.isSelected());
			holder.name.setTag(pin);

			return convertView;

		}

	}
}
