package smartx.multiview.collectors.topology;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;

import smartx.multiview.DataLake.MongoDB_Connector;


public class latency_DailyreportConsumer_extended_sites  implements Runnable {
	private Thread thread;
	private String topologyMongoCollection_latency_extended = "daily-report-latency-data-raw_extended"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "Latency extended Sites Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "daily_report_latency_extended_sites"; 
	private Document document;

	public latency_DailyreportConsumer_extended_sites(String bootstrapserver, MongoDB_Connector MongoConn) {
		
		bootstrapServer        = bootstrapserver;
		mongoConnector         = MongoConn;
	}

	@Override
	public void run() {
		System.out.println("Running "+ThreadName);
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 58);
		today.set(Calendar.SECOND, 0);

		// every night at 2am you run your task
		Timer timer = new Timer();
		//timer.schedule(new get_latency_aggregate(mongoConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
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
				while (true) 
				{
					ConsumerRecords<String, String> records = consumer.poll(0);
					for (ConsumerRecord<String, String> record : records)
					{

						ping_collection(record.value());
						System.out.println("Writing the latency data at table: daily_report_latency_data_raw  "+record.value());
					}

				}
	}

	private void ping_collection(String record) {
		// TODO Auto-generated method stub
		document = new Document();
		String[] record_values = record.split(" ");
		System.out.println(Arrays.toString(record_values));
		
//		System.out.printf("Length:%d",record_values.length);
//		System.out.println(record_values[0]);
//		System.out.println(record_values[1]);
//BoxID_GIST1=1, BoxID_ID=2, BoxID_PH=3, BoxID_PKS=4, BoxID_HUST=5, BoxID_GIST2=6, BoxID_CHULA=7, BoxID_NCKU=8, BoxID_MY=9, BoxID_MYREN=10, BoxID_GIST3=11, BoxID_NUC=12
		if (record_values.length>2)
		{
			System.out.println(record_values[2]);
			document.put("timestamp",   (record_values[0]+" "+record_values[1]));
			document.put("SmartX-Box-SOURCE", record_values[2]);
			if (record_values.length>3)
			{
				document.put("SmartX-Box-GIST1",   record_values[3]);
				//				document.put("SmartX-Box-ID",   record_values[4]);
				if (record_values.length>5)
				{
					document.put("SmartX-Box-PH",   record_values[5]);
					//				document.put("SmartX-Box-PKS",   record_values[6]);
					if (record_values.length>7)
					{
						document.put("SmartX-Box-HUST",   record_values[7]);
						//				document.put("SmartX-Box-GIST2",   record_values[8]);
						if (record_values.length>8 )
						{
							document.put("SmartX-Box-GIST2",   record_values[8]);
							if (record_values.length>9 )
							{
								document.put("SmartX-Box-CHULA",   record_values[9]);

								//document.put("SmartX-Box-NCKU",   record_values[10]);
								//				document.put("SmartX-Box-MY",   record_values[11]);
								if (record_values.length>10 )
								{
									document.put("SmartX-Box-NCKU",    record_values[10].isEmpty() ? "0" : record_values[10]);
									if (record_values.length>11 )
									{
										document.put("SmartX-Box-MY",    record_values[11].isEmpty() ? "0" : record_values[11]);
								
								
								if (record_values.length>12 )
								{
									document.put("SmartX-Box-MYREN",    record_values[12].isEmpty() ? "0" : record_values[12]);
									if (record_values.length>13 )
									{
										document.put("SmartX-Box-GIST3",    record_values[13].isEmpty() ? "0" : record_values[13]);	


										if (record_values.length>14 )
										{
											document.put("SmartX-Box-GIST_NUC",   record_values[14].isEmpty() ? "0" : record_values[14]);	
										}
									}
									else
									{
										document.put("SmartX-Box-GIST3", "0");
									}
								}
								else
								{
									document.put("SmartX-Box-MYREN", "0");
								}
							}
									else
									{
										document.put("SmartX-Box-MY", "0");
									}
								}
									else
									{
										document.put("SmartX-Box-NCKU", "0");
									}
								}
							else
							{
								document.put("SmartX-Box-CHULA", "0");
							}
						}
							
						else
						{
							document.put("SmartX-Box-GIST2", "0");
						}


					}
					else
					{
						document.put("SmartX-Box-HUST", "0");
					}
				}
				else
				{
					document.put("SmartX-Box-PH", "0");
				}
			}
			else
			{
				document.put("SmartX-Box-GIST1", "0");
			}
		}
		mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_extended).insertOne(document);
		document.clear();


}
	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}
}
