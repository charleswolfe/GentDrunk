package us.gajo.android.gentdrunk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;



public class MoreActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // TextView textview1 = new TextView(this);
       // textview1.setText("This is the more tab");
       // setContentView(textview1);
        
        setContentView(R.layout.more_layout);
        
        
        //probably need to remove the text view in the xml, so far it isnt hurting us since we set content view
        
        //TODO: make buttons do something when clicked maybe?
        
        
        ImageButton fb_post = (ImageButton) findViewById(R.id.imageButton1);
        fb_post.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	
            //	String fburl = "http://www.facebook.com/Gentlemandrunk";// + 
            //	r_title
            //		+"&%20link="+
            //	market_url
            //		+"&%20picture="+
            //	pic_url
            //		+"&%20redirect_uri=http://www.gajo.us&%20description="+
            //	r_par
            //	+"&%20&display=touch";
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( getString(R.string.fb_url) ));
            		startActivity(browserIntent);

            	
            }
        });  //end of fb_post button
        
        ImageButton tw_post = (ImageButton) findViewById(R.id.imageButton2);
        tw_post.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	
            	//String twurl = "https://mobile.twitter.com/#!/Gentdrunk";
            	
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( getString(R.string.tw_url) ));
            		startActivity(browserIntent);

            	
            }
        });  //end of tw_post button
        
        ImageButton it_post = (ImageButton) findViewById(R.id.imageButton3);
        it_post.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {

            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( getString(R.string.it_url) ));
            		startActivity(browserIntent);
          	
            }
        });  //end of it_post button        
        
        ImageButton gp_post = (ImageButton) findViewById(R.id.imageButton4);
        gp_post.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {

            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( getString(R.string.gp_url) ));
            		startActivity(browserIntent);
          	
            }
        });  //end of gp_post button                
        
        ImageButton email_post = (ImageButton) findViewById(R.id.imageButton5);
        email_post.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {

            	//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( getString(R.string.email_url) ));
            	//	startActivity(browserIntent);
          	//Above lines commented out because we dont want to open browser foe email, duh
        	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        //	String[] recipients = new String[]{"my@email.com", "",};
        	
        	String[] recipients = new String[]{ "freebeer@gentlemandrunk.com" , "",};
        	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients );

        	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Found you from your Android app");

        	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

        	emailIntent.setType("text/plain");

        	startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        	finish();
        	
        	
        	
            }
        });  //end of email_post button                 
        
        ImageButton web_post = (ImageButton) findViewById(R.id.imageButton6);
        web_post.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {

            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( getString(R.string.web_url) ));
            		startActivity(browserIntent);
          	
            }
        });  //end of web_post button                 
 
        ImageButton paypal_post = (ImageButton) findViewById(R.id.imageButton7);
        paypal_post.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        		String paypal_url = "https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=freebeer@gentlemandrunk.com&item_name=The+Gentleman+Drunk+Podcast&item_number=donation+via+Android&currency_code=USD";
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( paypal_url ));
            		startActivity(browserIntent);
          	
            }
        });  //end of paypal_post button          
        
        
        
        
    }
}