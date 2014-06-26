package us.gajo.android.gentdrunk;





import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;




public class PodcastActivity extends ListActivity {
	
    private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Order> m_orders = null;
    private OrderAdapter m_adapter;
    private Runnable viewOrders;
    private List<Message> messages;
    public  int clicked_index;
    
    public int getIndex()
    {
    	return clicked_index;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 
        m_orders = new ArrayList<Order>();
        this.m_adapter = new OrderAdapter(this, R.layout.row, m_orders);
        setListAdapter(this.m_adapter);
       
        viewOrders = new Runnable(){
            @Override
            public void run() {
                getOrders();
            }
        };
        
        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(PodcastActivity.this,    
              "Please wait...", "Retrieving data ...", true);
        
        
       
        
   
        
        
    }
    public void onListItemClick(ListView parent, View v, int position, long id) {
        /** puExtra method for if you want to pass variable to next activity */
       // Intent i = new Intent(this, listViewSub.class);
       // i.putExtra("name", sitesList.getName());
       // startActivity(i);
    	//int iiii = m_adapter.getItem(position);
    	
    	String this_uri="";
    	String this_title="";
    	String this_desc="";
    	String this_date="";
    	String this_duration="";
    	int    this_progress=0;
    	int    this_id=0;
    	int pos = position;
//    	try{
//          	BaseFeedParser parser = new BaseFeedParser();
//    	messages = parser.parse();
    	
    	//List<String> titles = new ArrayList<String>(messages.size());
    	int j=0;
    	for (Message msg : messages){
    		//titles.add(msg.getTitle());
    		if(j == pos) {
    			this_id       = j;
    			this_uri      = msg.getGuid();
    		   	this_title    = msg.getTitle();
    	    	this_desc     = msg.getDescription();
    	    	this_date     = msg.getDate();
    	    	this_duration = msg.getDuration();
    	    	this_progress = msg.getPosition();
    		}
    		j++;
    	}  

   	
    	Intent myIntent = new Intent(v.getContext(), podPlay.class);
    	Bundle my_bundle = new Bundle();   // moved from below the if's
    	
    	my_bundle.putInt("clickedIndex", pos);
    	my_bundle.putString("title", this_title);
    	my_bundle.putString("date", this_date);
    	my_bundle.putString("desc", this_desc);
    	my_bundle.putString("url", this_uri);
    	my_bundle.putString("duration", this_duration);
    	my_bundle.putInt("seek_pos", this_progress );
    	my_bundle.putInt("msg_id", this_id);
    	myIntent.putExtras(my_bundle);
        startActivityForResult(myIntent, 0);
    	
    	
    }

    
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(m_orders != null && m_orders.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_orders.size();i++)
                m_adapter.add(m_orders.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
    private void getOrders(){


          try{
          	BaseFeedParser parser = new BaseFeedParser();
	    	messages = parser.parse();
	    	m_orders = new ArrayList<Order>();
	    	//List<String> titles = new ArrayList<String>(messages.size());
	    	for (Message msg : messages){
	    		//titles.add(msg.getTitle());
	    		  Order o4 = new Order();
                  //o4.setOrderName(msg.getTitle());
	    		  o4.setOrderName(msg.getTitle());
	    		  
	    		  
	    		  
                 // o4.setOrderStatus("Completed");
                  m_orders.add(o4);
	    	}
	    	

              
              //Thread.sleep(5000);
              Log.i("ARRAY", ""+ m_orders.size());
              
            } catch (Exception e) {
              Log.e("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes);
            
        }
    
    private class OrderAdapter extends ArrayAdapter<Order> {

        private ArrayList<Order> items;

        public OrderAdapter(Context context, int textViewResourceId, ArrayList<Order> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                Order o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                   //     TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if (tt != null) {
                              tt.setText( o.getOrderName());   //title                         }
                    //    if(bt != null){
                    //          bt.setText( o.getOrderStatus());  //description
                    //    }
                }
                return v;
                }
                return v;
        }
                

}
}
