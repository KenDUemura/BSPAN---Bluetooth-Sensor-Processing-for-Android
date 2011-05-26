package com.t2;





import com.t2.SpineReceiver.BioFeedbackData;
import com.t2.SpineReceiver.BioFeedbackStatus;
import com.t2.SpineReceiver.OnBioFeedbackMessageRecievedListener;
import com.t2.biofeedback.Constants;

import spine.SPINEFactory;
import spine.SPINEManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AndroidSpineServerMainActivity extends Activity implements OnBioFeedbackMessageRecievedListener {
	private static final String TAG = Constants.TAG;
    EditText mEditText;
    private static SPINEManager manager;
	private SpineReceiver receiver;
	private AlertDialog connectingDialog;	
    
	private void doThis()
	{
		
//	       AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//	          dialog.setTitle("");
//	          dialog.setMessage("status  is on");
//	          dialog.show();
//
//		       AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
//		          dialog1.setTitle("");
//		          dialog1.setMessage("status  is on");
//		          dialog1.show();
	          
//      // Create a connecting dialog.
//      this.connectingDialog = new AlertDialog.Builder(this)
//      	// Close the app if connecting was not finished.
//	        .setOnCancelListener(new OnCancelListener() {
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					finish();
//				}
//			})
//			// Allow the biofeedback device settings to be used.
//			.setPositiveButton("BioFeedback Settings", new OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					startActivity(new Intent("com.t2.biofeedback.MANAGER"));
//				}
//			})
//			.setMessage("Connecting...")
//			.create();		
	}
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        Resources resources = this.getResources();
        AssetManager assetManager = resources.getAssets();
        
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            	doThis();            	
            	
            	
            	
            	
            	
            	
            	
            	
            	
            	
            }
        });        
        
        
        
		// Initialize SPINE by passing the fileName with the configuration properties
		try {
			manager = SPINEFactory.createSPINEManager("SPINETestApp.properties", resources);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        
        
                
        // Create a broadcast receiver.
        this.receiver = new SpineReceiver(this);
        
        // Create a connecting dialog.
        this.connectingDialog = new AlertDialog.Builder(this)
        	// Close the app if connecting was not finished.
	        .setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			})
			// Allow the biofeedback device settings to be used.
			.setPositiveButton("BioFeedback Settings", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent("com.t2.biofeedback.MANAGER"));
				}
			})
			.setMessage("Connecting...")
			.create();
        
        
        
        
        
    }

    @Override
	protected void onDestroy() {
    	super.onDestroy();
    	this.sendBroadcast(new Intent("com.t2.biofeedback.service.STOP"));
    	this.unregisterReceiver(this.receiver);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.sendBroadcast(new Intent("com.t2.biofeedback.service.START"));
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.t2.biofeedback.service.data.BROADCAST");
		filter.addAction("com.t2.biofeedback.service.status.BROADCAST");
		this.registerReceiver(
				this.receiver, 
        		filter
		);
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.settings:
				startActivity(new Intent("com.t2.biofeedback.MANAGER"));
				return true;
		
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onDataReceived(BioFeedbackData bfmd) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Data Received" );		
		
	}


	@Override
	public void onStatusReceived(BioFeedbackStatus bfs) {
		if(bfs.messageId.equals("CONN_CONNECTING")) {
			Log.i(TAG, "Received command : CONN_CONNECTING" );		
			this.connectingDialog.show();
			
		} else if(bfs.messageId.equals("CONN_ANY_CONNECTED")) {
			Log.i(TAG, "Received command : CONN_ANY_CONNECTED" );		
			this.connectingDialog.hide();
		}
	}
	
}