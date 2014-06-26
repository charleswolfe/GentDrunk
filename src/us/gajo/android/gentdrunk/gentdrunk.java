package us.gajo.android.gentdrunk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class gentdrunk extends TabActivity {
    /** Called when the activity is first created. */
	AlarmManager am;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
       
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, PodcastActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("podcasts").setIndicator("Podcasts",
                          res.getDrawable(R.drawable.ic_tab_podcast))
                      .setContent(intent);
       tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, MoreActivity.class);
        spec = tabHost.newTabSpec("more").setIndicator("More",
                          res.getDrawable(R.drawable.ic_tab_more))
                      .setContent(intent);
        tabHost.addTab(spec);

 
       tabHost.setCurrentTab(0);

        
        // Create the adView for ads
        AdView adView = (AdView)this.findViewById(R.id.adView);
        adView.loadAd(new AdRequest());



        //CustomAlarm GDalarm = new CustomAlarm();
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        setRepeatingAlarm();
        
        
        
        
        
        
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    
    public void setRepeatingAlarm() {
    	  Intent intent = new Intent(this, TimeAlarm.class);
    	  PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
    	    intent, PendingIntent.FLAG_CANCEL_CURRENT);
    	  am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
    		10800000, pendingIntent);
    	  //10800000 should be 3 hours, 150000 about 3 minutes
    	 }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:     
						            Intent settingsActivity = new Intent(getBaseContext(), settings_pref.class);
						            startActivity(settingsActivity);
                                break;
            case R.id.about:     
						            Intent aboutActivity = new Intent(getBaseContext(), aboutActivity.class);
						            startActivity(aboutActivity);
                                break;
            
        }
        return true;
    }
    
    
}




