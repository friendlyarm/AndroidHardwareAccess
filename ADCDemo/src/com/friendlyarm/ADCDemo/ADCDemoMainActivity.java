package com.friendlyarm.ADCDemo;
import java.util.ArrayList;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.friendlyarm.AndroidSDK.HardwareControler;

public class ADCDemoMainActivity extends Activity {

	MyCustomAdapter dataAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adc_main);
		Log.d("ADCDemo", "BoardID: " + boardType);
		timer.schedule(task, 0, 500);
	}

	private int boardType = HardwareControler.getBoardType();
	private Timer timer = new Timer();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			// Array list of countries
			ArrayList<ADC> adcValueList = new ArrayList<ADC>();

			switch (msg.what) {
			case 1:
				if (boardType == HardwareControler.S3C6410_COMMON) {
					int[] channels = { 0, 1, 4, 5, 6, 7 };
					int[] adc = HardwareControler.readADCWithChannels(channels);

					for (int i = 0; i < 6; i++) {
						ADC adcObj = new ADC(adc[i], String.format("[AIN%d]",
								channels[i]));
						adcValueList.add(adcObj);
					}

				} else if (boardType == HardwareControler.S5PV210_COMMON) {
					int[] channels = { 0, 1, 6, 7, 8, 9 };
					int[] adc = HardwareControler.readADCWithChannels(channels);

					for (int i = 0; i < 6; i++) {
						ADC adcObj = new ADC(adc[i], String.format("[AIN%d]",
								channels[i]));
						adcValueList.add(adcObj);
					}

				} else if (boardType == HardwareControler.S5P4412_COMMON) {
					int[] channels = { 0, 1, 2, 3 };
					int[] adc = HardwareControler.readADCWithChannels(channels);

					for (int i = 0; i < 4; i++) {
						ADC adcObj = new ADC(adc[i], String.format("[AIN%d]",
								channels[i]));
						adcValueList.add(adcObj);
					}
					
				} else if (boardType == HardwareControler.Smart4418SDK) {
					int[] channels = { 1, 3, 4, 5, 6, 7 };
					int[] adc = HardwareControler.readADCWithChannels(channels);

					for (int i = 0; i < channels.length; i++) {
						ADC adcObj = new ADC(adc[i], String.format("[AIN%d]",
								channels[i]));
						adcValueList.add(adcObj);
					}
				} else {
					int adc = HardwareControler.readADCWithChannel(0);
					ADC adcObj = new ADC(adc, String.format("[AIN%d]", 0));
					adcValueList.add(adcObj);
				}

				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.adc_listview_item, adcValueList);
				ListView listView = (ListView) findViewById(R.id.listView1);
				listView.setAdapter(dataAdapter);

				break;
			}
			super.handleMessage(msg);
		}
	};
	private TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	

	private class MyCustomAdapter extends ArrayAdapter<ADC> {

		private ArrayList<ADC> adcValueList;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<ADC> adcValueList) {
			super(context, textViewResourceId, adcValueList);
			this.adcValueList = new ArrayList<ADC>();
			this.adcValueList.addAll(adcValueList);
		}

		private class ViewHolder {
			TextView nameTextView;
			TextView valueTextView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.adc_listview_item, null);

				holder = new ViewHolder();
				
				holder.nameTextView = (TextView) convertView
						.findViewById(R.id.listTextView1);
				
				holder.valueTextView = (TextView) convertView
						.findViewById(R.id.listTextView2);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ADC adcObj = adcValueList.get(position);
			if (holder.nameTextView != null) {
				holder.nameTextView.setText(adcObj.getName());
			}
			if (holder.valueTextView != null) {
				holder.valueTextView.setText(String.valueOf(adcObj.getValue()));
			}
			
			return convertView;

		}

	}
}