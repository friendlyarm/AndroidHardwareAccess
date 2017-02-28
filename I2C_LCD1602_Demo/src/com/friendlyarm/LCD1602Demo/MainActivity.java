package com.friendlyarm.LCD1602Demo;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.friendlyarm.AndroidSDK.FileCtlEnum;
import com.friendlyarm.LCD1602Demo.R;
/* for thread */
import android.os.Message;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends Activity implements OnClickListener {
	EditText writeEditor;
	TextView statusView;
	private Handler messageHandler;
	Thread writeThread = null;
	int devFD = -1;
	final int MAX_LINE_SIZE = 16;

    private class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }
        
        @Override
        public void handleMessage(Message msg) {
        	String result = (String) msg.obj;
        	if (result.equals("ERROR")) {
        		statusView.setText("Status: Error");
        	} else if (result.equals("DONE")) {
        		statusView.setText("Status: Done");
        	} else {
        		statusView.setText("Status: Processing");
        	}
        }
    }
    
    @Override
    public void onDestroy() {
    	if (devFD >= 0) {
    	    HardwareControler.close(devFD);
    	    devFD = -1;
    	}
    	super.onDestroy();
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        
        writeEditor = (EditText) findViewById(R.id.writeEditor);
    	writeEditor.setText("Don't give up and don't give in.");
    	
    	statusView = (TextView) findViewById(R.id.statusView);
    	Button writeButton = (Button)findViewById(R.id.sendButton);
    	writeButton.setOnClickListener(this);
        
        Looper looper = Looper.myLooper();
        messageHandler = new MessageHandler(looper);
        
        devFD = HardwareControler.open("/dev/i2c-0", FileCtlEnum.O_RDWR);
        if (devFD < 0) {
        	statusView.setText("Fail to open I2C device.");
        	writeEditor.setEnabled(false);
        	writeButton.setEnabled(false);
        } else {
        	// for LCD1602 (chip: pcf8574)
        	if (HardwareControler.setI2CSlave(devFD, 0x27) < 0) {
        		statusView.setText("Fail to set I2C slave.");
            	writeEditor.setEnabled(false);
            	writeButton.setEnabled(false);
        	} else {
        		statusView.setText("Status: Ready");
        	}
        }
    }

    private int LCD1602Init() throws InterruptedException {
        Thread.sleep(0,1000*15);
        if (PCF8574.writeCmd4(devFD, (byte)(0x03 << 4)) == -1) {
            return -1;
        }
        Thread.sleep(0,100*41);
        if (PCF8574.writeCmd4(devFD, (byte)(0x03 << 4)) == -1) {
            return -1;
        }
        Thread.sleep(0,100);
        if (PCF8574.writeCmd4(devFD, (byte)(0x03 << 4)) == -1) { 
            return -1;
        }
        if (PCF8574.writeCmd4(devFD, (byte)(0x02 << 4)) == -1) {
            return -1;
        }    
        if (PCF8574.writeCmd8(devFD, (byte)(0x28)) == -1) {
            return -1;
        }
        if (PCF8574.writeCmd8(devFD, (byte)(0x0c)) == -1) {
            return -1;
        }
        Thread.sleep(0,2000);
        if (PCF8574.writeCmd8(devFD, (byte)(0x06)) == -1) {
            return -1;
        }
        if (PCF8574.writeCmd8(devFD, (byte)(0x02)) == -1) {
            return -1;
        }
        Thread.sleep(0,2000);
        return 0;
    }
    
    private int LCD1602DispStr(byte x, byte y, String str) throws InterruptedException
    {
    	byte addr;
        addr = (byte)(((y + 2) * 0x40) + x);

        if (PCF8574.writeCmd8(devFD, addr) == -1) {
            return -1;
        }
        byte[] strBytes = str.getBytes();
        byte b;

		for (int i = 0; i < strBytes.length && i<MAX_LINE_SIZE; i++) {
			b = strBytes[i];
            if (PCF8574.writeData8(devFD, b) == -1) {
                return -1;
            }
		}
        return 0;
    }

    private int LCD1602DispLines(String line1, String line2) throws InterruptedException {
        int ret = LCD1602DispStr((byte)0, (byte)0, line1);
        if (ret != -1 && line2.length() > 0) {
            ret = LCD1602DispStr((byte)0, (byte)1, line2);
        }
        return ret;
    }
    
    private int LCD1602Clear() throws InterruptedException {
        if (PCF8574.writeCmd8(devFD, (byte)0x01) == -1) { 
             return -1;
        }
        return 0;
    }
    
    private boolean lcd1602Inited = false;
    private void sendMessage(String msg) {
		Message message = Message.obtain();
		message.obj = msg;
		messageHandler.sendMessage(message);
    }
    private void startWriteEEPROMThread(final String displayText) {
    	statusView.setText("Status: Processing");
    	new Thread() {
            @Override
            public void run() {
            	if (!lcd1602Inited) {
            		try  {  
                        if (LCD1602Init() == 0) {
                        	lcd1602Inited = true;
                        }
                    } catch  (Exception e) {  
                		sendMessage("ERROR");
        				return ;
                    }
            	}

            	if (!lcd1602Inited) {
            		sendMessage("ERROR");
    				return ;
            	}
            	
            	try {
            		LCD1602Clear();
            		Thread.sleep(100, 0);
            		
            		String line1 = "";
            		String line2 = "";
            		for (int i=0; i<displayText.length(); i++) {
            			if (line1.length() >= MAX_LINE_SIZE) {
            				if (line2.length() >= MAX_LINE_SIZE) {
            					break;
            				} else {
            					line2 = line2 + displayText.charAt(i);
            				}
            			} else {
            				line1 = line1 + displayText.charAt(i);
            			}
            		}
            		
            		if (LCD1602DispLines(line1, line2) == -1) {
            			sendMessage("ERROR");
            			return ;
            		}
            	} catch (Exception e) {
            		sendMessage("ERROR");
    				return ;
            	}
				sendMessage("DONE");
            }

        }.start();
    }
    
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.sendButton:
    		startWriteEEPROMThread(writeEditor.getText().toString());
    		break;
		default:
			break;
		}
	}
	
}
