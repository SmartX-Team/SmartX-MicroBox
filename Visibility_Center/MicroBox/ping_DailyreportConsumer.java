package smartx.multiview.collectors.microbox;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import smartx.multiview.DataLake.Elasticsearch_Connector;
import smartx.multiview.DataLake.MongoDB_Connector;


import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

public class ping_DailyreportConsumer  implements Runnable {
	private static int Date = 0;
	private static int HOUR_OF_DAY=0;
	private static int MINUTE=0;
	private Thread thread;
	private String topologyMongoCollection_ping = "microbox-ping-data-raw"; //Change collection name
	private String topologyMongoCollection_ping_con="test_con";
	private String topologyMongoCollection_ping_aggregate = "microbox-daily-report-ping-data-aggregate"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "MicroBox Ping & Latency Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "microbox_daily_report_ping"; 
	private Document document, document_con;
	private Date timestamp;
	private String ESindex = "pers_ping_es";
	private Elasticsearch_Connector ESConnector;
	
	

	public ping_DailyreportConsumer(String bootstrapserver, MongoDB_Connector MongoConn,Elasticsearch_Connector eSConnector2) {
		
		bootstrapServer        = bootstrapserver;
		mongoConnector         = MongoConn;
		
		
		
		ESConnector         = eSConnector2;
	}

	@Override
	public void run() {
		System.out.println("Running "+ThreadName);
		//String Date,HOUR_OF_DAY,MINUTE;
		
		
		
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

			JSONArray remarks = (JSONArray) jsonObject.get("remarks");
			//converting the JSONObject into JSONArray as remark was an array.
			Iterator<String> iterator = remarks.iterator();
			//Iterator is used to access the each element in the list 
			//loop will continue as long as there are elements in the array.
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
				//accessing each elemnt by using next function.
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*String pathname="/Multiview-Custom-Collectors/resources/timer.json";
		JSONObject obj;
		try (InputStream input = new FileInputStream(pathname)) {
		    obj = new JSONObject(new JSONTokener(input));
		    System.out.println("obj.getJSONObject(\"Date\""+obj.getJSONObject("Date"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, this.Date);
		today.set(Calendar.HOUR_OF_DAY, this.HOUR_OF_DAY);
		today.set(Calendar.MINUTE, this.MINUTE);
		today.set(Calendar.SECOND, 0);
		
		
		/*Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, 0);
		today.set(Calendar.HOUR_OF_DAY,6);
		today.set(Calendar.MINUTE, 56);
		today.set(Calendar.SECOND, 0);*/

		// every night at 2am you run your task
		Timer timer = new Timer();
		timer.schedule(new get_ping_aggregate(mongoConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		Timer timer1 = new Timer();
		timer1.schedule(new get_ping_elasticsearch_aggregate(mongoConnector,ESConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		
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
		document_con=new Document();
		document_con.put("test", "");
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
					 String mongoStatus=  mongoConnector.getDbConnection().toString();
					 System.out.println("mongoStatus:"+mongoStatus);
					 
				 } catch (MongoException e) {
					 System.out.println("Error Reading MongoDB  ");
				 }*/
				
				
				
				/*try {
					get_daily_ping_aggregate();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				while (true) 
				{
					 try {
            			 ESConnector.checkCon(ESindex);
            			 mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_con).insertOne(document_con);
            			 document_con.clear();
    					 ConsumerRecords<String, String> records = consumer.poll(0);
    					 for (ConsumerRecord<String, String> record : records)
					{

						ping_collection(record.value());
						System.out.println("Writing the ping data at table: daily_report_ping_data_raw  "+record.value());
					}
    				 } catch (IOException e) {
    					 System.out.println("Error Reading elastic Search  ");
    				 }
					 
					
					
					
					
				}
	}

	private void ping_collection(String record) {
		// TODO Auto-generated method stub
		document = new Document();
		
		String[] record_values = record.split(" ");
		System.out.println("********Arrays.toString(record_values)***********");
		System.out.println(Arrays.toString(record_values));

		if (record_values.length>2)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();
		  //  System.out.println("Local Time: " + sdf.format(timestamp));
			//System.out.println(record_values[2]);
			//document.put("timestamp",   (record_values[0]+" "+record_values[1]));
			
			
			
			Calendar today_now = Calendar.getInstance();
			
			
			/*if (record_values.length>5)
			{
			document.put("timestamp",   sdf.format(timestamp));
			document.put("microbox-SOURCE",   record_values[2]);
			document.put("microbox-gist-1",    record_values[3].isEmpty() ? "0" : record_values[3]);
			document.put("microbox-gist-2",    record_values[4].isEmpty() ? "0" : record_values[4]);
			document.put("microbox-um-1",    record_values[5].isEmpty() ? "0" : record_values[5]);
			document.put("microbox-um-2",    record_values[6].isEmpty() ? "0" : record_values[6]);
			document.put("microbox-rub-1",    record_values[7].isEmpty() ? "0" : record_values[7]);
			document.put("microbox-drukren-1",    record_values[8].isEmpty() ? "0" : record_values[8]);
			document.put("microbox-vnu-1",    record_values[9].isEmpty() ? "0" : record_values[9]);
			document.put("microbox-ptit",    record_values[10].isEmpty() ? "0" : record_values[10]);
			
			//mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping).insertOne(document);
			System.out.println(document.get("microbox-SOURCE"));
			System.out.println(document.get("microbox-gist-1"));
			System.out.println(document.get("microbox-gist-2"));
			System.out.println(document.get("microbox-um-1"));
			System.out.println(document.get("microbox-um-2"));
			System.out.println(document.get("microbox-rub-1"));
			System.out.println(document.get("microbox-drukren-1"));
			System.out.println(document.get("microbox-vnu-1"));
			System.out.println(document.get("microbox-ptit"));
			System.out.println("DONE");
			document.clear();
			}*/
			
			/*if (record_values.length>3)
			{
				document.put("smartx-microbox-gist-1",   record_values[3]);
				//				document.put("SmartX-Box-ID",   record_values[4]);
				if (record_values.length>5)
				{
					document.put("SmartX-Box-PH",   record_values[4]);
					//				document.put("SmartX-Box-PKS",   record_values[6]);
					if (record_values.length>7)
					{
						document.put("SmartX-Box-HUST",   record_values[5]);
						//				document.put("SmartX-Box-GIST2",   record_values[8]);
						if (record_values.length>9 )
						{
							document.put("SmartX-Box-CHULA",   record_values[6]);
						}
						//				document.put("SmartX-Box-NCKU",   record_values[10]);
						//				document.put("SmartX-Box-MY",   record_values[11]);
						if (record_values.length>12 )
						{
							document.put("SmartX-Box-MYREN",    record_values[7].isEmpty() ? "0" : record_values[7]);
							document.put("SmartX-Box-GIST3",    record_values[8].isEmpty() ? "0" : record_values[8]);
						}
						if (record_values.length>14 )
						{
							document.put("SmartX-Box-GIST_NUC",   record_values[14].isEmpty() ? "0" : record_values[14]);	
						}
					}
				}
			}*/
	}
		
		
		try {
			whenLoadMultipleYAMLDocuments_thenLoadCorrectJavaObjects("/microbox_sites.yaml",record_values);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


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
	
	public void whenLoadMultipleYAMLDocuments_thenLoadCorrectJavaObjects(String path,String[] record_values) throws IOException {
		
        System.out.printf("-- loading from %s --%n", path);
        Yaml yaml = new Yaml();
        try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
            Iterable<Object> itr = yaml.loadAll(in);
            System.out.println("-- iterating loaded Iterable --");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();
            int i=3;
            document.put("timestamp", (record_values[0]+" "+record_values[1])/*  sdf.format(timestamp)*/);
			document.put("microbox-SOURCE",   record_values[2]);
            for (Object o : itr) {
                //System.out.println("element type: " + o.getClass());
                //System.out.println(o);
                //String partBeforeFullStop = ((String) o).split("\\=")[0];
                //System.out.println(partBeforeFullStop);
                String microbox_site = ((String) o).substring(0, ((String) o).indexOf('='));
                System.out.println("record_values[i] length:"+record_values.length);
                System.out.println("i:"+i);
               // System.out.println("(record_values[i]):"+(record_values[i]));
                
                
                if( !(record_values.length<=i))
                {
                	 System.out.println("TRUE");
                	 document.put(microbox_site,    record_values[i].isEmpty() ? "0" : record_values[i]);
                	//insert value in elastic search
                	 
                	 if (!record_values[i].contains("-")) 
                	 {
                		 System.out.println("Float.parseFloat(record_values[i]):"+Float.parseFloat(record_values[i]));
                		
                		 
                		 try {
                			 ESConnector.insertPingLatencyDataRaw(ESindex,/*simpleDateFormat.format*/timestamp, record_values[2].replace("-", "_"),microbox_site.replace("-", "_"),record_values[i].isEmpty() ? Float.parseFloat("0") :Float.parseFloat(record_values[i]));
        					 
        				 } catch (MongoException e) {
        					 System.out.println("Error Reading elasticSearch");
        				 }
                	 }
                	 
             		
                }
               
                System.out.println(microbox_site+","+document.get(microbox_site));
                
                
                
//                System.out.println(document.get(microbox_site));
    			i=i+1;
    			
    			
            }
            
            try {
            	mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping).insertOne(document);
				 
			 } catch (MongoException e) {
				 System.out.println("Error Reading MongoDB");
			 }
            
        }
	}
	
	public void start() {
		
		
		
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}
}
