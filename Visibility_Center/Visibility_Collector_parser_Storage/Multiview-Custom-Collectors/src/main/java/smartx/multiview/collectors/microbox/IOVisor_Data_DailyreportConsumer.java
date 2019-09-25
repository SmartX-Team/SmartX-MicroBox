package smartx.multiview.collectors.microbox;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import smartx.multiview.DataLake.MongoDB_Connector;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class IOVisor_Data_DailyreportConsumer implements Runnable {
	private static int Date = 0;
	private static int HOUR_OF_DAY=0;
	private static int MINUTE=0;
	private Document document;
	private String IOVISORMongoCollection_latency = "daily-report-IOVISOR-data"; 
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private Thread thread;
	private String ThreadName = "IOVISOR Daily Collection report Thread";
	private Date timestamp;
	private int Count_GIST1=0,Count_GIST2=0,Count_GIST3=0,Count_HUST=0,Count_MYREN=0,Count_CHULA=0,Count_PH,Count_NCKU=0;
	
	public IOVisor_Data_DailyreportConsumer(String bootstrapserver, MongoDB_Connector MongoConn) {
		
		bootstrapServer        = bootstrapserver;
		mongoConnector         = MongoConn;
	}
	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running "+ThreadName);
		
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("newfile.json"));
			//parsing the JSON string inside the file that we created earlier.

			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);
			//Json string has been converted into JSONObject

			String Date = (String) jsonObject.get("Date");
			this.Date=Integer.parseInt(Date);
			
			System.out.println(this.Date);

			String HOUR_OF_DAY = (String) jsonObject.get("HOUR_OF_DAY");
			this.HOUR_OF_DAY=Integer.parseInt(HOUR_OF_DAY);
			System.out.println(this.HOUR_OF_DAY);

			String  MINUTE = (String) jsonObject.get("MINUTE");
			this.MINUTE=Integer.parseInt(MINUTE);
			System.out.println(this.MINUTE);

			long year = (long) jsonObject.get("year");
			System.out.println(year);
			//Displaying values from JSON OBject by using Keys

			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, this.Date);
		today.set(Calendar.HOUR_OF_DAY, this.HOUR_OF_DAY);
		today.set(Calendar.MINUTE, this.MINUTE);
		today.set(Calendar.SECOND, 0);
		
		/*Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, 0);
		today.set(Calendar.HOUR_OF_DAY,6);
		today.set(Calendar.MINUTE, 27);
		today.set(Calendar.SECOND, 0);
*/
		
		Timer timer = new Timer();
		timer.schedule(new get_IOVISOR_aggregate(mongoConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
		
		
		
	}
		   	   
	
	private TimerTask Consume()  throws IOException {	return null;
	}
	

}
