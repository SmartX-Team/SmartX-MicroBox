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

public class vcenter_DailyreportConsumer1  implements Runnable {
	private static int Date = 0;
	private static int HOUR_OF_DAY=0;
	private static int MINUTE=0;
	private Thread thread;
	private String topologyMongoCollection_vcenter = "microbox-vcenter-data"; //Change collection name
	private String topologyMongoCollection_ping_aggregate = "microbox-daily-report-ping-data-aggregate"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "vcenter Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "microbox_daily_report_vcenter"; 
	private Document document;
	private Date timestamp;
	private Elasticsearch_Connector ESConnector;
	private String ESindex = "pers_vcenter_liveliness_es";
	
	

	public vcenter_DailyreportConsumer1(String bootstrapserver, MongoDB_Connector MongoConn, Elasticsearch_Connector eSConnector2) {
		
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
		today.set(Calendar.HOUR_OF_DAY,7);
		today.set(Calendar.MINUTE, 30);
		today.set(Calendar.SECOND, 0);*/

		// every night at 2am you run your task
		Timer timer = new Timer();
		timer.schedule(new get_vcenter_aggregate(mongoConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		Timer timer1 = new Timer();
		timer1.schedule(new get_vcenter_elasticsearch_aggregate(mongoConnector,ESConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		
		
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
						System.out.println("Writing the vcenter data at table: daily_report_vcenter_data_raw  "+record.value());
					}
					
					
				}
	}

	private void ping_collection(String record) {
		// TODO Auto-generated method stub
		timestamp = new Date();
		document = new Document();
		String[] record_values = record.split(" ");
		//System.out.println(Arrays.toString(record_values));
//		System.out.printf("Length:%d",record_values.length);
//		System.out.println(record_values[0]);
//		System.out.println(record_values[1]);
		if (record_values.length>2)
		{
			System.out.println(record_values[2]);
			document.put("timestamp",   (record_values[0]+" "+record_values[1]));
			document.put("SmartX-Box-SOURCE",   record_values[2]);
			if (record_values.length>3)
			{
				document.put("vcenter_Connection",   record_values[3]);
				
				
				if(!record_values[3].contains("-"))
				{
					//ESConnector.insertVcenterDataRaw(ESindex,/*simpleDateFormat.format*/timestamp, record_values[2].replace("-", "_"),Integer.parseInt(record_values[3]));
					try {
						ESConnector.insertVcenterDataRaw(ESindex,/*simpleDateFormat.format*/timestamp, record_values[2].replace("-", "_"),Integer.parseInt(record_values[3]));
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			else
			{
				document.put("vcenter_Connection", "0");
			}
				
		}
		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_vcenter).insertOne(document);
		document.clear();


}
	
	
	

	
	public void start() {
		
		
		
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}
}
