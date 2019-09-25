package smartx.multiview.collectors.microbox;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
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
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.jets3t.service.impl.soap.axis._2006_03_01.User;
import org.json.JSONTokener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
//import com.ibm.json.java.JSONObject;

import smartx.multiview.DataLake.Elasticsearch_Connector;
import smartx.multiview.DataLake.MongoDB_Connector;


public class collectd_DailyreportConsumer2  implements Runnable {
	private Thread thread;
	private String topologyMongoCollection_vcenter = "microbox-collectd-data"; //Change collection name
	//private String topologyMongoCollection_vcenter = "daily-report-vcenter-data-raw"; //Change collection name
	private String ThreadName = "collectd Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	//private String topic = "smartx-microbox-metrics"; 
	private Document document;
	private JSONParser parser = new JSONParser();
	private Elasticsearch_Connector ESConnector;
	private String ESindex_disk = "pers_collecd_disk";
	private String ESindex_load = "pers_collectd_load";
	private String ESindex_interface = "pers_collectd_interface";
	private String ESindex_memory = "pers_collectd_memory";
	private String ESindex_cpu = "pers_collectd_cpu";
	private String indexName;
	private long index;
	private Date timestamp;
	private String topic = "smartx-microbox-metrics"; 
	private String topic_collectd = "microbox-collectd"; 
	//private String topic_collectd = "microbox_collectd_cpu";
	private Document document_collectd;
	
	public collectd_DailyreportConsumer2(String bootstrapserver,MongoDB_Connector MongoConn, Elasticsearch_Connector eSConnector2) {
		
		bootstrapServer        = bootstrapserver;
		mongoConnector         = MongoConn;
		ESConnector         = eSConnector2;
		ESConnector.createIndex(ESindex_disk);
		ESConnector.createIndex(ESindex_load);
		ESConnector.createIndex(ESindex_memory);
		ESConnector.createIndex(ESindex_cpu);
		
		
	}

	@Override
	public void run() {
		System.out.println("Running "+ThreadName);
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 10);
		today.set(Calendar.MINUTE, 47);
		today.set(Calendar.SECOND, 0);

		// every night at 2am you run your task
		Timer timer = new Timer();
	//	timer.schedule(new get_vcenter_aggregate(mongoConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		try {
			this.Consume(ESConnector);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

	private void Consume(Elasticsearch_Connector ESConn)  throws IOException {
		// TODO Auto-generated method stub
		//Kafka & Zookeeper Properties
			//	ESConnector = ESConn;
				//index=ESConnector.createIndex(ESindex);
				
		
				Properties props = new Properties();
				props.put("bootstrap.servers", bootstrapServer);
				props.put("group.id", "test");
				props.put("enable.auto.commit", "true");
				props.put("auto.commit.interval.ms", "1000");
				props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
				props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
				
				System.out.println("starting kafka consumer:   ");
				KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
				consumer.subscribe(Arrays.asList(topic));
				while (true) 
				{
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
					//timestamp = new Date();
					ConsumerRecords<String, String> records = consumer.poll(0);
					System.out.println("Waiting for data");
					for (ConsumerRecord<String, String> record : records)
					{
						timestamp = new Date();
						
						System.out.println("Writing the collectd data at table:   "+record.value());
						
						 JSONParser parser = new JSONParser();
							JSONObject data;
							try {
								Object object = parser.parse(record.value());
								JSONArray array = (JSONArray) object;
								System.out.println("values:");
								//System.out.println(array.get(0));
								
								JSONObject jsonObject = (JSONObject) array.get(0);
								
								
								
								
								
								
								
								System.out.println("host:" + jsonObject.get("host").toString());
								System.out.println("plugin:" + jsonObject.get("plugin"));
								System.out.println("type_instance:" + jsonObject.get("type"));
								System.out.println("dstypes:");
								System.out.println("dstypes:" + jsonObject.get("dstypes"));
								System.out.println("dsnames:" + jsonObject.get("dsnames"));
								System.out.println("values:" + jsonObject.get("values"));
								
								System.out.println("plugin_instance:" + jsonObject.get("plugin_instance"));
								
								System.out.println("*************************************");
								
								
								 try {
									 	java.util.concurrent.TimeUnit.MILLISECONDS.sleep(300);
									 	document_collectd= new Document();
									 	
									 	long l = Double.valueOf(jsonObject.get("time").toString()).longValue();
									 	l = l * 1000;
									 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
									 	Date date = new Date(l);
									 	
									 	
									 	if(jsonObject.get("plugin").toString().contains("interface") && jsonObject.get("type").toString().contains("if_octets") && jsonObject.get("plugin_instance").toString().contains("eno0")) 
									 	{
									 		ESConnector.insertData1(ESindex_interface,date,jsonObject.get("dsnames").toString(),jsonObject.get("type").toString(),jsonObject.get("host").toString().replace("-", "_"),  jsonObject.get("plugin_instance").toString(),jsonObject.get("type_instance").toString(), jsonObject.get("plugin").toString(), jsonObject.get("values").toString()/*, dataBytes, frameSize*/);
									 	}
									 	
									 	
									 	if(jsonObject.get("plugin").toString().contains("load")) 
									 	{
									 		ESConnector.insertData1(ESindex_load,date,jsonObject.get("dsnames").toString(),jsonObject.get("type").toString(),jsonObject.get("host").toString().replace("-", "_"),  jsonObject.get("plugin_instance").toString(),jsonObject.get("type_instance").toString(), jsonObject.get("plugin").toString(), jsonObject.get("values").toString()/*, dataBytes, frameSize*/);	
									 	}
									 	if(jsonObject.get("plugin").toString().contains("memory")) 
									 	{
									 		ESConnector.insertData1(ESindex_memory,date,jsonObject.get("dsnames").toString(),jsonObject.get("type").toString(),jsonObject.get("host").toString().replace("-", "_"),  jsonObject.get("plugin_instance").toString(),jsonObject.get("type_instance").toString(), jsonObject.get("plugin").toString(), jsonObject.get("values").toString()/*, dataBytes, frameSize*/);	
									 	}
									 	if(jsonObject.get("type").toString().contains("cpu")) 
									 	{
									 		ESConnector.insertData1(ESindex_cpu,date,jsonObject.get("dsnames").toString(),jsonObject.get("type").toString(),jsonObject.get("host").toString().replace("-", "_"),  jsonObject.get("plugin_instance").toString(),jsonObject.get("type_instance").toString(), jsonObject.get("plugin").toString(), jsonObject.get("values").toString()/*, dataBytes, frameSize*/);
									 		if (jsonObject.get("type_instance").toString().contains("idle"))
									 		{
									 			String values_collectd=(jsonObject.get("values").toString());
									 			values_collectd=values_collectd.replaceAll("\\[", "").replaceAll("\\]","");
									 			System.out.println("************************************:");
									 			System.out.println(jsonObject.get("type_instance").toString());
									 			document_collectd.put("timestamp", sdf.format(date));
										 		document_collectd.put("microbox-SOURCE",jsonObject.get("host").toString());
										 		document_collectd.put("type_instance",jsonObject.get("type_instance").toString());
										 		document_collectd.put("values",round(Float.parseFloat(values_collectd),2));
										 		System.out.println("document_collectd"+document_collectd.toString());
										 		mongoConnector.getDbConnection().getCollection(topic_collectd).insertOne(document_collectd);	
									 		}
									 		
									 	}
									 	if(jsonObject.get("type").toString().contains("disk_io_time")) 
									 	{
									 		ESConnector.insertData1(ESindex_disk,date,jsonObject.get("dsnames").toString(),jsonObject.get("type").toString(),jsonObject.get("host").toString().replace("-", "_"),  jsonObject.get("plugin_instance").toString(),jsonObject.get("type_instance").toString(), jsonObject.get("plugin").toString(), jsonObject.get("values").toString()/*, dataBytes, frameSize*/);	
									 	}
									 	
									 	//ESConnector.insertData1(ESindex,date,jsonObject.get("dsnames").toString(),jsonObject.get("type").toString(),jsonObject.get("host").toString().replace("-", "_"),  jsonObject.get("plugin_instance").toString(),jsonObject.get("type_instance").toString(), jsonObject.get("plugin").toString(), jsonObject.get("values").toString()/*, dataBytes, frameSize*/);
									} catch(Exception e) { //this generic but you can control another types of exception
									    // look the origin of excption 
										
										e.printStackTrace();
									}
								 
								
								/*
							    JSONParser helper = new JSONParser();
							    data = (JSONObject)helper.parse(record.value());*/
							} catch (ParseException e) {
							    // Invalid syntax
								e.printStackTrace();
							}
							// Note that these may throw several exceptions
							/*JSONObject node = (JSONObject)data.get("type");
							JSONArray array = (JSONArray)node.get("plugin");
							*/
							
							
							//System.out.println("Writing type_instance:   "+features.toString());
						
						//ping_collection(record.value());
						
					}

				}
	}

	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	private void ping_collection(String record) throws MalformedJsonException, IOException {
		// TODO Auto-generated method stub
		document = new Document();
		
		record = record.replaceAll("[<>\\[\\]-]", "");
		/*record = record.replace("0,0", "0");
		record = record.replace("\"derive\",\"derive\"", "derive");
		record = record.replace("\"rx\",\"tx\"", "rx-tx");
		record = record.replace("\"read\",\"write\"", "read-write");
		record = record.replace("io_time\",\"weighted_io_time\"", "io_time-weighted_io_time");*/
		System.out.println("record: "+record);
		
		
		//insertData()
		
		
		 JsonElement jelement = new JsonParser().parse(record);
        // JsonObject  jobject = jelement.getAsJsonObject();
         
		 if (jelement instanceof JsonObject) {
			    JsonObject  jsonObject =jelement .getAsJsonObject();
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    System.out.println(jsonObject.get("host").getAsString());
				System.out.println(jsonObject.get("plugin").getAsString());
				System.out.println(jsonObject.get("plugin_instance").getAsString());
				System.out.println(jsonObject.get("type").getAsString());
				System.out.println(jsonObject.get("type_instance").getAsString());
			 } else if (jelement instanceof JsonArray) {
			    JsonArray  jarray =  jelement.getAsJsonArray();
			    
			    
			    System.out.println("jarray:"+jarray.toString());
			 }
		 
		/*try
		{
		JsonObject jsonObject = new JsonParser().parse(record).getAsJsonObject();
		System.out.println(jsonObject.get("host").getAsString());
		System.out.println(jsonObject.get("plugin").getAsString());
		System.out.println(jsonObject.get("plugin_instance").getAsString());
		System.out.println(jsonObject.get("type").getAsString());
		System.out.println(jsonObject.get("type_instance").getAsString());
		
		}
		catch (NumberFormatException numberformatexception)
	    {
	        throw new JsonSyntaxException(numberformatexception);
	    }*/
				
		}
		//mongoConnector.getDbConnection().getCollection(topologyMongoCollection_vcenter).insertOne(document);
		//document.clear();



	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}
}
