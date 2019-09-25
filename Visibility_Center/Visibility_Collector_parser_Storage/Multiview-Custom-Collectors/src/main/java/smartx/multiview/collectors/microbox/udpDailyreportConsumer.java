//Grid based UDP throughput data for Sites using active monitoring
package smartx.multiview.collectors.microbox;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;
import org.yaml.snakeyaml.Yaml;

import smartx.multiview.DataLake.MongoDB_Connector;

public class udpDailyreportConsumer implements Runnable {
	private Thread thread;
	private String topologyMongoCollection_bandwidth = "microbox-udp-data"; //Change collection name
	private String topologyMongoCollection = "daily-report-udp-data-raw"; //Change collection name
	
	private String ThreadName = "UDP Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "microbox_daily_report_throughput_udp"; 
	private Document document;
	private Date timestamp;
	private int count_site_yaml=0;
	private String[] sites_name;
	public String path="/microbox_sites.yaml";
	public String path_sites_working="/microbox_sites_working_tcp_udp.yaml";
	private String sites_name_working[];
	int count_site_working=0;

	@Override
	public void run() {

		System.out.println("Running "+ThreadName);
		try {
			this.Consume();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public udpDailyreportConsumer(String bootstrapserver, MongoDB_Connector MongoConn) 
	{
		bootstrapServer        = bootstrapserver;
		mongoConnector         = MongoConn;
	}

	public void Consume() throws IOException{
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
		Map<Integer, String> hmap = new HashMap<Integer,String>();

		

		
		try {
			count_site_yaml=getNumberofSites();
						
			sites_name=  new String[count_site_yaml];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while (true) 
		{
			try {
				ConsumerRecords<String, String> records = consumer.poll(0);
				//System.out.println("START");
				for (ConsumerRecord<String, String> record : records)
				{

					StoreToDB_raw(record.value());
					System.out.println("Printing the udp data at udpDailyreportConsumer "+record.value());
				}
			}
			catch(Exception e) {
				  //  Block of code to handle errors
				}
		}
	}


	//Printing the udp throughput data
	//private int BoxID_GIST1=1, BoxID_ID=2, BoxID_PH=3, BoxID_PKS=4, BoxID_HUST=5, BoxID_GIST2=6, BoxID_CHULA=7, BoxID_NCKU=8, BoxID_MY=9, BoxID_MYREN=10, BoxID_GIST3=11, BoxID_NUC=12;
	private void StoreToDB_raw(String record) {
		// TODO Auto-generated method stub
		document = new Document();
		String[] record_values = record.split(" ");
		System.out.println(Arrays.toString(record_values));
		System.out.printf("Length:%d",record_values.length);
		//System.out.println(record_values[0]);
		//System.out.println(record_values[1]);
		
		//get working sites list
		
				get_working_sites_list();
		
		try {
			whenLoadMultipleYAMLDocuments_thenLoadCorrectJavaObjects("/microbox_sites.yaml",record_values);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		document.clear();

		 
		
		




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

	private void get_working_sites_list() {
		// TODO Auto-generated method stub
		
		try {
			count_site_working=getNumberofSitesWorking();

			//count_oneday_ping= new int[count_site_working];
			//anStringArray= new String[count_site_working+1][count_site_working+1];

			sites_name_working=  new String[count_site_working];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("count_site_working"+count_site_yaml);
		
		
		
		System.out.printf("-- loading from %s --%n", path);
		 Yaml yaml = new Yaml();
		 try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path_sites_working)) {
			 Iterable<Object> itr = yaml.loadAll(in);
			 //Iterable<Object> itr1 = itr;
			 System.out.println("-- iterating loaded Iterable --");
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			 timestamp = new Date();
			 int i=0;

			 String microbox_site_source="";
			 //					

			 //We calculate the total number of pings collected from each microbox to other microboxes
			 for (Object o : itr) 														
			 { 
				 microbox_site_source = ((String) o).substring(0, ((String) o).indexOf('='));
				 sites_name_working[i]=microbox_site_source;
				 
				 i+=1;
			 }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	private int getNumberofSitesWorking() throws IOException {
		// TODO Auto-generated method stub
		
		
		System.out.printf("-- loading from %s --%n", path);
		Yaml yaml = new Yaml();
		int siteCount = 0;
		try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path_sites_working)) {
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
	
	
public void whenLoadMultipleYAMLDocuments_thenLoadCorrectJavaObjects(String path,String[] record_values) throws IOException {
		
        System.out.printf("-- loading from %s --%n", path);
        Yaml yaml = new Yaml();
        try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
            Iterable<Object> itr = yaml.loadAll(in);
            System.out.println("-- iterating loaded Iterable --");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			timestamp = new Date();
			int j= 3;//Becasue first three elements are date time and source
            int i=0;
            document.put("timestamp",    (record_values[0])/*sdf.format(timestamp)*/);
			document.put("microbox-SOURCE",   record_values[2]);
			System.out.println("Starting udp loop:" );
            for (Object o : itr) {
  
                String microbox_site = ((String) o).substring(0, ((String) o).indexOf('='));
                sites_name[i]=microbox_site;
                document.put(microbox_site,    record_values[j].isEmpty() ? "0" : record_values[j]);
                System.out.println(microbox_site+","+document.get(microbox_site));
                
                j=j+1;
    			i=i+1;
    			
    			
            }
            mongoConnector.getDbConnection().getCollection(topologyMongoCollection_bandwidth).insertOne(document);
            
            //printing to file
            String date_time = "Daily-report-udp-data"+"_"+record_values[0];
    		
    		String csvFile = date_time.replace("/", "")+".csv";
    		
    		if (equalsWorkingBoxList(sites_name_working,document.get("microbox-SOURCE").toString().replace("smartx-", "")))
			{
    			
    			System.out.println("TRUE");
    			try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/microbox/Daily-report-udp-data/"+csvFile, true);

    				BufferedWriter bw = new BufferedWriter(fw);

    				PrintWriter out = new PrintWriter(bw))
    		{
    			File file = new File("/home/netcs/active_monitoring/microbox/Daily-report-udp-data/"+csvFile);
    			if (file.length() == 0)
    	        {
    				out.print("Date"+" "+"TIME"+" "+"SOURCE-SITE"+" ");
    				for ( i = 0; i < sites_name_working.length; i++) {
    					out.print(sites_name_working[i]+" ");	
    				}
    				out.println("");
    	        }
    			else
    				out.println("");
    			
    			out.print(document.get("timestamp")+" "+document.get("microbox-SOURCE")+" ");
    			
    			for (String s: sites_name_working) {  
    				out.print(document.get(s)+" ");
    				System.out.println("sites_name_working[i]:"+s);
    				System.out.println("document.get(sites_name_working[i]:"+document.get(s));
    			}
    			/*
    			
    			/*for ( i = 0; i < sites_name.length; i++)
    			{
    				for ( j = 0; j < sites_name_working.length; j++)
        			{
    					if (sites_name[i]==sites_name_working[j])
    					{
    						out.print(record_values[i+3].toString()document.get(sites_name[i])+" ");
    						System.out.println("document.get(sites_name[i]):"+document.get(sites_name[i]));
    					}
        			}
    				
    			}*/
    			
    			
    		} catch (IOException e) {
    			//exception handling left as an exercise for the reader
    			System.out.println(e);
    		}
			}
    		else
    		{
    			System.out.println("NOT True");
    		}
    		
    					
    		System.out.println("TRUE");	
    		
            
            
    		
        }
	}
public static boolean equalsWorkingBoxList(String[] arr, String targetValue) {
	return Arrays.asList(arr).contains(targetValue);
}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}

}
