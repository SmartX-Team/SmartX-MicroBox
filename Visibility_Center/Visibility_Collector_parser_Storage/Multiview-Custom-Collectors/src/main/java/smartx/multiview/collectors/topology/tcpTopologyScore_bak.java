package smartx.multiview.collectors.topology;

import java.util.ArrayList;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import smartx.multiview.DataLake.MongoDB_Connector;

public class tcpTopologyScore_bak implements Runnable {

	private Thread thread;
	private String ThreadName = "TCP Topology Score Thread";
	private String topologyMongoCollection = "topology-tcp-data-raw"; // Change collection name
	private String topologyMongoCollectionrt = "topology-tcp-data-rt";

	private float max = 0, min = 0;

	private MongoDB_Connector mongoConnector;

	public tcpTopologyScore_bak(MongoDB_Connector MongoConn) {
		mongoConnector = MongoConn;
	}

	public void getScore() {
		MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
				.getCollection(topologyMongoCollection);
		List<Document> documents = collection_process.find().into(new ArrayList<Document>());
		int firstrun = 0;
		for (Document document : documents) {
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
		}

		for (Document document : documents) {
			System.out.println(document);
			System.out.printf("(Integer.parseInt(document.getString(\"value\"))-min) / (max-min)=%.2f%n",(Float.parseFloat(document.getString("value"))));
			System.out.printf("(Integer.parseInt(document.getString(\"value\"))-min) / (max-min)=%.2f%n",(max - min));
			System.out.printf("(Integer.parseInt(document.getString(\"value\"))-min) / (max-min)=%.2f%n",(((Float.parseFloat(document.getString("value")) - min) / (max - min)) * 100)>0 ? ((Float.parseFloat(document.getString("value")) - min) / (max - min)) * 100 : 1);
		}
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000 * 60 * 60 * 12);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Running " + ThreadName);
		
		this.getScore();
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}
	}

}
