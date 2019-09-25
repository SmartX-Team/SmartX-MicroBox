package smartx.multiview.collectors.topology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import smartx.multiview.DataLake.MongoDB_Connector;

public class get_vcenter_aggregate extends TimerTask {
	private MongoDB_Connector mongoConnector;
	private String topologyMongoCollection_vcenter = "daily-report-vcenter-data-raw"; //Change collection name
	private String topologyMongoCollection_vcenter_aggregate = "daily-report-vcenter-data-aggregate"; //Change collection name
	public List<Document> documents_OneDay;
	private Date timeStamp_file;
	public int count_oneday_vcenter_SmartX_Box=0,Box_GIST1_vcenter=0,Box_PH_vcenter=0,Box_HUST_vcenter=0,Box_CHULA_vcenter=0,Box_MYREN_vcenter=0,Box_GIST3_vcenter=0, Box_GIST0_vcenter=0, vcenter_record_total_today=0;
	private Document document_vcenter_status;
	
	public get_vcenter_aggregate(MongoDB_Connector mongoConnector2) {
		mongoConnector = mongoConnector2;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("************************************************vcenter Timer Called************************************************************** ");
		try {
			this.get_vcenter();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	@SuppressWarnings("unchecked")
	private void get_vcenter() throws ParseException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		documents_OneDay = new ArrayList<Document>();
		MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
				.getCollection(topologyMongoCollection_vcenter);
		
		List<Document> documents = collection_process.find().into(new ArrayList<Document>());
		
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0); 
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());//2018/07/03
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		for (Document document : documents) 
		{
			//System.out.println(document);

			
			if (document.getString("timestamp").contains(timeStamp))
			{

				//documents_OneDay.add(document);
				if(!document.containsValue("-1"))
        		{
        			documents_OneDay.add(document);
        		}

			}
		}
		SimpleDateFormat simpleDateFormat_startDate = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss zzz");
		SimpleDateFormat simpleDateFormatNoZone = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat simpleDateFormatNoTime = new SimpleDateFormat ("yyyy/MM/dd"); 
		timeStamp_file=  new Date();
		String date_time = "Daily-report-vcenter-data"+"_"+simpleDateFormat_startDate.format(timeStamp_file);
		String csvFile = date_time.replace("/", "")+".csv";
		document_vcenter_status = new Document();
		try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/Daily-report-vcenter-data/"+csvFile, true);

				BufferedWriter bw = new BufferedWriter(fw);

				PrintWriter out = new PrintWriter(bw))
		{
			File file = new File("/home/netcs/active_monitoring/Daily-report-vcenter-data/"+csvFile);
			if (file.length() == 0)
	        {
	        	out.println("Date"+" "+"TIME"+" "+"SOURCE-SITE"+" "+"SmartX-Box-GIST1"+" "+"SmartX-Box-PH"+" "+"SmartX-Box-HUST"+" "+"SmartX-Box-CHULA"+" "+"SmartX-Box-MYREN"+" "+"SmartX-Box-GIST3");	
	        }
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-GIST1              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			String[] sites = { "SmartX-Box-GIST1", "SmartX-Box-PH","SmartX-Box-HUST","SmartX-Box-CHULA","SmartX-Box-MYREN","SmartX-Box-GIST3" };
			for( int i = 0; i <= sites.length - 1; i++)
			{
				for (Document document : documents_OneDay) 
				{
					//if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-GIST1"))
					//{
						//System.out.println(document.getString("SmartX-Box-SOURCE"));
					if (document.getString("SmartX-Box-SOURCE").contains(sites[i]))
							{
						System.out.println(document.toString());
						calculate_vcenter(document);
							}
					
					///}
				}
			}
			System.out.println("Liveliness of Connection with VCenter:" + Box_GIST1_vcenter +" "+ Box_PH_vcenter +" "+ Box_HUST_vcenter +" "+ Box_CHULA_vcenter +" "+ Box_MYREN_vcenter +" "+ Box_GIST3_vcenter);
			float f1= round((((float)Box_GIST1_vcenter/144)*100),2);
			float f2= round((((float)Box_PH_vcenter/144)*100),2);
			float f3= round((((float)Box_HUST_vcenter/144)*100),2);
			float f4= round((((float)Box_CHULA_vcenter/144)*100),2);
			float f5= round((((float)Box_MYREN_vcenter/144)*100),2);
			float f6= round((((float)Box_GIST3_vcenter/144)*100),2);
			
			document_vcenter_status.put("timestamp",   simpleDateFormat.format(timeStamp_file));
			document_vcenter_status.put("SmartX-Box-SOURCE",   "Visibility_Center");
			if (f1>100)
				document_vcenter_status.put("SmartX-Box-GIST1","100");
			else
				document_vcenter_status.put("SmartX-Box-GIST1", Float.toString(f1));
			
			if (f2>100)
				document_vcenter_status.put("SmartX-Box-PH","100");
			else
				document_vcenter_status.put("SmartX-Box-PH",  Float.toString(f2));
			
			if (f3>100)
				document_vcenter_status.put("SmartX-Box-HUST","100");
			else
				document_vcenter_status.put("SmartX-Box-HUST", Float.toString(f3));
			
			if (f1>100)
				document_vcenter_status.put("SmartX-Box-CHULA","100");
			else
				document_vcenter_status.put("SmartX-Box-CHULA",Float.toString(f4));
			
			if (f1>100)
				document_vcenter_status.put("SmartX-Box-MYREN","100");
			else
				document_vcenter_status.put("SmartX-Box-MYREN", Float.toString(f5));
			
			if (f1>100)
				document_vcenter_status.put("SmartX-Box-GIST3","100");
			else
				document_vcenter_status.put("SmartX-Box-GIST3", Float.toString(f6));
			
			System.out.println("SmartX-Box-GIST1:"+document_vcenter_status.getString("SmartX-Box-GIST1"));
			
			mongoConnector.getDbConnection().getCollection(topologyMongoCollection_vcenter_aggregate).insertOne(document_vcenter_status);
			out.println(simpleDateFormatNoTime.format(timeStamp_file)+" "+document_vcenter_status.get("SmartX-Box-SOURCE")+" "+document_vcenter_status.getString("SmartX-Box-GIST1")+"%" + " "+document_vcenter_status.getString("SmartX-Box-PH")+"%" + " "+document_vcenter_status.getString("SmartX-Box-HUST")+"%"+" "+document_vcenter_status.getString("SmartX-Box-CHULA")+"%" + " "+document_vcenter_status.getString("SmartX-Box-MYREN") +"%" + " "+document_vcenter_status.getString("SmartX-Box-GIST3")+"%");
			//out.println(simpleDateFormatNoTime.format(timeStamp_file)+" "+document_vcenter_status.get("SmartX-Box-SOURCE")+" "+document_vcenter_status.getDouble("SmartX-Box-GIST1")+"%" + " "+document_vcenter_status.getDouble("SmartX-Box-PH")+"%" + " "+document_vcenter_status.getDouble("SmartX-Box-HUST")+"%"+" "+document_vcenter_status.getDouble("SmartX-Box-CHULA")+"%" + " "+document_vcenter_status.getDouble("SmartX-Box-MYREN") +"%" + " "+document_vcenter_status.getDouble("SmartX-Box-GIST3")+"%");
			document_vcenter_status.clear();
			reset_values();
		
		
		} catch (IOException e) {
			//exception handling left as an exercise for the reader
		}
	}
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	private void reset_values() {
		// TODO Auto-generated method stub
		count_oneday_vcenter_SmartX_Box=0;Box_GIST1_vcenter=0;Box_PH_vcenter=0;Box_HUST_vcenter=0;Box_CHULA_vcenter=0;
		Box_MYREN_vcenter=0;Box_GIST3_vcenter=0;Box_GIST0_vcenter=0;vcenter_record_total_today=0;
	}
	private void calculate_vcenter(Document document) {

		count_oneday_vcenter_SmartX_Box+=1;
		if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-GIST1")&&(document.getString("vcenter_Connection").contains("1")))
		{
			Box_GIST1_vcenter+=Integer.parseInt(document.getString("vcenter_Connection"));
		}
		
		if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-PH")&&(document.getString("vcenter_Connection").contains("1")))
		{
			Box_PH_vcenter+=Integer.parseInt(document.getString("vcenter_Connection"));
		}
		
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-HUST")&&(document.getString("vcenter_Connection").contains("1")))
		{
			Box_HUST_vcenter+=Integer.parseInt(document.getString("vcenter_Connection"));
		}
		
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-CHULA")&&(document.getString("vcenter_Connection").contains("1")))
		{
			Box_CHULA_vcenter+=Integer.parseInt(document.getString("vcenter_Connection"));
		}
		
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-MYREN")&&(document.getString("vcenter_Connection").contains("1")))
		{
			Box_MYREN_vcenter+=Integer.parseInt(document.getString("vcenter_Connection"));
		}
		
			if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-GIST3")&&(document.getString("vcenter_Connection").contains("1")))
		{
			Box_GIST3_vcenter+=Integer.parseInt(document.getString("vcenter_Connection"));
		}
		/*if ((document.getString("SmartX-Box-GIST_NUC").contains("-")))
		{
			Box_GIST0_vcenter+=Integer.parseInt(document.getString("SmartX-Box-GIST_NUC"));
		}*/

	}
}
	
