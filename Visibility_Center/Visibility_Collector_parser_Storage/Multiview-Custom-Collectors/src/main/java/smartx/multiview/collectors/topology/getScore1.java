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

public class getScore1 extends TimerTask {
	private Thread thread;
	private String ThreadName = "TCP Topology Score Thread";
	private String topologyMongoCollection = "topology-tcp-data-raw"; // Change collection name
	private String topologyMongoCollectionrt = "topology-tcp-data-score";

	private float max = 0, min = 0, value=0;
	private int k=0;
	private MongoDB_Connector mongoConnector;
	//private Date date;
	private Document document_score;
	
	/*public getScore1(MongoDB_Connector MongoConn) {
		mongoConnector = MongoConn;
	}*/

	/*public getScore1(MongoDB_Connector mongoConnector) {
		mongoConnector = mongoConnector;
	}*/

	public getScore1(MongoDB_Connector mongoConnector2) {
		// TODO Auto-generated constructor stub
		mongoConnector = mongoConnector2;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("************************************************TCP Score Timer Called************************************************************** ");
		try {
			this.getScore();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
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
        String date ="2018/07/01"; //not using it
        String date1 ="2018/07/02"; //not using it
        Date startDate= simpleDateFormat.parse(date); 
        Date endDate= simpleDateFormat.parse(date1);
		
			startDate = simpleDateFormat.parse(date);
			endDate = simpleDateFormat.parse(date1);
			/*BasicDBObject query1 = new BasicDBObject("timestamp", new BasicDBObject("$gte",startDate).append("$lt",endDate ));*/
			BasicDBObject query1 = new BasicDBObject("timestamp", new BasicDBObject("$gte",new Date(System.currentTimeMillis() + (9 * 60 * 60 * 1000) - (12 * 60 * 60 * 1000))).append("$lt",new Date(System.currentTimeMillis()+ (9 * 60 * 60 * 1000)) ));
			
			List<Document> documents = collection_process.find(query1).into(new ArrayList<Document>());
			
	        collection_process.find(query1);
	        System.out.println(collection_process.find(query1));
			
		 
        
        
        
		//collection_process.drop();//new
		List<Document> insertList = new ArrayList<Document>();
		
		 Date date11 = new Date();
	       
	        //collection_process.insertMany(documents);
	        System.out.println("Printing Current Time - 6 hours, new Date(System.currentTimeMillis() - (6 * 60 * 60 * 1000))");
	        Date currDate= (new Date((System.currentTimeMillis() + (9 * 60 * 60 * 1000) - (12 * 60 * 60 * 1000))));
	        System.out.println(currDate.toString());
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
	        	System.out.println("Printing last 6 hours data only using document with query");
	        	System.out.println(document);
			}
		
		//List<Document> documents = collection_process.find().into(new ArrayList<Document>());
		
		int firstrun = 0;
		 
		document_score = new Document();
		
		//MongoCursor<String> files = collection_process.distinct("destBoxname", String.class).iterator();
		//error here
		
		
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
				 
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
				//Date currDate1= (document.getDate("timestamp") - (9 * 60 * 60 * 1000) ));
				Date timestamp = document.getDate("timestamp");
				
				//document_score.put("timestamp",   sdf.format(document.getDate("timestamp")));
				document_score.put("timestamp",   sdf.format(timestamp.getTime()- (9 * 60 * 60 * 1000)));
				document_score.put("srcBoxname",   document.getString("srcBoxname"));
				document_score.put("destBoxname",   document.getString("destBoxname"));
				document_score.put("srcBoxID",  (document.getInteger("srcBoxID")));
				document_score.put("destBoxID",   document.getInteger("destBoxID").toString());
				document_score.put("value",  (document.getString("value")));
				document_score.put("Score",   (numberAsString));
				//document_score.put("Score",   (value));
				mongoConnector.getDbConnection().getCollection(topologyMongoCollectionrt).insertOne(document_score);
				document_score.clear();
			//}
		}
		
		 
	}

}
