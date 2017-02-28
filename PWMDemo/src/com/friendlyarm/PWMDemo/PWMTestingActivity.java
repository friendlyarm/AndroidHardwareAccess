package com.friendlyarm.PWMDemo;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.friendlyarm.PWMDemo.R;
import com.friendlyarm.Utils.CommonFuncs;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class PWMTestingActivity extends Activity implements OnClickListener {
	HardwareControler hw;
	EditText textEditor;
	Button btnSub;
	Button btnAdd;
	
	final int smart4418PWMGPIOPin = 97; // 97, 77, 78
	private int boardType = HardwareControler.getBoardType();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwmtestingactivity);
        
        btnSub = ((Button)findViewById(R.id.btnSub));
        btnAdd = ((Button)findViewById(R.id.btnAdd));
        
    	btnSub.setOnClickListener(this);
    	btnAdd.setOnClickListener(this);

        textEditor = ((EditText)findViewById(R.id.fEditor));
    	hw = new HardwareControler();
    }

    private void playPWM()
    {
        int f;
        f = Integer.valueOf(textEditor.getText().toString()).intValue();
        
        if (boardType == HardwareControler.Smart4418SDK) {
            if (hw.PWMPlayEx(smart4418PWMGPIOPin, f) != 0) {
                CommonFuncs.showAlertDialog(this,"Fail to play!");
            }
        } else {
            if (hw.PWMPlay(f) != 0) {
                CommonFuncs.showAlertDialog(this,"Fail to play!");
            }
        }
    }
    
    private void stopPWM()
    {
        if (boardType == HardwareControler.Smart4418SDK) {
        	if (hw.PWMStopEx(smart4418PWMGPIOPin) != 0) {
    			CommonFuncs.showAlertDialog(this,"Fail to stop!");
    		}
        } else {
        	if (hw.PWMStop() != 0) {
    			CommonFuncs.showAlertDialog(this,"Fail to stop!");
    		}
        }
    }
    
	public void onClick(View v) {
		int f;
		switch (v.getId()) {
    	case R.id.btnSub:
    		f = Integer.valueOf(textEditor.getText().toString()).intValue() - 100;
    		if (f < 1) {
    			f = 1;
    		}
    		textEditor.setText( "" + f );
            playPWM();
    		break;
    	case R.id.btnAdd:
    		f = Integer.valueOf(textEditor.getText().toString()).intValue() + 100;
    		if (f < 1) {
    			f = 1;
    		}
    		textEditor.setText( "" + f );
            playPWM();
    		break;
		default:
			break;
		}
	}
	
	@Override
    public void onStart() {
		super.onStart();
	}

	@Override
    public void onRestart() {
		super.onRestart();
	}

	@Override
    public void onResume() {
		super.onResume();
		playPWM();
	}

	@Override
    public void onPause() {
		stopPWM();
		super.onPause();
	}

	@Override
    public void onStop() {
		stopPWM();
		super.onStop();
	}

	@Override
    public void onDestroy() {
		stopPWM();
		super.onDestroy();
	}
}
