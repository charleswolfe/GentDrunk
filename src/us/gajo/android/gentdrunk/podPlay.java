package us.gajo.android.gentdrunk;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;


public class podPlay extends Activity {
    String this_title="";
    String this_desc="";
    String this_date="";
    String this_url="";
    String this_length="";
    String extStorageDirectory="";
    int    this_pos=0;
    int    this_id=0;
    /*
    private DownloadManager dMgr;
    private long downloadId;
    */
    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;
    private int play_or_pause = 0; //0 play 1st, 1 paused //2 resume
    private NotificationManager mNotificationManager;
    Button playpod;
    private SeekBar timeline=null;
    private TextView txt_now;
    private Handler handler;
    private List<Message> messages;
    private Message pod_msg;
    
    
    AlertDialog.Builder alert;
	@Override
    public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.podplay_layout);
	       
	      

	       
	        Bundle b    = getIntent().getExtras();
		    this_title  = b.getString("title");
		    this_desc   = b.getString("desc");
		    this_date   = b.getString("date");
		    this_url    = b.getString("url");
		    this_length = b.getString("duration");
		    this_pos    = b.getInt("seek_pos");
		    this_id     = b.getInt("msg_id");
		    extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		    
		    
	   		 
		    
		    
		    
		TextView txt_title = (TextView) findViewById(R.id.textView1);
	    TextView txt_date = (TextView) findViewById(R.id.textView2);
	    TextView txt_desc = (TextView) findViewById(R.id.textView3);
	             txt_now = (TextView) findViewById(R.id.nowTime);
	    TextView txt_all = (TextView) findViewById(R.id.allTime);
		//get clicked number
//	    Toast.makeText(this, "clicked" + Integer.toString( podNum ), Toast.LENGTH_SHORT).show();
	    //get title etc from our list
		
		
		
	    txt_title.setText(this_title);
	    txt_date.setText(this_date);
	    txt_desc.setText(this_desc);
	    txt_all.setText(this_length);
	    timeline = (SeekBar) findViewById(R.id.PBar);
	    timeline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	    	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
	           // mProgressText.setText(progress + " " + 
	           //         getString(R.string.seekbar_from_touch) + "=" + fromTouch);
	    		if(mediaPlayer != null && !mediaPlayer.isPlaying())
            	{
            		mediaPlayer.seekTo(progress);
            	} 
	    		
        	
	    		
	        }

	        public void onStartTrackingTouch(SeekBar seekBar) {
	           // mTrackingText.setText(getString(R.string.seekbar_tracking_on));
	        	mediaPlayer.pause();
	        }

	        public void onStopTrackingTouch(SeekBar seekBar) {
	           // mTrackingText.setText(getString(R.string.seekbar_tracking_off));
	        	/*if(mediaPlayer != null && !mediaPlayer.isPlaying())
            	{
            		mediaPlayer.start();
            	}*/
	        	
	        	handler = new Handler();
        		
            	if(mediaPlayer != null && !mediaPlayer.isPlaying())
            	{
            		playpod.setBackgroundResource(R.layout.s_pause);
                	play_or_pause=1;
            		//mediaPlayer.seekTo(progress);
            		mediaPlayer.start();
                    String ns = Context.NOTIFICATION_SERVICE;
                    //NotificationManager 
                    mNotificationManager = (NotificationManager) getSystemService(ns);
                    int icon = R.drawable.player_play;
                    CharSequence tickerText = "Now Playing";
                    long when = System.currentTimeMillis();

                    Notification notification3 = new Notification(icon, tickerText, when);
                    notification3.flags = notification3.flags | Notification.FLAG_ONGOING_EVENT;
                    Context context = getApplicationContext();

                    CharSequence contentTitle = getString(R.string.app_name);
                    CharSequence contentText = this_title;  
                    

                    Intent notificationIntent = new Intent(podPlay.this, gentdrunk.class);
                    notificationIntent.setAction("android.intent.action.MAIN");
                    notificationIntent.addCategory("android.intent.category.LAUNCHER");
              
                	PendingIntent contentIntent = PendingIntent.getActivity(podPlay.this, 0, notificationIntent, 0);
                    notification3.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
            		mNotificationManager.notify(5, notification3);
            		
            		handler.postDelayed(onEverySecond, 1000);
            	}
	        	
	        }

	    });
	    handler = new Handler();

	    
	    //Download to SD card?
	    PhoneStateListener phoneStateListener = new PhoneStateListener() {
	        @Override
	        public void onCallStateChanged(int state, String incomingNumber) {
	            if (state == TelephonyManager.CALL_STATE_RINGING) {
	                //Incoming call: Pause music
	            	
            		if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            			playpod.setBackgroundResource(R.layout.s_play);
                		play_or_pause=2; //chnage to play
            			playbackPosition = mediaPlayer.getCurrentPosition();
            			mediaPlayer.pause();
            			//mNotificationManager.cancelAll();
            			mNotificationManager.cancel(5);
            		}
	            } else if(state == TelephonyManager.CALL_STATE_IDLE) {
	                //Not in call: Play music
	            } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
	                //A call is dialing, active or on hold
	            }
	            super.onCallStateChanged(state, incomingNumber);
	        }
	    };
	    TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
	    if(mgr != null) {
	        mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	    }
	    
	    //set up facebook and twitter shit
        Button fb_post = (Button) findViewById(R.id.imageButton1);
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
            //	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( getString(R.string.fb_url) ));
            //		startActivity(browserIntent);
        	Facebook facebook = new Facebook("130616880366309");
        	// get information about the currently logged in user

        	  
        	

        	
            Bundle parameters = new Bundle();
            //parameters.putString("message", this_url);
            parameters.putString("method", "feed");
            parameters.putString("name", this_title);
            parameters.putString("link", this_url);
            parameters.putString("picture", "http://img850.imageshack.us/img850/1580/27674625990862735415010.jpg");
            parameters.putString("caption", "The Gentleman Drunk");  //under bold title
            parameters.putString("description", this_desc);
           // parameters.putString("access_token",facebook.getAccessToken());
            
        	facebook.dialog(podPlay.this,"feed", parameters, 

        		      new DialogListener() {
        		           @Override
        		           public void onComplete(Bundle values) {}

        		           @Override
        		           public void onFacebookError(FacebookError error) {}

        		           @Override
        		           public void onError(DialogError e) {}

        		           @Override
        		           public void onCancel() {}
        		      }
        		);
        	
        	
            } //onclick
        });  //end of fb_post button
        
        Button tw_post = (Button) findViewById(R.id.imageButton2);
        tw_post.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	

        		String temptw = "Check out " + this_title + // " " + this_url + 
        		                 " @gentdrunk " +
        		                 "%23gentlemandrunk" + "&related=gentdrunk&via=gentdrunk";
        			
        		String cleantw = temptw.replace(" ", "%20");
        	//	temptw = cleantw.replace("#", "%23");
        	//	cleantw = temptw.replace("/", "%2F");
        		//replace(":", "%3A");
        		temptw = cleantw.replace("@", "%40");
        		//what about #
        		cleantw = temptw;
        		
        	    String twurl = "http://twitter.com/intent/tweet?status=" + cleantw;
        	                    	
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( twurl ));
            		startActivity(browserIntent);

            	
            }
        });  //end of tw_post button
	    
        
        //Setup download button
        Button save_sd = (Button) findViewById(R.id.button1);
        save_sd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	//AlertDialog.Builder 
            	alert = new AlertDialog.Builder(podPlay.this);

            	alert.setTitle("Save to SDcard?");
            	alert.setMessage("This can take quite a long time, are you sure?");

            	// Set an EditText view to get user input 
 //           	final EditText input = new EditText(this);
 //           	alert.setView(input);

            	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            	  //String value = input.getText();
            	  // Do something with value!
            	  new DownloadFile().execute();
            	  }
            	});

            	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            	  public void onClick(DialogInterface dialog, int whichButton) {
            	    // Canceled.
            	  }
            	});

            	alert.show();
            	//Toast.makeText(podPlay.this, "This has been known to lock up your phone until download complete", Toast.LENGTH_SHORT).show();
            	//  new DownloadFile().execute();   
/*
            	DownloadManager.Request dmReq = new DownloadManager.Request(
            			Uri.parse(this_url));
            	dmReq.setTitle("Gentleman Drunk");
            	dmReq.setDescription("download name");
            	
            	IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            	registerReceiver(mReceiver, filter);
            	downloadId = dMgr.enqueu(dmReq);
            	
    */        	
            	
            	
            	
                	
                }//end of onclick
            });  //end of save_SD button        
        
        
        //Setup play button
        //Button 
        playpod = (Button) findViewById(R.id.button2);
        playpod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            //cheapo media solution
            //   	Intent intent = new Intent();  
            //	intent.setAction(android.content.Intent.ACTION_VIEW);  
            //	intent.setDataAndType(Uri.parse(this_url), "audio/*");  
            //	startActivity(intent);
            //end of cheapo media solution
            	if(play_or_pause == 0)
            	{
	            	//playpod.setText("Pause");
	            	playpod.setBackgroundResource(R.layout.s_pause);
	            	play_or_pause=1;

	            	try{
	            	playAudio(this_url);
	            	
	            	} catch(Exception e){
	            		e.printStackTrace();
	            	}
            	}
            	else if(play_or_pause == 1)
            	{
            		//puase
            		//playpod.setText("Resume");
            		playpod.setBackgroundResource(R.layout.s_play);
            		play_or_pause=2; //chnage to play
            		if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            			playbackPosition = mediaPlayer.getCurrentPosition();
            			mediaPlayer.pause();
            			//mNotificationManager.cancelAll();
            			mNotificationManager.cancel(5);
            		}
            	}
            	else if(play_or_pause == 2)
            	{
            		//playpod.setText("Pause");
            		playpod.setBackgroundResource(R.layout.s_pause);
	            	play_or_pause=1;
	            	if(mediaPlayer != null && !mediaPlayer.isPlaying())
	            	{
	            		mediaPlayer.seekTo(playbackPosition);
	            		mediaPlayer.start();
	                    String ns = Context.NOTIFICATION_SERVICE;
	                    //NotificationManager 
	                    mNotificationManager = (NotificationManager) getSystemService(ns);
	                    int icon = R.drawable.player_play;
	                    CharSequence tickerText = "Now Playing";
	                    long when = System.currentTimeMillis();

	                    Notification notification3 = new Notification(icon, tickerText, when);
	                    notification3.flags = notification3.flags | Notification.FLAG_ONGOING_EVENT;
	                    Context context = getApplicationContext();

	                    CharSequence contentTitle = getString(R.string.app_name);
	                    CharSequence contentText = this_title;  
	                    
	                    //Intent notificationIntent = new Intent(Intent.ACTION_MAIN);
	                    //notificationIntent.setClass(getApplicationContext(), podPlay.class);

	                    Intent notificationIntent = new Intent(podPlay.this, gentdrunk.class);
	                 //Intent  notificationIntent = new Intent();  
	                //	notificationIntent.setAction(podPlay.this);  
	                    notificationIntent.setAction("android.intent.action.MAIN");
	                    notificationIntent.addCategory("android.intent.category.LAUNCHER");
	              
	                	
	                	
	                    PendingIntent contentIntent = PendingIntent.getActivity(podPlay.this, 0, notificationIntent, 0);
	                    notification3.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	            		mNotificationManager.notify(5, notification3);
	            		
	            		handler.postDelayed(onEverySecond, 1000);
	            	}
            	}
                	
                }//end of onclick
            });  //end of play button        
        
        Button stoppod = (Button) findViewById(R.id.button3);
        stoppod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            
            	if(mediaPlayer != null )
            	{
            		mediaPlayer.stop();
            		playbackPosition=0;
            		timeline.setProgress(0);
            		txt_now.setText( parseDur(0) );
            		playpod.setBackgroundResource(R.layout.s_play);
            		play_or_pause=0;
            	    //if (mNotificationManager != null)
            	    //{
            	    //  try
            	    //  {
            	        mNotificationManager.cancel(5);
            	     // }
            	     // catch (Throwable ex)
            	    //  {
            	     //   ex.printStackTrace();
            	    //  }
            	   // }
            	   // mNotificationManager = null;

            		//mNotificationManager.cancel(5);
            	}
            	
            	
            } //end of onclick
        });//end of stoppod button listener
        
	    
        // Create the adView for ads
        AdView adView = (AdView)this.findViewById(R.id.adView);
        adView.loadAd(new AdRequest());

	    
		
	} //end of podPlay onCreate
	
	
	
	private class DownloadFile extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... url) {
            try {
                URL url2 = new URL(this_url);
                URL url3 = url2;
                String[] path2 = url3.getPath().split("/");
                String fileName = path2[ path2.length - 1 ];
                fileName = fileName.replace("%20","");

                HttpURLConnection c = (HttpURLConnection) url2.openConnection();
                
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                int lengthOfFile = c.getContentLength();

                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                Log.v("", "PATH: " + PATH);
                File file = new File(PATH);
                file.mkdirs();
//-------------------------
                //notify?
                //ProgressBar progressBar;
                int progress = 0;
                //crashes setContentView(R.layout.download_progress); 
                Intent intent = new Intent();
                final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                // configure the notification
                final Notification notification = new Notification(R.drawable.icon, "Starting download", System
                        .currentTimeMillis());
                notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
                notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.download_progress);
                notification.contentIntent = pendingIntent;
                notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.icon);
                notification.contentView.setTextViewText(R.id.status_text, "Downloading " + fileName);
                notification.contentView.setProgressBar(R.id.status_progress, 100, progress, false);

                final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(
                        getApplicationContext().NOTIFICATION_SERVICE);

                notificationManager.notify(4, notification);
                
                	//-----------------------------
                
               //  String fileName = "testSDRtrack.mp3";

                File outputFile = new File(file, fileName);
//                FileOutputStream fos = new FileOutputStream(outputFile);
                
                InputStream is = c.getInputStream();
               
                BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(outputFile)); 
                
                
 //               byte[] buffer = new byte[1024];
                byte byt[] = new byte[2048];
                int i;
//                while ((len1 = is.read(buffer)) > 0) {
                	//                          != -1
                
                for (long l = 0L; (i = is.read(byt)) != -1; l += i ) {

              //      publishProgress((int)(len1*100/lengthOfFile));
               // 	int progsize = (int)(len1*100/lengthOfFile);
               //crashes 	Toast.makeText(podPlay.this, "% done: " + String.valueOf(progsize), Toast.LENGTH_SHORT).show();
                    
                	//fos.write(buffer, 0, len1);
                	buffer.write(byt, 0, i);
                	
                    progress += (i);
                    
                    //progress++;
                    notification.contentView.setProgressBar(R.id.status_progress, lengthOfFile, progress, false);

                    // inform the progress bar of updates in progress
                    notificationManager.notify(4, notification);
                }
                //fos.close();
                buffer.close();
                is.close();
                
              //  Toast.makeText(podPlay.this, "clicked", Toast.LENGTH_SHORT).show();
                
                
                //notify?
                notificationManager.cancelAll();
                	//------------------------------- new notify 'donwload done'
                
                String ns = Context.NOTIFICATION_SERVICE;
                //NotificationManager 
                mNotificationManager = (NotificationManager) getSystemService(ns);
                int icon = R.drawable.icon;
                CharSequence tickerText = "Download finished";
                long when = System.currentTimeMillis();

                Notification notification2 = new Notification(icon, tickerText, when);
                //notification2.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
                Context context = getApplicationContext();
                CharSequence contentTitle = "Download finished";
                CharSequence contentText = "New ep: " +fileName;
                
             //   Intent notificationIntent = new Intent(podPlay.this, podPlay.class);
                
            	Intent  notificationIntent = new Intent();  
            //	notificationIntent.setAction(android.content.Intent.ACTION_VIEW);  
          //for the alerttimer, should open app, not play media
            	notificationIntent.setDataAndType(Uri.fromFile(outputFile), "audio/*");  
            //	startActivity(notificationIntent);
            	
            	
                PendingIntent contentIntent = PendingIntent.getActivity(podPlay.this, 0, notificationIntent, 0);
                notification2.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
                
                mNotificationManager.notify(4, notification2);
                //end of notify?
                
                
                
             
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                		Uri.parse("file://" +
                		Environment.getExternalStorageDirectory())));
                
                }catch (IOException e) {
                       e.printStackTrace();
                }


            return null;
        }



}

    private void playAudio(String url) throws Exception
    {
    	killMediaPlayer();
    	 
    	
    	
    	mediaPlayer = new MediaPlayer();
    	mediaPlayer.setDataSource(url);
    	mediaPlayer.prepare();
    	if(this_pos > 0) {
    		mediaPlayer.seekTo(this_pos);
    	}
    	mediaPlayer.start();
    	
	    timeline.setProgress(0);
	    timeline.setMax(mediaPlayer.getDuration());

        //
        
        String ns = Context.NOTIFICATION_SERVICE;
        //NotificationManager 
        mNotificationManager = (NotificationManager) getSystemService(ns);
        int icon = R.drawable.player_play;
        CharSequence tickerText = "Now Playing";
        long when = System.currentTimeMillis();

        Notification notification3 = new Notification(icon, tickerText, when);
        notification3.flags = notification3.flags | Notification.FLAG_ONGOING_EVENT;
        Context context = getApplicationContext();

        CharSequence contentTitle = getString(R.string.app_name);
        CharSequence contentText = this_title;  
        
        Intent notificationIntent = new Intent(this, gentdrunk.class);       
        notificationIntent.setAction("android.intent.action.MAIN");
        notificationIntent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent contentIntent = PendingIntent.getActivity(podPlay.this, 0, notificationIntent, 0);
        notification3.setLatestEventInfo(context, contentTitle, contentText, contentIntent);        
        mNotificationManager.notify(5, notification3);                
        //findViewById(R.id.button2)
        
        handler.postDelayed(onEverySecond, 1000);
        
        
        
    }

//    @Override
//    protected void onDestroy() {
    	//super.onDestroy();
//    	killMediaPlayer();
    	
//    }
    
 //   @Override
 //   protected void onPause() {
 //       super.onPause(); // Don't forget this line
 //       //mediaPlayer.pause(); // Or whatever the function is to pause it
 //       killMediaPlayer();
 //      
 //
 //   }

    private void killMediaPlayer() {
    	if(mediaPlayer != null) {
    		try {
    			//play_or_pause=0; 
    			mediaPlayer.release();
    			mNotificationManager.cancel(5);
    			//findViewById(R.id.button2)testButton
    			//playpod.setText("Pause");
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
    private Runnable onEverySecond=new Runnable() {
    	public void run() {
	    //	if (lastActionTime>0 &&
	    //	SystemClock.elapsedRealtime()-lastActionTime>3000) {
	    //	clearPanels(false);
	    //	}
	
	    	if (mediaPlayer!=null  && play_or_pause > 0 ) {
	    	timeline.setProgress(mediaPlayer.getCurrentPosition());
	    	txt_now.setText( parseDur(mediaPlayer.getCurrentPosition()) );
	    	}
	
	    	if (mediaPlayer.isPlaying()) {
	    	handler.postDelayed(onEverySecond, 1000);
	    	}
	    	else
	    	{
	    		mNotificationManager.cancel(5);
	    	}
	    }
    };

    // parse milliseconds to time
    private String parseDur(int Dur) {
            String format = "mm:ss";
            if(Dur >= 3600*1000) {
                    format = "hh:mm:ss";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date toMin = new Date(Dur);
            return sdf.format(toMin);
    }

    public void onBackPressed() {
        // do something on back.
    	//killMediaPlayer();
    	
    	
    	if(mediaPlayer != null )
    	{
    		// set something to  mediaPlayer.getCurrentPosition();
    		
        
        
    		
    		mediaPlayer.stop();
    		playbackPosition=0;
    		timeline.setProgress(0);
    		txt_now.setText( parseDur(0) );
    		playpod.setBackgroundResource(R.layout.s_play);
    		play_or_pause=0;
    		//killMediaPlayer();
    	    //if (mNotificationManager != null)
    	    //{
    	    //  try
    	    //  {
    	        mNotificationManager.cancel(5);
    	     // }
    	     // catch (Throwable ex)
    	    //  {
    	     //   ex.printStackTrace();
    	    //  }
    	   // }
    	   // mNotificationManager = null;

    		//mNotificationManager.cancel(5);
    	}
    	super.onBackPressed();
    	
        return;
    }
	
	
} //end of podPlay