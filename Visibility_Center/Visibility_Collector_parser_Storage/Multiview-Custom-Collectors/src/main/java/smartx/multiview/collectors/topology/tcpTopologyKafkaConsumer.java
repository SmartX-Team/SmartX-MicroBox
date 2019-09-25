/**
 * @author Muhammad Ahmad
 * @version 0.1
 */

package smartx.multiview.collectors.topology;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.bson.Document;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import java.util.Arrays;

import smartx.multiview.DataLake.MongoDB_Connector;

public class tcpTopologyKafkaConsumer implements Runnable{
	private Thread thread;
	private String topologyMongoCollection = "topology-tcp-data-raw"; //Change collection name
	
	private String bootstrapServer;
	private String topic = "active_monitoring_throughput_tcp"; //Change Topic name
	private String ThreadName = "TCP Topology Thread";
	private int BoxID_GIST1=1, BoxID_ID=2, BoxID_PH=3, BoxID_PKS=4, BoxID_HUST=5, BoxID_GIST2=6, BoxID_CHULA=7, BoxID_NCKU=8, BoxID_MY=9, BoxID_MYREN=10, BoxID_GIST3=11, BoxID_NUC=12;
    private MongoDB_Connector mongoConnector;
    private Document document;
    private Date timestamp;
    private FindIterable<Document> pBoxData;
    private String BoxValue[];
    private int i=0, max=0, min=0;
    private int[] BoxValue_int;
	private float[] BoxValue_rawint= new float[20];	
	private Logger LOG = Logger.getLogger("IOVisorKafka");//???
	private String[] record_measure= new String[10];
	private int[] record_measure_int= new int[10];
	private int j=0;
    
    public tcpTopologyKafkaConsumer(String bootstrapserver, MongoDB_Connector MongoConn) 
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
    	
    	
        while (true) 
        {
            ConsumerRecords<String, String> records = consumer.poll(0);
            //tcpTopologyScore  tcpTopologyScore_= new tcpTopologyScore();
            //BoxValue= new String[10];
            //tcpTopologyScore_.getScore();
            //System.out.print("Calling getScore()");
            //getScore();
            
            //i=0;
            for (ConsumerRecord<String, String> record : records)
            {
            	/*System.out.printf("getYesterdayDateString()%n");
            	String yesterday= getYesterdayDateString();
            	System.out.printf(yesterday);
            	System.out.printf("getTodayDateInt()%n");

            	int today_int= getTodayDateInt();
            	System.out.printf("%d",today_int);
            	System.out.printf("getTodayDateString()%n");

            	String today_str = getTodayDateString();
            	System.out.printf(today_str);
            	
            	//System.out.printf("TodayString:%s,TodayInt:%d, Yesterday=%s%n",getTodayDateString(),getTodayDateInt(),getYesterdayDateString());
            	*/
            	if (!record.value().contains(",") && record.value()!=null)
            	{
            	
            		System.out.printf("getTodayDateInt(): %s%n",getTodayDateString());
            	//	System.out.printf("TodayString:%s,TodayInt:%d, Yesterday=%s%n",getTodayDateString(),getTodayDateInt(),getYesterdayDateString());
            		/*
            		hmap.put(getTodayDateInt(),record.value());//put in hashmap Date in integer format	 with values
            		for (Map.Entry me : hmap.entrySet()) {
            	          System.out.println("Key: "+me.getKey() + " & Value: " + me.getValue());
            	        }
            		 System.out.println("While Loop:");
            	        Iterator iterator = hmap.entrySet().iterator();
            	        while (iterator.hasNext()) {
            	             Map.Entry me2 = (Map.Entry) iterator.next();
            	          System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
            	        } */
            		
            		
            		
            		
            		
            		
            		String[] record_values = record.value().split(" ");
            		
            		
            		/*if(record_values[0]==(today_str))//change it to yesterday to check all entries from yesterday
            		{
            		  	
            		}*/
            		
            		

            	System.out.printf("i=%d%n",i);
            	System.out.printf("*record_values*=%s%n",record.value());
            	
            	//System.out.printf("*record_values*=%s%n",record_values[4]);
//            	
            	
//            	BoxValue_rawint[i]=Integer.parseInt(record_values[4]);
//            	record_measure[i]=record_values[4];
            	
            	//System.out.printf("*record_measure*=%s%n",record_measure[i]);
//            	record_measure_int[i]=(int) Float.parseFloat(record_measure[i]);
            	//System.out.printf("*record_measure_int*=%d, record_measure_int.length=%d, i=%d%n",record_measure_int[i],record_measure_int.length,i);
            	
            	i+=1;
            	
            	/*if(i>3)
                	{
            		max= record_measure_int[0];
                    min=record_measure_int[0];
            		for(j=0; j<4; j++)
                	{
            			//System.out.printf("*BoxValue_rawint*=%d%n",BoxValue_rawint[i]);
            			 if (record_measure_int[j] > max)
                  	       max = record_measure_int[j];
            	       else if(record_measure_int[j] < min)
                	       min = record_measure_int[j];
                		//System.out.printf("record_measure_int=%d%n",record_measure_int[j]);
                	}
            		 System.out.printf("max=%d, min=%d%n%n",max,min);
            		 for( j = 0; j <4 ; j++)
         	    	{
            			 System.out.printf("record_measure_int[j]-min:%d-%d=%.2f      ",record_measure_int[j],min,(float)(record_measure_int[j]-min)); 
            			 System.out.printf("max-min=%.2f       ",(float)(max-min));
            			 System.out.printf("(record_measure_int[j]-min) / (max-min)=%.2f%n",((float)(record_measure_int[j]-min)/(float)(max-min))*100);
            			 //record_measure[i]
            			 //record_measure_int[j] =  ((record_measure_int[j]-min)/(max-min)) * 100;
            			 //System.out.printf("record_measure_int=%d%n",record_measure_int[j]); 
         	    	}
            	}*/
            	
            	
            	this.StoreToDB_raw(record.value());
            	}
            	
            	
		    }
                        
            for (ConsumerRecord<String, String> record : records)
            {
            	
            	System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            	
               //this.StoreToDB(record.value());
            	
            	/*DB db = client.getDB( "your_db_name" );
            	DBCollection collection = mongoConnector.getDbConnection().getCollection(topologyMongoCollection);
            	mongoConnector.getDbConnection().getCollection(topologyMongoCollection).insertOne(document);*/
            	
            	MongoCollection<Document> collection_process = mongoConnector.getDbConnection().getCollection(topologyMongoCollection);
            	List<Document> documents = (List<Document>) collection_process.find().into(new ArrayList<Document>());
         
                       for(Document document : documents){
                    	   //System.out.println("                     *******************PRINTING FROM MONGODB*******************");
                           //System.out.println(document);
                       }
                       
                       
		    }
            
            
        }
        
     }
    
    public void StoreToDB_raw(String record)
    {
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
    	 
    	 
    	 timestamp = new Date();
    	 Date currDate= (new Date(System.currentTimeMillis() + (9 * 60 * 60 * 1000)));//For korean time GMT+9
   
    	 System.out.println("Local Time: " + sdf.format(timestamp));		
    	

    	document = new Document();
		String[] record_values = record.split(" ");
		int srcBoxID = 0, destBoxID= 0;
    	// Extract values and add as key-value in MongoDB Collection
		
		
		
		
    	//document.put("timestamp",   sdf.format(timestamp));
		document.put("timestamp",   currDate);
		//document.put("timestamp",   (timestamp));
		document.put("srcBoxname",  record_values[1]);
		document.put("destBoxname",  record_values[2]);
    	srcBoxID = (record_values[1].contains("103.22.221.170") ? BoxID_GIST1 : record_values[1].contains("167.205.51.36") ? BoxID_ID :record_values[1].contains("202.90.150.4") ? BoxID_PH : record_values[1].contains("111.68.98.228") ? BoxID_PKS : record_values[1].contains("203.191.48.228") ? BoxID_HUST :  record_values[1].contains("103.22.221.31") ? BoxID_GIST2 : record_values[1].contains("161.200.25.99") ? BoxID_CHULA : record_values[1].contains("140.116.214.241") ? BoxID_NCKU : record_values[1].contains("203.80.21.4") ? BoxID_MY :record_values[1].contains("103.26.47.228") ? BoxID_MYREN :record_values[1].contains("103.22.221.30") ? BoxID_GIST3 :record_values[1].contains("103.22.221.62") ? BoxID_NUC : 0);
    	
    	destBoxID = (record_values[2].contains("103.22.221.170") ? BoxID_GIST1 : record_values[2].contains("167.205.51.36") ? BoxID_ID :record_values[2].contains("202.90.150.4") ? BoxID_PH : record_values[2].contains("111.68.98.228") ? BoxID_PKS : record_values[2].contains("203.191.48.228") ? BoxID_HUST :  record_values[2].contains("103.22.221.31") ? BoxID_GIST2 : record_values[2].contains("161.200.25.99") ? BoxID_CHULA : record_values[2].contains("140.116.214.241") ? BoxID_NCKU : record_values[2].contains("203.80.21.4") ? BoxID_MY :record_values[2].contains("103.26.47.228") ? BoxID_MYREN :record_values[2].contains("103.22.221.30") ? BoxID_GIST3 :record_values[2].contains("103.22.221.62") ? BoxID_NUC : 0);
    	document.put("srcBoxID", srcBoxID );
		document.put("destBoxID",  destBoxID); 
		document.put("Type",  record_values[3]); 
		if(record_values.length>4)
		{
			document.put("value",  record_values[4]);	
		}
		else
			document.put("value",  "0");//When box is down set value as 0
		
		System.out.printf("Contains1********srcBoxID =%d,destBoxID =%d %n",srcBoxID,destBoxID);
		System.out.printf("********srcBox:%s, destBox=%s**************%n",record_values[1],record_values[2] );
		System.out.println(currDate.toString());
		System.out.println(currDate.getTime());
/*		if (record_values[1].contains("103.22.221.62"))
		{
			System.out.printf("Contains2********record_values[1]==\103.22.221.62");
			srcBoxID= 123; 
		}
		if (record_values[1].equals("103.22.221.62"))
		{
			System.out.printf("Equals********record_values[1]==\103.22.221.62");
			srcBoxID= 123; 
		}
*/		
				   
		
		/*if (record_values[2]=="103.26.47.228")
		{
			System.out.printf("********ecord_values[2]==\103.26.47.228");
			destBoxID =  456;
		}
				 
		System.out.printf("********srcBoxID: %d, destBoxID=%d, BoxID_MYREN:%d,BoxID_GIST1:%d**************%n",srcBoxID,destBoxID,BoxID_MYREN,BoxID_GIST1 );
		*/
		mongoConnector.getDbConnection().getCollection(topologyMongoCollection).insertOne(document);
		document.clear();
    }
    
   /* public void StoreToDB(String record) throws IOException
    {
    	timestamp = new Date();
    	document = new Document();
		String[] record_values = record.split(" ");
		
    	// Extract values and add as key-value in MongoDB Collection
    	document.put("timestamp",   timestamp);
		document.put("srcBoxname",  record_values[1]);
		document.put("srcBoxID", record_values[1]=="103.22.221.170" ? BoxID_GIST1 : record_values[1]=="167.205.51.36" ? BoxID_ID :record_values[1]=="202.90.150.4" ? BoxID_PH : record_values[1]=="111.68.98.228" ? BoxID_PKS : record_values[1]=="203.191.48.228" ? BoxID_HUST :  record_values[1]=="103.22.221.31" ? BoxID_GIST2 : record_values[1]=="161.200.25.99" ? BoxID_CHULA : record_values[1]=="140.116.214.41" ? BoxID_NCKU : record_values[1]=="203.80.21.4" ? BoxID_MY :record_values[1]=="103.26.47.228" ? BoxID_MYREN :record_values[1]=="103.22.221.30" ? BoxID_GIST3 : 0);
		document.put("destBoxID",record_values[2]=="103.22.221.170" ? BoxID_GIST1 : record_values[2]=="167.205.51.36" ? BoxID_ID :record_values[2]=="202.90.150.4" ? BoxID_PH : record_values[2]=="111.68.98.228" ? BoxID_PKS : record_values[2]=="203.191.48.228" ? BoxID_HUST :  record_values[2]=="103.22.221.31" ? BoxID_GIST2 : record_values[2]=="161.200.25.99" ? BoxID_CHULA : record_values[2]=="140.116.214.41" ? BoxID_NCKU : record_values[2]=="203.80.21.4" ? BoxID_MY :record_values[2]=="103.26.47.228" ? BoxID_MYREN :record_values[2]=="103.22.221.30" ? BoxID_GIST3 : 0);
		//document.put("destBoxID",1);
		//System.out.printf("destBoxID: %d", record_values[2]=="103.22.221.170" ? BoxID_GIST1 : record_values[2]=="167.205.51.36" ? BoxID_ID :record_values[2]=="202.90.150.4" ? BoxID_PH : record_values[2]=="111.68.98.228" ? BoxID_PKS : record_values[2]=="203.191.48.228" ? BoxID_HUST :  record_values[2]=="103.22.221.31" ? BoxID_GIST2 : record_values[2]=="161.200.25.99" ? BoxID_CHULA : record_values[2]=="140.116.214.41" ? BoxID_NCKU : record_values[2]=="203.80.21.4" ? BoxID_MY :record_values[2]=="103.26.47.228" ? BoxID_MYREN :record_values[2]=="103.22.221.30" ? BoxID_GIST3 : 0);
			
			
		//getScore();
		document.put("destBoxname",  record_values[3]);
		document.put("value",  record_values[4]);
		
		System.out.print("test%n");
		
		mongoConnector.getDbConnection().getCollection(topologyMongoCollection).insertOne(document);
		//getScore();
		document.clear();
    }*/
   
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    private Date today() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }
    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(yesterday());
    }
    
    private int getTodayDateInt() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(dateFormat.format(date)));
    }
    private String getTodayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(today());
    }
		    
    public void run() 
	{
		System.out.println("Running "+ThreadName);
		try {
			this.Consume();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void start() {
		if (thread==null){
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}
}



