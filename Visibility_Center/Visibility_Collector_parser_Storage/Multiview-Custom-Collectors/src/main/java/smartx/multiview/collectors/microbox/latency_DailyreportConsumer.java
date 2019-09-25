package smartx.multiview.collectors.microbox;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;
import org.yaml.snakeyaml.Yaml;
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


public class latency_DailyreportConsumer  implements Runnable {
	private static int Date = 0;
	private static int HOUR_OF_DAY=0;
	private static int MINUTE=0;
	private Thread thread;
	private String microboxMongoCollection_latency = "microbox-latency-data-raw"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "Microbox latency Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "microbox_daily_report_latency"; 
	private Document document;
	private Date timestamp;
	public String path="/microbox_sites.yaml";
	String sites_name[];
	int count_site=0,Number_of_sites=0, count_site_yaml;
	private Elasticsearch_Connector ESConnector;
	private String ESindex = "pers_latency_es";
	
	public latency_DailyreportConsumer(String bootstrapserver, MongoDB_Connector MongoConn, Elasticsearch_Connector eSConnector2) {
		
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
		
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, this.Date);
		today.set(Calendar.HOUR_OF_DAY, this.HOUR_OF_DAY);
		today.set(Calendar.MINUTE, this.MINUTE);
		today.set(Calendar.SECOND, 0);
		
		/*Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, 0);
		today.set(Calendar.HOUR_OF_DAY,7);
		today.set(Calendar.MINUTE, 20);
		today.set(Calendar.SECOND, 0);*/


		// every night at 2am you run your task
		Timer timer = new Timer();
		timer.schedule(new get_latency_aggregate(mongoConnector/*,ESConnector*/), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		Timer timer1 = new Timer();
		timer.schedule(new get_latency_elasticsearch_aggregate(mongoConnector,ESConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		
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
				try {
					count_site_yaml=getNumberofSites();
					sites_name=  new String[count_site_yaml];
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while (true) 
				{
					ConsumerRecords<String, String> records = consumer.poll(0);
					for (ConsumerRecord<String, String> record : records)
					{

						ping_collection(record.value());
						System.out.println("Writing the latency data at table: microbox_latency_data_raw  "+record.value());
					}

				}
	}

	private int getNumberofSites() throws IOException {
		// TODO Auto-generated method stub
	Map<String, Float> uptime_oneday_ping_microbox_hmap = new HashMap<String, Float>();
	String write_to_file = "";
	System.out.printf("-- loading from %s --%n", path);
	Yaml yaml = new Yaml();
	int siteCount = 0;
	try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
		Iterable<Object> itr = yaml.loadAll(in);
		
		System.out.println("-- iterating loaded Iterable --");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
		timestamp = new Date();
		
		
		
		
		
		for (Object o : itr) 															//We calcualcuate the total number of pings collected from each microbox to other microboxes
		{ 
			siteCount++;
		}
	}
		return siteCount;
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
			
			
			
			
			String write_to_file = "";
			
			
				
				
//				
				
				//We calculate the total number of pings collected from each microbox to other microboxes
				
				
			if (record_values.length>5)
			{
				
			}
			
			System.out.println("DONE");
			document.clear();
			
			
				
	}
		
		
		try {
			whenLoadMultipleYAMLDocuments_thenLoadCorrectJavaObjects("/microbox_sites.yaml",record_values);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}



	public void whenLoadMultipleYAMLDocuments_thenLoadCorrectJavaObjects(String path,String[] record_values) throws IOException {
		
        System.out.printf("-- loading from %s --%n", path);
        Yaml yaml = new Yaml();
        try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
            Iterable<Object> itr = yaml.loadAll(in);
            System.out.println("-- iterating loaded Iterable --");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();
            int i=3;
            document.put("timestamp",    (record_values[0]+" "+record_values[1])/*sdf.format(timestamp)*/);
			document.put("microbox-SOURCE",   record_values[2]);
            for (Object o : itr) {
                
                String microbox_site = ((String) o).substring(0, ((String) o).indexOf('='));
                
                //System.out.println("record_values[i] length:"+record_values.length);
              //  System.out.println("i:"+i);
              //  System.out.println("record_values[i]:"+record_values[i].toString());
                
                if( !(record_values.length<=i))
                {
                	 document.put(microbox_site,   record_values[i].isEmpty() ? "0" : record_values[i]);
                	 if (!record_values[i].contains("-")) 
                	 {
                		 
                		 ESConnector.insertPingLatencyDataRaw(ESindex,/*simpleDateFormat.format*/timestamp, record_values[2].replace("-", "_"),microbox_site.replace("-", "_"),record_values[i].isEmpty() ? Float.parseFloat("0") :Float.parseFloat(record_values[i]));
                	 }
                	 
                }
                System.out.println(microbox_site+","+document.get(microbox_site));
               
//                System.out.println(document.get(microbox_site));
    			i=i+1;
    			
            }
            
            mongoConnector.getDbConnection().getCollection(microboxMongoCollection_latency).insertOne(document);
        }
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}
}
