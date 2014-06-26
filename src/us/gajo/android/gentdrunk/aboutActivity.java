package us.gajo.android.gentdrunk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class aboutActivity extends Activity {
	

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout); 
        
        
        Button rate_this = (Button) findViewById(R.id.button1);
        rate_this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//String market_url = "market://details?id=" + getPackageName();
            	String market_url = "http://market.android.com/details?id=" + getPackageName();
            	//Log.i("fuck",market_url );
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( market_url ));
        		startActivity(browserIntent);
            }//end of onclick
        
        
        });//end of listener
        
        Button more_apps = (Button) findViewById(R.id.button2);
        more_apps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://market.android.com/developer?pub=gajo.us" ));
        		startActivity(browserIntent);
            }//end of onclick
        
        
        });//end of listener
        
        
        
        
}//end of oncreate
	
	
	
}//end of activity