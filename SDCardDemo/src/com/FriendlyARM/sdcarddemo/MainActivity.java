package com.FriendlyARM.sdcarddemo;

import java.io.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.os.Environment;

public class MainActivity extends Activity {
	private static final String tag="SDCardDemo";
	EditText txtData;
	Button btnWriteSDFile;
	Button btnReadSDFile;
	Button btnClearScreen;
	Button btnClose;

	private static final String sdRootPath = "/sdcard";
	private static final String appPath = "Android/com.FriendlyARM.sdcarddemo";

	public boolean createDirIfNotExists(String path) {
	    boolean ret = true;
	    File file = new File(path);
	    if (!file.exists()) {
	        if (!file.mkdirs()) {
		     Toast.makeText(getBaseContext(),
                                                        "Create dir failed: "+ path,
                                                        Toast.LENGTH_SHORT).show();
	            ret = false;
	        }
	    }
	    return ret;
	}

	public String getAppPathOnSDCard()
	{
		return sdRootPath + "/" + appPath;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createDirIfNotExists(getAppPathOnSDCard());
		File file = new File(getAppPathOnSDCard());
		if (!file.exists()) {
			Toast.makeText(getBaseContext(),
                                "Path not found: "+ getAppPathOnSDCard(),
                                Toast.LENGTH_SHORT).show();	
		}

		// bind GUI elements with local controls
		txtData = (EditText) findViewById(R.id.txtData);
		txtData.setHint("Enter some lines of data here, the text will write to: " +getAppPathOnSDCard() + "/mysdfile.txt");

		btnWriteSDFile = (Button) findViewById(R.id.btnWriteSDFile);
		btnWriteSDFile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// write on SD card file data in the text box
				try {
					if (txtData.getText().length()==0) {
						Toast.makeText(getBaseContext(),
							"Please type something in the textbox first.",
							Toast.LENGTH_SHORT).show();
						return ;
					}
					File myFile = new File(getAppPathOnSDCard() + "/mysdfile.txt");
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = 
											new OutputStreamWriter(fOut);
					myOutWriter.append(txtData.getText());
					myOutWriter.close();
					fOut.close();
					Toast.makeText(getBaseContext(),
							"Done writing SD 'mysdfile.txt'",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}// onClick
		}); // btnWriteSDFile

		btnReadSDFile = (Button) findViewById(R.id.btnReadSDFile);
		btnReadSDFile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// write on SD card file data in the text box
				try {
					File myFile = new File(getAppPathOnSDCard() + "/mysdfile.txt");
					FileInputStream fIn = new FileInputStream(myFile);
					BufferedReader myReader = new BufferedReader(
							new InputStreamReader(fIn));
					String aDataRow = "";
					String aBuffer = "";
					while ((aDataRow = myReader.readLine()) != null) {
						aBuffer += aDataRow + "\n";
					}
					txtData.setText(aBuffer);
					myReader.close();
					Toast.makeText(getBaseContext(),
							"Done reading SD 'mysdfile.txt'",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}// onClick
		}); // btnReadSDFile

		btnClearScreen = (Button) findViewById(R.id.btnClearScreen);
		btnClearScreen.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// clear text box
				txtData.setText("");
			}
		}); // btnClearScreen

		btnClose = (Button) findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// clear text box
				finish();
			}
		}); // btnClose
	}// onCreate
}
