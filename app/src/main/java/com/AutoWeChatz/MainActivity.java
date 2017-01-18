package com.AutoWeChatz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;



public class MainActivity extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		final EditText Name1 =(EditText)findViewById(R.id.editText1);
		final EditText Name2 =(EditText)findViewById(R.id.editText2); 
		final EditText Name3 =(EditText)findViewById(R.id.editText3); 
		final EditText Name4 =(EditText)findViewById(R.id.editText4); 
		final EditText Name5 =(EditText)findViewById(R.id.editText5); */
		

	     final EditText editText1 =(EditText)findViewById(R.id.editText1); 
	     final EditText editText2 =(EditText)findViewById(R.id.editText1); 
	     final EditText editText3 =(EditText)findViewById(R.id.editText1); 
	     final EditText editText4 =(EditText)findViewById(R.id.editText1); 
	     final EditText editText5 =(EditText)findViewById(R.id.editText1); 

		findViewById(R.id.Start_AcceServ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	/*
            	Parameters.name1=Name1.getText().toString(); 
        		Parameters.name2=Name2.getText().toString();
        		Parameters.name3=Name3.getText().toString();
        		Parameters.name4=Name4.getText().toString();
        		Parameters.name5=Name5.getText().toString();
            	*/
            	Parameters.name1 =editText1.getText().toString();
            	Parameters.name2 =editText2.getText().toString();
            	Parameters.name3 =editText3.getText().toString();
            	Parameters.name4 =editText4.getText().toString();
            	Parameters.name5 =editText5.getText().toString();
            	Log.w("TEXT", Parameters.name1);
                open();
            }
        });	
	}

	private void open() {
        try {
        	
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "找到Automatic to WeChat，然后开启服务即可", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
