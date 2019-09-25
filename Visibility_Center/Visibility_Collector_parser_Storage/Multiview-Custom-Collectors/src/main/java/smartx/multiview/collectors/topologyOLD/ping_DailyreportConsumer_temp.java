package smartx.multiview.collectors.topologyOLD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.management.Query;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import smartx.multiview.DataLake.MongoDB_Connector;


public class ping_DailyreportConsumer_temp  implements Runnable {
	private Thread thread;
	private String topologyMongoCollection_ping = "daily-report-ping-data-raw"; //Change collection name
	private String topologyMongoCollection_ping_aggregate = "daily-report-ping-data-aggregate"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "TCP Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "daily_report_ping"; 
	private Document document;
	private Date timestamp;

	public ping_DailyreportConsumer_temp(String bootstrapserver, MongoDB_Connector MongoConn) {
		
		bootstrapServer        = bootstrapserver;
		mongoConnector         = MongoConn;
	}

	@Override
	public void run() {
		System.out.println("Running "+ThreadName);
		
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 01);
		today.set(Calendar.MINUTE, 07);
		today.set(Calendar.SECOND, 0);

		// every night at 2am you run your task
		Timer timer = new Timer();
		timer.schedule(new get_ping_latency_aggregate_temp(mongoConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		
		
		try {
			this.Consume();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

	private void Consume()  throws IOException {
		// TODO Auto-generated method stub
		//Kafka & Zookeeper Properties
				Properties props = new Properties();
				props.put("bootstrap.servers", bootstrapServer);
				props.put("group.id", "test");
				props.put("enable.auto.commit", "true");
				props.put("auto.commit.interval.ms", "1000");
				props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
				props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
				KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
				consumer.subscribe(Arrays.asList(topic));
				
				/*try {
					get_daily_ping_aggregate();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				while (true) 
				{
					ConsumerRecords<String, String> records = consumer.poll(0);
					for (ConsumerRecord<String, String> record : records)
					{

						ping_collection(record.value());
						System.out.println("Writing the ping data at table: daily_report_ping_data_raw  "+record.value());
					}
					
					
				}
	}

	private void ping_collection(String record) {
		// TODO Auto-generated method stub
		document = new Document();
		String[] record_values = record.split(" ");
		System.out.println("********Arrays.toString(record_values)***********");
		System.out.println(Arrays.toString(record_values));
//		System.out.printf("Length:%d",record_values.length);
//		System.out.println(record_values[0]);
//		System.out.println(record_values[1]);
		if (record_values.length>2)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();
		  //  System.out.println("Local Time: " + sdf.format(timestamp));
			System.out.println(record_values[2]);
			//document.put("timestamp",   (record_values[0]+" "+record_values[1]));
			
			
			
			Calendar today_now = Calendar.getInstance();
			
			
			
			document.put("timestamp",   sdf.format(timestamp));
			document.put("SmartX-Box-SOURCE",   record_values[2]);
			if (record_values.length>3)
			{
				document.put("SmartX-Box-GIST1",   record_values[3]);
				//				document.put("SmartX-Box-ID",   record_values[4]);
				document.put("SmartX-Box-PH",   record_values[5]);
				//				document.put("SmartX-Box-PKS",   record_values[6]);
				document.put("SmartX-Box-HUST",   record_values[7]);
				//				document.put("SmartX-Box-GIST2",   record_values[8]);
				if (record_values.length>9 )
				{
					document.put("SmartX-Box-CHULA",   record_values[9]);
				}
				//				document.put("SmartX-Box-NCKU",   record_values[10]);
				//				document.put("SmartX-Box-MY",   record_values[11]);
				if (record_values.length>12 )
				{
				document.put("SmartX-Box-MYREN",    record_values[12].isEmpty() ? "0" : record_values[12]);
				document.put("SmartX-Box-GIST3",    record_values[13].isEmpty() ? "0" : record_values[13]);
				}
				if (record_values.length>14 )
				{
					document.put("SmartX-Box-GIST_NUC",   record_values[14].isEmpty() ? "0" : record_values[14]);	
				}

			}
	}
		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping).insertOne(document);
		document.clear();
		
		


}
	/*private void get_daily_ping_aggregate() throws ParseException {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
				.getCollection(topologyMongoCollection_ping);
		
		FindIterable<Document> findCursor
        = collection_process.find(
            Filters.and(
                Filters.gte("timestamp", new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000))),
                Filters.lte("timestamp", new Date(System.currentTimeMillis())))); //not using this code
		
		  
		SimpleDateFormat simpleDateFormat_startDate = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat simpleDateFormat_endDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
		timestamp = new Date();
    	System.out.println("Local Time: " + simpleDateFormat_endDate.format(timestamp));
		
        String date =simpleDateFormat_startDate.format(timestamp)+ " 00:00:01 KST"; //start of the today's date
        
        String date1 =simpleDateFormat_endDate.format(timestamp); //Current data and time
       
        
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss zzz");  
        
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
			BasicDBObject query1 = new BasicDBObject("timestamp", new BasicDBObject("$gte",startDate).append("$lt",endDate ));
			BasicDBObject query1 = new BasicDBObject("timestamp", new BasicDBObject("$gte",today_start.getTime()).append("$lt",today_now.getTime()));
			
			
			
			List<Document> documents = collection_process.find(query1).into(new ArrayList<Document>());
			
	        collection_process.find(query1);
	       // collection_process.find("SelectedDate": {'$gte': startdate,'$lt': enddate}});
	        System.out.println(collection_process.find(query1));
	        System.out.println("Collection Count for todays data");
	        System.out.println(collection_process.count());
		 
        
        
        
		//collection_process.drop();//new
		List<Document> insertList = new ArrayList<Document>();
		
		 Date date11 = new Date();
	       
	        //collection_process.insertMany(documents);
	        System.out.println("Printing line 62-65");
	       
	       // MongoCursor<Document> doc = collection_process.find(new Document("date", date)).iterator();
	        //System.out.println(doc.next().getDate("date"));
	        
	        for (Document document1 : findCursor) 
			{
				
					
					System.out.println("Printing last 2 hours data only using cursor");
					System.out.println(document1);
					//System.out.println(document1.getString("timestamp"));
				}   
	        
	        for (Document document : documents) 
			{
	        	System.out.println("Printing one day (today) ping data only");
	        	System.out.println(document);
			}
	}*/

	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}
}
