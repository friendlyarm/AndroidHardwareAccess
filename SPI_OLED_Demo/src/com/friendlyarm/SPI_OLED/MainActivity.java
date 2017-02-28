package com.friendlyarm.SPI_OLED;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.friendlyarm.SPI_OLED.R;
import com.friendlyarm.SPI_OLED.OLED;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "SPILCD";
	private EditText toEditor = null;
	
    private final String devName = "/dev/spidev0.0";   /*For S5P4418*/
    private final int gpioPin_For_DC = 75;   			/*GPIOC11 on Smart4418*/
    private final int gpioPin_For_Reset = 74;          /*GPIOC10 on Smart4418*/
    OLED oled;

    private Timer timer = new Timer();
    private Timer timer2 = new Timer();
    private int spi_init_step = 0; 
    
    @Override
    public void onDestroy() {
    	timer.cancel();
    	timer2.cancel();
    	
    	HardwareControler.unexportGPIOPin( gpioPin_For_DC );
    	HardwareControler.unexportGPIOPin( gpioPin_For_Reset );
    	oled.Deinit();
    	
    	super.onDestroy();
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
		setTitle("SPI OLED");

        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        ((Button)findViewById(R.id.cleanButton)).setOnClickListener(this);
       
        toEditor = (EditText)findViewById(R.id.toEditor);
        toEditor.setText("ABCDEFGHIJKOMNOPabcdefghijkomnop1234567890123456,./<>?;':\"[]{}*@");
        
        
        
        /* no focus when begin */
        toEditor.clearFocus();
        toEditor.setFocusable(false);
        toEditor.setFocusableInTouchMode(true);
        

        oled = new OLED();
		if (HardwareControler.exportGPIOPin( gpioPin_For_DC ) == 0 
				&& HardwareControler.exportGPIOPin( gpioPin_For_Reset ) == 0) {
			
			Log.d(TAG, "exportGPIOPin ok");
			
			/*
			 * 1->set direction gpio  
			 * 2->set gpio value  
			 * 3->unexport  
			 * >3 quit timer
			 */
			
			spi_init_step = 1;  
			timer.schedule(init_task, 100, 100); 
		} else {
			Toast.makeText(this,"exportGPIOPin failed!",Toast.LENGTH_SHORT).show();
		}
    }
    
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				timer.cancel();
				break;
			case 2:
				timer2.cancel();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void quitTimer(int timerId) {
		Message message = new Message();
		message.what = timerId;
		handler.sendMessage(message);
	}
    
	private TimerTask init_task = new TimerTask() {
		public void run() {
			if (spi_init_step == 1) {
				if (oled.Init(devName, gpioPin_For_DC, gpioPin_For_Reset) == 0) {
					spi_init_step ++;
				} else {
					quitTimer(1);
				}
			} else if (spi_init_step == 2) {
				spi_init_step ++;
				quitTimer(1);
				timer2.schedule(flash_text_task, 1000, 1000);
			}
		}
	}; 
	
	public static String[] splitByByteSize(String content, int size){
	    byte[] bytes = content.getBytes();
	    int totalSize = bytes.length;
	    int partNum = 0;
	    if(totalSize == 0){
	        return new String[0];
	    }
	    if(totalSize % size == 0){
	        partNum = totalSize / size;
	    }else{
	        partNum = totalSize / size + 1;
	    }
	    String[] arr = new String[partNum];
	    int arrIndex = -1;
	    for(int i=0;i<totalSize;i++){
	        if(i%size == 0){
	            arrIndex++;
	            arr[arrIndex] = "";
	        }
	        arr[arrIndex]+=((char)bytes[i]);
	    }
	    return arr;

	}

	private TimerTask flash_text_task = new TimerTask() {
		public void run() {
			Log.d(TAG, "Enter flash_text_task");
			final int MAX_CHATS_PER_LINE = 16;
			final int MAX_LINE = 4;
			String displayText = toEditor.getText().toString();
			//clear
			if (oled.OLEDCleanScreen() != 0) {
				Log.e(TAG, "oled.OLEDCleanScreen failed.");
			} else {
				Log.e(TAG, "oled.OLEDCleanScreen ok.");
			}
			if (displayText.length() > 0) {
				Log.e(TAG, "need display text: " + displayText);
				String[] lines = splitByByteSize(displayText, MAX_CHATS_PER_LINE);
				for (int i=0; i<MAX_LINE && i<lines.length; i++) {
					Log.e(TAG, "display line: " + i + ", text: " + lines[i]);
					if (oled.OLEDDisp8x16Str(0, i*MAX_CHATS_PER_LINE, lines[i].getBytes()) != 0) {
						Log.e(TAG, "oled.OLEDDisp8x16Str failed.");
					} else {
						Log.e(TAG, "oled.OLEDDisp8x16Str ok.");
					}
				}
			}
			quitTimer(2);
			Log.d(TAG, "Leave flash_text_task");
			
		}
	};
    
    public void onClick(View v)
    {
    	switch (v.getId()) {
    	case R.id.backButton:
    		finish();
    		break;
    	case R.id.cleanButton:
    		toEditor.setText("");
    		break;
    	}
    }
}
