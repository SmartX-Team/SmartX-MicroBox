package smartx.multiview.collectors.topology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import smartx.multiview.DataLake.MongoDB_Connector;

public class get_latency_aggregate  extends TimerTask {
	private Thread thread;
	private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String topologyMongoCollection_latency_aggregate = "daily-report-latency-data-aggregate"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "TCP Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "daily_report_latency"; 
	private Document document;
	private Date timestamp;
	public float Box_GIST1_latency=0,Box_PH_latency=0,Box_HUST_latency=0,Box_CHULA_latency=0,Box_MYREN_latency=0,Box_GIST3_latency=0, Box_GIST0_latency=0, latency_record_total_today=0; 
	public int count_oneday_latency_SmartX_Box=0, count_oneday_latency_SmartX_Box_PH=0, count_oneday_latency_SmartX_Box_HUST=0, count_oneday_latency_SmartX_Box_CHULA=0, 
			count_oneday_latency_SmartX_Box_MYREN=0, count_oneday_latency_SmartX_Box_GIST3=0, count_oneday_latency_SmartX_Box_GIST_0=0;
	public List<Document> documents_OneDay;
	private Document document_latencyscore;
	private DecimalFormat df = new DecimalFormat("###.##");
	public float avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1=0, avg_oneday_latency_SmartX_Box_HUST_dest_Box_PH=0, avg_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA=0, avg_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN=0, avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3=0, avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0=0;

	public get_latency_aggregate(MongoDB_Connector mongoConnector2) {
		mongoConnector = mongoConnector2;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("************************************************Latency Timer Called************************************************************** ");
		try {
			this.get_latency();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void get_latency() throws ParseException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
				.getCollection(topologyMongoCollection_latency);



		SimpleDateFormat simpleDateFormat_startDate = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat simpleDateFormat_endDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
		timestamp = new Date();
		System.out.println("Local Time: " + simpleDateFormat_endDate.format(timestamp));

		String date =simpleDateFormat_startDate.format(timestamp)+ " 00:00:01 KST"; //start of the today's date

		String date1 =simpleDateFormat_endDate.format(timestamp); //Current data and time

		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy/MM/dd");  

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss zzz");  
		SimpleDateFormat simpleDateFormatNoZone = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat simpleDateFormatNoTime = new SimpleDateFormat ("yyyy/MM/dd");
		Date startDate= simpleDateFormat.parse(date); 
		Date endDate= simpleDateFormat.parse(date1);
		System.out.println("**************************Start time and end time*******************************: ");
		System.out.println(startDate);
		System.out.println(endDate);

		Calendar today_start = Calendar.getInstance();
		today_start.set(Calendar.HOUR_OF_DAY, 00);
		today_start.set(Calendar.MINUTE, 0);
		today_start.set(Calendar.SECOND, 0);
		System.out.println(today_start.getTime());

		Calendar today_now = Calendar.getInstance();

		System.out.println(today_now.getTime());

		BasicDBObject query1 = new BasicDBObject("timestamp", new BasicDBObject("$gte",new Date(System.currentTimeMillis() - (12 * 60 * 60 * 1000))).append("$lt",new Date(System.currentTimeMillis())));

		BasicDBObject query2 = new BasicDBObject("timestamp", new BasicDBObject("$gte",Calendar.MILLISECOND - (12 * 60 * 60 * 1000)).append("$lt",Calendar.MILLISECOND));

		FindIterable<Document> findCursor
		= collection_process.find(
				Filters.and(
						Filters.gte("timestamp", new Date(System.currentTimeMillis() - (6 * 60 * 60 * 1000))),
						Filters.lte("timestamp", new Date(System.currentTimeMillis())))); //not using this code



		List<Document> documents = collection_process.find().into(new ArrayList<Document>());


		List<Document> documents1 = collection_process.find(query2).into(new ArrayList<Document>());
		documents_OneDay = new ArrayList<Document>();

		System.out.println(collection_process.find(query1));
		System.out.println("Collection Count for todays latency data");
		System.out.println(collection_process.count());


		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0); 
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());//2018/07/03
		String date_check = sdf.parse(date).toString();//Tue Jul 03 00:00:00 KST 2018



		for (Document document : documents) 
		{
			//System.out.println(document);

			String date_check_timestamp= document.getString("timestamp");

			//System.out.println(date_check);//Tue Jul 03 00:00:00 KST 2018
			//System.out.println(date_check_timestamp); //2018/07/03 16:00:25 KST		 
			//System.out.println(today.getTime());// Tue Jul 03 00:16:00 KST 2018
			//System.out.println(timeStamp);//2018/07/03
			//String custom_date="2018/07/16";
			if (document.getString("timestamp").contains(timeStamp))
			{

				//documents_OneDay.add(document);
				if(!document.containsValue("-1"))
        		{
        			documents_OneDay.add(document);
        		}

			}
		}
		
		
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-GIST              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (Document document : documents_OneDay) 
		{
			System.out.println("Printing one day (today) latency data only from documents_OneDay variable");
			System.out.println(document);
		}

		String date_time = "Daily-report-latency-data"+"_"+simpleDateFormat_startDate.format(timestamp);
		String csvFile = date_time.replace("/", "")+".csv";
		String csvFile1 = date_time.replace("/", "")+"1.csv";
		document_latencyscore = new Document();
		try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/Daily-report-latency-data/"+csvFile, true);

				BufferedWriter bw = new BufferedWriter(fw);

				PrintWriter out = new PrintWriter(bw))
		{
			File file = new File("/home/netcs/active_monitoring/Daily-report-latency-data/"+csvFile);
			if (file.length() == 0)
	        {
	        	out.println("Date"+" "+"TIME"+" "+"SOURCE-SITE"+" "+"SmartX-Box-GIST1"+" "+"SmartX-Box-PH"+" "+"SmartX-Box-HUST"+" "+"SmartX-Box-CHULA"+" "+"SmartX-Box-MYREN"+" "+"SmartX-Box-GIST3");	
	        }
			
			
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-GIST1              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (Document document : documents_OneDay) 
		{
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-GIST1"))
			{
				//System.out.println(document.getString("SmartX-Box-SOURCE"));
				calculate_latency(document);
			}
		}
		System.out.println("Printing count_oneday_latency_SmartX_Box and latency count at each site" );
		System.out.println("SmartX_Box_GIST1:"+"-"+ " " +Box_PH_latency+" "+Box_HUST_latency+" "+Box_CHULA_latency+" "+Box_MYREN_latency+" "+Box_GIST3_latency+ " " +Box_GIST0_latency);
		System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);

		//float avg_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST1= (Box_GIST1_latency/count_oneday_latency_SmartX_Box);
		
		float avg_oneday_latency_SmartX_Box_GIST1_dest_Box_PH=(float) Box_PH_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST=(float) Box_HUST_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA=(float) Box_CHULA_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN=(float) Box_MYREN_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3=(float) Box_GIST3_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST0=(float) Box_GIST0_latency/(count_oneday_latency_SmartX_Box);
		System.out.println("Printing count_oneday_latency_SmartX_Box, avg" );
		System.out.println("-"+ " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_PH+ " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST + " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA + " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN + " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3 + " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST0);

		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");

		document_latencyscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(timestamp));
		document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_GIST1");
		document_latencyscore.put("SmartX-Box-GIST1",   "-");
		document_latencyscore.put("SmartX-Box-PH",  df.format(avg_oneday_latency_SmartX_Box_GIST1_dest_Box_PH));
		document_latencyscore.put("SmartX-Box-HUST",  df.format(avg_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST));
		document_latencyscore.put("SmartX-Box-CHULA",  df.format(avg_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA));
		document_latencyscore.put("SmartX-Box-MYREN",  df.format(avg_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN));
		document_latencyscore.put("SmartX-Box-GIST3",  df.format(avg_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3));

		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
		
		 
		//out.println(simpleDateFormatNoZone.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+"-"+ " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_PH+ " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST + " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA + " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN + " "+avg_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3);
		out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+"-"+" "+document_latencyscore.getString("SmartX-Box-PH")+" "+document_latencyscore.getString("SmartX-Box-HUST")+" "+document_latencyscore.getString("SmartX-Box-CHULA")+" "+document_latencyscore.getString("SmartX-Box-MYREN")+" "+document_latencyscore.getString("SmartX-Box-GIST3"));
		
		document_latencyscore.clear();
		reset_values();
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-PH              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		for (Document document : documents_OneDay) 
		{
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-PH"))
			{
				//System.out.println(document.getString("SmartX-Box-SOURCE"));
				calculate_latency(document);
			}
		}
		System.out.println("");
		System.out.println("Printing count_oneday_latency_SmartX_Box_PH and latency count at each site" );
		System.out.println("SmartX_Box_PH:"+ " " +Box_GIST1_latency+" "+"-"+" "+Box_HUST_latency+" "+Box_CHULA_latency+" "+Box_MYREN_latency+" "+Box_GIST3_latency+ " " +Box_GIST0_latency);
		System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);

		float avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST1=(float) Box_GIST1_latency/(count_oneday_latency_SmartX_Box);
		//float avg_oneday_latency_SmartX_Box_PH_dest_Box_PH= (Box_PH_latency/count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_PH_dest_Box_HUST=(float) Box_HUST_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_PH_dest_Box_CHULA=(float) Box_CHULA_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_PH_dest_Box_MYREN=(float) Box_MYREN_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST3=(float) Box_GIST3_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST0=(float) Box_GIST0_latency/(count_oneday_latency_SmartX_Box);
		System.out.println("Printing count_oneday_latency_SmartX_Box, avg, Downtime" );
		System.out.println(avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST1+ " "+"-"+ " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_HUST + " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_CHULA + " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_MYREN + " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST3 + " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST0);

		document_latencyscore.put("timestamp",   simpleDateFormat.format(timestamp));
		document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_PH");
		document_latencyscore.put("SmartX-Box-GIST1",   df.format(avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST1));
		document_latencyscore.put("SmartX-Box-PH",  "-");
		document_latencyscore.put("SmartX-Box-HUST",  df.format(avg_oneday_latency_SmartX_Box_PH_dest_Box_HUST));
		document_latencyscore.put("SmartX-Box-CHULA",  df.format(avg_oneday_latency_SmartX_Box_PH_dest_Box_CHULA));
		document_latencyscore.put("SmartX-Box-MYREN",  df.format(avg_oneday_latency_SmartX_Box_PH_dest_Box_MYREN));
		document_latencyscore.put("SmartX-Box-GIST3",  df.format(avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST3));

		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
		//out.println(simpleDateFormatNoZone.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST1+ " "+"-"+ " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_HUST + " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_CHULA + " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_MYREN + " "+avg_oneday_latency_SmartX_Box_PH_dest_Box_GIST3);
		out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+document_latencyscore.getString("SmartX-Box-GIST1")+" "+"-"+" "+document_latencyscore.getString("SmartX-Box-HUST")+" "+document_latencyscore.getString("SmartX-Box-CHULA")+" "+document_latencyscore.getString("SmartX-Box-MYREN")+" "+document_latencyscore.getString("SmartX-Box-GIST3"));
		document_latencyscore.clear();
		reset_values();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-HUST              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (Document document : documents_OneDay) 
		{
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-HUST"))
			{
				//System.out.println(document.getString("SmartX-Box-SOURCE"));
				calculate_latency(document);
			}
		}
		System.out.println("");
		System.out.println("Printing count_oneday_latency_SmartX_Box_HUST and latency count at each site" );
		System.out.println("SmartX_Box_HUST:"+ " " +Box_GIST1_latency+" "+" "+Box_PH_latency+" -"+" "+Box_CHULA_latency+" "+Box_MYREN_latency+" "+Box_GIST3_latency+ " " +Box_GIST0_latency);
		System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);

		if (count_oneday_latency_SmartX_Box>0)
		{
		avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1=(float) Box_GIST1_latency/(count_oneday_latency_SmartX_Box);
		avg_oneday_latency_SmartX_Box_HUST_dest_Box_PH=(float) Box_PH_latency/(count_oneday_latency_SmartX_Box);
		//float avg_oneday_latency_SmartX_Box_HUST_dest_Box_HUST= Box_HUST_latency/(count_oneday_latency_SmartX_Box);
		avg_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA=(float) Box_CHULA_latency/(count_oneday_latency_SmartX_Box);
		avg_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN=(float) Box_MYREN_latency/(count_oneday_latency_SmartX_Box);
		avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3=(float) Box_GIST3_latency/(count_oneday_latency_SmartX_Box);
		avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0=(float) Box_GIST0_latency/(count_oneday_latency_SmartX_Box);
		}
		
		System.out.println("Printing count_oneday_latency_SmartX_Box, avg, Downtime" );
		System.out.println(avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1+ " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_PH + " "+"-"+ " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA + " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN + " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3 + " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0);

		document_latencyscore.put("timestamp",   simpleDateFormat.format(timestamp));
		document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_HUST");
		document_latencyscore.put("SmartX-Box-GIST1",   df.format(avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1));
		document_latencyscore.put("SmartX-Box-PH",  df.format(avg_oneday_latency_SmartX_Box_HUST_dest_Box_PH));
		document_latencyscore.put("SmartX-Box-HUST",  "-");
		document_latencyscore.put("SmartX-Box-CHULA",  df.format(avg_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA));
		document_latencyscore.put("SmartX-Box-MYREN",  df.format(avg_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN));
		document_latencyscore.put("SmartX-Box-GIST3",  df.format(avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3));

		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
		//out.println(simpleDateFormatNoZone.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1+ " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_PH + " "+"-"+ " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA + " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN + " "+avg_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3);
		out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+document_latencyscore.getString("SmartX-Box-GIST1")+" "+document_latencyscore.getString("SmartX-Box-PH")+" "+"-"+" "+document_latencyscore.getString("SmartX-Box-CHULA")+" "+document_latencyscore.getString("SmartX-Box-MYREN")+" "+document_latencyscore.getString("SmartX-Box-GIST3"));
		document_latencyscore.clear();
		reset_values();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-CHULA              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (Document document : documents_OneDay) 
		{
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-CHULA"))
			{
				//System.out.println(document.getString("SmartX-Box-SOURCE"));
				calculate_latency(document);
			}
		}
		System.out.println("");
		System.out.println("Printing count_oneday_latency_SmartX_Box_CHULA and latency count at each site" );
		System.out.println("SmartX_Box_CHULA:"+ " " +Box_GIST1_latency+" "+Box_PH_latency+" "+Box_HUST_latency+" "+"-"+" "+Box_MYREN_latency+" "+Box_GIST3_latency+ " " +Box_GIST0_latency);
		System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);

		float avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1=(float) Box_GIST1_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_CHULA_dest_Box_PH=(float) Box_PH_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST= Box_HUST_latency/(count_oneday_latency_SmartX_Box);
		//float avg_oneday_latency_SmartX_BoxCHULA_dest_Box_CHULA=(float) Box_CHULA_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN=(float) Box_MYREN_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3=(float) Box_GIST3_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST0=(float) Box_GIST0_latency/(count_oneday_latency_SmartX_Box);
		System.out.println("Printing count_oneday_latency_SmartX_Box, avg, Downtime" );
		System.out.println(avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1+ " "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_PH +" "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST+ " "+"-"+ " "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN + " "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3 + " "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST0);

		document_latencyscore.put("timestamp",   simpleDateFormat.format(timestamp));
		document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_CHULA");
		document_latencyscore.put("SmartX-Box-GIST1",   df.format(avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1));
		document_latencyscore.put("SmartX-Box-PH",  df.format(avg_oneday_latency_SmartX_Box_CHULA_dest_Box_PH));
		document_latencyscore.put("SmartX-Box-HUST",  df.format(avg_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST));
		document_latencyscore.put("SmartX-Box-CHULA",  "-");
		document_latencyscore.put("SmartX-Box-MYREN",  df.format(avg_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN));
		document_latencyscore.put("SmartX-Box-GIST3",  df.format(avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3));

		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
		//out.println(simpleDateFormatNoZone.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1+ " "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_PH +" "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST+ " "+"-"+ " "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN + " "+avg_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3);
		out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+document_latencyscore.getString("SmartX-Box-GIST1")+" "+document_latencyscore.getString("SmartX-Box-PH")+" "+document_latencyscore.getString("SmartX-Box-HUST")+" "+"-"+" "+document_latencyscore.getString("SmartX-Box-MYREN")+" "+document_latencyscore.getString("SmartX-Box-GIST3"));
		document_latencyscore.clear();
		reset_values();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-MYREN              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (Document document : documents_OneDay) 
		{
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-MYREN"))
			{
				//System.out.println(document.getString("SmartX-Box-SOURCE"));
				calculate_latency(document);
			}
		}
		System.out.println("");
		System.out.println("Printing count_oneday_latency_SmartX_Box_MYREN and latency count at each site" );
		System.out.println("SmartX_Box_MYREN:"+ " " +Box_GIST1_latency+" "+Box_PH_latency+" "+Box_HUST_latency+Box_CHULA_latency+" "+"- "+Box_GIST3_latency+ " " +Box_GIST0_latency);
		System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);

		float avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1=(float) Box_GIST1_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_MYREN_dest_Box_PH=(float) Box_PH_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST= Box_HUST_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA=(float) Box_CHULA_latency/(count_oneday_latency_SmartX_Box);
		//float avg_oneday_latency_SmartX_Box_MYREN_dest_Box_MYREN=(float) Box_MYREN_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3=(float) Box_GIST3_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST0=(float) Box_GIST0_latency/(count_oneday_latency_SmartX_Box);
		System.out.println("Printing count_oneday_latency_SmartX_Box, avg, Downtime" );
		System.out.println(avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1+ " "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_PH + " "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST + " "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA+" "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3 + " "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST0);

		document_latencyscore.put("timestamp",   simpleDateFormat.format(timestamp));
		document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_MYREN");
		document_latencyscore.put("SmartX-Box-GIST1",   df.format(avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1));
		document_latencyscore.put("SmartX-Box-PH",  df.format(avg_oneday_latency_SmartX_Box_MYREN_dest_Box_PH));
		document_latencyscore.put("SmartX-Box-HUST",  df.format(avg_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST));
		document_latencyscore.put("SmartX-Box-CHULA",  df.format(avg_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA));
		document_latencyscore.put("SmartX-Box-MYREN",  "-");
		document_latencyscore.put("SmartX-Box-GIST3",  df.format(avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3));

		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
		//out.println(simpleDateFormatNoZone.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1+ " "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_PH + " "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST + " "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA+" "+"-"+ " "+avg_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3);
		out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+document_latencyscore.getString("SmartX-Box-GIST1")+" "+document_latencyscore.getString("SmartX-Box-PH")+" "+document_latencyscore.getString("SmartX-Box-HUST")+" "+document_latencyscore.getString("SmartX-Box-CHULA")+" "+"-"+" "+document_latencyscore.getString("SmartX-Box-GIST3"));
		document_latencyscore.clear();
		reset_values();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-GIST3              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (Document document : documents_OneDay) 
		{
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-GIST3"))
			{
				//System.out.println(document.getString("SmartX-Box-SOURCE"));
				calculate_latency(document);
			}
		}
		System.out.println("");
		System.out.println("Printing count_oneday_latency_SmartX_Box_GIST3 and latency count at each site" );
		System.out.println("SmartX_Box_GIST3:"+ " " +Box_GIST1_latency+" "+Box_PH_latency+" "+Box_HUST_latency+" "+Box_CHULA_latency+" "+Box_MYREN_latency+ " "+"- "+Box_GIST0_latency);
		System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);

		float avg_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1=(float) Box_GIST1_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST3_dest_Box_PH=(float) Box_PH_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST= Box_HUST_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA=(float) Box_CHULA_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN=(float) Box_MYREN_latency/(count_oneday_latency_SmartX_Box);
		//float avg_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST3=(float) Box_GIST3_latency/(count_oneday_latency_SmartX_Box);
		float avg_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST0=(float) Box_GIST0_latency/(count_oneday_latency_SmartX_Box);
		System.out.println("Printing count_oneday_latency_SmartX_Box, avg, Downtime" );
		System.out.println(avg_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1+ " "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_PH + " "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST +" "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA+ " "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN + " "+"-"+ " "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST0);

		document_latencyscore.put("timestamp",   simpleDateFormat.format(timestamp));
		document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_GIST3");
		document_latencyscore.put("SmartX-Box-GIST1",   df.format(avg_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1));
		document_latencyscore.put("SmartX-Box-PH",      df.format(avg_oneday_latency_SmartX_Box_GIST3_dest_Box_PH));
		document_latencyscore.put("SmartX-Box-HUST",    df.format(avg_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST));
		document_latencyscore.put("SmartX-Box-CHULA",   df.format(avg_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA));
		document_latencyscore.put("SmartX-Box-MYREN",   df.format(avg_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN));
		document_latencyscore.put("SmartX-Box-GIST3",    "-");

		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
		//out.println(simpleDateFormatNoZone.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1+ " "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_PH + " "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST +" "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA+ " "+avg_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN + " "+"-");
		out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+document_latencyscore.getString("SmartX-Box-GIST1")+" "+document_latencyscore.getString("SmartX-Box-PH")+" "+document_latencyscore.getString("SmartX-Box-HUST")+" "+document_latencyscore.getString("SmartX-Box-CHULA")+" "+document_latencyscore.getString("SmartX-Box-MYREN")+" "+"-");
		document_latencyscore.clear();
		reset_values();
		
		} catch (IOException e) {
			//exception handling left as an exercise for the reader
		}
		List<Document> insertList = new ArrayList<Document>();

		Date date11 = new Date();

		//collection_process.insertMany(documents);


		// MongoCursor<Document> doc = collection_process.find(new Document("date", date)).iterator();
		//System.out.println(doc.next().getDate("date"));

		/*for (Document document1 : findCursor) 
					{


							System.out.println("Printing last 2 hours data only using cursor");
							System.out.println(document1);
							//System.out.println(document1.getString("timestamp"));
						}*/   

		/*for (Document document : documents) 
					{
			        	System.out.println("Printing one day (today) latency data only");
			        	System.out.println(document);
					}*/
	}

	private void reset_values() {
		Box_GIST1_latency=0;Box_PH_latency=0;Box_HUST_latency=0;Box_CHULA_latency=0;Box_MYREN_latency=0;Box_GIST3_latency=0; Box_GIST0_latency=0; latency_record_total_today=0; 
		count_oneday_latency_SmartX_Box=0; count_oneday_latency_SmartX_Box_PH=0; count_oneday_latency_SmartX_Box_HUST=0; count_oneday_latency_SmartX_Box_CHULA=0; 
		count_oneday_latency_SmartX_Box_MYREN=0; count_oneday_latency_SmartX_Box_GIST3=0; count_oneday_latency_SmartX_Box_GIST_0=0;

	}

	private void calculate_latency(Document document) {

		count_oneday_latency_SmartX_Box+=1;
		if (!(document.getString("SmartX-Box-GIST1").contains("-")))
		{
			Box_GIST1_latency+=Float.parseFloat(document.getString("SmartX-Box-GIST1"));
		}
		if (!(document.getString("SmartX-Box-PH").contains("-")))
		{
			Box_PH_latency+=Float.parseFloat(document.getString("SmartX-Box-PH"));
		}
		if (!(document.getString("SmartX-Box-HUST").contains("-")))
		{
			Box_HUST_latency+=Float.parseFloat(document.getString("SmartX-Box-HUST"));
		}
		if (!(document.getString("SmartX-Box-CHULA").contains("-")))
		{
			Box_CHULA_latency+=Float.parseFloat(document.getString("SmartX-Box-CHULA"));
		}
		if (!(document.getString("SmartX-Box-MYREN").contains("-")))
		{
			Box_MYREN_latency+=Float.parseFloat(document.getString("SmartX-Box-MYREN"));
		}
		if (!(document.getString("SmartX-Box-GIST3").contains("-")))
		{
			Box_GIST3_latency+=Float.parseFloat(document.getString("SmartX-Box-GIST3"));
		}
		/*if (!(document.getString("SmartX-Box-GIST_NUC").contains("-")))
		{
			Box_GIST0_latency+=Float.parseFloat(document.getString("SmartX-Box-GIST_NUC"));
		}*/

	}
}
