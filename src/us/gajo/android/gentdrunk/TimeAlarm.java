package us.gajo.android.gentdrunk;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeAlarm extends BroadcastReceiver {


	 
	 @Override
	 public void onReceive(Context context, Intent intent) {
		// getNewService gns = new getNewService;
		 context.startService(new Intent(context, getNewService.class));

	  
	 }
	 

	 

	}