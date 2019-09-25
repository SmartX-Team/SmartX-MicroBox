package smartx.multiview.collectors.topology;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.joda.time.DateTime;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import smartx.multiview.DataLake.MongoDB_Connector;

public class tcpTopologyScore implements Runnable {

	private Thread thread;
	private String ThreadName = "TCP Topology Score Thread";
	private String topologyMongoCollection = "topology-tcp-data-raw"; // Change collection name
	private String topologyMongoCollectionrt = "topology-tcp-data-rt";

	private float max = 0, min = 0, value=0;
	private int k=0;
	private MongoDB_Connector mongoConnector;
	//private Date date;
	private Document document_score;
	
	
	public tcpTopologyScore(MongoDB_Connector MongoConn) {
		mongoConnector = MongoConn;
	}

	 
	 
	 
	public void getScore() throws ParseException {
		MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
				.getCollection(topologyMongoCollection);
		FindIterable<Document> findCursor
        = collection_process.find(
            Filters.and(
                Filters.gte("timestamp", new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000))),
                Filters.lte("timestamp", new Date(System.currentTimeMillis()))));
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd");  
        String date ="2018/07/01"; 
        String date1 ="2018/07/02"; 
        Date startDate= simpleDateFormat.parse(date); 
        Date endDate= simpleDateFormat.parse(date1);
		
			startDate = simpleDateFormat.parse(date);
			endDate = simpleDateFormat.parse(date1);
			/*BasicDBObject query1 = new BasicDBObject("timestamp", new BasicDBObject("$gte",startDate).append("$lt",endDate ));*/
			BasicDBObject query1 = new BasicDBObject("timestamp", new BasicDBObject("$gte",new Date(System.currentTimeMillis() - (12 * 60 * 60 * 1000))).append("$lt",new Date(System.currentTimeMillis()) ));
			
			List<Document> documents = collection_process.find(query1).into(new ArrayList<Document>());
			
	        collection_process.find(query1);
	        System.out.println(collection_process.find(query1));
			
		 
        
        
        
		//collection_process.drop();//new
		List<Document> insertList = new ArrayList<Document>();
		
		 Date date11 = new Date();
	       
	        //collection_process.insertMany(documents);
	        System.out.println("Printing line 62-65");
	        System.out.println(collection_process.count());
	       // MongoCursor<Document> doc = collection_process.find(new Document("date", date)).iterator();
	        //System.out.println(doc.next().getDate("date"));
	        
	        for (Document document1 : findCursor) 
			{
				
					
					System.out.println("Printing last 2 hours data only using cursor");
					System.out.println(document1);
					//System.out.println(document1.getString("timestamp"));
				}   
	        System.out.println("Printing line 62-65");
	        for (Document document : documents) 
			{
	        	System.out.println("Printing last 12 hours data only using document with query");
	        	System.out.println(document);
			}
		
		//List<Document> documents = collection_process.find().into(new ArrayList<Document>());
		List<Document> documents_unique = new ArrayList<Document>();
		int firstrun = 0;
		 date11 = new Date(System.currentTimeMillis() - (12 * 60 * 60 * 1000));
		
		 System.out.println("getScore, Date- (12 * 60 * 60 * 1000):  "+ date11.toString());
		document_score = new Document();
		
		//MongoCursor<String> files = collection_process.distinct("destBoxname", String.class).iterator();
		//error here
		/*@SuppressWarnings("unchecked")
		MongoCursor<String> files = (MongoCursor<String>) collection_process.find(
	            Filters.and(
	                Filters.gte("timestamp_field", new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000))),
	                Filters.lte("timestamp_field", new Date(System.currentTimeMillis()))));
		
		
		System.out.println("Printing line 92");
		    while(files.hasNext()) //only distinct IPs in the destination box are checked to avoid repetetion.
		    {
		    	  String values = files.next();
		    	  System.out.println(values);	
		    	  k=0;
				  firstrun=0;
		    }  
		   */ 
		//Error here    
		/*@SuppressWarnings("unchecked")
			
		    List<Document> documents_oneday = (List<Document>) collection_process.find(
		            Filters.and(
			                Filters.gte("timestamp_field", new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000))),
			                Filters.lte("timestamp_field", new Date(System.currentTimeMillis()))));
		    
		    System.out.println("Printing line 107");
		    for (Document document : documents_oneday) 
			{
				System.out.println(document);
				
			}*/
		
		/*for (Document document : documents_unique) 
		{
			System.out.println(document);
			
		}
		
		
		for (Document document : documents) 
		{
			
			
		}*/
		/*for (Document document : documents) {
			for (Document document1 : documents) {
				if (document.getString("timestamp").contains(date.toString().replace('-', '/'))) {
					if((document.getString("srcBoxID").equals(document1.getString("srcBoxID")) && (document.getString("destBoxID").equals(document1.getString("destBoxID")))))
					{
						
					}
					else
						System.out.println(document);
					System.out.printf("(Integer.parseInt(document.getString(\"value\"))-min) / (max-min)=%.2f%n",
							(Float.parseFloat(document.getString("value"))));
					System.out.printf("(Integer.parseInt(document.getString(\"value\"))-min) / (max-min)=%.2f%n",
							(max - min));
					System.out.printf("(Integer.parseInt(document.getString(\"value\"))-min) / (max-min)=%.2f%n",
							(((Float.parseFloat(document.getString("value")) - min) / (max - min)) * 100) > 0
									? ((Float.parseFloat(document.getString("value")) - min) / (max - min)) * 100
									: 1);	
						
				}
				
			
			}
		}*/
		
		    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			//System.out.println(dateFormat.format(cal)); //2016/11/16 12:08:43
		    /*for (Document document : documents) 
			{
				if(document.getString("timestamp").contains(dateFormat.format(cal)))
				{
					
					System.out.println("Printing today's data only");
					System.out.println(document);
				}
				
				
			}*/   
		    
		    firstrun=0;
		for (Document document : documents) {
			//if (document.getString("timestamp").contains(date.toString().replace('-', '/'))) {
				System.out.println(document);
				if (firstrun == 0) {
					max = Float.parseFloat(document.getString("value"));
					min = max;
					firstrun = 1;
				}

				if (Float.parseFloat(document.getString("value")) > max)
					max = Float.parseFloat(document.getString("value"));
				else if (Float.parseFloat(document.getString("value")) < min)
					min = Float.parseFloat(document.getString("value"));
				System.out.printf("max=%.2f, min=%.2f%n%n", max, min);
			//}
		}

		for (Document document : documents) {
			//if (document.getString("timestamp").contains(date.toString().replace('-', '/'))) {
				System.out.println(document);
				System.out.printf("(Integer.parseInt(document.getString(\"value\")=%.2f%n",
						(Float.parseFloat(document.getString("value"))));
				System.out.printf("(max-min)=%.2f%n",
						(max - min));
				System.out.printf("(Integer.parseInt(document.getString(\"value\"))-min) / (max-min)=%.2f%n%n",
						(((Float.parseFloat(document.getString("value")) - min) / (max - min)) * 100) > 0
								? ((Float.parseFloat(document.getString("value")) - min) / (max - min)) * 100
								: 1);
				value= (((Float.parseFloat(document.getString("value")) - min) / (max - min)) * 100) > 0
						? ((Float.parseFloat(document.getString("value")) - min) / (max - min)) * 100
						: 1;
				String numberAsString = String.format ("%.2f", value);
				document_score.put("timestamp",   document.getDate("timestamp"));
				
				document_score.put("srcBoxname",   document.getString("srcBoxname"));
				document_score.put("destBoxname",   document.getString("destBoxname"));
				document_score.put("srcBoxID",  (document.getInteger("srcBoxID")));
				document_score.put("destBoxID",   document.getInteger("destBoxID").toString());
				document_score.put("value",  (document.getString("value")));
				document_score.put("Score",   (numberAsString));
				mongoConnector.getDbConnection().getCollection(topologyMongoCollectionrt).insertOne(document_score);
				document_score.clear();
			//}
		}

	}
	
	public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);       
        return bd;
    }

	@Override
	public void run() {
		
		/*try {
			this.getScore();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 00);
		today.set(Calendar.SECOND, 0);

		// every night at 2am you run your task
		Timer timer = new Timer();
		timer.schedule(new getScore1(mongoConnector), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 12 hours
		
		
		
		

		
	}








	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}

}
