package smartx.multiview.collectors.topology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

public class get_ping_latency_aggregate  extends TimerTask {
	private Thread thread;
	private String topologyMongoCollection_ping = "daily-report-ping-data-raw"; //Change collection name
	private String topologyMongoCollection_ping_aggregate = "daily-report-ping-data-aggregate"; //Change collection name
	private String topologyMongoCollection_ping_collection = "daily-report-ping-collection"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "TCP Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "daily_report_ping"; 
	private Document document;
	private Date timestamp;
	public int Box_GIST1_ping=0,Box_PH_ping=0,Box_HUST_ping=0,Box_CHULA_ping=0,Box_MYREN_ping=0,Box_GIST3_ping=0, Box_GIST0_ping=0, ping_record_total_today=0; 
	
	public int count_oneday_ping_SmartX_Box=0,count_oneday_ping_SmartX_Box_GIST1=0, count_oneday_ping_SmartX_Box_PH=0, count_oneday_ping_SmartX_Box_HUST=0, count_oneday_ping_SmartX_Box_CHULA=0, 
			count_oneday_ping_SmartX_Box_MYREN=0, count_oneday_ping_SmartX_Box_GIST3=0, count_oneday_ping_SmartX_Box_GIST_0=0;
	public List<Document> documents_OneDay;
	private Document document_pingscore;
	private Document document_collection;
	public float uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH=0,uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_PH=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_PH=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_PH=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST0=0;
	
	
	private DecimalFormat f = new DecimalFormat("##.00");
	public get_ping_latency_aggregate(MongoDB_Connector mongoConnector2) {
		mongoConnector = mongoConnector2;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("************************************************Ping Timer Called************************************************************** ");
		try {
			this.get_pinglatency();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}

	@SuppressWarnings("unchecked")
	private void get_pinglatency() throws ParseException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
						.getCollection(topologyMongoCollection_ping);
				
				
				  
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
				
					//startDate = simpleDateFormat.parse(date);
					//endDate = simpleDateFormat.parse(date1);
					
					BasicDBObject query1 = new BasicDBObject("timestamp", new BasicDBObject("$gte",new Date(System.currentTimeMillis() - (12 * 60 * 60 * 1000))).append("$lt",new Date(System.currentTimeMillis())));
					
					BasicDBObject query2 = new BasicDBObject("timestamp", new BasicDBObject("$gte",today_start.MILLISECOND - (12 * 60 * 60 * 1000)).append("$lt",today_start.MILLISECOND));
					
					FindIterable<Document> findCursor
			        = collection_process.find(
			            Filters.and(
			                Filters.gte("timestamp", new Date(System.currentTimeMillis() - (6 * 60 * 60 * 1000))),
			                Filters.lte("timestamp", new Date(System.currentTimeMillis())))); //not using this code
					
					
					
					List<Document> documents = collection_process.find().into(new ArrayList<Document>());
					
					
					List<Document> documents1 = collection_process.find(query2).into(new ArrayList<Document>());
					documents_OneDay = new ArrayList<Document>();
			       
			        System.out.println(collection_process.find(query1));
			        System.out.println("Collection Count for todays Ping data");
			        System.out.println(collection_process.count());
			        
			        //ping_record_total_today=(int) collection_process.count();
			        /*for (Document document1 : findCursor) 
					{
					System.out.println("Printing last 6 hours Ping data only using cursor");
					System.out.println(document1);
							//System.out.println(document1.getString("timestamp"));
					}*/ 
			        Calendar today = Calendar.getInstance();
			        today.set(Calendar.HOUR_OF_DAY, 0); 
			        String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());//2018/07/03
			        String date_check = sdf.parse(date).toString();//Tue Jul 03 00:00:00 KST 2018
			        String yesterday_timeStamp = getYesterdayDateString();
			        String today_timeStamp = getTodayDateString();
			        //String timeStamp =getYesterdayDateString();
		        	
			        //String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HHmmss").format(Calendar.getInstance().getTime());
			        
			        for (Document document : documents) 
					{
			        	//System.out.println(document);
			        	
			        	String date_check_timestamp= document.getString("timestamp");

			        	//System.out.println(date_check);//Tue Jul 03 00:00:00 KST 2018
			        	//System.out.println(date_check_timestamp); //2018/07/03 16:00:25 KST		 
			        	//System.out.println(today.getTime());// Tue Jul 03 00:16:00 KST 2018
			        	//System.out.println(timeStamp);//2018/07/03
			        	//String custom_date="2018/07/16";
			        	if (document.getString("timestamp").contains(/*timeStamp*/today_timeStamp/*yesterday_timeStamp*//*"2018/11/26"*/))
			        	{
			        		//System.out.println(document);
			        		//System.out.println("Printing one day (today) ping data only from documents_OneDay variable");
			        		if(!document.containsValue("-1"))
			        		{
			        			documents_OneDay.add(document);
			        		}
			        		
			        		//System.out.println(documents_OneDay);
			        	}
			        	/*if (document.getString("timestamp").contains("2018/10/06 04:56:31 KST"))
			        	{
			        		//System.out.println(document);
			        		//System.out.println("Printing one day (today) ping data only from documents_OneDay variable");
			        		if(!document.containsValue("-1"))
			        		{
			        			documents_OneDay.add(document);
			        		}
			        		
			        		//System.out.println(documents_OneDay);
			        	}*/
			        	
					}
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-GIST              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	System.out.println("Printing one day (today) ping data only from documents_OneDay variable");
			        	System.out.println(document);
					}
			        
			       /* for (Document document : documents) 
					{
			        	if (document.getString("timestamp")==(sdf.parse(date)).toString())//does not match, both giving different dates 
			        	{
			        		documents_OneDay= (List<Document>) document;
			        		//System.out.println("Printing one day (today) ping data only from documents_OneDay variable");
			        		//System.out.println(documents_OneDay);
			        		
			        		
			        		if (document.getString("SmartX-Box-GIST1")=="1")
			        		{
			        			Box_GIST1_ping+=1;
			        		}
			        		if (document.getString("SmartX-Box-PH")=="1")
			        		{
			        			Box_PH_ping+=1;
			        		}
			        		if (document.getString("SmartX-Box-HUST")=="1")
			        		{
			        			Box_HUST_ping+=1;
			        		}
			        		if (document.getString("SmartX-Box-CHULA")=="1")
			        		{
			        			Box_CHULA_ping+=1;
			        		}
			        		if (document.getString("SmartX-Box-MYREN")=="1")
			        		{
			        			Box_MYREN_ping+=1;
			        		}
			        		if (document.getString("SmartX-Box-GIST3")=="1")
			        		{
			        			Box_GIST3_ping+=1;
			        		}
			        	}
			        	
			        	//System.out.println(document);
					}*/
			        document_pingscore = new Document();
			        
			        String date_time = "Daily-report-ping-data"+"_"+simpleDateFormat_startDate.format(timestamp);
			        String Ping_collection = "Daily-report-total_ping"+"_"+simpleDateFormat_startDate.format(timestamp);
					String csvFile = date_time.replace("/", "")+".csv";
					String Ping_collection_day = Ping_collection.replace("/", "")+".csv";
					
					try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/Daily-report-ping-data/"+csvFile, true);

							BufferedWriter bw = new BufferedWriter(fw);

							PrintWriter out = new PrintWriter(bw))
					{
						File file = new File("/home/netcs/active_monitoring/Daily-report-ping-data/"+csvFile);
						if (file.length() == 0)
				        {
				        	out.println("Date"+" "+"SOURCE-SITE"+" "+"SmartX-Box-GIST1"+" "+"SmartX-Box-PH"+" "+"SmartX-Box-HUST"+" "+"SmartX-Box-CHULA"+" "+"SmartX-Box-MYREN"+" "+"SmartX-Box-GIST3");	
				        }
					
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-GIST1              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-GIST1"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_ping(document);
			        	}
					}
			        System.out.println("Printing count_oneday_ping_SmartX_Box and ping count at each site" );
			        System.out.println("SmartX_Box_GIST1:"+"-"+ " " +Box_PH_ping+" "+Box_HUST_ping+" "+Box_CHULA_ping+" "+Box_MYREN_ping+" "+Box_GIST3_ping+ " " +Box_GIST0_ping);
			        System.out.println("Total Count: "+ count_oneday_ping_SmartX_Box);
			        count_oneday_ping_SmartX_Box_GIST1=count_oneday_ping_SmartX_Box;
			        
			        
			        //float uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST1= (Box_GIST1_ping/count_oneday_ping_SmartX_Box)*100;
			        if(count_oneday_ping_SmartX_Box>0) 
			        {
			        
			        	
						
			        uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH=round(((float) Box_PH_ping/count_oneday_ping_SmartX_Box)*100,2);
			        
			        //uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH=(Float.isNaN(uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH)) ? 0 : uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH;
			        
					
			        //uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_HUST= ((float) Box_HUST_ping/(count_oneday_ping_SmartX_Box)*100);
			        uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_HUST=round(((float) Box_HUST_ping/count_oneday_ping_SmartX_Box)*100,2);
			        uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_CHULA=round(((float) Box_CHULA_ping/count_oneday_ping_SmartX_Box)*100,2);
			        uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_MYREN=round(((float) Box_MYREN_ping/count_oneday_ping_SmartX_Box)*100,2);
			        uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST3=round(((float) Box_GIST3_ping/count_oneday_ping_SmartX_Box)*100,2);
			        //uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST0=round(((float) Box_GIST0_ping/count_oneday_ping_SmartX_Box)*100,2);
			        System.out.println("Printing count_oneday_ping_SmartX_Box, uptime, Downtime" );
			        System.out.println("-"+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH+ " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_HUST + " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_CHULA + " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_MYREN + " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST3 + " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST0);
			        }			        
			        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			        
				        document_pingscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(timestamp));
				        document_pingscore.put("SmartX-Box-SOURCE",   "SmartX_Box_GIST1");
			        document_pingscore.put("SmartX-Box-GIST1",   "-");
			        document_pingscore.put("SmartX-Box-PH",  uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH);
			        document_pingscore.put("SmartX-Box-HUST",  uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_HUST);
			        document_pingscore.put("SmartX-Box-CHULA",  uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_CHULA);
			        document_pingscore.put("SmartX-Box-MYREN",  uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_MYREN);
			        document_pingscore.put("SmartX-Box-GIST3",  uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST3);
			        
					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_aggregate).insertOne(document_pingscore);
					System.out.println(document_pingscore.getString("timestamp")+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" "+document_pingscore.getDouble("SmartX_Box_GIST1")+" "+document_pingscore.getDouble("SmartX_Box_PH")+" "+document_pingscore.getDouble("SmartX_Box_HUST")+" "+document_pingscore.getDouble("SmartX_Box_CHULA")+" "+document_pingscore.getDouble("SmartX_Box_MYREN")+" "+document_pingscore.getDouble("SmartX_Box_GIST3"));
					
					
				    out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" "+"-"+ " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH+"%"+ " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_HUST+"%" + " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_CHULA+"%" + " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_MYREN+"%" + " "+uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST3+"%");
						//more code

						//more code
					
					
					document_pingscore.clear();
			        reset_values();
			        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        ///////////////////////////////////////////            SmartX-Box-PH              /////////////////////////////////////////
			        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-PH"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_ping(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_ping_SmartX_Box_PH and ping count at each site" );
			        System.out.println("SmartX_Box_PH:"+ " " +Box_GIST1_ping+" "+"-"+" "+Box_HUST_ping+" "+Box_CHULA_ping+" "+Box_MYREN_ping+" "+Box_GIST3_ping+ " " +Box_GIST0_ping);
			        System.out.println("Total Count: "+ count_oneday_ping_SmartX_Box);
			        count_oneday_ping_SmartX_Box_PH=count_oneday_ping_SmartX_Box;
			        if(count_oneday_ping_SmartX_Box>0) 
			        {
			        	
			        	
			        
			        uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST1=round((((float) Box_GIST1_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //float uptime_oneday_ping_SmartX_Box_PH_dest_Box_PH= (Box_PH_ping/count_oneday_ping_SmartX_Box)*100;
			        uptime_oneday_ping_SmartX_Box_PH_dest_Box_HUST=round((((float) Box_HUST_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_PH_dest_Box_CHULA=round((((float) Box_CHULA_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_PH_dest_Box_MYREN=round((((float) Box_MYREN_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST3=round((((float) Box_GIST3_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST0=round((((float) Box_GIST0_ping/count_oneday_ping_SmartX_Box)*100),2);
			        System.out.println("Printing count_oneday_ping_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST1+ " "+"-"+uptime_oneday_ping_SmartX_Box_PH_dest_Box_HUST + " "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_CHULA + " "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_MYREN + " "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST3 + " "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST0);
			        }
			        document_pingscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(timestamp));
			        document_pingscore.put("SmartX-Box-SOURCE",   "SmartX_Box_PH");
			        document_pingscore.put("SmartX-Box-GIST1",   uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST1);
			        document_pingscore.put("SmartX-Box-PH",  "-");
			        document_pingscore.put("SmartX-Box-HUST",  uptime_oneday_ping_SmartX_Box_PH_dest_Box_HUST);
			        document_pingscore.put("SmartX-Box-CHULA",  uptime_oneday_ping_SmartX_Box_PH_dest_Box_CHULA);
			        document_pingscore.put("SmartX-Box-MYREN",  uptime_oneday_ping_SmartX_Box_PH_dest_Box_MYREN);
			        document_pingscore.put("SmartX-Box-GIST3",  uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST3);
			        
					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_aggregate).insertOne(document_pingscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST1+"%"+ " "+"-"+ " "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_HUST+"%" + " "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_CHULA+"%" + " "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_MYREN+"%" + " "+uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST3+"%");
					document_pingscore.clear();
			        reset_values();
			        
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-HUST              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-HUST"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_ping(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_ping_SmartX_Box_HUST and ping count at each site" );
			        System.out.println("SmartX_Box_HUST:"+ " " +Box_GIST1_ping+" "+" "+Box_PH_ping+" -"+" "+Box_CHULA_ping+" "+Box_MYREN_ping+" "+Box_GIST3_ping+ " " +Box_GIST0_ping);
			        System.out.println("Total Count: "+ count_oneday_ping_SmartX_Box);
			        count_oneday_ping_SmartX_Box_HUST=count_oneday_ping_SmartX_Box;
			        if(count_oneday_ping_SmartX_Box>0) 
			        {
			        	
			        uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1=round((((float) Box_GIST1_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1= Float.isNaN(uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1) ? 0: uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1; 
			        	
			        uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH=round((((float) Box_PH_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH= Float.isNaN(uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH) ? 0: uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH;
			        //float uptime_oneday_ping_SmartX_Box_HUST_dest_Box_HUST= Box_HUST_ping/(count_oneday_ping_SmartX_Box)*100;
			        
			        uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA=round((((float) Box_CHULA_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA= Float.isNaN(uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA) ? 0: uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA;
			        
			        uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN=round((((float) Box_MYREN_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN= Float.isNaN(uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN) ? 0: uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN;
			        
			        uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3=round((((float) Box_GIST3_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3= Float.isNaN(uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3) ? 0: uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3;
			        
			        //uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST0=round((((float) Box_GIST0_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST0= Float.isNaN(uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST0) ? 0: uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST0;
			        System.out.println("Printing count_oneday_ping_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1+ " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH + " "+"-"+ " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA + " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN + " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3 + " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST0);
			        }
			        document_pingscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(timestamp));
			        document_pingscore.put("SmartX-Box-SOURCE",   "SmartX_Box_HUST");
			        document_pingscore.put("SmartX-Box-GIST1",   uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1);
			        document_pingscore.put("SmartX-Box-PH",  uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH);
			        document_pingscore.put("SmartX-Box-HUST",  "-");
			        document_pingscore.put("SmartX-Box-CHULA",  uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA);
			        document_pingscore.put("SmartX-Box-MYREN",  uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN);
			        document_pingscore.put("SmartX-Box-GIST3",  uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3);
			        
					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_aggregate).insertOne(document_pingscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1+"%"+ " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH+"%" + " "+"-"+ " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA+"%" + " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN+"%" + " "+uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3+"%");
					document_pingscore.clear();
			        reset_values();
			        
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-CHULA              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-CHULA"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_ping(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_ping_SmartX_Box_CHULA and ping count at each site" );
			        System.out.println("SmartX_Box_CHULA:"+ " " +Box_GIST1_ping+" "+Box_PH_ping+" "+Box_HUST_ping+" "+"-"+" "+Box_MYREN_ping+" "+Box_GIST3_ping+ " " +Box_GIST0_ping);
			        System.out.println("Total Count: "+ count_oneday_ping_SmartX_Box);
			        count_oneday_ping_SmartX_Box_CHULA=count_oneday_ping_SmartX_Box;
			        if(count_oneday_ping_SmartX_Box>0) 
			        {
			        	
			        	
			       	uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST1=round((((float) Box_GIST1_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_PH=round((((float) Box_PH_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_HUST=round((((float) Box_HUST_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //float uptime_oneday_ping_SmartX_BoxCHULA_dest_Box_CHULA=(float) Box_CHULA_ping/(count_oneday_ping_SmartX_Box)*100;
			        uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_MYREN=round((((float) Box_MYREN_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST3=round((((float) Box_GIST3_ping/count_oneday_ping_SmartX_Box)*100),2);
			       // uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST0=round((((float) Box_GIST0_ping/count_oneday_ping_SmartX_Box)*100),2);
			        System.out.println("Printing count_oneday_ping_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST1+ " "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_PH +" "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_HUST+ " "+"-"+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_MYREN + " "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST3 + " "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST0);
			        }
			        document_pingscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(timestamp));
			        document_pingscore.put("SmartX-Box-SOURCE",   "SmartX_Box_CHULA");
			        document_pingscore.put("SmartX-Box-GIST1",   uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST1);
			        document_pingscore.put("SmartX-Box-PH",  uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_PH);
			        document_pingscore.put("SmartX-Box-HUST",  uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_HUST);
			        document_pingscore.put("SmartX-Box-CHULA",  "-");
			        document_pingscore.put("SmartX-Box-MYREN",  uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_MYREN);
			        document_pingscore.put("SmartX-Box-GIST3",  uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST3);
			        
					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_aggregate).insertOne(document_pingscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST1+"%"+ " "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_PH+"%" +" "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_HUST+"%"+ " "+"-"+ " "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_MYREN+"%" + " "+uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST3+"%");
					document_pingscore.clear();
			        reset_values();
			        
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-MYREN              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-MYREN"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_ping(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_ping_SmartX_Box_MYREN and ping count at each site" );
			        System.out.println("SmartX_Box_MYREN:"+ " " +Box_GIST1_ping+" "+Box_PH_ping+" "+Box_HUST_ping+" "+Box_CHULA_ping+" "+"-"+Box_GIST3_ping+ " " +Box_GIST0_ping);
			        System.out.println("Total Count: "+ count_oneday_ping_SmartX_Box);
			        count_oneday_ping_SmartX_Box_MYREN=count_oneday_ping_SmartX_Box;
			        if(count_oneday_ping_SmartX_Box>0) 
			        {
			        	
			        		
			       	uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST1=round((((float) Box_GIST1_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_PH=round((((float) Box_PH_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_HUST= round((((float) Box_HUST_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_CHULA=round((((float) Box_CHULA_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //float uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_MYREN=(float) Box_MYREN_ping/(count_oneday_ping_SmartX_Box)*100;
			        uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST3=round((((float) Box_GIST3_ping/count_oneday_ping_SmartX_Box)*100),2);
			       // uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST0=round((((float) Box_GIST0_ping/count_oneday_ping_SmartX_Box)*100),2);
			        System.out.println("Printing count_oneday_ping_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST1+ " "+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_PH + " "+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_HUST + " "+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_CHULA+" "+"-"+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST3 + " "+"-"+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST0);
			        }
			        document_pingscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(timestamp));
			        document_pingscore.put("SmartX-Box-SOURCE",   "SmartX_Box_MYREN");
			        document_pingscore.put("SmartX-Box-GIST1",   uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST1);
			        document_pingscore.put("SmartX-Box-PH",  uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_PH);
			        document_pingscore.put("SmartX-Box-HUST",  uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_HUST);
			        document_pingscore.put("SmartX-Box-CHULA",  uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_CHULA);
			        document_pingscore.put("SmartX-Box-MYREN",  "-");
			        document_pingscore.put("SmartX-Box-GIST3",  uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST3);
			        
					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_aggregate).insertOne(document_pingscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST1+"%"+ " "+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_PH+"%" + " "+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_HUST+"%" + " "+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_CHULA+"%"+" "+"-"+ " "+uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST3+"%");
					document_pingscore.clear();
			        reset_values();
			        
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-GIST3              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-GIST3"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_ping(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_ping_SmartX_Box_GIST3 and ping count at each site" );
			        System.out.println("SmartX_Box_GIST3:"+ " " +Box_GIST1_ping+" "+Box_PH_ping+" "+Box_HUST_ping+" "+Box_CHULA_ping+" "+Box_MYREN_ping+ " "+"- "+Box_GIST0_ping);
			        System.out.println("Total Count: "+ count_oneday_ping_SmartX_Box);
			        count_oneday_ping_SmartX_Box_GIST3=count_oneday_ping_SmartX_Box;
			        if(count_oneday_ping_SmartX_Box>0) 
			        {
			        	
			        	
			        	
			       	uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST1=round((((float) Box_GIST1_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_PH=round((((float) Box_PH_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_HUST= round((((float) Box_HUST_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_CHULA=round((((float) Box_CHULA_ping/count_oneday_ping_SmartX_Box)*100),2);
			        uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_MYREN=round((((float) Box_MYREN_ping/count_oneday_ping_SmartX_Box)*100),2);
			        //float uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST3=(float) Box_GIST3_ping/(count_oneday_ping_SmartX_Box)*100;
			       // uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST0=round((((float) Box_GIST0_ping/count_oneday_ping_SmartX_Box)*100),2);
			        System.out.println("Printing count_oneday_ping_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST1+ " "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_PH + " "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_HUST +" "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_CHULA+ " "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_MYREN + " "+"-");
			        }
			        
			        document_pingscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(timestamp));
			        document_pingscore.put("SmartX-Box-SOURCE",   "SmartX_Box_GIST3");
			        document_pingscore.put("SmartX-Box-GIST1",   uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST1);
			        document_pingscore.put("SmartX-Box-PH",      uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_PH);
			        document_pingscore.put("SmartX-Box-HUST",    uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_HUST);
			        document_pingscore.put("SmartX-Box-CHULA",   uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_CHULA);
			        document_pingscore.put("SmartX-Box-MYREN",   uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_MYREN);
			        document_pingscore.put("SmartX-Box-GIST3",    "-");
			        
					try
					{
						mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_aggregate).insertOne(document_pingscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST1+"%"+" "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_PH+"%" + " "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_HUST+"%" +" "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_CHULA+"%"+ " "+uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_MYREN+"%" + " "+"-");
			        
					}
					catch (Exception e) {
						//exception handling left as an exercise for.the reader
					}
				
			        
					document_pingscore.clear();
			        reset_values();

					try(FileWriter fw_collection = new FileWriter("/home/netcs/active_monitoring/Daily-report-ping-collection/"+Ping_collection_day, true);

							BufferedWriter bw_collection = new BufferedWriter(fw_collection);

							PrintWriter out_collection = new PrintWriter(bw_collection))
					{
						File file_collection = new File("/home/netcs/active_monitoring/Daily-report-ping-collection/"+Ping_collection_day);
						if (file_collection.length() == 0)
				        {
							out_collection.println("Date"+" "+"Expected_Collection"+" "+"SmartX-Box-GIST1"+" "+"SmartX-Box-PH"+" "+"SmartX-Box-HUST"+" "+"SmartX-Box-CHULA"+" "+"SmartX-Box-MYREN"+" "+"SmartX-Box-GIST3");	
				        }
					
						System.out.println(simpleDateFormatNoTime.format(timestamp)+" "+"144"+" "+count_oneday_ping_SmartX_Box_GIST1+" "+count_oneday_ping_SmartX_Box_PH+" "+count_oneday_ping_SmartX_Box_HUST+" "+count_oneday_ping_SmartX_Box_CHULA+" "+count_oneday_ping_SmartX_Box_MYREN+" "+count_oneday_ping_SmartX_Box_GIST3);
						out_collection.println(simpleDateFormatNoTime.format(timestamp)+" "+"144"+" "+count_oneday_ping_SmartX_Box_GIST1+" "+count_oneday_ping_SmartX_Box_PH+" "+count_oneday_ping_SmartX_Box_HUST+" "+count_oneday_ping_SmartX_Box_CHULA+" "+count_oneday_ping_SmartX_Box_MYREN+" "+count_oneday_ping_SmartX_Box_GIST3);
						document_collection = new Document();
						
						document_collection.put("timestamp",  simpleDateFormatNoTime.format(timestamp)/*"2018/11/28"*/);
						document_collection.put("Expected Collection",   "144");
						document_collection.put("SmartX-Box-GIST1",   count_oneday_ping_SmartX_Box_GIST1);
						document_collection.put("SmartX-Box-PH",      count_oneday_ping_SmartX_Box_PH);
						document_collection.put("SmartX-Box-HUST",    count_oneday_ping_SmartX_Box_HUST);
						document_collection.put("SmartX-Box-CHULA",   count_oneday_ping_SmartX_Box_CHULA);
						document_collection.put("SmartX-Box-MYREN",   count_oneday_ping_SmartX_Box_MYREN);
						document_collection.put("SmartX-Box-GIST3",    count_oneday_ping_SmartX_Box_GIST3);
						mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_collection).insertOne(document_collection);
					}
					 catch (IOException e) {
							//exception handling left as an exercise for the reader
						}
					
					
					document_pingscore.clear();
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
			        	System.out.println("Printing one day (today) ping data only");
			        	System.out.println(document);
					}*/
	}

	private String getYesterdayDateString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(yesterday());
	}
	private Date yesterday() {
		// TODO Auto-generated method stub
		final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    return cal.getTime();
	}
	private String getTodayDateString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(today());
	}
	private Date today() {
		// TODO Auto-generated method stub
		final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, 0);
	    return cal.getTime();
	}

	private void reset_values() {
		Box_GIST1_ping=0;Box_PH_ping=0;Box_HUST_ping=0;Box_CHULA_ping=0;Box_MYREN_ping=0;Box_GIST3_ping=0; Box_GIST0_ping=0; ping_record_total_today=0; 
		count_oneday_ping_SmartX_Box=0; /*count_oneday_ping_SmartX_Box_PH=0; count_oneday_ping_SmartX_Box_HUST=0; count_oneday_ping_SmartX_Box_CHULA=0; 
				count_oneday_ping_SmartX_Box_MYREN=0; count_oneday_ping_SmartX_Box_GIST3=0; count_oneday_ping_SmartX_Box_GIST_0=0*/;
		
	}

	private void calculate_ping(Document document) {
		
		count_oneday_ping_SmartX_Box+=1;
		if (document.getString("SmartX-Box-GIST1").contains("1"))
		{
			Box_GIST1_ping+=1;
		}
		if (document.getString("SmartX-Box-PH").contains("1"))
		{
			Box_PH_ping+=1;
		}
		if (document.getString("SmartX-Box-HUST").contains("1"))
		{
			Box_HUST_ping+=1;
		}
		if (document.getString("SmartX-Box-CHULA").contains("1"))
		{
			Box_CHULA_ping+=1;
		}
		if (document.getString("SmartX-Box-MYREN").contains("1"))
		{
			Box_MYREN_ping+=1;
		}
		if (document.getString("SmartX-Box-GIST3").contains("1"))
		{
			Box_GIST3_ping+=1;
		}
		if (document.getString("SmartX-Box-GIST_NUC").contains("1"))
		{
			Box_GIST0_ping+=1;
		}
		
	}
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
