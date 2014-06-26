package us.gajo.android.gentdrunk;
import java.io.IOException;  
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class BaseFeedParser {

	static String feedUrlString = "http://blog.gentlemandrunk.com/podcasts-only/rss2.aspx";
	
	// names of the XML tags
	static final String RSS = "rss";
	static final String CHANNEL = "channel";
	static final String ITEM = "item";
	
	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "summary";
	static final String LINK = "link";
	static final String TITLE = "title";
	static final String GUID = "guid";
	static final String DURATION = "duration";
	
	private final URL feedUrl;

	protected BaseFeedParser(){
		try {
			this.feedUrl = new URL(feedUrlString);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			feedUrl.openConnection().setConnectTimeout(50000);
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Message> parse() {
		final Message currentMessage = new Message();
		RootElement root = new RootElement(RSS);
		//TODO het xmlns:itunes from RSS, so that it isnt hardcoded below
		final List<Message> messages = new ArrayList<Message>();
		Element itemlist = root.getChild(CHANNEL);
		Element item = itemlist.getChild(ITEM);
		item.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTitle(body);
			}
		});
		item.getChild(LINK).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setLink(body);
			}
		});
		item.getChild("http://www.itunes.com/dtds/podcast-1.0.dtd", DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDescription(body);
			}
		});
		item.getChild(GUID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setGuid(body);
			}
		});
		item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDate(body);
			}
		});
		item.getChild("http://www.itunes.com/dtds/podcast-1.0.dtd", DURATION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDuration(body);
			}
		});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
}