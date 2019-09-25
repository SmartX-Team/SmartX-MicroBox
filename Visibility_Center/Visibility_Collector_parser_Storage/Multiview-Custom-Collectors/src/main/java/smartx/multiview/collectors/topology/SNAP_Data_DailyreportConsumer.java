package smartx.multiview.collectors.topology;

import java.io.File;
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

import smartx.multiview.DataLake.MongoDB_Connector;

public class SNAP_Data_DailyreportConsumer implements Runnable {
	private Document document;
	private String IOVISORMongoCollection_latency = "daily-report-IOVISOR-data"; 
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private Thread thread;
	private String ThreadName = "Intel SNAP Daily Collection report Thread";
	private Date timestamp;
	private int Count_GIST1=0,Count_GIST2=0,Count_GIST3=0,Count_HUST=0,Count_MYREN=0,Count_CHULA=0,Count_PH,Count_NCKU=0;
	//private final static int HOUR = 6;
    //private final static int MINUTES = 45;
	
	public SNAP_Data_DailyreportConsumer(String bootstrapserver, MongoDB_Connector MongoConn) {
		
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
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, 0);
		today.set(Calendar.HOUR_OF_DAY,8);
		today.set(Calendar.MINUTE, 39);
		today.set(Calendar.SECOND, 0);
		Timer timer = new Timer();
		timer.schedule(new get_SNAP_aggregate(mongoConnector),/*getTomorrowMorning()*/ today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
	}
	
	
	

}
