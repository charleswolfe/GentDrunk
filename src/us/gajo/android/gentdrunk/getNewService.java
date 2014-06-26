package us.gajo.android.gentdrunk;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;



public  class getNewService extends Service {
	
	 NotificationManager nm;
	 private List<Message> messages;


	 private int old_hash=0;
	 private int new_hash=0;
	 private boolean show_new_ep = true;
	 public static final String PREFS_NAME = "GDPrefsFile";
	 private String new_title = "";
	 private Runnable getNewCount;
	 Context this_context;
	 Intent  notificationIntent;// = new Intent(this, gentdrunk.class);
	 
	    public getNewService() {
	        super();
	    } 
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // do something here
		 this_context = getApplicationContext();
		  nm = (NotificationManager) this_context
		    .getSystemService(Context.NOTIFICATION_SERVICE);

		  SharedPreferences settings = this_context.getSharedPreferences(PREFS_NAME, 0);
		  old_hash = settings.getInt("podcast_hash", 0);
	      show_new_ep = settings.getBoolean("newep_notify", true);

	      notificationIntent = new Intent(this, gentdrunk.class);
	      
			getNewCount= new Runnable(){
			    @Override
			    public void run() {
			        getNewLists();
			       
			        if( old_hash != new_hash )
		        	  {
		        		  //store new variable
		        	      SharedPreferences settings2 = this_context.getSharedPreferences(PREFS_NAME, 0);
		        	      SharedPreferences.Editor editor = settings2.edit();
		        	      editor.putInt("podcast_hash", new_hash );
		        	      //editor.putInt("podcast_count", new_podcast_count );
		        	      // Commit the edits!
		        	      editor.commit();
		        	      if( old_hash != 0   && show_new_ep)
		        	      {
		       	    	  
							  CharSequence from = "New Episode Available";
							  CharSequence message =  new_title;
							  

							  PendingIntent contentIntent = PendingIntent.getActivity(this_context, 0,
									  notificationIntent, 0);
							  Notification notif = new Notification(R.drawable.icon,
							    "New Podcast Available", System.currentTimeMillis());
							  notif.setLatestEventInfo(this_context, from, message, contentIntent);
							  notif.flags |= Notification.FLAG_AUTO_CANCEL;
							  nm.notify(1, notif);
							  
		        	      }
		        	  }//
					
			    }
			};
			Thread thread =  new Thread(null, getNewCount, "MagentoBackground");
	        thread.start();
    	
    	
    	//
    	 
    	 
        return START_STICKY;
    }
    
	 private void getNewLists()
	 {
		//might need to put in own class and run in thread
	      try{
	        	BaseFeedParser parser = new BaseFeedParser();
		    	messages = parser.parse();
		    	
		    	
		    	for (Message msg : messages){
		    		//Order o4 = new Order();
	                //o4.setOrderName(msg.getTitle());
		    		new_title = msg.getTitle();
		    		new_hash = msg.hashCode();
		    		break;
	                //m_orders.add(o4);
		    	}
		    	
		    	
		    	//Thread.sleep(5000);
	          } catch (Exception e) {
	            Log.e("BACKGROUND_PROC", e.getMessage());
	          }
	          //runOnUiThread(returnRes);
	 }
	 
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}